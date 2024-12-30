package egovframework.vo;

public class MemberVO {
	private int memberSn; // 사용자 시리얼 번호 PK
	private String memberId; // 사용자 ID
	private String password; // 비밀번호
	private String name; // 이름
	private boolean isDeleted; // 삭제 여부
	private String createdAt; // 생성일
	private String updatedAt; // 수정일
	
	// Getter and Setter
	public int getMemberSn() {
		return memberSn;
	}
	public void setMemberSn(int memberSn) {
		this.memberSn = memberSn;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	

	
	
}
