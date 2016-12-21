<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ucodegen.*" %>
<%
	String cCodetext = request.getParameter("ccodetext");
	String trimcCodetext= cCodetext.replaceAll("\r\n", " ");
	trimcCodetext =trimcCodetext.replaceAll("\t", " ");
	String[] test = new String[5];
	UcodeCodeGen.main(test);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 위 3개의 메타 태그는 *반드시* head 태그의 처음에 와야합니다; 어떤 다른 콘텐츠들은 반드시 이 태그들 *다음에* 와야 합니다 -->
<link href="./css/bootstrap.min.css" rel="stylesheet">
<link href="./css/result.css" rel="stylesheet">
<title>RESULT PAGE</title>

</head>
<body>
	<div class="container">
		<div class="header">
			<nav>
				<ul class="nav nav-pills pull-right">
					<li role="presentation" ><a href="#">Home</a></li>
					<li role="presentation" class="active"><a href="#">RESULT</a></li>
				</ul>
			</nav>
			<h3 class="text-muted" ><b>Compiler Item3</b></h3>
		</div>
		<div class="jumbotron" >
			<div class ="row" >
				<div class="col-md-4" id="ucodediv" style="border: solid 1px black; height:400px;">
					U code
				</div>
				<div class="col-md-7 col-md-offset-1" id="controlflowgraphdiv" style="border: solid 1px black; height:400px;">
					Control Flow Graph
				</div>
			</div>
			<div class ="row" >
				<div class="col-md-12" id="groupblockdiv" style="border: solid 1px black; height:200px;">
					Group Block
				</div>
			</div>
		</div>
		<footer class="footer">
        	<p>© Compiler 2016 고현민, 하정수</p>
      	</footer>
	</div>	
</body>
</html>