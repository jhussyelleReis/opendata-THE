package com.opendatathe.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;
import com.opendatathe.entities.User;
import com.opendatathe.utils.JDOUtils;

public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 8851805141472171824L;


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//String confirmCode = req.getParameter("confirmCode");
		String data = req.getParameter("data");
		Gson gson = new Gson();
		User user = gson.fromJson(data, User.class);
		JDOUtils jdo = new JDOUtils();
		User user2 = jdo.findFirstByAttribute(User.class, "email", user.getEmail());
		if(user2.getConfirmCode().equals(user.getConfirmCode())){
			user2.setConfirmed(true);
			jdo.save(user2);
			resp.getWriter().write("{confirmed:true}");
		} else{
			resp.getWriter().write("{confirmed:false}");
		}
	}


	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JDOUtils jdo = new JDOUtils();
		String login = req.getParameter("data");
		Gson gson = new Gson();
		User user = gson.fromJson(login, User.class);
		List<User> result = jdo.findByAttribute(User.class, "email", user.getEmail());
		if(result == null || result.isEmpty()){
			user.setConfirmed(false);
			user.setConfirmCode(new Random(System.nanoTime()).nextInt(100));
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			User user2 = jdo.save(user);
			resp.getWriter().write("{user:{id:"+user2.getId()+",email:"+user2.getEmail()+",confirmCode:"+user2.getConfirmCode()+"}}");//TODO format string
		}else{
			resp.getWriter().write("{'error':'user already exists.'}");
		}
	}
}
