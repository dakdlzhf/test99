<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
hello.jsp 페이지
<form action="/file" method="POST" enctype="multipart/form-data">
업로드할 파일 : <input type="file" name="file" id="file"/>
<button>전송</button>
</form>
</body>
</html>