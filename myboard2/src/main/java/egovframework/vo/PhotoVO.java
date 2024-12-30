package egovframework.vo;

import java.sql.Timestamp;

public class PhotoVO {
	private int photoId; // 사진 아이디 (Primary Key)
    private int articleId; // 게시글 아이디 (Foreign Key)
    private String fileName; // 파일 이름
    private String uuid; // UUID를 String으로 변경 Mybatis가 uuid 미지원// 고유 번호
    private String filePath; // 파일 경로
    private long fileSize; // 파일 크기
    private boolean isThumbnail; // 썸네일 여부
    private boolean isDeleted; // 삭제 여부
    private Timestamp uploadedAt; // 업로드 날짜

    // Getter and Setter
    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        isThumbnail = thumbnail;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "PhotoVO{" +
                "photoId=" + photoId +
                ", articleId=" + articleId +
                ", fileName='" + fileName + '\'' +
                ", uuid=" + uuid +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", isThumbnail=" + isThumbnail +
                ", isDeleted=" + isDeleted +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
