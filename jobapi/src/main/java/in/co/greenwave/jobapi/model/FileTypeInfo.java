package in.co.greenwave.jobapi.model;


public class FileTypeInfo {
    public String extension;
    public String mimeType;

    public FileTypeInfo(String ext, String mime) {
        this.extension = ext;
        this.mimeType = mime;
    }
}
