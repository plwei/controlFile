$.extend($.fn.combobox.defaults, {width: 200, editable: false});

$.extend($.fn.numberbox.defaults, {width: 200});

$.extend($.fn.textbox.defaults, {width: 200});

$.extend($.fn.window.methods, {
	//打开的window显示的是一个完整的页面，其中content属性为panel的属性
	iframeWin:function(jq,data) {
		if(data.minimizable == undefined)
			data.minimizable = false;
		if(data.style == undefined)
			data.style = '';
		data.content = '<iframe scrolling="auto" frameborder="0" src="'+data.url+'" style="width:100%;height:99%;overflow-x:hidden;"></iframe>';
		jq.window(data);
	}
})


