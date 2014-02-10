package com.opendatathe.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Simple filter for checking the source of the request, that is, an ajax request
 * targeting the <b>specific element</b> or a request for a <b>complete page</b> - when there is no param 'ajax'
 * or this param has value equals false.
 * @author Ismael Sarmento
 *
 */
public class AjaxFilter implements Filter{

	protected ServletContext servletContext;
 
    @Override
    public void init(FilterConfig filterConfig) {
        servletContext = filterConfig.getServletContext();
    }

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		String data = req.getParameter("data");
		JSONObject json = (JSONObject)JSONValue.parse(data);
		Boolean ajax = (Boolean) json.get("ajax");
		if(ajax == null || !ajax){
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.html");
			requestDispatcher.forward(req, resp);
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
	}
}
