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

public class DatasetsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4443309870910357955L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JDOUtils jdo = new JDOUtils();
		Gson gson = new Gson();
		String params = req.getParameter("data");   
		Dataset dataset = gson.fromJson(params, Dataset.class);
		List<Dataset> result = null;
		if(dataset.getCategory() != null){
			result = jdo.findByAttribute(Dataset.class, "category", dataset.getCategory());
		}else if(dataset.getDescription() != null){
			result = jdo.findByAttributeInAnyPosition(Dataset.class, "description", dataset.getDescription());
		}
		if (result != null && !result.isEmpty()) {
			resp.getWriter().print(gson.toJson(result)); 
		} else {
			resp.getWriter().print("Nenhum conjunto de dados encontrado."); 
		}
	}

}
