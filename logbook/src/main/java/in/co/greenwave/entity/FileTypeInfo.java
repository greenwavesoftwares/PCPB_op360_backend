package in.co.greenwave.entity;

public class FileTypeInfo {
    public String extension;
    public String mimeType;

    public FileTypeInfo(String ext, String mime) {
        this.extension = ext;
        this.mimeType = mime;
    }
}
