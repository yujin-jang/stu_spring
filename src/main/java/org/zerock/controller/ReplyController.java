package org.zerock.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.ReplyCriteria;
import org.zerock.domain.ReplyPageMaker;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

@RestController
@RequestMapping("/replies")
public class ReplyController {

	@Inject
	private ReplyService service;
	
	// 새로운 댓글 추가
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody ReplyVO vo){
		
		ResponseEntity<String> entity = null;
		try{
			service.addReply(vo);
			entity = new ResponseEntity<String>(
						"success", 
						HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			entity =new ResponseEntity<String>(
						e.getMessage(), 
						HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	// 특정 게시물의 전체 댓글리스트
	@RequestMapping(value="/listAll/{bno}", method=RequestMethod.GET)
	public ResponseEntity<List<ReplyVO>> listAll(@PathVariable("bno") Integer bno){
		ResponseEntity<List<ReplyVO>> entity = null;
		try{
			List<ReplyVO> list = new ArrayList<>();
			list = service.listAll(bno);
			entity = new ResponseEntity<List<ReplyVO>>(list, HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<List<ReplyVO>>(HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
		
	// 특정 댓글을 수정
	@RequestMapping(value="/{rno}",
				method={RequestMethod.PATCH, RequestMethod.PUT})
	public ResponseEntity<String> update(
						@PathVariable("rno") Integer rno, 
						@RequestBody ReplyVO vo){
		
		ResponseEntity<String> entity = null;
		try{
			vo.setRno(rno);
			service.modifyReply(vo);
			entity = new ResponseEntity<String>(
										"success",
										HttpStatus.OK);
		}catch(Exception e){
			entity = new ResponseEntity<String>(
										e.getMessage(),
										HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	// 특정 댓글을 삭제
	@RequestMapping(value="/{rno}")
	public ResponseEntity<String> remove(
							@PathVariable("rno") Integer rno){
		
		ResponseEntity<String> entity = null;
		try{
			service.removeReply(rno);
			entity = new ResponseEntity<String>(
					"success", HttpStatus.OK);
		}catch(Exception e){
			entity = new ResponseEntity<String>(
					e.getMessage(),	HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	// 한페이지 만큼의 댓글 리스트와 페이지 처리정보 가져오기
	// URI를 통해 게시물 번호와 현재 댓글 페이지정보 가져옴
	@RequestMapping(value="/{bno}/{page}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listPage(
								@PathVariable("bno") Integer bno,
								@PathVariable("page") Integer page){
		
		ResponseEntity<Map<String,Object>> entity = null;
		try{
			ReplyCriteria cri = new ReplyCriteria();
			cri.setPage(page); //현재 댓글페이지 정보 입력
			// 특정 게시물의 한페이지 만큼의 댓글 리스트 가져오기
			List<ReplyVO> list = service.listReplyPage(bno, cri);
						
			ReplyPageMaker pageMaker = new ReplyPageMaker();
			// 댓글의 페이징 처리 정보 생성
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(service.count(bno));
			
			// REST방식에선 뷰를 리턴하지 않으므로 Model을 사용하지 못함
			// 그래서 Map에 담아서 데이터 전송
			Map<String, Object> map = new HashMap<>();
			map.put("list", list); // 댓글 리스트와
			map.put("pageMaker", pageMaker); // 페이징 처리 정보
			
			entity = new ResponseEntity<Map<String, Object>>(
											map, HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<Map<String,Object>>(
											HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
}
