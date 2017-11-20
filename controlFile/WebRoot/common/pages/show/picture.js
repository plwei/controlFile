$(function(){
	$('.header .item a').on('click', function() {
		$(this).parent().siblings().children().removeClass('active');
		$(this).addClass('active');
	})
	$('.header .item ul').each(function(i, n) {
		var height = $(this).height();
		$(this).parent().height(height);
	})
})









