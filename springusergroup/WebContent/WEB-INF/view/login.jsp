<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>Spring User Group</title>
	<!-- Framework CSS -->
    <link rel="stylesheet" href="blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="blueprint/print.css" type="text/css" media="print">
    <!--[if lt IE 8]><link rel="stylesheet" href="../blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
</head>
<body> 

<div class="container">
	<h2>스프링 사용자 모임</h2>
	<hr />
	<div class="span-12 append-12 last">
		<h4>스프링 사용자 모임에 오신 것을 환영합니다.</h4>
	</div>
	<div class="span-12 append-12 last">
	<form:form modelAttribute="login">
		<fieldset>
			<p>
				<form:errors cssClass="error" path="" />
			</p>
			<legend> 회원 로그인 </legend>
			<p>
				<form:label path="username">사용자이름</form:label><br/> 
				<form:input path="username" size="12" maxlength="12"/>
				<form:errors cssClass="error" path="username" />
			</p>
			<p>
				<form:label path="password">비밀번호</form:label><br/>
				<form:password path="password" size="12" maxlength="12"/>
				<form:errors cssClass="error" path="password" />
			</p>
			<p>
				<input type="submit" value="  로그인   " />
			</p>
		</fieldset>
	</form:form>
	</div>
	<div class="span-12 append-12 last">
		<div class="notice">
			처음 방문하셨으면 <a href="register">회원가입</a>을 해주세요.
		</div>
	</div>
</div>

</body>
</html>