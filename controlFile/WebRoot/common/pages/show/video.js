$(function(){
	$('.subnav a').on('click', function() {
		$(this).parent().siblings().children().removeClass('active');
		$(this).addClass('active');
	})
	$('.subnav ul').each(function(i, n) {
		var height = $(this).height();
		$(this).parent().height(height);
	})
})









