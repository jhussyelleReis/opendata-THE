function loadDataset(dataset){
		var jsobj = {'dataset':dataset};
		var results = httpGet("/data", jsobj);
		renderDatasets(results);
}

var results = [{title:'Linhas Onibus',description:'Linhas de onibus urbanos disponiveis em Teresina.'},
               {title:'Percursos Onibus', description:'Percursos feitos pelos onibus'}]; 
var version = 1.2;

function renderDatasets(results) {
		var ds_results = new EJS({url:'templates/dataset_result.ejs?'+version}).render(results);
		$("#ds_results").append(ds_results);
};