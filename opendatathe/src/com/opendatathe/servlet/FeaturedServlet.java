package com.opendatathe.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.opendatathe.entities.Dataset;
import com.opendatathe.utils.JDOUtils;

public class FeaturedServlet extends HttpServlet {

	private static final long serialVersionUID = -4288250305606644280L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JDOUtils jdo = new JDOUtils();
		List<Dataset> result = jdo.findByAttribute(Dataset.class, "featured", true);
		resp.getWriter().write(new Gson().toJson(result));
	}

}
