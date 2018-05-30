<!-- 
@author Swarna (swarnaprava@sdrc.co.in)
-->

<%@page
	import="org.sdrc.missionmillet.model.UserRoleFeaturePermissionMappingModel"%>
<%@page import="org.sdrc.missionmillet.util.Constants"%>
<%@page import="org.sdrc.missionmillet.model.CollectUserModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.apache.commons.lang.WordUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>
<%
	Integer role = 0;
Integer typeOfUser = 0;
CollectUserModel user = null;
List<String> features = new ArrayList<String>();
List<String> permissions = new ArrayList<String>();

	if (request.getSession().getAttribute(Constants.USER_PRINCIPAL) != null) {
		user = (CollectUserModel) request.getSession().getAttribute(
				Constants.USER_PRINCIPAL);
		typeOfUser = user.getTypeOfUser();
		if(typeOfUser==46)
			role = user.getUserAreaModels().get(0)
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole()
				.getRoleId();

	}
%>

<div id="header-area" class="header_area navbar-fixed-top">
	<div class="header_bottom">
		<div class="container">
			<div class="row">
				<div class="col-md-4 logo-sec">
					<!-- <h2 class="header-info">Special Programme for Promotion of Millets in Tribal Areas</h2> -->
					<img class="millet-logo" src="resources/images/Millet_title.png"/>
				</div>
				<div class="col-md-8 menu-list">
					<div>
						<nav role="navigation" class="navbar navbar-default mainmenu">
							<!-- Brand and toggle get grouped for better mobile display -->
							<div class="navbar-header">
								<button type="button" data-target="#navbarCollapse"
									data-toggle="collapse" class="navbar-toggle">
									<span class="sr-only">Toggle navigation</span> <span
										class="icon-bar"></span> <span class="icon-bar"></span> <span
										class="icon-bar"></span>
								</button>
							</div>
							<!-- Collection of nav links and other content for toggling -->
							<div id="navbarCollapse" class="collapse navbar-collapse ">
								<ul id="fresponsive"
									class="nav navbar-nav dropdown float-right float-left">
									<%if (user != null) {%><li class="active"><a href="#">Welcome
											<%=WordUtils.capitalize(user.getName())%></a></li>
									<%}%>

									<%if (user != null && role == 3) {%><li class="dropdown"><a
										data-toggle="dropdown" class="dropdown-toggle">Workspace<span
											class="caret"></span></a>
										<ul class=" dropdown-menu">
											<li><a href="ngoSoE">Statement of Expenditure</a></li>
											<li><a href="ngoReport">Reports</a></li>
										</ul></li>
									<%}%>

									<%if (user != null && role == 1) {%><li class="dropdown"><a
										data-toggle="dropdown" class="dropdown-toggle">Workspace<span
											class="caret"></span></a>
										<ul class=" dropdown-menu">
											<li><a href="stateSoE">Statement of Expenditure</a></li>
											<li><a href="stateReports">Reports</a></li>
										</ul></li>
									<%}%>

									<%if (user != null && role == 1) {%><li class="dropdown"><a
										data-toggle="dropdown" class="dropdown-toggle">NGO
											Workspace<span class="caret"></span>
									</a>
										<ul class=" dropdown-menu">
											<li class="dropdown"><a data-toggle="dropdown"
												class="dropdown-toggle">Statement of Expenditure<span
													class="caret"></span></a>
												<ul class="dropdown-menu bgClr-inTablt">
													<li><a href="stateNgoSoEManage">Manage</a></li>
													<li><a href="stateNgoSoEHistory">History</a></li>
												</ul></li>
											<li><a href="stateNgoReport">Reports</a></li>
										</ul></li>
									<%}%>


									<%if (user != null && role == 2) {%><li class="dropdown"><a
										data-toggle="dropdown" class="dropdown-toggle">NGO
											Workspace<span class="caret"></span>
									</a>
										<ul class=" dropdown-menu">
											<li class="dropdown"><a data-toggle="dropdown"
												class="dropdown-toggle">Statement of Expenditure<span
													class="caret"></span></a>
												<ul class=" dropdown-menu dropdown-district">
													<li><a href="districtNgoSoEManage">Manage</a></li>
													<li><a href="districtNgoSoEHistory">History</a></li>
												</ul></li>
											<li><a href="districtNgoReport">Reports</a></li>
										</ul></li>
									<%}%>
									<%if (user != null && role == 1) {%><li><a
										href="aggregation">Aggregation</a></li>
									<%}%>
									<%if (user != null && role == 1) {%><li><a
										href="configuration">Configuration</a></li>
									<%}%> 
									
									
									<%if (user != null || role == 1 || role == 2 || role == 3) {%><li class="dropdown"><a
										data-toggle="dropdown" class="dropdown-toggle"><span class="glyphicon glyphicon-cog setting-icon"></span> <span
											class="caret setting-caret"></span></a>
										<ul class=" dropdown-menu">
											<li><a href="changepassword">Change Password</a></li>
											<%if (user != null) {%><li><a href="logout">Logout</a></li><%}%>
										</ul></li>
									<%}%>
									
									<%-- <%if (user == null && role != 1 && role != 2 && role != 3) {%><li><a
										href="login">Login</a></li>
									<%}%> --%>
								</ul>
							</div>
						</nav>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>
<!-- /.header_bottom -->
<div class="loaderMask" id="loader-mask" style="display: none;" loading>
	<div class="windows8">
		<div class="wBall" id="wBall_1">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_2">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_3">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_4">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_5">
			<div class="wInnerBall"></div>
		</div>
	</div>
</div>



<script type="text/javascript">
// $(document).ready(function(){
// 	$(".dropdown, .btn-group").hover(function(){
// 		var dropdownMenu = $(this).children(".dropdown-menu");
// 		if(dropdownMenu.is(":visible")){
// 			dropdownMenu.parent().toggleClass("open");
// 		}
// 	});
// });		
</script>


<script>
/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */
// function myFunction() {
//     document.getElementById("myDropdown").classList.toggle("show");
// }

// Close the dropdown if the user clicks outside of it
// window.onclick = function(event) {
//   if (!event.target.matches('.dropbtn')) {

//     var dropdowns = document.getElementsByClassName("dropdown-content");
//     var i;
//     for (i = 0; i < dropdowns.length; i++) {
//       var openDropdown = dropdowns[i];
//       if (openDropdown.classList.contains('show')) {
//         openDropdown.classList.remove('show');
//       }
//     }
//   }
// }
</script>



