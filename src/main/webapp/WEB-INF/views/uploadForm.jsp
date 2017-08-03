<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
iframe{
	width:0px;
	height:0px;
	border:0px;
}
</style>
</head>
<body>
	<!--target은 form제출결과가 출력되는 프레임이나 창을 지정하는 속성이다-->
	<!-- target 속성 default는 폼이 포함 되어 있는 창이다.
		그래서 지정하지 않더라도 자동으로 화면이 전환된다. -->
	<!-- 화면 전환효과를 없애기 위해 form제출 결과 출력 프레임을 innerFrame으로 돌리고
		innerFrame의 크기를 0으로 만들어 보이지 않도록하면 
		현재 프레임은 화면전환없이 유지된다.	-->		
	<!-- 즉 처리결과를 기존 프래임 내부에있는 내부프래임으로 돌림으로써
	 기존프래임을 유지하는 방법임 -->		
	<form id="form1" action="uploadForm" method="post"
	 enctype="multipart/form-data" target="zeroFrame"> 
	 
		<input type="file" name="file">
		<input type="submit">
		
	</form>
	
	<!-- HTML내부에 또다른 HTML문서를 보여주는 내부프레임(InnerFrame) -->
	<!-- id가 아닌 반드시 name을 지정해야 한다!!!! -->
	<iframe name="zeroFrame"></iframe>
	
<script type="text/javascript">
	function addFilePath(msg) {
		alert(msg);
		document.getElementById("form1").reset();
	}
</script>
</body>
</html>