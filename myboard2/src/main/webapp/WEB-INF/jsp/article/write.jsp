<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 등록</title>
<style>
.container {
	width: 600px;
	margin: 0 auto;
	font-family: Arial, sans-serif;
}

.form-group {
	margin-bottom: 20px;
}

label {
	font-weight: bold;
	display: block;
	margin-bottom: 5px;
}

input[type="text"], textarea, input[type="file"] {
	width: 100%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 5px;
}

textarea {
	height: 150px;
}

.buttons {
	text-align: center;
}

button {
	padding: 10px 20px;
	margin: 5px;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.cancel {
	background-color: #f44336;
	color: white;
}

.submit {
	background-color: #4CAF50;
	color: white;
}

.file-preview-container {
	margin-top: 10px;
	display: flex;
	flex-wrap: wrap;
	gap: 10px;
}

.file-preview {
	position: relative;
	width: 100px;
	height: 100px;
	border: 1px solid #ccc;
	border-radius: 5px;
	overflow: hidden;
}

.file-preview img {
	width: 100%;
	height: 100%;
	object-fit: cover;
	cursor: pointer;
}

.file-preview .delete-btn {
	position: absolute;
	top: 5px;
	right: 5px;
	background-color: red;
	color: white;
	border: none;
	border-radius: 50%;
	width: 20px;
	height: 20px;
	font-size: 14px;
	cursor: pointer;
}
/* 썸네일 선택 시 강조 표시 */
.thumbnail-selected {
	border: 3px solid blue !important;
}
</style>
</head>
<body>
	<div class="container">
		<c:choose>
			<c:when test="${not empty article}">
				<!-- 수정 모드 -->
				<c:set var="formAction"
					value="${pageContext.request.contextPath}/edit/${article.articleId}" />
				<h1>게시글 수정</h1>
			</c:when>
			<c:otherwise>
				<!-- 등록 모드 -->
				<c:set var="formAction"
					value="${pageContext.request.contextPath}/write" />
				<h1>게시글 등록</h1>
			</c:otherwise>
		</c:choose>
		<form action="${formAction}" method="post"
			enctype="multipart/form-data">
			<!-- 글 작성자 -->
			<div class="form-group">
				<label for="memberId">작성자 <span style="color: red;">*</span></label>
				<input type="text" id="memberId" name="memberId"
					value="${article != null ? article.memberId : sessionScope.memberId}"
					readonly>
			</div>

			<!-- 글 제목 -->
			<div class="form-group">
				<label for="title">제목 <span style="color: red;">*</span></label> <input
					type="text" id="title" name="title"
					value="${article != null ? article.title : ''}"
					placeholder="제목을 입력하세요." required>
			</div>

			<!-- 글 내용 -->
			<div class="form-group">
				<label for="content">내용</label>
				<div id="content" name="content" contenteditable="true"
					style="width: 100%; height: 150px; border: 1px solid #ccc; border-radius: 5px; padding: 10px; overflow-y: auto;">
					${article != null ? article.content : ''}</div>
				<!-- 숨겨진 textarea: 최종적으로 여기 담아 서버로 전송 -->
				<textarea id="hiddenContent" name="content" style="display: none;"></textarea>
			</div>

			<!-- 파일 업로드 -->
			<div class="form-group">
				<label for="file">첨부파일</label> <input type="file" id="file"
					name="files" multiple accept="image/*">

				<!-- 미리보기 영역 -->
				<div id="file-preview" class="file-preview-container">
					<!-- 기존 이미지 (DB에서 불러온) 미리보기 -->
					<c:if test="${article != null && article.photos != null}">
						<c:forEach var="photo" items="${article.photos}">
							<div class="file-preview">
								<!-- 기존 이미지는 filePath로부터 보여준다 -->
								<img src="${pageContext.request.contextPath}${photo.filePath}"
									alt="이미지" data-photo-id="${photo.photoId}"
									class="existing-photo-img">
								<button type="button" class="delete-btn"
									data-photo-id="${photo.photoId}">X</button>
							</div>
						</c:forEach>
					</c:if>
				</div>
				<!-- 썸네일 구분값 -->
				<input type="hidden" id="thumbnail" name="thumbnail"
					value="${article != null ? article.thumbnail : ''}">
			</div>

			<!-- 삭제할 photoId 모음용(hidden) -->
			<input type="hidden" id="deletedPhotoIds" name="deletedPhotoIds"
				value="">

			<!-- 버튼 -->
			<div class="buttons">
				<a href="${pageContext.request.contextPath}/" class="cancel">취소</a>
				<button type="submit" class="submit">${article != null ? "수정" : "저장"}</button>
			</div>
		</form>
	</div>

	<script>
    // --------------------------------------------------------------------------------
    // [1] 페이지 로딩 시, 기존 이미지에 대한 썸네일 클릭 이벤트/삭제(X) 이벤트 바인딩
    // --------------------------------------------------------------------------------
    // 썸네일 클릭하면 파란색 테두리 + #thumbnail 값에 photoId 저장
    document.querySelectorAll('.existing-photo-img').forEach(function(imgElem) {
        imgElem.addEventListener('click', function() {
            // 모든 썸네일 테두리 해제
            document.querySelectorAll('#file-preview img').forEach(function(img) {
                img.classList.remove('thumbnail-selected');
            });
            // 현재 클릭된 이미지에만 테두리 적용
            imgElem.classList.add('thumbnail-selected');
            
            // photoId를 썸네일에 세팅
            var photoId = imgElem.getAttribute('data-photo-id');
            document.getElementById('thumbnail').value = photoId;
        });

        // 내용영역(#content)에도 해당 이미지를 추가해보기 (기존 이미지도 내용 미리보기)
        var contentDiv = document.getElementById('content');
        var cloneImg = imgElem.cloneNode(true);
        // 기존 이미지의 delete-btn은 필요없으니 alt나 style만 조절
        cloneImg.style.width = '100px';
        cloneImg.style.height = '100px';
        cloneImg.style.objectFit = 'cover';
        contentDiv.appendChild(cloneImg);
    });

    // 기존 이미지 X 버튼: 삭제할 photoId를 hidden input(deletedPhotoIds)에 모아둠
    document.querySelectorAll('.delete-btn').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var photoId = btn.getAttribute('data-photo-id');
            if (!photoId) return;

            // [선택] 실제로 삭제할 건지 사용자에게 묻기
            if (!confirm('정말 이 이미지를 삭제하시겠습니까?')) {
                return;
            }

            // 삭제할 이미지를 hidden input에 추가 (콤마로 구분)
            var deletedInput = document.getElementById('deletedPhotoIds');
            if (deletedInput.value === '') {
                deletedInput.value = photoId;
            } else {
                deletedInput.value += ',' + photoId;
            }

            // 미리보기 영역에서 제거
            var previewDiv = btn.parentNode;
            previewDiv.parentNode.removeChild(previewDiv);

            // #content 영역에서 동일 photoId를 가진 img도 제거
            // (기존 이미지 태그는 data-photo-id가 있으므로 그것을 기준으로 제거)
            var contentDiv = document.getElementById('content');
            var imgsInContent = contentDiv.querySelectorAll('img[data-photo-id]');
            imgsInContent.forEach(function(img) {
                if (img.getAttribute('data-photo-id') === photoId) {
                    img.remove();
                }
            });

            // 썸네일이 이 이미지였으면 썸네일값 초기화
            var thumbnailInput = document.getElementById('thumbnail');
            if (thumbnailInput.value === photoId) {
                thumbnailInput.value = '';
            }
        });
    });

    // --------------------------------------------------------------------------------
    // [2] 새로 업로드한 이미지 미리보기 + X버튼 + 썸네일 지정
    // --------------------------------------------------------------------------------
    document.getElementById('file').addEventListener('change', function(event) {
        const filePreviewContainer = document.getElementById('file-preview');
        const thumbnailInput = document.getElementById('thumbnail');
        const contentDiv = document.getElementById('content');

        // 새로 파일 선택 시 기존(새로 선택된) 미리보기 모두 초기화
        // (이미 DB에 있던 기존 이미지들은 건드리지 않음)
        // 만약 '새로 선택된 파일' 들을 한 번 더 변경해서 리스트를 갱신해야 한다면,
        // 기존에 append했던 요소를 제거하는 로직이 필요합니다.
        // 여기서는 간단히 "새로 올라간 이미지 미리보기"만 없애보겠습니다.
        // 기존 이미지는 .existing-photo-img을 사용하므로 여기선 구분 처리
        const newlyUploadedPreviews = filePreviewContainer.querySelectorAll('.new-file-wrapper');
        newlyUploadedPreviews.forEach(div => div.remove());
        
        // 내용영역에 들어간 '새로 추가된 이미지'만 제거해준다(기존 이미지는 유지).
        const newImgsInContent = contentDiv.querySelectorAll('img[data-new-file="true"]');
        newImgsInContent.forEach(img => img.remove());

        const files = Array.from(event.target.files);

        files.forEach((file, index) => {
            if (file.type.startsWith('image/')) {
                const imageUrl = URL.createObjectURL(file);
                // [a] 미리보기 DOM 생성
                const fileWrapper = document.createElement('div');
                fileWrapper.className = 'file-preview new-file-wrapper'; // 구분용 class
                fileWrapper.style.position = 'relative';
                
                const img = document.createElement('img');
                img.src = imageUrl;
                img.style.width = '100%';
                img.style.height = '100%';
                img.style.objectFit = 'cover';
                
                // 썸네일 클릭(새파일)은 파일 이름으로 구분
                img.addEventListener('click', function() {
                    // 모든 썸네일 선택 해제
                    document.querySelectorAll('#file-preview img').forEach(function(th) {
                        th.classList.remove('thumbnail-selected');
                    });
                    img.classList.add('thumbnail-selected');
                    thumbnailInput.value = file.name; // 새로 올린 파일이면 파일명을 썸네일로
                });

                // [b] X 버튼 (새로 업로드한 파일 제거)
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'X';
                deleteButton.classList.add('delete-btn');
                deleteButton.style.position = 'absolute';
                deleteButton.style.top = '5px';
                deleteButton.style.right = '5px';
                
                deleteButton.addEventListener('click', function() {
                    // 배열에서 제거
                    files.splice(index, 1);
                    // 새 FileList 만들기
                    const dt = new DataTransfer();
                    files.forEach(f => dt.items.add(f));
                    document.getElementById('file').files = dt.files;

                    // 미리보기 DOM 제거
                    fileWrapper.remove();
                    
                    // 내용영역에서 이 이미지 제거
                    contentImg.remove();
                    
                    // 썸네일이 이 파일명이었다면 썸네일값 초기화
                    if (thumbnailInput.value === file.name) {
                        thumbnailInput.value = '';
                    }
                });

                fileWrapper.appendChild(img);
                fileWrapper.appendChild(deleteButton);
                filePreviewContainer.appendChild(fileWrapper);

                // [c] 내용영역에 이미지 추가
                const contentImg = img.cloneNode(true);
                contentImg.setAttribute('data-new-file', 'true'); // 새 파일 이미지 구분
                contentImg.style.width = '100px';
                contentImg.style.height = '100px';
                contentImg.style.objectFit = 'cover';
                contentDiv.appendChild(contentImg);

            } else {
                alert('이미지 파일만 업로드 가능합니다.');
            }
        });
    });

    // --------------------------------------------------------------------------------
    // [3] 폼 전송 시, contenteditable div 내용을 textarea로 옮기기
    //     (이미지는 지우고 텍스트만 저장하려면 아래 로직 계속 사용)
    // --------------------------------------------------------------------------------
    document.querySelector('form').addEventListener('submit', function(event) {
        const contentDiv = document.getElementById('content');
        const hiddenTextarea = document.getElementById('hiddenContent');

     	// HTML 파싱 후 줄바꿈 처리
        const htmlContent = contentDiv.innerHTML;
        
     	// <div>와 <br>을 줄바꿈으로 변환
        let textWithLineBreaks = htmlContent
            .replace(/<div>/g, '\n')   // <div>는 줄바꿈으로 변환
            .replace(/<\/div>/g, '')  // </div> 제거
            .replace(/<br\s*\/?>/g, '') // <br> 태그를 제거
            .trim(); // 앞뒤 공백 제거
        
        // ★ 이미지 태그를 문자열에서 제거
        textWithLineBreaks = textWithLineBreaks.replace(/<img[^>]*>/g, '');
        // 변환된 텍스트를 숨겨진 textarea에 저장
        hiddenTextarea.value = textWithLineBreaks;
    });
</script>
</body>
</html>
