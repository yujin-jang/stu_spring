<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="../include/header.jsp"%>

<style>
.fileDrop{
	width: 80%;
	height: 100px;
	border: 1px dotted gray;
	background-color: lightslategrey;
	margin: auto;
}
</style>

<!-- Main content -->
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

<form id="modifyForm" role="form" method="post">
	<!-- 페이징 처리에 대한 정보를 유지 -->
	<input type="hidden" name="page" value="${cri.page}">
	<input type="hidden" name="perPageNum" value="${cri.perPageNum}">
	<input type="hidden" name="searchType" value="${cri.searchType}">
	<input type="hidden" name="keyword" value="${cri.keyword}">
	
	<div class="box-body">
		<div class="form-group">
			<label for="exampleInputEmail1">BNO</label> <input type="text"
				name='bno' class="form-control" value="${boardVO.bno}"
				readonly="readonly">
		</div>

		<div class="form-group">
			<label for="exampleInputEmail1">Title</label> <input type="text"
				name='title' class="form-control" value="${boardVO.title}">
		</div>
		<div class="form-group">
			<label for="exampleInputPassword1">Content</label>
			<textarea class="form-control" name="content"
			 rows="3">${boardVO.content}</textarea>
		</div>
		<div class="form-group">
			<label for="exampleInputEmail1">Writer</label> <input
				type="text" name="writer" class="form-control"
				value="${boardVO.writer}" readonly="readonly">
		</div>
		<div class="form-group">
			<label for="exampleInputEmail1">File DROP Here</label>
			<div class="fileDrop"></div>
		</div>
	</div>
	<!-- /.box-body -->
	
	<div class="box-footer">
		<div>
			<hr>
		</div>
				
		<ul class="mailbox-attachments clearfix uploadedList">
		</ul>
	</div>

<div class="box-footer">
	<button type="submit" class="btn btn-primary">SAVE</button>
	<button type="button" class="btn btn-warning">CANCEL</button>
</div>
</form>

			</div>
			<!-- /.box -->
		</div>
		<!--/.col (left) -->
	</div>
	<!-- /.row -->
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script src="/resources/js/upload.js" type="text/javascript"></script>
<script 
 src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js">
</script>
<script id="template" type="text/x-handlebars-template">
<li>
	<span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}"
	 alt="Attachment"></span>
	<div class="mailbox-attachment-info">
		<a href="{{getLink}}" class="mailbox-attachment-name">{{fileName}}</a>
		<a href="{{fullName}}" class="btn btn-default btn-xs pull-right delbtn">
			<i class="fa fa-fw fa-remove"></i>
		</a>
	</div>
</li>
</script>
<script>
var template = Handlebars.compile($("#template").html());

$(".fileDrop").on("dragenter dragover", function(event){
	event.preventDefault();
});

$(".fileDrop").on("drop", function(event){
	event.preventDefault();
	
	var files = event.originalEvent.dataTransfer.files;
	var file = files[0];
	
	var formData = new FormData();
	formData.append("file", file);
	
	$.ajax({
		url:"/uploadAjax",
		type:"post",
		data:formData,
		processData: false,
		contentType: false,
		dataType: "text",
		success : function(data){
			
			var fileInfo = getFileInfo(data);
			
			var html = template(fileInfo);
			
			$(".uploadedList").append(html);
		}
	});
});

$(".uploadedList").on("click",".delbtn", function(event){
	event.preventDefault();
	var fullName = $(this).attr("href");
	$.post("/deleteFile",{fileName:fullName},function(){});
	$(this).parent("div").parent("li").remove();
});

$(document).ready(function() {
	
	// 초기 첨부파일 리스트 출력
	var bno = ${boardVO.bno};
	$.getJSON("/sboard/getAttach/"+bno, function(list){
		$(list).each(function(){
			var fileInfo = getFileInfo(this);
			var html = template(fileInfo);
			$(".uploadedList").append(html);
		});
	});
	
	// cancel
	$(".btn-warning").on("click", function() {
		self.location = "/sboard/list?page=1"
						 +"&perPageNum=10"
						 +"&searchType="
						 +"&keyword=";
	});
	
	// save
	$("#modifyForm").submit(function(event){
		event.preventDefault();
		var form = $(this);// form = $(modifyForm)
		var str = "";
		
		$(".uploadedList .delbtn").each(function(index){
			// $(this).attr('href') = fullName
			str += "<input type='hidden' name='files["+index+"]' value='"
					+ $(this).attr("href")+"'>";
		});
		
		form.append(str);
		// jQuery의 get(0)은 순수한 DOM객체를 얻기위해 사용
		form.get(0).submit();
	});
});
</script>

<%@include file="../include/footer.jsp"%>
