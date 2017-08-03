package org.zerock.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardVO;
import org.zerock.domain.SearchCriteria;
import org.zerock.persistence.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {
	@Inject
	private BoardDAO dao;
	
	@Transactional
	@Override
	public void regist(BoardVO board) throws Exception {
		dao.create(board);
		
		String[] files = board.getFiles();
		if(files == null){ return; }
		for(String fileName : files){
			dao.addAttach(fileName);
		}
	}
	
	// 다른 연결이 커밋하지 않은 데이터는 볼 수 없도록(기본)
	@Transactional(isolation=Isolation.READ_COMMITTED)
	@Override
	public BoardVO read(Integer bno) throws Exception {
		dao.updateViewCnt(bno);
		return dao.read(bno);
	}
	
	@Transactional
	@Override
	public void modify(BoardVO board) throws Exception {
		//기존 게시글 수정
		dao.update(board);
		
		Integer bno = board.getBno();
		//tbl_attach 에 bno에 해당하는 리스트 삭제
		dao.deleteAttach(bno);
		
		String[] files = board.getFiles();
		
		if(files == null){return;}
		
		for(String fileName : files){
			// 새로운 첨부파일정보들 입력(insert)
			dao.replaceAttach(fileName, bno);
		}
	}

	@Transactional
	@Override
	public void remove(Integer bno) throws Exception {
		// tbl_attach 의 bno는 tbl_boarddml bno의 외래키 관계이므로
		// attach 를 먼저 지우고나서 board를 지워야 함
		dao.deleteAttach(bno);
		dao.delete(bno);		
	}

	@Override
	public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception {

		return dao.listSearch(cri);
	}

	@Override
	public int listSearchCount(SearchCriteria cri) throws Exception {

		return dao.listSearchCount(cri);
	}

	@Override
	public List<String> getAttach(Integer bno) throws Exception {

		return dao.getAttach(bno);
	}
}
