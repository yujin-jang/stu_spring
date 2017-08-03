package org.zerock.service;

import java.util.List;

import org.zerock.domain.ReplyCriteria;
import org.zerock.domain.ReplyVO;

public interface ReplyService {
	
	public List<ReplyVO> listAll(Integer bno) throws Exception;
		
	public void addReply(ReplyVO vo) throws Exception;
	
	public void modifyReply(ReplyVO vo) throws Exception;
	
	public void removeReply(Integer rno) throws Exception;
	
	public List<ReplyVO> listReplyPage(Integer bno, ReplyCriteria cri) throws Exception;
	
	public int count(Integer bno) throws Exception;
}
