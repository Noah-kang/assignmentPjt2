package egovframework.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import egovframework.service.ArticleService;
import egovframework.vo.ArticleVO;

public class ArticleAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private ArticleService articleService;
    //로거 사용
    private static final Logger logger = LoggerFactory.getLogger(ArticleAuthInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    		
    	// 로그인 사용자 ID 가져오기  	
        String memberId = (String) request.getSession().getAttribute("memberId");
        if (memberId == null) {
        	logger.info("비로그인 사용자가 접근을 시도했습니다. Redirecting to login page.");
            response.sendRedirect(request.getContextPath() + "/member/login");
            return false;
        }

        // 요청 경로에서 articleId 추출
        String path = request.getRequestURI();
        String[] segments = path.split("/");
        int articleId = Integer.parseInt(segments[segments.length - 1]);

        // 게시글 작성자 확인
        ArticleVO article = articleService.getArticleById(articleId);
        if (article == null || !memberId.equals(article.getMemberId())) {
        	logger.warn("권한 없는 사용자가 글을 수정/삭제하려 합니다. memberId={}, articleId={}", memberId, articleId);
            response.sendRedirect(request.getContextPath() + "/error/forbidden");
            return false;
        }
        logger.debug("Interceptor triggered for: {}", request.getRequestURI());
        return true;
    }
}
