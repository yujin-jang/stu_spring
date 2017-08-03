<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.fileDrop{
	width:100%;
	height:200px;
	border:1px dotted blue;
}

.small{
	margin-left:3px;
	font-weight:bold;
	color:gray;
}
</style>
</head>
<body>

	<h3>Ajax File Upload</h3>
	<div class="fileDrop"></div>
	
	<div class="uploadedList"></div>

<script type="text/javascript" src="//code.jquery.com/jquery-1.11.3.min.js"></script>	
<script type="text/javascript">
	
	
	// 파일드랍을 안막아주면 파일을 브라우저에 끌어놨을 경우
	// 브라우저에서 그 파일이 보이게 됌.
	// 그 기본 기능을 막는 것임
	$(".fileDrop").on("dragenter dragover", function(event){
		event.preventDefault();
	});
	
	$(".fileDrop").on("drop", function(event){
		event.preventDefault();
		
		/* 전달된 파일 데이터 가져오기 */
		// jQuery를 이용하는 경우 event가 순수한 DOM이벤트가 아니므로
		//   순수한 DOM 이벤트를 가져옴
		var originalDOMevent = event.originalEvent;
		// 이벤트와 같이 전달된 데이터 가져오기
		var transferedData = originalDOMevent.dataTransfer;
		// 그 데이터 안에 포함된 파일 데이터를 가져오기
		var files = transferedData.files; 
		var file = files[0];
		//console.log(file);
		
		var formData = new FormData();
		formData.append("file", file);
		
		//<form>태그의 Multipart방식으로 보내기 위해선
		//반드시 processData와 contentType을 false로 지정
		$.ajax({
			url:'/uploadAjax',
			type:'POST',
			data: formData,
			// 데이터를 'application/x-www-form-urlencoded'타입으로 변환여부 지정
			// false 지정하여 변환하지 않도록 함.
			processData:false,
			// 기본값은 'application/x-www-form-urlencoded'
			// 파일의 경우 'multipart/form-data 방식으로 전송위해 false 지정
			contentType:false,
			dataType:'text',
			success: function(data){
				var str="";
				// 이미지 일 경우
				if(checkImageType(data)){
					str="<div>"
						+"<a href='displayFile?fileName="+getImageLink(data)+"'>"
						+"<img src='displayFile?fileName="+data+"'/></a>"
						+"<small data-src="+data+">X</small></div>";
				}else{ // 이미지 아닌 경우
					str="<div><a href='displayFile?fileName="+data+"'>"
						+getOriginalName(data) + "</a>"
						+"<small data-src=" + data + ">X</small></div>";
				}
				$(".uploadedList").append(str);
			}
		});
	});
	
	// 첨부파일 삭제 이벤트
	$(".uploadedList").on("click","small", function(event){
		$.ajax({
			url:"deleteFile",
			type:"post",
			data:{fileName : $(this).attr("data-src")},
			dataType:"text",
			success:function(result){
				if(result == 'deleted'){
					$(event.target).parent().remove();
				}
			}
		});
	});
	
	function checkImageType(fileName){
		// i는 대소문자 구분없음
		var pattern = /jpg|gif|png|jpeg/i;
		
		return fileName.match(pattern);
	}
	// 서버에 저장된 파일 명칭에서 오리지날명칭 구하기
	function getOriginalName(fileName){
		
		if(checkImageType(fileName)){
			return;
		}else{
			var originalName = 
				fileName.substr(fileName.indexOf('_')+1);
			return originalName;
		}
	}
	// 's_'만 제거해서 원본파일 링크 구하기
	function getImageLink(fileName){
		if(!checkImageType(fileName)){
			return;
		}
		// '/2017/07/24/' 추출
		// substr(시작인덱스, 길이)
		var front = fileName.substr(0,12);
		//'s_'이후 파일이름 가져오기
		var end = fileName.substr(14);
		// 's_'만 빠진 원본의 링크
		return (front + end);
	}
</script>
</body>
</html>