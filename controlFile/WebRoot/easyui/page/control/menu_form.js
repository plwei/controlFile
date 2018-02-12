$(function() {
	getYxbz();
	var json = {};
	json.method = 'getSelect';
	json.procedure = 'QuerySelectTree';
	json.type = 'MenuSelect';
	getCombobox('pid', json);
	$('#cdlx').combobox({
		valueField: 'id',
		textField: 'text',
		panelHeight: 100,
		required: true,
		data: [{
			'id': '1',
			'text': '目录'
		}, {
			'id': '2',
			'text': '页面'
		}],
		onSelect: function(record) {
			if(record.id == 1) {
				$('.ym').hide();
				$('#pid').combobox({required: false});
				$('#url').textbox({required: false});
			} else {
				$('.ym').show();
				$('#pid').combobox({required: true});
				$('#url').textbox({required: true});
			}
		}
	})
})


function save() {
	if(!$('.formUl').form('validate')) return;
	var json = getObj($('.formUl'));
	if(json.cdlx == 1) {
		json.pid = '0';
		json.url = '';
	}
	json.method = 'menuIns';
	$.ajax({
		url: pubUrl,
		type: 'POST',
		data: {para: JSON.stringify(json)},
		success: function(data) {
			$.messager.alert('信息', '保存成功！', 'info', function() {
				parent.close();
			})
		},
		error: function() {
			$.messager.alert('错误', '保存失败！', 'error');
		}
	})
}
