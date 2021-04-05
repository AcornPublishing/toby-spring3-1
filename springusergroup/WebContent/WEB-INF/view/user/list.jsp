<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>	
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>Spring User Group</title>
	<!-- Framework CSS -->
    <link rel="stylesheet" href="../blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="../blueprint/print.css" type="text/css" media="print">
    <!--[if lt IE 8]><link rel="stylesheet" href="../../blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
</head>
<body>  

<div class="container">
	<h2>ȸ�� ���</h2>
	<div class="prepend-6 span-8 append-8 last">
		<b>${currentUser.name}</b>������ �α��� �ϼ̽��ϴ�.  <a href="../logout">[�α׾ƿ�]</a>
	</div>
	<hr />
	<div class="span-14 append-10 last">
		<table>
			<tr>
				<th>�̸�</th>
				<th>������̸�</th>
				<th>����</th>
				<th>�׷�</th>
				<th>������</th>
			</tr>
			<c:forEach var="user" items="${userList}" >
			<tr>
				<td><a href="view/${user.id}">${user.name}</a></td>
				<td>${user.username}</td>
				<td>${user.type.name}</td>
				<td>${user.group.name}</td>
				<td><spring:eval expression="user.created" /></td> 
			</tr>
			</c:forEach>
		</table>
	</div>
	
</div>

</body>
</html>