$(function() {
	
})

function getObj(obj) {
	var json = {};
	var arr = obj.find('input, select, textarea').serializeArray();
	$.each(arr, function(i, n) {
		json[n.name] = n.value;
	})
	return json;
}
