package egovframework.service;

import java.util.List;

import egovframework.vo.ArticleVO;

public interface ArticleService {
	//게시글 생성
	int createArticle(ArticleVO article);
	//게시글 목록 조회
    List<ArticleVO> getArticles(int page, int size, String searchType, String keyword);
    //총게시글 수 조회
    int getTotalArticleCount(String searchType, String keyword);
    //게시글 상세조회
    ArticleVO getArticleById(int articleId);
    //조회수 증가
    void incrementViewCount(int articleId);
    //게시글 수정
    void updateArticle(ArticleVO article);
    //게시글 삭제 - 미완
    void deleteArticle(int articleId);
    
}
