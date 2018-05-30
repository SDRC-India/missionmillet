<script>
$(document).ready(function(){
	var x;
	setTimeout(function(){
		$(".dropdown-menu .dropdown").click(function(){
			x= this;
			setTimeout(function(){
				$(x).parent().parent().addClass("open");
			}, 100);
		});
	}, 2000);

});

</script>



<footer>
	<div class="container footer-sec">
		<div class="row">
			<div class="col-md-6 col-sm-6 col-xs-6 missionMillet-logo">
				<h2 class="head-info">
<!-- 					&copy; -->
					<a href="#"> Special Programme for Promotion of Millets in Tribal Areas </a>
				</h2>
			</div>
			<div class="col-md-3 col-sm-3 col-xs-3 links">
<!-- 				<ul class="footer-links"> -->
<!-- 					<li><a href="#">Terms of Use</a></li> -->
<!-- 					<li><a href="#">Disclaimer</a></li> -->
<!-- 					<li><a href="#">Privacy Policy</a></li> -->
<!-- 					<li><a href="#">Sitemap</a></li> -->
					  <div style="color:#337ab7;">  Visitor Count : ${visitorCount} </div>
<!-- 				</ul> -->
			</div>
			<div class="col-md-3 col-sm-3 col-xs-3 sdrc-logo">
				Powered by&nbsp; <a href="http://sdrc.co.in/">SDRC</a>
			</div>
		</div>
	</div>
</footer>