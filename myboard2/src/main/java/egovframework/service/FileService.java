package egovframework.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import egovframework.vo.PhotoVO;

public interface FileService {
	// 파일을 실제 경로에 저장, PhotoVO를 DB에 insert
	void saveFile(MultipartFile file, PhotoVO photoFile, HttpServletRequest request);
		
    //photoId를 이용해서 PhotoVO 조회 (삭제된 파일도 포함해서 조회)
    PhotoVO getPhotoById(int photoId);
    
    //photoId에 해당하는 PhotoVO를 소프트 딜리트 (isDeleted = true)
    void softDeletePhoto(int photoId);
    
    //로컬(서버) 파일 시스템에서 해당 파일을 물리적으로 삭제
    //소프트 딜리트와는 별개로, 실제 파일만 지우는 로직
    void deleteFileFromSystem(String filePath);

    //썸네일 갱신: articleId에 속한 모든 사진의 썸네일을 false로 만들고,
    //해당 photoId만 true로 만듦
    void updateThumbnail(int articleId, int photoId);
    
    //필요하다면, articleId 기준으로 소프트 딜리트가 아닌 "영구 삭제" (DB 삭제) 메서드도
    //만들 수 있음 - 여기서는 예시로만 보여드림
    void deletePhotoPermanently(int photoId);
    
    //is_deleted = true 인 Photo 목록 조회
    List<PhotoVO> findDeletedPhotos();
}
