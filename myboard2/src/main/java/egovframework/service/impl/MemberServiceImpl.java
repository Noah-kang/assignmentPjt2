package egovframework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.mapper.MemberMapper;
import egovframework.service.MemberService;
import egovframework.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberMapper memberMapper;
	
	// 스프링시큐리티 암호화
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	// 회원가입
	@Transactional
	@Override
	public void registerMember(MemberVO member) {
		if(memberMapper.isMemberIdExists(member.getMemberId())) {
			throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
		}
		if (member.getName().length() > 50) {
	        throw new IllegalArgumentException("이름은 최대 50자까지 가능합니다.");
	    }
		// 비밀번호 암호화
		String encyptedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encyptedPassword);
		
		memberMapper.insertMember(member);
	}
	
	// 아이디 중복확인 
	@Override
	public boolean isMemeberIdExists(String memberId) {
		return memberMapper.isMemberIdExists(memberId);
	}
	
	// 로그인 (암호화)
	@Override
	public MemberVO login(String memberId, String rawPassword) {
		MemberVO member = memberMapper.getMemberByMemberId(memberId);
		if (member != null && passwordEncoder.matches(rawPassword, member.getPassword())) {
			return member;
		}
		return null;
	}
	
	
	
}
