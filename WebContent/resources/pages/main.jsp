<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BackNForth</title>
<link href="${pageContext.request.contextPath}/resources/CSS/page.css" type="text/css" rel="stylesheet"/>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/JS/jquery-1.12.3.min.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/JS/angular.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/JS/page.js"></script>
</head>
<body ng-app="BackNForthAngularApp">
<div ng-controller="BackNForthController as BNFCTRL" class="center">
	<textarea class="nonResize" id="messageDisplay" rows="30" cols="70" readonly></textarea>
	<br/>
	<hr class="hrLine" />
	<span class="sideSection">Java: </span>
	<input ng-keypress="BNFCTRL.SubmitIfEnter($event)" ng-model="BNFCTRL.message" class="messageBox" id="messageData" type="text"/><button ng-click="BNFCTRL.SubmitMessage()" class="BNFButton" id="submitter" >Send</button>

</div>
</body>
</html>