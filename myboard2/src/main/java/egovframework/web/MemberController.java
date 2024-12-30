package egovframework.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.service.MemberService;
import egovframework.vo.MemberVO;



@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	// 회원가입 화면
	@GetMapping("/register")
	public String registerForm() {
		return "member/register";
	}
	
	// 회원가입 처리
	@PostMapping("/register")
	public String register(@ModelAttribute MemberVO member, Model model) {
		try {
			// 사용자 정보 저장 
			memberService.registerMember(member);
			return "redirect:/member/login";
		} catch (IllegalArgumentException e) {
			// 실패시 에러 
			model.addAttribute("erroMessage", e.getMessage());
			return "member/register";
		}
	}
	
	// 로그인 화면
	@GetMapping("/login")
	public String loginForm() {
		return "member/login";
	}
	
	// 로그인 처리 (암호화)
	@PostMapping("/login")
	public String login(@RequestParam String memberId,@RequestParam String password,HttpSession session, Model model) {
		MemberVO member = memberService.login(memberId, password);
		if (member !=null) {
			//로그인 성공
			session.setAttribute("memberId", member.getMemberId());
			//memberSn 사용유무 고민중
			//session.setAttribute("memberSn", member.getMemberSn());
			
			return "redirect:/";
		} else {
			//로그인 실패
			model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
			return "member/login";
			
		}
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	// 아이디 중복확인
	@GetMapping("/checkId")
	public ResponseEntity<Boolean> checkId(@RequestParam("memberId") String memberId) {
	    boolean exists = memberService.isMemeberIdExists(memberId);
	    return ResponseEntity.ok()
	                         .contentType(MediaType.APPLICATION_JSON) // JSON 응답 명시
	                         .body(exists);
	}

}
