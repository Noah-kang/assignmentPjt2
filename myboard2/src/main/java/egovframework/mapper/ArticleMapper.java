package egovframework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.vo.ArticleVO;

public interface ArticleMapper {
	// 게시글생성
	void insertArticle(ArticleVO article);

	// 게시글 전체조회(검색)
	List<ArticleVO> getArticles(@Param("offset") int offset, @Param("size") int size,
			@Param("searchType") String searchType, @Param("keyword") String keyword);

	// 게시글전체수 조회
	int getTotalArticleCount(@Param("searchType") String searchType, @Param("keyword") String keyword);

	// 게시글 상세 조회(사진도 조회)
	ArticleVO getArticleById(int articleId);

	// 조회수 증가
	void incrementViewCount(int articleId);
	
	// 게시글 수정
	void updateArticle(ArticleVO article);
	
	// 게시글 소프트 딜리트 (is_deleted = true)
    void softDeleteArticle(@Param("articleId") int articleId);
}
