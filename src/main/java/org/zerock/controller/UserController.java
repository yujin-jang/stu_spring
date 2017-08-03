package org.zerock.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;
import org.zerock.domain.UserVO;
import org.zerock.dto.LoginDTO;
import org.zerock.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Inject
	private UserService service;
	
	// 로그인 화면으로 이동
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public void loginGET() throws Exception{
		
	}
	// 로그인 처리
	@RequestMapping(value="/loginPost", method=RequestMethod.POST)
	public void loginPOST(LoginDTO dto, HttpSession session, Model model) throws Exception{
		
		UserVO vo = service.login(dto);
	
		if(vo==null){
			return;
		}
		model.addAttribute("userVO", vo);
		// rememberMe 체크했을 경우
		if(dto.isUseCookie()){
			int amount = 60*60*24*7;
			
			Date sessionLimit = new Date(System.currentTimeMillis()+(amount*1000));
			// tbl_user 에 해당 uid를 가진 자료에 sessionkey와 sessionlimit 컬럼 업댓
			service.keepLogin(vo.getUid(), session.getId(), sessionLimit);
		}
	}
	
	// 로그아웃 처리
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(
			HttpServletRequest request, 
			HttpServletResponse response,
			HttpSession session) throws Exception{
		
		Object obj = session.getAttribute("login");
		if(obj!=null){
			// 세션에 기록된 UserVO객체 삭제
			session.removeAttribute("login");
			session.invalidate();
			
			// 기록된 쿠키 삭제
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
			loginCookie.setPath("/");
			loginCookie.setMaxAge(0);
			response.addCookie(loginCookie);
			
			// DB에 저장된 내용 업댓
			UserVO vo = (UserVO) obj;
			service.keepLogin(vo.getUid(), session.getId(), new Date());
		}
		
		return "/user/login";
	}
}
