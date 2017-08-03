package org.zerock.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static final String LOGIN="login";
	private static final Logger logger =
			LoggerFactory.getLogger(LoginInterceptor.class);
	
	// 기존 HttpSession에 남아있는 정보가 있는 경우 정보를 삭제
	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler)	throws Exception {
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute(LOGIN)!=null){
			System.out.println("clear login data before");
			session.removeAttribute(LOGIN);
		}	
		return true;
	}
	
	// UserController에서 'userVO'라는 객체를 받아 HttpSession에 저장
	@Override
	public void postHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView) throws Exception {
		
		HttpSession session = request.getSession();
		
		/*ModelMap modelMap = modelAndView.getModelMap();
		Object userVO = modelMap.get("userVO");*/
		Object userVO = modelAndView.getModel().get("userVO");
		
		if(userVO!=null){
			System.out.println("new login success");
			
			session.setAttribute(LOGIN, userVO);
			//remember me 체크박스에 체크 한 경우
			if(request.getParameter("useCookie")!=null){
				System.out.println("remember me...............");
				
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				loginCookie.setPath("/");
				loginCookie.setMaxAge(60*60*24*7);//일주일
				response.addCookie(loginCookie);	
			}else{
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				loginCookie.setPath("/");
				loginCookie.setMaxAge(0);//일주일
				response.addCookie(loginCookie);	
			}
			
			
			// 로그인을 필요로하는 uri에 접근하여 로그인페이지로 온 경우
			// 기존 접근했던 uri 정보 불러오기
			Object dest = session.getAttribute("dest");
			
			response.sendRedirect((dest!=null)?(String)dest : "/");
		}
	}	
}
