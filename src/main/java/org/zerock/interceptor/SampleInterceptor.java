package org.zerock.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SampleInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws Exception {
		
		System.out.println("pre handle................");
		
		HandlerMethod method = (HandlerMethod) handler;
		
		System.out.println("Bean : " + method.getBean());
		System.out.println("Method : "+ method.getMethod());
		
		// 다음 인터셉터나 대상 컨트롤러를 호출할 것인지 여부 결정(true/false)
		return true;
	}

	@Override
	public void postHandle(
			HttpServletRequest request, 
			HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("post handle................");
		
		Object result = modelAndView.getModel().get("result");
		
		if(result != null){
			request.getSession().setAttribute("result", result);
			response.sendRedirect("/doA");
		}
	}
}
