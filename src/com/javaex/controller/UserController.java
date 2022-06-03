package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		
		if("joinForm".equals(action)) {//조인폼
			//조인폼 포워드
			WebUtil.forward(request, response, "./WEB-INF/views/user/joinForm.jsp");
			
		} else if("join".equals(action)) {
			
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo(id,password,name,gender);
			System.out.println(userVo);
			
			UserDao userDao = new UserDao();
			userDao.insert(userVo);
			
			WebUtil.forward(request, response,"./WEB-INF/views/user/joinOk.jsp");
			 
		} else if("loginForm".equals(action)) { //로그인폼
			
			WebUtil.forward(request, response,"./WEB-INF/views/user/loginForm.jsp");
			
		} else if("login".equals(action)) {//로그인
		
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			UserVo userVo = new UserVo();
			userVo.setId(id);
			userVo.setPassword(password);
			
			UserDao userDao = new UserDao();
			UserVo authUser  = userDao.getUser(userVo); //id, password
			
			//authUser 주소값이 있으면 -->로그인 성공
			//authUser null 주소값이 없으면 -->로그인 실패
			if(authUser == null) {
				System.out.println("로그인 실패");
			}else {
				System.out.println("로그인 성공");
				
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authUser);
				
				//메인 리다이렉트
				WebUtil.redirect(request, response, "/mysite2/main");
			}
		} else if("logout".equals(action)) {
			//세션값을 지운다
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			//메인으로 리다이렉트
			WebUtil.redirect(request, response, "/mysite2/main");
		} else if("modifyForm".equals(action)) {
			System.out.println("123456");
		} else if("modify".equals(action)) {
			
		}
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
