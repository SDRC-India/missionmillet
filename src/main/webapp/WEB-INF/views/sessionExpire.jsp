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
	<div id="wrapper1">
		<jsp:include page="fragments/header.jsp" />

		<div class="content">
			<div class="container-fluid">
				<spring:url value="resources/images/oops.jpg" var="exceptionImage" />
				<div class="row text-center">
					<h2 class="text-center">
						<img class="oppsImg" src="${exceptionImage}" alt="exception image"
							style="height: 330px; margin-top: 65px;" />
					</h2>
					<h3 class="text404" style="color: #c9302c;">Your session has been expired. Please <a href="home">click here</a> to login again.</h3>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="height1-bottom"></div>
		</div>
	</div>

	<jsp:include page="fragments/footer.jsp" />

</body>
</html>
