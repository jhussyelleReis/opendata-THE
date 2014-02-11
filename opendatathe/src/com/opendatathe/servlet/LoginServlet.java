package com.opendatathe.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.opendatathe.entities.User;
import com.opendatathe.utils.JDOUtils;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -3776830881235222642L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("pages/login_signup.html");
		requestDispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JDOUtils jdo = new JDOUtils();
		String data = req.getParameter("data");
		Gson gson = new Gson();
		User user = gson.fromJson(data, User.class);
		if(user.isAuth(jdo)){
			resp.getWriter().write("login OK");
		}else {
			resp.sendRedirect("/login"); //incluir mensagem
		}
	}
	
}






