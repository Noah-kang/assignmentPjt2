package egovframework.web;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import egovframework.service.ArticleService;
import egovframework.service.FileService;
import egovframework.vo.ArticleVO;
import egovframework.vo.PhotoVO;

@Controller
@RequestMapping("/")
public class ArticleController {
	@Autowired
	private ArticleService articleService;

	@Autowired
	private FileService fileService;

	// 게시글 목록 조회
	@GetMapping
	public String list(@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size, Model model) {
		// 1) 게시글 목록, 총 게시글 수
		List<ArticleVO> articles = articleService.getArticles(page, size, searchType, keyword);
		int totalArticles = articleService.getTotalArticleCount(searchType, keyword);
		// 2) 전체 페이지
		int totalPages = (int) Math.ceil((double) totalArticles / size);
		
		// 3) startPage, endPage 계산
	    //    "10페이지씩" 묶는 예시
	    int startPage = ((page - 1) / 10) * 10 + 1;
	    int endPage = Math.min(startPage + 9, totalPages);

		model.addAttribute("articles", articles);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);   // 페이징에서 사용
	    model.addAttribute("endPage", endPage);       // 페이징에서 사용
	    model.addAttribute("searchType", searchType); // 검색 파라미터 유지
	    model.addAttribute("keyword", keyword);       // 검색 파라미터 유지
		return "article/list";
	}

	// 게시글 등록 페이지 이동
	@GetMapping("/write")
	public String writePage(HttpSession session) {
		if (session.getAttribute("memberId") == null) {
			return "redirect:/member/login";
		}
		return "article/write";
	}

	// 게시글 등록
	@PostMapping("/write")
	public String createArticle(@ModelAttribute ArticleVO article, @RequestParam("files") MultipartFile[] files,
			@RequestParam("thumbnail") String thumbnail, HttpSession session, HttpServletRequest request) {
		// 로그인한 사용자 정보 가져오기
		String memberId = (String) session.getAttribute("memberId");
		// 만약 세션이 풀렸으면 다시 로그인해야합니다.
		if (memberId == null) {
			// 세션에 memberId가 없으면 로그인 페이지로 리다이렉트
			return "redirect:/member/login";
		}
		article.setMemberId(memberId);

		// 게시글 저장 후 생성된 articleId 가져오기
		int articleId = articleService.createArticle(article);

		// 파일 처리 및 썸네일 설정
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					// PhotoVO 생성
					PhotoVO photoFile = new PhotoVO();
					photoFile.setArticleId(articleId);
					photoFile.setFileName(file.getOriginalFilename());
					photoFile.setUuid(UUID.randomUUID().toString());
					photoFile.setFileSize(file.getSize());
					photoFile.setThumbnail(file.getOriginalFilename().equals(thumbnail));

					// 파일 저장 및 경로 설정 (FileService 활용)
					fileService.saveFile(file, photoFile, request);
				}
			}
		}

		return "redirect:/";
	}

	// 게시글 상세 조회
	@GetMapping("/{articleId}")
	public String getArticleDetail(@PathVariable("articleId") int articleId, Model model) {
		// 조회수 증가
		articleService.incrementViewCount(articleId);

		// 게시글 상세 정보 가져오기
		ArticleVO article = articleService.getArticleById(articleId);
		if (article == null) {
			return "redirect:/article?error=notfound"; // 게시글이 없으면 목록으로 리다이렉트
		}

		// 모델에 게시글 정보 추가
		model.addAttribute("article", article);

		return "article/detail";
	}

	// 수정 화면으로 이동
	@GetMapping("/edit/{articleId}")
	public String editArticleForm(@PathVariable("articleId") int articleId, Model model) {
		ArticleVO article = articleService.getArticleById(articleId);
		model.addAttribute("article", article);
		model.addAttribute("actionUrl", "/edit/" + articleId); // 수정 시 POST 요청 URL
		return "article/write";
	}

	// 게시글 수정
	@PostMapping("/edit/{articleId}")
	public String updateArticle(
	    @PathVariable("articleId") int articleId,
	    @ModelAttribute ArticleVO article,
	    @RequestParam(value="files", required=false) MultipartFile[] files,
	    @RequestParam(value="deletedPhotoIds", required=false) String deletedPhotoIds,
	    @RequestParam(value="thumbnail", required=false) String thumbnail,
	    HttpServletRequest request
	) {
	    // 1) 게시글 수정
	    article.setArticleId(articleId);
	    articleService.updateArticle(article);

	    // 2) 기존 파일 소프트 딜리트 (deletedPhotoIds = "3,5,10"...)
	    if (deletedPhotoIds != null && !deletedPhotoIds.isEmpty()) {
	        String[] photoIdArray = deletedPhotoIds.split(",");
	        for (String pid : photoIdArray) {
	            int photoId = Integer.parseInt(pid.trim());
	            // 소프트 딜리트
	            fileService.softDeletePhoto(photoId);

	            // 만약 소프트 딜리트 시점에 로컬 파일도 지우고 싶다면:
	            //PhotoVO photo = fileService.getPhotoById(photoId);
	            //if (photo != null && photo.getFilePath() != null) {
	            //    fileService.deleteFileFromSystem(photo.getFilePath());
	            //}
	        }
	    }

	    // 3) 새로 업로드된 파일 저장
	    if (files != null && files.length > 0) {
	        for (MultipartFile mf : files) {
	            if (!mf.isEmpty()) {
	                PhotoVO photoVO = new PhotoVO();
	                photoVO.setArticleId(articleId);
	                photoVO.setFileName(mf.getOriginalFilename());
	                photoVO.setUuid(UUID.randomUUID().toString());
	                photoVO.setFileSize(mf.getSize());

	                // 썸네일 여부 (새파일은 파일명과 thumbnail이 같으면 true)
	                if (mf.getOriginalFilename().equals(thumbnail)) {
	                    photoVO.setThumbnail(true);
	                } else {
	                    photoVO.setThumbnail(false);
	                }

	                fileService.saveFile(mf, photoVO, request);
	            }
	        }
	    }

	    // 4) 기존 이미지 중 썸네일 지정(숫자면 photoId)
	    //    thumbnail이 "3" 같은 숫자라면, 그 photoId만 true로 갱신
	    if (thumbnail != null && thumbnail.matches("\\d+")) {
	        int thumbPhotoId = Integer.parseInt(thumbnail);
	        fileService.updateThumbnail(articleId, thumbPhotoId);
	    }

	    return "redirect:/" + articleId;
	}

	// 게시글 삭제
	@PostMapping("/delete/{articleId}")
	public String deleteArticle(@PathVariable("articleId") int articleId) {
		articleService.deleteArticle(articleId);
		return "redirect:/";
	}

}
