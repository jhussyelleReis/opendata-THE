package com.opendatathe.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.opendatathe.entities.User;
import com.opendatathe.utils.JDOUtils;

/**
 * This filter is responsible to check if the user who requests access to given restricted resource is authorized
 * to access it.
 * @author Ismael Sarmento
 *
 */
public class LoginFilter implements Filter {
    
	protected ServletContext servletContext;
    private JDOUtils jdo = new JDOUtils();
 
    @Override
    public void init(FilterConfig filterConfig) {
        servletContext = filterConfig.getServletContext();
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        Gson gson = new Gson();
        User user = gson.fromJson(req.getParameter("data"), User.class);
        if (user == null || !user.isAuth(jdo)) {
            resp.sendRedirect("/login");
            return; 
        }
     
        chain.doFilter(request, response);        
    }

	@Override
	public void destroy() {
	}
 
}