package org.zerock.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class PageMaker {
	
	private int totalCount; //총 게시글 수
	private int startPage; //현재화면의 시작페이지
	private int endPage; //현재화면의 끝페이지
	private boolean prev; //뒤로 이동 허용여부
	private boolean next; //앞으로 이동 허용여부
	//화면에 보여줄 페이지 네비게이터갯수
	private int displayPageNum = 10;
	
	private SearchCriteria cri; // 입력 페이지 정보
	
	public void setCri(SearchCriteria cri){
		this.cri = cri;
	}
	
	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
		//전체 게시물 갯수 구한뒤에 계산 시작
		calcData();
	}
	
	private void calcData(){
		endPage = ((int) (Math.ceil(cri.getPage() /
				  (double)displayPageNum)))*displayPageNum;
		
		startPage = endPage - displayPageNum +1;
		
		int lastEndPage = (int)Math.ceil((totalCount / 
						  (double)cri.getPerPageNum()));
		
		if(endPage>lastEndPage){
			endPage = lastEndPage;
		}
		
		prev = (startPage==1)?false:true;
		next = (endPage==lastEndPage)?false:true;
	}
	
	public String makeQuery(int page){
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		uriBuilder.queryParam("page", page);
		uriBuilder.queryParam("perPageNum", cri.getPerPageNum());
				
		UriComponents uriComponents = uriBuilder.build();
		return uriComponents.toUriString();
	}
	
	public String makeSearch(int page){
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		uriBuilder.queryParam("page", page);
		uriBuilder.queryParam("perPageNum", cri.getPerPageNum());
		uriBuilder.queryParam("searchType", cri.getSearchType());
		
		String keyword = cri.getKeyword();
		if(keyword==null || keyword.trim().length()==0){
			keyword = "";
		}else{
			try {
				keyword = URLEncoder.encode(keyword, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				keyword = "";
			}
		}
		uriBuilder.queryParam("keyword", keyword);
				
		UriComponents uriComponents = uriBuilder.build();
		return uriComponents.toUriString();	
	}
	
	public SearchCriteria getCri(){
		return cri;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public boolean isNext() {
		return next;
	}

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public String toString() {
		return "PageMaker [totalCount=" + totalCount 
				+ ", startPage=" + startPage + ", endPage=" 
				+ endPage + ", prev="
				+ prev + ", next=" + next + ", displayPageNum=" 
				+ displayPageNum + ", cri=" + cri + "]";
	}
}
