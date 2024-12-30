package egovframework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.mapper.ArticleMapper;
import egovframework.mapper.FileMapper;
import egovframework.service.ArticleService;
import egovframework.vo.ArticleVO;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
    private FileMapper fileMapper; // Photo 관련 Mapper
	
	// 게시글생성
	@Override
	public int createArticle(ArticleVO article) {
		articleMapper.insertArticle(article);
		return article.getArticleId(); // 생성된 articleId 반환
	}

	// 전체게시글 조회
	@Override
	public List<ArticleVO> getArticles(int page, int size, String searchType, String keyword) {
		int offset = (page - 1) * size;
		return articleMapper.getArticles(offset, size, searchType, keyword);
	}

	// 전체게시글 수 조회
	@Override
	public int getTotalArticleCount(String searchType, String keyword) {
		return articleMapper.getTotalArticleCount(searchType, keyword);
	}

	// 게시글 상세조회
	@Override
	public ArticleVO getArticleById(int articleId) {
		return articleMapper.getArticleById(articleId);
	}

	// 조회수 증가
	@Override
	public void incrementViewCount(int articleId) {
		articleMapper.incrementViewCount(articleId);
	}

	// 게시글 수정
	@Override
	public void updateArticle(ArticleVO article) {
		articleMapper.updateArticle(article);
	}

	// 게시글 삭제 - 미완
	@Override
    public void deleteArticle(int articleId) {
        // 1) Article 테이블에서 is_deleted = true
        articleMapper.softDeleteArticle(articleId);

        // 2) Photo 테이블에서도 articleId로 묶인 사진들 모두 is_deleted = true
        fileMapper.softDeletePhotosByArticleId(articleId);
	}
}
