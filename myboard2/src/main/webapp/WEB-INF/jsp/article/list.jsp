<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>게시판 목록</title>
<style>
body {
	font-family: Arial, sans-serif;
}

/* 로그인 상태 영역 */
.auth-area {
	text-align: right;
	margin: 20px;
	font-weight: bold;
}

.auth-area a {
	margin-left: 10px;
	text-decoration: none;
	color: blue;
	border: 1px solid #ccc;
	border-radius: 5px;
	padding: 5px 10px;
}

.article-list {
	width: 90%;
	margin: 0 auto;
}

.search-bar {
	display: flex;
	justify-content: space-between;
	margin-bottom: 20px;
}

.search-bar input {
	width: 70%;
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 5px;
}

.search-bar select, .search-bar button {
	padding: 8px;
	margin-left: 10px;
	border: 1px solid #ccc;
	border-radius: 5px;
}

.articles {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
	gap: 20px;
}

.article-item-link {
	text-decoration: none; /* 링크 밑줄 제거 */
	color: inherit; /* 텍스트 색상 유지 */
	display: block; /* a 태그를 블록으로 만들어 article-item 전체를 감쌀 수 있도록 설정 */
}

.article-item {
	border: 1px solid #ddd;
	border-radius: 5px;
	overflow: hidden;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
	margin-bottom: 20px;
	transition: transform 0.2s ease; /* 클릭/호버 효과를 위한 애니메이션 */
}

.article-item:hover {
	transform: scale(1.02); /* 호버 시 약간 확대 */
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 호버 시 그림자 강조 */
}

.thumbnail img {
	width: 100%;
	height: 150px;
	object-fit: cover;
}

.info {
	padding: 10px;
}

.info h3 {
	font-size: 16px;
	margin-bottom: 10px;
}

.pagination {
	display: flex;
	justify-content: center;
	margin-top: 20px;
}

.pagination a {
	padding: 8px 12px;
	margin: 0 5px;
	text-decoration: none;
	color: #333;
	border: 1px solid #ddd;
	border-radius: 5px;
}

.pagination a.active {
	background-color: #007BFF;
	color: white;
}
</style>
</head>
<body>
	<h1>게시판 목록</h1>

	<!-- 로그인/로그아웃 영역 -->
	<div class="auth-area">
		<c:choose>
			<c:when test="${not empty sessionScope.memberId}">
				<!-- 로그인 된 상태 -->
				<span>${sessionScope.memberId}님 안녕하세요.</span>
				<a href="${pageContext.request.contextPath}/member/logout">로그아웃</a>
			</c:when>
			<c:otherwise>
				<!-- 로그인 안 된 상태 -->
				<a href="${pageContext.request.contextPath}/member/login">로그인</a>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="article-list">
		<div class="search-bar">
			<form action="${pageContext.request.contextPath}/" method="get">
				<select name="searchType">
					<option value="title" ${searchType == 'title' ? 'selected' : ''}>제목</option>
					<option value="writer" ${searchType == 'writer' ? 'selected' : ''}>작성자</option>
				</select> <input type="text" name="keyword" placeholder="검색어를 입력하세요.">
				<button type="submit">검색</button>
			</form>
		</div>

		<div class="articles">
			<c:forEach var="article" items="${articles}">
				<!-- 링크를 article-item 전체에 적용 -->
				<a href="${pageContext.request.contextPath}/${article.articleId}"
					class="article-item-link">
					<div class="article-item">
						<div class="thumbnail">
							<img src="${pageContext.request.contextPath}${article.thumbnail}"
								alt="썸네일" style="width: 150px; height: auto;">
						</div>
						<div class="info">
							<h3>${article.title}</h3>
							<p>
								작성일:
								<fmt:formatDate value="${article.createdAt}"
									pattern="yyyy-MM-dd" />
							</p>
							<p>조회수: ${article.viewCount}</p>
						</div>
					</div>
				</a>
			</c:forEach>
		</div>

		<!-- 페이징 영역 -->
		<div class="pagination">
			<!-- 처음 페이지 -->
			<a href="?page=1&searchType=${searchType}&keyword=${keyword}">&laquo;</a>

			<!-- 이전 페이지 -->
			<c:if test="${currentPage > 1}">
				<a
					href="?page=${currentPage - 1}&searchType=${searchType}&keyword=${keyword}">&lt;</a>
			</c:if>

			<!-- 페이지 번호 -->
			<c:forEach begin="${startPage}" end="${endPage}" var="page">
				<a href="?page=${page}&searchType=${searchType}&keyword=${keyword}"
					class="${currentPage == page ? 'active' : ''}"> ${page} </a>
			</c:forEach>

			<!-- 다음 페이지 -->
			<c:if test="${currentPage < totalPages}">
				<a
					href="?page=${currentPage + 1}&searchType=${searchType}&keyword=${keyword}">&gt;</a>
			</c:if>

			<!-- 마지막 페이지 -->
			<a
				href="?page=${totalPages}&searchType=${searchType}&keyword=${keyword}">&raquo;</a>
		</div>
	</div>


	<div>
		<a href="<c:url value='/write' />">글쓰기</a>
	</div>
</body>
</html>
