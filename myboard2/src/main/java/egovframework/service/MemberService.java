package egovframework.service;

import egovframework.vo.MemberVO;

public interface MemberService {
	// 아이디 등록(비밀번호 암호화) 
	void registerMember(MemberVO member);
	
	// 로그인 (암호화)
	MemberVO login(String userId, String rawPassword);
	// 아이디 중복확인
	boolean isMemeberIdExists(String userId);
	
}
