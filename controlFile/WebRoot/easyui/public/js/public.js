$(function() {
//	uiValidate('disableValidation');
})

function uiValidate(str) {
	$('input, select, textarea').each(function(i, n) {
		if($(n).hasClass('textbox-f')) {
			$(n).textbox(str);
		}
		if($(n).hasClass('combobox-f')) {
			console.info($(n).combobox('textbox'));
			$(n).combobox('disableValidation');
		}
	})
}

function getObj(obj) {
	var json = {};
	var arr = obj.find('input, select, textarea').serializeArray();
	$.each(arr, function(i, n) {
		json[n.name] = n.value;
	})
	return json;
}
