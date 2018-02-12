$(function() {
	
})

function getYxbz() {
	$('#yxbz').combobox({
		valueField: 'id',
		textField: 'text',
		panelHeight: 100,
		required: true,
		data: [ {
			'id': 'Y',
			'text': '有效'
		}, {
			'id': 'N',
			'text': '无效'
		} ]
	})
}

function getCombobox(id, para) {
	$('#'+id).combobox({
		valueField: 'dm',
		textField: 'mc',
		editable: false,
		panelHeight: 'auto',
		queryParams: {para : JSON.stringify(para)},
		onShowPanel: function() {
			var data = $('#' + id).combobox('getData');
			if(data.length == 0)
				$('#' + id).combobox('reload', pubUrl);
		}
	})
}

