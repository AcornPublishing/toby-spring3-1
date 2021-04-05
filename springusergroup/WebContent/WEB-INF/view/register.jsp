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
	<h2>회원 가입</h2>
	<hr />	
	<div class="span-14 append-10 last">
	<form:form modelAttribute="user">	
		<fieldset>
			<legend> 회원 정보 </legend>
			<p>
				<form:label path="name">이름:</form:label><br/>
				<form:input path="name" size="20" maxlength="20" /> 
				<form:errors cssClass="error" path="name" />
			</p>
			<p>
				<form:label path="username">사용자이름:</form:label><br/>
				<form:input path="username" size="12" maxlength="12" />
				<form:errors cssClass="error" path="username" />
			</p>
			<p>
				<form:label path="">비밀번호:</form:label><br/>
				<form:password path="password" showPassword="true" size="12" maxlength="12" />
				<form:errors cssClass="error" path="password" />
			</p>
			<p>
				<label for="group">그룹:</label><br/>
				<form:select path="group" items="${groupList}" itemLabel="name" itemValue="id" />
			</p>
			<p>
				<input type="submit" value="  등록   " />
			</p>
		</fieldset>
	</form:form>
	</div>
</div>

</body>
</html>