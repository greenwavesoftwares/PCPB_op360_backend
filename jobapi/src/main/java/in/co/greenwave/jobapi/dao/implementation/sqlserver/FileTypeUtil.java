package in.co.greenwave.jobapi.dao.implementation.sqlserver;

import in.co.greenwave.jobapi.model.FileTypeInfo;

public class FileTypeUtil {

    public static FileTypeInfo detect(byte[] bytes) {

        String hex = bytesToHex(bytes).toUpperCase();

        if (hex.startsWith("FFD8FF"))
            return new FileTypeInfo(".jpg", "image/jpeg");

        if (hex.startsWith("89504E47"))
            return new FileTypeInfo(".png", "image/png");

        if (hex.startsWith("25504446"))
            return new FileTypeInfo(".pdf", "application/pdf");

        if (hex.startsWith("504B0304"))
            return new FileTypeInfo(".zip", "application/zip");

        return new FileTypeInfo(".bin", "application/octet-stream");
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
