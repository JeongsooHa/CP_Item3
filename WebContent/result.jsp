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
		            <li role="presentation"><a href="./index.html">Home</a></li>
		            <li role="presentation"><a href="#" data-toggle="modal" data-target="#myModal">About</a></li>
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
		<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">만든이 - 201201422고현민, 201203420하정수</h4>
      </div>
      <div class="modal-body">
        <p>충남대학교 컴퓨터 공학과</p>
        <p><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>고현민: hyunmin.ko4578@gmail.com</p>
        <p><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>하정수: jeongsoo.ha1210@gmail.com</p>
        <img alt="profile" src="./cp.jpg" width="450px" height="500px">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
			<script src="./go.js"></script>
			<script src="./myjs.js"></script>
			    <!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요합니다) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
    <script src="./js/bootstrap.min.js"></script>	
</body>
</html>