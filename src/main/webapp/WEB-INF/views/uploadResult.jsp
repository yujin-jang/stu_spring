<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
	<h1>Result Page</h1>
<script>
	
	var result = '${savedName}';
	// iframe내에서 동작하기 위해 작성된 페이지이므로
	// iframe내에서 동작할 경우
	// 현재 script문은 uploadForm.jsp내의 script와 부모자식관계가 된다.
	// parent는 uploadForm.jsp 내의 script문에 해당한다.
	parent.addFilePath(result);
	
</script>
</body>
</html>    
