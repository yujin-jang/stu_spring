<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../include/header.jsp"%>
<script src="/resources/js/upload.js" type="text/javascript"></script>
<script
 src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js">
</script>
<!-- Main content -->
<style type="text/css">
.popup{position: absolute;}
.back{background-color:gray; opacity:0.5; width:100%; height:300%;
		 overflow:hidden; z-index:1101;}
.front{z-index:1110; opacity:1; border:1px; margin:auto;}
.show{
	position:relative;
	max-width:1200px;
	max-height:800px;
	overflow:auto;	
}		 
</style>

<div class="popup back" style="display:none;"></div>
<div id="popup_front" class="popup front" style="display:none;">
	<img id="popup_img">
</div>

<section class="content">
	<div class="row">
		<!-- left column -->
		<div class="col-md-12">
			<!-- general form elements -->
			<div class="box box-primary">
				<div class="box-header">
					<h3 class="box-title">READ BOARD</h3>
				</div>
				<!-- /.box-header -->

<div class="box-body">
	<div class="form-group">
		<label for="exampleInputEmail1">Title</label> <input type="text"
			name='title' class="form-control" value="${boardVO.title}"
			readonly="readonly">
	</div>
	<div class="form-group">
		<label for="exampleInputPassword1">Content</label>
		<textarea class="form-control" name="content" rows="3"
			readonly="readonly">${boardVO.content}</textarea>
	</div>
	<div class="form-group">
		<label for="exampleInputEmail1">Writer</label> <input type="text"
			name="writer" class="form-control" value="${boardVO.writer}"
			readonly="readonly">
	</div>
</div>
<!-- /.box-body -->

<ul class="mailbox-attachments clearfix uploadedList"></ul>

<div class="box-footer">
<c:if test="${login.uid == boardVO.writer}">
	<button type="submit" class="btn btn-warning modifyBtn">Modify</button>
	<button type="submit" class="btn btn-danger removeBtn">REMOVE</button>
</c:if>
	<button type="submit" class="btn btn-primary goListBtn">GO LIST</button>
</div>

			</div>
			<!-- /.box -->
		</div>
		<!--/.col (left) -->
	</div>
	<!-- /.row -->
<form role="form" action="modifyPage" method="post">
	<!-- 수정 및 삭제시 필요한 bno -->
	<input type="hidden" name="bno" value="${boardVO.bno}">
	<!-- 기존 페이지정보 : page와 perPageNum -->
	<input type="hidden" name="page" value="${cri.page}">
	<input type="hidden" name="perPageNum" value="${cri.perPageNum}">
	<input type="hidden" name="searchType" value="${cri.searchType}">
	<input type="hidden" name="keyword" value="${cri.keyword}">
</form>
<!-- 댓글 등록 영역 -->
<div class="row">
	<div class="col-md-12">
		<div class="box box-success">
			<div class="box-header">
				<h3 class="box-title">ADD NEW REPLY</h3>
			</div>
		<c:if test="${not empty login}">
			<div class="box-body">
				<label for="newReplyWriter">Writer</label>
				<input class="form-control" type="text" id="newReplyWriter" 
				 placeholder="USER ID" value="${login.uid}" readonly>
				<label for="newReplyText">ReplyText</label>
				<input class="form-control" type="text"
				 placeholder="REPLY TEXT" id="newReplyText">
			</div>
			<div class="box-footer">
				<button type="submit" class="btn btn-primary"
				 id="replyAddBtn">ADD REPLY</button>
			</div>
		</c:if>
		<c:if test="${empty login}">
			<div class="box-body">
				<div><a href="/user/login">Login Please</a></div>
			</div>
		</c:if>
		</div>
		<!-- 댓글의 목록과 페이징 처리 영역 -->
		<ul class="timeline">
			<li class="time-label" id="repliesDiv">
				<span class="bg-green">Replies List
				<small id='replycntSmall'>
				[ ${boardVO.replycnt} ]</small></span>
			</li>
		</ul>
		
		<div class="text-center">
			<ul id="pagination" class="pagination pagination-sm no-margin">
			</ul>
		</div>
	</div>
</div>

<!-- Modal -->
<div id="modifyModal" class="modal modal-primary fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content -->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
				 &times;</button>
				 <h4 class="modal-title"></h4>
			</div>
			<div class="modal-body" data-rno>
				<p><input type="text" id="replytext" class="form-control"></p>
			</div>
			<div class="modal-footer">
				<button type="button"
				 class="btn btn-info" id="replyModBtn">Modify</button>
				<button type="button"
				 class="btn btn-danger" id="replyDelBtn">DELETE</button>
				<button type="button"
				 class="btn btn-default" data-dismiss="modal"> Close</button>
			</div>
		</div>
	</div>
</div>

</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->
<!-- ----------------------------------------------------------------- -->
<!-- handlebars가 사용하는 HTML로 작성된 별도 템플릿 코드(재사용 장점) -->
<script id="template" type="text/x-handlebars-template">
{{#each.}}
<li class="replyLi" data-rno={{rno}}>
<i class="fa fa-comments bg-blue"></i>
	<div class="timeline-item">
		<span class="time">
			<i class="fa fa-clock-o"></i>{{prettifyDate regdate}}
		</span>
		<h3 class="timeline-header"><strong>{{rno}}</strong> -{{replyer}}</h3>	
		<div class="timeline-body">{{replytext}}</div>
		<div class="timeline-footer">
		{{#eqReplyer replyer}}
			<a class="btn btn-primary btn-xs" data-toggle="modal"
			 data-target="#modifyModal">Modify</a>
		{{/eqReplyer}}
		</div>
	</div>
</li>
{{/each}}
</script>
<!-- -------------------------------------------------------------------- -->
<script id="templateAttach" type="text/x-handlebars-template">
<li data-src='{{fullName}}'>
	<span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}"
	 alt="Attachment"></span>
	<div class="mailbox-attachment-info">
		<a href="{{getLink}}" class="mailbox-attachment-name">{{fileName}}</a>
	</div>
</li>
</script>
<!-- -------------------------------------------------------------------- -->
<script>
var bno = ${boardVO.bno};
var replyPage = 1;

// 'prettifyDate regdate' : handlebar의 기능을 확장
// handlebars는 helper라는 기능을 이용해서 데이터의 상세한 처리에 필요한
// 기능들을 처리(템플릿코드중 prettifyDate라고 적힌 부분은 Helper가 대입함)
Handlebars.registerHelper("prettifyDate", function(timeValue){
	var dateObj = new Date(timeValue);
	var year = dateObj.getFullYear();
	var month = dateObj.getMonth() + 1;
	var date = dateObj.getDate();
	return year+"/"+month+"/"+date;
});

Handlebars.registerHelper("eqReplyer", function(replyer, block){
	var accum='';
	if(replyer == '${login.uid}'){
		accum += block.fn();
	}
	return accum;
});

// 핸들바를 이용한 댓글리스트 출력 메서드
var printData = function(replyArr, target, templateObject){
	//템플릿 생성
	var template = Handlebars.compile(templateObject.html());
	
	// 데이터를 템플릿에 대입한 문자열 생성, regdate는 registerHelper가 대입
	var html = template(replyArr);
	// 기존 댓글목록 제거
	$(".replyLi").remove();
	// 타겟 뒤에 컨텐츠 삽입
	target.after(html);
};

var printPaging = function(pageMaker, target){
	var str = "";
	
	if(pageMaker.prev){
		str += "<li><a href='"+(pageMaker.startPage-1)+"'> << </a></li>";
	}
	for(var i=pageMaker.startPage, len=pageMaker.endPage; i <= len; i++){
		var strClass = (pageMaker.cri.page==i)?'class=active':'';
		str += "<li "+strClass+"><a href='"+i+"'>"+i+"</a></li>";
	}
	if(pageMaker.next){
		str += "<li><a href='"+(pageMaker.endPage+1)+"'> >> </a></li>";
	}
	target.html(str);
};

//pageInfo : /replies/{bno}/{page}
function getPage(pageInfo){
	// map(list, replyPageMaker) 객체를 JSON 타입 객체로 가져오겠지
	$.getJSON(pageInfo, function(data){
		printData(data.list, $("#repliesDiv"), $("#template"));
		printPaging(data.pageMaker, $(".pagination"));
		// 삭제나 수정 후 모달창 감추기 위해 설정
		$("#modifyModal").modal('hide');
	});
};

//Replies List 클릭시
$("#repliesDiv").on("click",function(){
	// Replies List 를 클릭을 반복할 경우
	// 이미 리스트가 있을시 갱신하지 않도록 함
	if($(".timeline li").size() > 1){
		return;
	}
	getPage("/replies/"+bno+"/1");
});

// 댓글 페이지 네비게이터 클릭시(li하위 모든 a태그에 이벤트 위임)
$(".pagination").on("click", "li a", function(event){
	event.preventDefault();
	replyPage = $(this).attr("href");
	getPage("/replies/"+bno+"/"+replyPage);
});

// 댓글 등록의 이벤트 처리
$("#replyAddBtn").click(function(){
	var replyer = $("#newReplyWriter").val();
	var replytext = $("#newReplyText").val();
	
	$.ajax({
		type:'post',
		url:'/replies/',
		headers:{
			"Content-Type":"application/json",
			"X-HTTP-Method-Override":"POST"
		},
		data:JSON.stringify(
				{bno:bno, replyer:replyer, replytext:replytext}),
		dataType:'text',
		success:function(result){
			if(result=='success'){
				alert("등록 되었습니다.");
				replyPage = 1;
				getPage("/replies/"+bno+"/"+replyPage);
				$("#newReplyWriter").val("");
				$("#newReplyText").val("");
			}
		}
	});
});

// timeline이라는 <ul>아래의 모든 replyLi라는 <li>태그에 위임
// timeline이라는 <ul>은 존재하는것이나 <li>는 클릭해야 생기는부분
$(".timeline").on("click", ".replyLi", function(){
	var reply = $(this);//replyLi라는 <li>
	var replytext = reply.find('.timeline-body').text();
	var data_rno = reply.attr("data-rno");
	
	$("#replytext").val(replytext);
	$(".modal-title").text(data_rno);	
});

$("#replyModBtn").click(function(){
	
	var rno = $(".modal-title").text();
	var replytext = $("#replytext").val();
	
	$.ajax({
		type:'put',
		url:'/replies/'+rno,
		headers:{
			"Content-Type":"application/json",
			"X-HTTP-Method-Override":"PUT"
		},
		data:JSON.stringify({replytext:replytext}),
		dataType:'text',
		success:function(result){
			if(result=='success'){
				alert("수정 되었습니다.");
				getPage("/replies/"+bno+"/"+replyPage);
			}
		}
	});
});

$("#replyDelBtn").click(function(){
	
	var rno = $(".modal-title").text();
	
	$.ajax({
		type:'delete',
		url:'/replies/'+rno,
		headers:{
			"Content-Type":"application/json",
			"X-HTTP-Method-Override":"DELETE"
		},
		dataType:'text',
		success:function(result){
			if(result=='success'){
				alert("삭제 되었습니다.");
				getPage("/replies/"+bno+"/"+replyPage);
			}
		}
	});
});

</script>

<script>				
$(document).ready(function(){	
	var formObj = $("form[role='form']");
	
	console.log(formObj);
	
	$(".modifyBtn").on("click", function(){
		formObj.attr("action", "/sboard/modifyPage");
		formObj.attr("method", "get");		
		formObj.submit();
	});
	
	$(".removeBtn").on("click", function(){
		
		var replyCnt = $("#replycntSmall").html().replace(/[^0-9]/g,"");
		if(replyCnt>0){
			alert("댓글 삭제 기능을 안만들어서 댓글있는건 삭제 안할게요");
			return;
		}
		
		var arr=[];
		$(".uploadedList li").each(function(index){
			arr.push($(this).attr("data-src"));
		});
		
		if(arr.length>0){
			$.post("/deleteAllFiles",{files:arr}, function(){
				
			});
		}
		
		formObj.attr("action", "/sboard/removePage");
		formObj.submit();
	});
	
	$(".goListBtn").on("click", function(){
		formObj.attr("method", "get");
		formObj.attr("action", "/sboard/list");
		formObj.submit();
	});
	
	var bno = ${boardVO.bno};
	var template = Handlebars.compile($("#templateAttach").html());
	
	$.getJSON("/sboard/getAttach/"+bno, function(list){
		$(list).each(function(){
			var fileInfo = getFileInfo(this);
			
			var html = template(fileInfo);
			
			$(".uploadedList").append(html);
		});
	});
	
	$(".uploadedList").on("click", ".mailbox-attachment-info a", function(event){
		
		var fileLink = $(this).attr("href");
		
		if(checkImageType(fileLink)){
			event.preventDefault();
			var imgTag = $("#popup_img");
			imgTag.attr("src", fileLink);
			
			console.log(imgTag.attr("src"));
			
			$(".popup").show('slow');
			imgTag.addClass("show");
		}
	});
	
	$("#popup_img").on("click", function(){
		$(".popup").hide('slow');
	});
	
});
</script>

<%@include file="../include/footer.jsp"%>
