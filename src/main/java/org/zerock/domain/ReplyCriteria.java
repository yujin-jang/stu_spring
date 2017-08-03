package org.zerock.domain;

public class ReplyCriteria {

	private int page;
	private int perPageNum;
	
	public ReplyCriteria() {
		this.page = 1;
		this.perPageNum = 10;
	}
	// 댓글 페이지번호
	public void setPage(int page) {
		if(page<=0){
			page = 1;
			return;
		}
		this.page = page;
	}
	//댓글 페이지당 보여줄 게시물 개수
	public void setPerPageNum(int perPageNum) {
		if(perPageNum<=0 || perPageNum>100){
			this.perPageNum=10;
			return;
		}
		this.perPageNum = perPageNum;
	}
	
	public int getPage() {
		return page;
	}
	// MyBatis SQL Mapper에서 사용하는 getter를 제공
	public int getPageStart(){
		//setPage에서 set하는 page는 화면에보이는 1,2,3,4 .. 이므로
		//sql에서 사용할 page로 변환해 주어야 함.(0, 10, 20, 30, ...)
		return ((this.page-1)*this.perPageNum);
	}
	// MyBatis SQL Mapper에서 사용하는 getter를 제공	
	public int getPerPageNum() {
		return perPageNum;
	}

	@Override
	public String toString() {
		return "ReplyCriteria [page=" + page 
				+ ", perPageNum=" + perPageNum + "]";
	}	
}
