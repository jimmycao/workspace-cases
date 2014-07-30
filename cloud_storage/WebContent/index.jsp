<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cloud Storage</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap 101 Template</title>

    <!-- Bootstrap -->
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
    </body>
  
	<div id="header">
		<span id="wd"></span>
	</div>


	<div id="content" class="container">
		Current Directory:<span>${wd}</span>
		<form action="upload" method="post"
			enctype="multipart/form-data">
			<table>
				<tr>
					<td><input type="file" name="image" id="image" /></td>
				</tr>
			</table>
			<input type="hidden" name="wd" value="${wd}"/>
			<input type="submit" value="Upload" />
		</form>
	
		<br/>
		<br/>
		<br/>
		<table class="table">
			<c:forEach items="${files}" var="file">
				<c:choose>
					<c:when test="${file.isDir()}">
						<tr>
							<td><a href="list?wd=${wd}/${file.name}">${file.name}</a></td>
							<td></td>
						</tr>
    				</c:when>
					<c:otherwise>
						<tr>
							<td>${file.name}</td>
							<td><a href="download?wd=${wd}&fileName=${file.name}">download</a></td>
						</tr>
    				</c:otherwise>
				</c:choose>
			</c:forEach>
		</table>
	</div>

</body>
</html>