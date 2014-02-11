$('#btnSearch').click(function(e){
		var name = $('#textSearch').val();
		$.ajax({url:"/resource/datasets?data={name:'"+name+"'}",success: function(data){
			if(isJson(data)){
				results = jQuery.parseJSON(data);
				renderDatasets(results);
			}else {
				$('#content').load('pages/list_dataset.html', function(){//FIXME the server has to send always json as response
					$("#dataContainer").append(data); 
				});
			}
		}});
});

$('.category').click(function(e){
	category = $(this).attr('href');
	pageurl = '/resource/datasets?data={category:'+category+'}';
	$.ajax({url:pageurl,success: function(data){
		if(isJson(data)){
			results = jQuery.parseJSON(data);
			renderDatasets(results);
		}else {
			$('#content').load('pages/list_dataset.html', function(){//FIXME the server has to send always json as response
				$("#dataContainer").append(data); 
			});
		}
		
		if(pageurl!=window.location){
			window.history.pushState({path:'categorias/'+category},'',pageurl);
		}
	}});
	return false;
});