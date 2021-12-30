package kr.bit.model;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MemberDAO {
	private static SqlSessionFactory sqlSessionFactory; // [O O O O O ]

	// 초기화 블럭-프로그램실행시 딱 한번만 실되는 코드영역
	static {
		try {
			String resource = "kr/bit/mybatis/config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);// 읽기
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 회원전체 리스트보기
	public List<MemberVO> memberList() {
		// [Connection+Statement]=>SqlSession
		SqlSession session = sqlSessionFactory.openSession();
		List<MemberVO> list = session.selectList("memberList");
		session.close();// 반납
		return list;
	}

	// 회원가입(파일업로드 x)
	public int memberInsert(MemberVO vo) {
		
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.insert("memberInsert", vo);
		System.out.println("파일업로드 x--->"+cnt);
		
		session.commit();
		session.close();// 반납
		
		return cnt;
	}

	
	//회원가입(파일업로드 O)
	public int memberInsertFile(MemberVO vo) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.insert("memberInsertFile",vo);
		
		System.out.println("파일업로드 o--->"+cnt);
		session.commit();
		session.close();
		
		return cnt;
		
	}
	
	
	// 회원삭제
	public int memberDelete(int num) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.delete("memberDelete", num);
		session.commit();
		session.close();
		return cnt;
	}

	// 회원상세보기
	public MemberVO memberContent(int num) {
		SqlSession session = sqlSessionFactory.openSession();
		MemberVO vo = session.selectOne("memberContent", num);
		session.close();
		return vo;
	}

	// 회원수정하기
	public int memberUpdate(MemberVO vo) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.update("memberUpdate", vo);
		session.commit();
		session.close();
		return cnt;
	}

	// 회원로그인
	public String memberLogin(MemberVO vo) {
		SqlSession session = sqlSessionFactory.openSession();
		String user_name = session.selectOne("memberLogin", vo);
		session.close();
		return user_name;
	}

	//id중복체크
	public String memberDbcheck(String id) {
		
		SqlSession session = sqlSessionFactory.openSession();
		String dbId = session.selectOne("memberDbcheck",id); //id 또는 null이 넘어옴
		
		String idDouble = "NO";
		
		if(dbId!=null) { //아이디 중복
			idDouble = "YES";
		}
		
		return idDouble; // YES(중복), NO(중복 아님)
	}
}



