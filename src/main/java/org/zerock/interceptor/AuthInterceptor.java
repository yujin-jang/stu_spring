package org.zerock.interceptor;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.zerock.domain.UserVO;
import org.zerock.service.UserService;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger =
			LoggerFactory.getLogger(AuthInterceptor.class);
	
	@Inject
	private UserService service;
	
	@Override
	public boolean preHandle(
			HttpServletRequest request, 
			HttpServletResponse response,
			Object handler)	throws Exception {
		
		HttpSession session = request.getSession();
		//로그인 안된 사람이 로그인 필요한 uri로 접근할 경우
		if(session.getAttribute("login")==null){
			
			System.out.println("current user is not logined");
			// 접근 시도했던 uri를 세션에 기록
			saveDest(request);
			
			// 하지만 과거 자동로그인 설정을 했었더라면!!
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
			if(loginCookie !=null){
				UserVO userVO = service.checkLoginBefore(loginCookie.getValue());
				System.out.println(userVO);
				
				if(userVO != null){
					session.setAttribute("login", userVO);
					return true;
				}
			}
			
			// 로그인 페이지로 이동
			response.sendRedirect("/user/login");
			
			return false;
		}
		
		return true;
	}
	
	// 접근 시도했던 uri 세션 기록
	private void saveDest(HttpServletRequest request){
		
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		
		if(query==null || query.equals("null")){
			query = "";
		}else{
			query = "?" + query;
		}
		
		String method = request.getMethod();		
		if(method.equals("GET")){
			System.out.println("dest : "+(uri+query));
			//세션에 기록
			request.getSession().setAttribute("dest", (uri+query));
		}
	}
}
