<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>简单登录</title>
</head>
<body>

	<form action=${requestScope.url} method="post">
		<input type="text" name="userName">账号
		</br>
		<input type="password" name="userPassword">密码
		</br>
		<button type="submit">登录</button>
	</form>
	
</body>
</html>