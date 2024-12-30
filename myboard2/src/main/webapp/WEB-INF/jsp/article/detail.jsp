<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 날짜포맷을 위한 라이브러리 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>게시글 상세</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 20px;
}

h1 {
	font-size: 24px;
	margin-bottom: 10px;
	border-bottom: none;
} /* 타이틀 아래 줄 제거 */
.container {
	width: 60%;
	margin: auto;
}

.meta {
	font-size: 14px;
	color: #666;
	margin-bottom: 10px;
}

.separator {
	border-top: 2px solid #ddd;
	margin-bottom: 15px;
}

.content {
	border: 1px solid #ddd;
	padding: 15px;
	margin-bottom: 15px;
}

.button-section {
	margin-top: 20px;
	display: flex; /* 가로 정렬 */
	align-items: center; /* 세로 중앙 정렬 */
	gap: 10px; /* 요소 사이의 간격 */
}

.button-section button, .button-section a, .button-section input[type="password"]
	{
	padding: 5px 10px;
	margin: 0; /* 버튼과 입력 필드의 기본 여백 제거 */
	text-decoration: none;
	color: white;
	background-color: #007BFF;
	border: none;
	cursor: pointer;
	display: inline-block; /* 인라인 블록 요소로 설정 */
}

.button-section input[type="password"] {
	padding: 5px 10px; /* 내부 여백 */
	height: 30px; /* 높이 설정 */
	border: 1px solid #ccc; /* 테두리 추가 */
	color: black; /* 글자 색상 */
	background-color: white; /* 배경색 설정 */
	outline: none; /* 클릭 시 파란색 외곽선 제거 */
	box-shadow: none; /* 그림자 제거 */
	border-radius: 3px; /* 모서리 둥글게 */
}

.button-section a {
	background-color: #6c757d; /* 목록 버튼의 색상 */
	color: white;
}

.button-section button:hover, .button-section a:hover {
	opacity: 0.8; /* 호버 시 투명도 효과 */
}
</style>
</head>
<body>
	<h1>${article.title}</h1>
	<p>작성자: ${article.memberId}</p>
	<p>
		작성일:
		<fmt:formatDate value="${article.createdAt}" pattern="yyyy-MM-dd" />
	</p>
	<p>조회수: ${article.viewCount}</p>
	<hr />

	<!-- 수정 및 삭제 버튼 -->
	<c:if test="${article.memberId == sessionScope.memberId}">
		<div>
			<a
				href="${pageContext.request.contextPath}/edit/${article.articleId}"
				class="button">수정</a>
			<form
				action="${pageContext.request.contextPath}/delete/${article.articleId}"
				method="post" style="display: inline;">
				<button type="submit" class="button">삭제</button>
			</form>
		</div>
	</c:if>

	<hr />

	<div>
		<h2>사진</h2>
		<c:forEach var="photo" items="${article.photos}">
			<div>
				<img src="${pageContext.request.contextPath}${photo.filePath}"
					alt="사진" style="width: 300px; height: auto;">
				<p>
					<a href="${pageContext.request.contextPath}${photo.filePath}"
						download="${photo.fileName}">다운로드</a>
				</p>
			</div>
		</c:forEach>
	</div>

	<hr />
	<div style="white-space: pre-line;">
		<c:out value="${article.content}" />
	</div>

	<hr />
	<a href="${pageContext.request.contextPath}/">목록으로 돌아가기</a>
</body>
</html>
