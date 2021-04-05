<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>Spring User Group</title>
	<!-- Framework CSS -->
    <link rel="stylesheet" href="../../blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="../../blueprint/print.css" type="text/css" media="print">
    <!--[if lt IE 8]><link rel="stylesheet" href="../../../blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
</head>
<body> 

<div class="container">
	<h2>ȸ�� ��������</h2>
	<hr />	
	<div class="span-14 append-10 last">
	<form:form modelAttribute="user">	
		<fieldset>
			<legend> ȸ�� ���� </legend>
			<p>
				<form:label path="name">�̸�:</form:label><br/>
				<form:input path="name" size="20" maxlength="20" /> 
				<form:errors cssClass="error" path="name" />
			</p>
			<p>
				<form:label path="username">������̸�:</form:label><br/>
				<form:input path="username" size="12" maxlength="12" />
				<form:errors cssClass="error" path="username" />
			</p>
			<p>
				<form:label path="">��й�ȣ:</form:label><br/>
				<form:password path="password" size="12" maxlength="12" showPassword="true"/>
				<form:errors cssClass="error" path="password" />
			</p>
			<p>
				<label>����:</label><br/>
				<form:select path="type" items="${typeList}" itemLabel="name" itemValue="value" />
			</p>
			<p>
				<label for="group">�׷�:</label><br/>
				<form:select path="group" items="${groupList}" itemLabel="name" itemValue="id" />
			</p>
			<p>
				<input type="submit" value="  ����   " />
			</p>
		</fieldset>
	</form:form>
	</div>
</div>

</body>
</html>