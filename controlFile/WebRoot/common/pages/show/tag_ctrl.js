var dqzt = 'insert';
$(function(){
	$('.subnav td').on('click', function() {
		$('.subnav td').removeClass('active');
		$(this).addClass('active');
		dqzt = this.id;
		$('.tag > div').remove();
		query_tag(0, null);
	});
	$('.tag').on('click', 'input[name^="bq_"]', function() {
		var id = $(this).val();
		query_tag(id, this);
	});
	$('.tag').on('click', 'ul span', function() {
		if(dqzt != 'update') return;
		var text = $(this).text();
		var html = '<input type="text" value="'+text+'" style="width: 110px">';
		$(this).replaceWith(html);
		$('input[value="'+text+'"]').focus();
	});
	$('.tag').on('click', 'ul del', function() {
		var text = $(this).text();
		var html = '<span>'+text+'</span>';
		$(this).replaceWith(html);
	});
	query_tag(0, null);	
})

function tag_height() {
	var height = 0;
	$('.tag ul').each(function(i, n) {
		var h = $(this).height();
		height += h;
		$(this).parent().height(h);
	});
	$('.tag').height(height);
}

function query_tag(id, obj) {
	$(obj).parents('div:not(.tag)').nextAll().remove();
	var bqjb = 0;
	if(obj != null)
		bqjb = obj.name.split('_')[1];
	var jbmc = (bqjb*1 + 1) + '级标签：';
	var msg = '未查到数据，建议请先添加'+(bqjb*1 + 1)+'级标签！';
	var json = {};
	json.id = id;
	json.method = 'query_tag';
	$.ajax({
		url: '../../../ControlServlet',
		data: {'para': JSON.stringify(json)},
		async: false,
		success: function(data) {
			var rows = JSON.parse(data);
			var html = '<div><span>'+jbmc+'</span><ul>';
			if(rows.length > 0) {
				$.each(rows, function(i, row) {
					html += ' <li><input type="radio" name="bq_'+row.bqjb+'" value="'+row.id+'">';
					html += '<span>'+row.name+'</span></li>';
				})
				if(dqzt != 'insert') {
					html += '</ul></div>';
					$('.tag').append(html);
					tag_height();
					return;
				}
			} else {
				if(dqzt == 'delete' && id != 0) {
					var html = '<del>'+$(obj).next().text()+'</del>';
					$(obj).next().replaceWith(html);
				} else {
					alert(msg);
				}
				if(dqzt != 'insert') return;
			}
			var length = $('input[name^="bqmc"]').length;
			html += '<li class="tag_input">';
			html += '<input type="text" name="bqmc' + (length+1) + '" style="width: 110px"> ';
			html += '<input type="button" value="+" onclick="addInput(this)">';
			html += '</li>';
			html += '</ul></div>';
			$('.tag').append(html);
			$('input[name="bqmc' + (length+1) + '"]').focus();
			tag_height();
		}
	})
}

function addInput(obj) {
	var length = $('input[name^="bqmc"]').length;
	var html = '<li class="tag_input">';
	html += '<input type="text" name="bqmc' + (length+1) + '" style="width: 110px"> ';
	html += '<input type="button" value="-" onclick="delInput(this)">';
	html += '</li>';
	$(obj).parents('ul').append(html);
	$('input[name="bqmc' + (length+1) + '"]').focus();
	tag_height();
}

function delInput(obj) {
	$(obj).parent().remove();
	tag_height();
}

function submit() {
	var json = {};
	var msg = '';
	var ids = new Array();
	if(dqzt == 'insert') {
		json = insert_tag();
		msg = '新建';
	} else if(dqzt == 'update') {
		json = update_tag();
		msg = '修改';
	} else if(dqzt == 'delete') {
		json = delete_tag();
		msg = '删除';
	}
	ids = json.ids;
	delete json.ids;
	$.ajax({
		url: '../../../ControlServlet',
		data: {'para': JSON.stringify(json)},
		dataType: 'json',
		success: function(data) {
			if(data.length > 0) {
				var count = json.length;
				alert(msg + '标签'+count+'条，成功'+data[0]+'条！');
			} else {
				alert(msg + '修改失败');
			}
		},
		complete: function() {
			$('.tag > div').remove();
			query_tag(0, null);
			var length = ids.length;
			if(length > 0) {
				$.each(ids, function(i, n) {
					if($('input[value="'+n+'"]').length > 0) {
						$('input[value="'+n+'"]').attr('checked', true);
						query_tag(n, $('input[value="'+n+'"]')[0]);
					}
				})
			}
		}
	})
}

/**
 * 删除数组中重复的项
 * @param arr
 * @returns
 */
function arrDel(arr) {
	for(var i = 0; i < arr.length; i++) {
		var v = arr[i];
		for(var j = i + 1; j < arr.length; j++) {
			if(v == arr[j])
				arr.splice(j, 1);
		}
	}
	return arr;
}

function insert_tag() {
	var json = {};
	json.method = 'insert_tag';
	var arr = new Array();
	var j = 0;
	$('.tag input[type="text"]').each(function(i, n){
		if(n.value == '') return true;
		var obj = {};
		var pid = 0;
		var bqjb = 1;
		var prev = $(n).parents('div:not(.tag)').prev().find('input:checked');
		if(prev.length > 0) {
			pid = prev.val();
			bqjb = prev.attr('name').split('_')[1]*1 + 1;
		}
		obj.pid = pid;
		obj.bqjb = bqjb;
		obj.name = n.value;
		arr[j] = obj;
		j++;
	})
	json.data = JSON.stringify(arr);
	json.length = arr.length;
	var ids = new Array();
	$('input:checked').each(function(i, n) {
		ids.push($(n).val());
	})
	json.ids = ids;
	return json;
}

function update_tag() {
	var json = {};
	json.method = 'update_tag';
	var arr = new Array();
	$('.tag input[type="text"]').each(function(i, n) {
		if(n.value == '') return true;
		var obj = {};
		obj.id = $(this).prev().val();
		obj.name = $(this).val();
		arr[i] = obj;
	})
	var ids = new Array();
	$('input:checked').each(function(i, n) {
		ids.push($(n).val());
	})
	json.data = JSON.stringify(arr);
	json.length = arr.length;
	json.ids = ids;
	return json;
}

function delete_tag() {
	var json = {};
	json.method = 'delete_tag';
	var id = "'";
	var length = 0;
	$('.tag del').each(function(i, n) {
		id += $(this).prev().val() + "','";
		length++;
	})
	json.id = id.substring(0, id.length-2);
	var ids = new Array();
	$('input:checked').each(function(i, n) {
		ids.push($(n).val());
	})
	json.length = length;
	json.ids = ids;
	return json;
}