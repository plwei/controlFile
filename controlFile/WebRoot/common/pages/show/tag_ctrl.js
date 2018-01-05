var dqzt = '';
$(function(){
	$('.tag ul').each(function(i, n) {
		var height = $(this).height();
		$(this).parent().height(height);
	})
	$('.subnav td').on('click', function() {
		$('.subnav td').removeClass('active');
		$(this).addClass('active');
		dqzt = this.id;
	})
	$('.tag div:last-child').hide();
	$('input[name="bqjb"]').on('click', function() {
		var value = $(this).val();
		if(value == 0) {
			$('.tag div:last-child').show();
		} else {
			tr_tag(0);
		}
	})
	$('input[name^="bq_"]').on('click', function() {
		var bqjb = $('input[name="bqjb"]').val();
		var jb = this.name.split('_')[1];
		var id = $(this).val();
		if(jb < bqjb) {
			tr_tag(id)
		} else {
			$('.tag div:last-child').show();
		}
	})
})

function tr_tag(id) {
	$.ajax({
		url: '',
		data: '',
		success: function() {
			
		}
	})
}

function addInput() {
	var html = '<li class="tag_input">';
	html += '<input type="text" name="bqmc"> ';
	html += '<input type="button" value="-" onclick="delInput(this)">';
	html += '</li>';
	$('.tag div:last-child ul').append(html);
}

function delInput(obj) {
	$(obj).parent().remove();
}

function tag_upd(obj) {
	var text = $(obj).text();
	var input = '<input type="text" name="bqmc" value='+text+' style="width: 90px">';
	$(obj).replaceWith(input);
}



