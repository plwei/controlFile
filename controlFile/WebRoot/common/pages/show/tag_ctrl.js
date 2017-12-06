$(function(){
	$('.tag ul').each(function(i, n) {
		var height = $(this).height();
		$(this).parent().height(height);
	})
	$('.subnav td').on('click', function() {
		$('.subnav td').removeClass('active');
		$(this).addClass('active');
	})
	$('input[name="bqjb"]').on('click', function() {
		var value = $(this).val();
		if(value == 1) {
			$('#add > div').eq(1).hide();
			$('#add > div').eq(2).hide();
			$('#add > div').eq(3).hide();
		} else if(value == 2) {
			$('#add > div').eq(2).hide();
			$('#add > div').eq(3).hide();
		} else if(value == 3) {
			$('#add > div').eq(3).hide();
		}
	})
})

function addInput() {
	
}







