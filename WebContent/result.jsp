<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ucodegen.UcodeCodeGen" %>
<%@ page import="ucodegen.UcodeGenListener" %>
<%@ page import="basicblock.*" %>
<%@ page import="antlrMiniC.*" %>
<%@ page import="json.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>

<%

	String cCodetext = request.getParameter("ccodetext");
	String trimcCodetext= cCodetext.replaceAll("\r\n", " ");
	trimcCodetext =trimcCodetext.replaceAll("\t", " ");
	String result = UcodeCodeGen.minic2ucode(trimcCodetext);
	ArrayList<GroupBlock> blockResult = BasicBlock.BasicBlockCal(result);
	JSONObject jsonResult = JsonProcess.makeJsonObject(blockResult);
	
	result = result.replaceAll("\n", "<br>").replaceAll(" ", "&nbsp");
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 위 3개의 메타 태그는 *반드시* head 태그의 처음에 와야합니다; 어떤 다른 콘텐츠들은 반드시 이 태그들 *다음에* 와야 합니다 -->
<link href="./css/bootstrap.min.css" rel="stylesheet">
<link href="./css/result.css" rel="stylesheet">
<title>Complier Item3 - 하정수, 고현민</title>

</head>
<body onload="init()">
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
				<label for="ucodeResult" class="col-md-2 control-label">U Code</label>
				<label for="groupBlockResult" class="col-md-2  col-md-offset-1 control-label">Information</label>
				<label for="cfgResult" class="col-md-6 col-md-offset-1 control-label">CFG</label>
				<div class="col-md-2 well pre-scrollable" id="ucodediv" style="border: solid 1px black; background: white;">
					<span id="ucodeResult"><%=result %></span>
				</div>
				<div class="col-md-2 well col-md-offset-1 pre-scrollable" id="groupblockdiv" style="border: solid 1px black; background: white;">
				<% 
					int i = 0;
					while(i < blockResult.size()){
						GroupBlock temp = blockResult.get(i);
						%>
							<p>BB<%=temp.getBlockIndex() %></p>
							<p>Leader: <%=temp.getLeaderLabel() %></p>
						<%
							int j = 0;
							while(j < temp.getLabelList().size()){
								Instruction insTemp = temp.getLabelList().get(j);
								%>
									<span><%=insTemp.toString().replaceAll("\t", "&nbsp&nbsp&nbsp")%></span><br>
								<%
								j++;
							}
							%>
								<br>
							<%
						i++;
					}
				
				%>
				</div>
				<div class="col-md-6 col-md-offset-1 well" id="controlflowgraphdiv" style="border: solid 1px black; background: white; height:450px">
					
				</div>
					<textarea id="mySavedModel" style="width:100%;height:300px; display: none">
						<%=jsonResult %>
   					</textarea>
   					<div id="myPaletteDiv" style="display: none"></div>
			</div>
		</div>
		<footer class="footer">
        	<p>© Compiler 2016 고현민, 하정수</p>
      	</footer>
	</div>
			<script src="./go.js"></script>
			<script src="./myjs.js"></script>	
</body>
</html>