package egovframework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.vo.PhotoVO;

public interface FileMapper {
	// PhotoVO 저장(INSERT)
	void insertFile(PhotoVO photoFile);

	// PhotoVO 단일 조회
	PhotoVO selectPhotoById(@Param("photoId") int photoId);

	// 소프트 딜리트 (is_deleted = true)
	void updateFileSoftDelete(@Param("photoId") int photoId);

	// 게시글(articleId)의 모든 사진 썸네일 false로 초기화
	void resetThumbnail(@Param("articleId") int articleId);

	// 특정 photoId만 썸네일 true
	void setThumbnail(@Param("photoId") int photoId);

	// 영구 삭제 (물리 삭제) - 미사용
	void deleteFilePermanently(@Param("photoId") int photoId);

	// 게시글 ID로 묶인 모든 photo 레코드를 softDelete
	void softDeletePhotosByArticleId(@Param("articleId") int articleId);

	// 소프트 딜리트된(is_deleted=true) 사진들 조회
	List<PhotoVO> selectDeletedPhotos();

}
