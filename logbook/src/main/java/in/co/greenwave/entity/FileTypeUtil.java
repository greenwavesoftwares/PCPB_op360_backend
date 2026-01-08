package in.co.greenwave.entity;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileTypeUtil {

	// Magic Numbers (Hex Signatures)
	private static final String PDF_SIG = "25504446";
	private static final String JPG_SIG = "FFD8FF";
	private static final String PNG_SIG = "89504E47";
	private static final String GIF_SIG = "47494638";
	private static final String ZIP_SIG = "504B0304"; // Shared by DOCX, XLSX, ZIP
	private static final String OLE2_SIG = "D0CF11E0"; // Shared by DOC, XLS, MSG

	// Sub-signatures for distinguishing formats
	private static final String XLSX_SUB_SIG = "786C2F"; // "xl/"
	private static final String DOCX_SUB_SIG = "776F72642F"; // "word/"
	private static final String XLS_SUB_SIG = "576F726B626F6F6B"; // "Workbook"
	private static final String DOC_SUB_SIG = "576F7264446F63756D656E74"; // "WordDocument"

	public static FileTypeInfo detect(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return new FileTypeInfo(".txt", "text/plain");
		}

		// Convert only the header (first 2048 bytes) to Hex to avoid memory issues with large files
		int lengthToCheck = Math.min(bytes.length, 2048);
		String headerHex = bytesToHex(Arrays.copyOfRange(bytes, 0, lengthToCheck)).toUpperCase();

		// 1. PDF
		if (headerHex.startsWith(PDF_SIG)) 
			return new FileTypeInfo(".pdf", "application/pdf");

		// 2. Images
		if (headerHex.startsWith(JPG_SIG)) 
			return new FileTypeInfo(".jpg", "image/jpeg");
		if (headerHex.startsWith(PNG_SIG)) 
			return new FileTypeInfo(".png", "image/png");
		if (headerHex.startsWith(GIF_SIG)) 
			return new FileTypeInfo(".gif", "image/gif");

		// 3. Office OpenXML / Zip (DOCX, XLSX, ZIP)
		if (headerHex.startsWith(ZIP_SIG)) {
			if (headerHex.contains(DOCX_SUB_SIG)) {
				return new FileTypeInfo(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			} else if (headerHex.contains(XLSX_SUB_SIG)) {
				return new FileTypeInfo(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				return new FileTypeInfo(".zip", "application/zip");
			}
		}

		// 4. OLE2 Legacy Office (DOC, XLS, MSG)
		if (headerHex.startsWith(OLE2_SIG)) {
			if (headerHex.contains(DOC_SUB_SIG)) {
				return new FileTypeInfo(".doc", "application/msword");
			} else if (headerHex.contains(XLS_SUB_SIG)) {
				return new FileTypeInfo(".xls", "application/vnd.ms-excel");
			} else {
				// MSG files also use OLE2 but don't strictly have a simple sub-sig like "Workbook".
				// We default to MSG if it's OLE2 but not DOC or XLS.
				return new FileTypeInfo(".msg", "application/vnd.ms-outlook");
			}
		}

		// 5. Text-based formats (JSON, EML, TXT)
		// Check identifying characters at the start
		String textStart = new String(Arrays.copyOfRange(bytes, 0, Math.min(bytes.length, 20)), StandardCharsets.UTF_8).trim();

		if (textStart.startsWith("{") || textStart.startsWith("[")) {
			return new FileTypeInfo(".json", "application/json");
		}

		// EML often starts with headers like "From", "Received", or "Return-Path"
		if (textStart.startsWith("From") || textStart.startsWith("Received") || textStart.startsWith("Return-Path")) {
			return new FileTypeInfo(".eml", "message/rfc822");
		}

		// Default to Text
		return new FileTypeInfo(".txt", "text/plain");
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
}