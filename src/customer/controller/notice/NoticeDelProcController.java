package customer.controller.notice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import customer.controller.Controller;
import customer.dao.NoticeDao;
import customer.vo.Notice;

public class NoticeDelProcController implements Controller{
	public void execute(HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		request.setCharacterEncoding("utf-8");
		
		System.out.println("========== < NoticeDelProcController > ==========");
	
		String seq=request.getParameter("c");
		
		NoticeDao dao=new NoticeDao();
		int del=dao.delete(seq);
		
		response.sendRedirect("notice.do");
		
	}
}
