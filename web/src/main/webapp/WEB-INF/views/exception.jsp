<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.StringWriter"%>
<!DOCTYPE html>

<html lang="en">
<head>
<jsp:include page="fragments/headtags.jsp" />
</head>
<body>
<!-- 	<div id="wrapper"> -->
<div id="wrapper">
<jsp:include page="fragments/header.jsp" />
		
		<div class="content">
			<div class="container-fluid">
				<spring:url value="resources/images/oops.jpg" var="exceptionImage" />
				<div class="row text-center">
					<h2 class="text-center">
						<img class="oppsImg" src="${exceptionImage}" alt="exception image"
						 style="height: 330px; margin-top: 65px;"/>
					</h2>

					<h2 class="text404 excptionmsg">Oops !!! Looks like you are caught in a wrong place ...</h2>

					<p class="text404" style="color: #c9302c;">${exception.message}</p>

					<%
						Logger logger = LoggerFactory.getLogger( "LOGGER" );

						RuntimeException rte = (RuntimeException) (request.getAttribute("exception"));
						StackTraceElement[] stes = rte != null ? rte.getStackTrace() : null;

						if (stes != null && stes.length > 0) {

							StringWriter stringWritter = new StringWriter();
							PrintWriter printWritter = new PrintWriter(stringWritter, true);
							((RuntimeException) (request.getAttribute("exception"))).printStackTrace(printWritter);
							printWritter.flush();
							stringWritter.flush();

							logger.error("An exception occourred , Stack Trace :" + stringWritter.toString());

							// 		logger.error(((RuntimeException)(request.getAttribute("exception"))).printStackTrace(logger));
							// 		for(StackTraceElement ste : stes)
							// 		logger.error(ste);
						}
					%>
					<p>Click <a href="home">here</a> to redirect to Home.</p>
					<!-- Exception: ${exception.message}.
		  	<c:forEach items="${exception.stackTrace}" var="stackTrace"> 
				${stackTrace} 
			</c:forEach>
	  	-->
				</div>
			</div>
			<div class="clearfooter"></div>
		</div>
		</div>
		<div class="clearfooter"></div>
<!-- 	</div> -->

<jsp:include page="fragments/footer.jsp" />
	
</body>
</html>
