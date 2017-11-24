$(function(){
	$('.header .item ul').each(function(i, n) {
		var height = $(this).height();
		$(this).parent().height(height);
	})
	$('.title td').on('click', function() {
		$('.title td').removeClass('active');
		$(this).addClass('active');
	})
})









