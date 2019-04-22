package net.javatutorial.tutorials.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/*" }
, initParams = { @WebInitParam(name = "avoid-urls", value = "index.html")})
public class MyFilter implements Filter {
	private ArrayList<String> urlList;

	public void init(FilterConfig config) throws ServletException {
		
		  String urls = config.getInitParameter("avoid-urls"); StringTokenizer token =
		  new StringTokenizer(urls, ",");
		  
		  urlList = new ArrayList<String>();
		  
		  while (token.hasMoreTokens()) { urlList.add(token.nextToken());
		  
		  }
		 
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(false);
//        if (null == session) {
//        	System.out.println("session is null");
////			response.sendRedirect("login.html");
//			request.getRequestDispatcher("jsp/login.html").include(request, response);
////			request.getRequestDispatcher("login.html").include(request, response);
//		}


		String url = request.getServletPath();
		System.out.println(url);
		out.print(url +"<br>filter is invoked before<br>");
		boolean allowedRequest = false;

		if (urlList.contains(url)) {
			allowedRequest = true;
		}

		if (!allowedRequest) {
			
	        String name=request.getParameter("name");  
	        String password=request.getParameter("password");  

	        if(password.equals("admin123")){  
	        out.print("Welcome, "+name);  
            
	        if (session != null) {
	        	session.invalidate();
            }
            
	        session.setMaxInactiveInterval(5*50);
	        session.setAttribute("name",name);  
	        
	        }  
	        else{  
	        	
	            out.print("Sorry, username or password error!");  
	            request.getRequestDispatcher("jsp/login.html").include(request, response);  
	            session.invalidate();
	        }  
			
		}

		chain.doFilter(request, response);// sends request to next resource

		out.print("<br>filter is invoked after");
		out.close();
	}

	public void destroy() {
	}

}