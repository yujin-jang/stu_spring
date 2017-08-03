package org.zerock.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.PageMaker;
import org.zerock.domain.SearchCriteria;
import org.zerock.service.BoardService;

@Controller
@RequestMapping("/sboard/*")
public class SearchBoardController {

	private static final Logger logger =
			LoggerFactory.getLogger(SearchBoardController.class);
	
	@Inject
	private BoardService service;
	
	@RequestMapping("/getAttach/{bno}")
	@ResponseBody
	public List<String> getAttach(@PathVariable("bno") Integer bno) throws Exception{
		
		return service.getAttach(bno);
	}
	
	// 리스트 페이지 : cir(현재페이지, 페이지당게시물수, searchType, keyword)
	//                 pageMaker(페이징처리 구성요소, uri 메이커)
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public void listPage(@ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception{
		
		logger.info(cri.toString());
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.listSearchCount(cri));
		
		model.addAttribute("list", service.listSearchCriteria(cri));
		model.addAttribute("pageMaker", pageMaker);
	}
	// 게시물 등록 UI 이동
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public void registerGET(){
		logger.info("register view ............");
	}
	// 게시물 등록 후 리스트로 이동
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPOST(BoardVO board, RedirectAttributes rttr) throws Exception{
		logger.info("register POST ............");
		service.regist(board);
		rttr.addFlashAttribute("msg", "success");
		return "redirect:/sboard/list";
	}
	// 게시물 조회 (BoardVO_조회용, cri_조회후 복귀페이지정보)
	@RequestMapping(value="/readPage", method=RequestMethod.GET)
	public void read(@RequestParam("bno") int bno, 
					 @ModelAttribute("cri") SearchCriteria cri,
					 Model model) throws Exception{
		
		model.addAttribute(service.read(bno));
	}
	// 게시물 삭제 (bno_삭제용, cri_삭제후 복귀페이지정보)
	@RequestMapping(value="/removePage", method=RequestMethod.POST)
	public String remove(@RequestParam("bno") int bno, 
						 SearchCriteria cri, 
						 RedirectAttributes rttr) throws Exception{
		
		service.remove(bno);
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		rttr.addFlashAttribute("msg", "success");
		
		return "redirect:/sboard/list";
	}
	// 게시물수정 UI 이동(Board_초기수정전표시, cri_수정후 복귀페이지정보)
	@RequestMapping(value="/modifyPage", method=RequestMethod.GET)
	public void modifyPagingGET(@RequestParam("bno") int bno, 
								@ModelAttribute SearchCriteria cri, 
								Model model) throws Exception{
		
		logger.info("modify GET.............");
		model.addAttribute(service.read(bno));
	}
	// 게시물 수정후 리스트로 이동(cri_복귀페이지정보)
	@RequestMapping(value="/modifyPage", method=RequestMethod.POST)
	public String modifyPagingPOST(
			BoardVO board,
			SearchCriteria cri,
			RedirectAttributes rttr) throws Exception{
		service.modify(board);
		logger.info("modify POST.............");
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		rttr.addFlashAttribute("msg", "success");
		
		return "redirect:/sboard/list";
	}
}
