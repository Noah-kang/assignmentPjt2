package egovframework.mapper;

import org.apache.ibatis.annotations.Mapper;

import egovframework.vo.MemberVO;

@Mapper
public interface MemberMapper {
	// 아이디 중복확인
	boolean isMemberIdExists(String memberId);
	// 아이디 생성
	void insertMember(MemberVO member);
	// 아이디 가져오기
	MemberVO getMemberByMemberId(String memberId);

}
