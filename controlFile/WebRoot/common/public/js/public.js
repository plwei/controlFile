$(function(){
	$('.btn_add').hover(function () {
		$('.btn_add_menu').show();
	}, function () {
		$('.btn_add_menu').hide();
	})
	
//	getNavigator();
})

function getNavigator() {
	var json = {};
	json.id = 0;
	json.method = 'query_tag';
	$.ajax({
		url: '../../../ControlServlet',
		data: {'para': JSON.stringify(json)},
		success: function() {
			
		}
	})
}

