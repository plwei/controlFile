$(function(){
	$('.tag ul').each(function(i, n) {
		var height = $(this).height();
		$(this).parent().height(height);
	})
	$('.subnav td').on('click', function() {
		$('.subnav td').removeClass('active');
		$(this).addClass('active');
	})
})









