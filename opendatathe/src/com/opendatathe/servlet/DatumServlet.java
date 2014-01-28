package com.opendatathe.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.opendatathe.entities.Dataset;

public class DatumServlet extends HttpServlet{

	private static final long serialVersionUID = -6319353783561802269L;

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JDOUtils jdo = new JDOUtils();
		String data = req.getParameter("data"); 
		Gson gson =  new Gson();
		Dataset datum = gson.fromJson(data, Dataset.class); 
//		List<Datum> data = jdo.findByAttribute(Datum.class, "name", datum.getName());
//		if(data == null || data.isEmpty()){
			jdo.save(datum); 
			resp.getWriter().print(gson.toJson(datum));
//		}else{
//			resp.getWriter().print(gson.toJson("Ja existe este dado no conjunto."));
//		}
	}

	
}
