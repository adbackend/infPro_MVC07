package kr.bit.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.bit.model.MemberDAO;

public class MemberDbcheckController implements Controller{
	
	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//$.ajax()
		String id = request.getParameter("id"); // {"id":id}
		
		MemberDAO dao = new MemberDAO();
		String dbDouble = dao.memberDbcheck(id); //YES또는 NO 반환받음
		
		System.out.println("yes or no?   중복이면 yes...-> "+ dbDouble);
		
		response.getWriter().print(dbDouble); //callback함수로 이동 function dbCheck(data)~
		
		return null;
	}
}
