$(function() {
	dg_para();
})

function dg_para() {
	$('#dg').datagrid({
		url: '',
		toolbar: '#toolbar',
		striped: true,
		idField: 'id',
		pagination: true,
		rownumbers: true,
		singleSelect: true,
		columns: [ [ {
			field: 'name',
			title: '菜单名称',
			halign: 'center',
			align: 'center'
		}, {
			field: 'pname',
			title: '上级菜单名称',
			halign: 'center',
			align: 'center'
		}, {
			field: 'plxh',
			title: '排列序号',
			halign: 'center',
			align: 'center'
		}, {
			field: 'url',
			title: '链接地址',
			halign: 'center',
			align: 'center'
		} ] ]
	})
}

function query() {
	var json = getObj($('.formUl'));
}


function ins() {
	$('#win').window('iframeWin', {
		width: 600,
		height: 400,
		title: '新增',
		url: 'menu_form.html'
	})
}

function upd() {
	
}

function del() {
	
}

function close() {
	$('#win').window('close');
}