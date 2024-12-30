package egovframework.vo;

import java.sql.Timestamp;
import java.util.List;

public class ArticleVO {
	// 게시글아이디
	private int articleId;
	// 작성자아이디
	private String memberId;
	// 게시글제목
	private String title;
	// 게시글내용
	private String content;
	// 조회수
	private int viewCount;
	// 삭제여부
	private boolean isDeleted;
	// 생성일
	private Timestamp createdAt;
	// 수정일
	private Timestamp updatedAt;

	private String thumbnail; // 대표 이미지의 파일 이름을 저장

	// PhotoVO 리스트 추가
	private List<PhotoVO> photos;

	// Getter and Setter
	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public List<PhotoVO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoVO> photos) {
        this.photos = photos;
    }

}
