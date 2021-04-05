<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
	<h2>ȸ����ȸ</h2>
	<a href="../edit/${id}">[��������]</a>
	<a href="../delete/${id}">[ȸ������]</a>	
	<a href="../list">[���ư���]</a>
	
	<hr />
	<table>
		<tr>
			<td class="span-2">�̸�:</td>
			<td class="span-10 value">${user.name}</td>
		</tr>
		<tr>
			<td class="span-2">������̸�:</td>
			<td class="span-10 value">${user.username}</td>
		</tr>
		<tr>
			<td class="span-2">Ÿ��:</td>
			<td class="span-10 value">${user.type.name}</td>
		</tr>
		<tr>
			<td class="span-2">�׷�:</td>
			<td class="span-10 value">${user.group.name}</td>
		</tr>
		<tr>
			<td class="span-2">������:</td>
			<td class="span-10 value"><spring:eval expression="user.created" /></td>
		</tr>
		<tr>
			<td class="span-2">������:</td>
			<td class="span-10 value"><spring:eval expression="user.modified" /></td>
		</tr> 
		<tr>
			<td class="span-2">�α���Ƚ��:</td>
			<td class="span-10 value"><spring:eval expression="user.logins" /></td>
		</tr>
	</table>	
</div>

</body>
</html> 