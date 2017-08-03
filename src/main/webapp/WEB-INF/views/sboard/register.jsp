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
					<h3 class="box-title">REGISTER BOARD</h3>
				</div>
				<!-- /.box-header -->

				<form id="registerForm" role="form" action="register" method="post">
					<div class="box-body">
						<div class="form-group">
							<label for="exampleInputEmail1">Title</label>
							<input type="text" name="title" class="form-control"
							 placeholder="Enter Title">
						</div>
						<div class="form-group">
							<label for="exampleInputPassword1">Content</label>
							<textArea class="form-control" name="content" 
							 rows="3" placeholder="Enter..."></textArea>
						</div>
						<!-- EL의 경우 자동으로 HttpSession의 login을 찾아 사용 -->
						<div class="form-group">
							<label for="exampleInputEmail1">Writer</label>
							<input type="text" name="writer" class="form-control"
							 placeholder="Enter Writer" value='${login.uid}' readonly>
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
						
						<button type="submit" class="btn btn-primary">Submit</button>
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

$("#registerForm").submit(function(event){
	event.preventDefault();
	var form = $(this);// form = $(registerForm)
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
</script>

<%@include file="../include/footer.jsp"%>
