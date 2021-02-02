$(document).ready(function () {
	moment.locale('pt-BR');
    var table = $('#table-vacinas').DataTable({
    	"language":{
    		"lengthMenu": "Mostrando _MENU_ registros por páginas",
    		"zeroRecords": "Nenhum registro encontrado",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": " ",
            "search": "Pesquise: ",
            "searchPlaceholder": "Descrição ou Espécie...",
            "paginate": {
                "first":      "Primeiro",
                "last":       "Ultimo",
                "next":       "Próximo",
                "previous":   "Anterior"
            } 
    	},
    	searching: true,
    	order: [[ 0, "asc" ]],
    	lengthMenu: [5, 10],
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '/imunizacoes/datatables/server',
            data: 'data'
        },
        "columnDefs":[
         	{"width":"5%", "targets": [4,5]}
         ],
        columns: [
            {data: 'descricao'},
            {data: 'doses'},
            {data: 'intervalo'},
            {data: 'especie.nome'},
            {orderable: false, 
             data: 'id',
                "render": function(id) {
                    return '<a class="btn btn-success btn-sm btn-block" href="/imunizacoes/editar/'+ 
                    	id +'" role="button"><i class="fas fa-edit"></i></a>';
                }
            },
            {orderable: false,
             data: 'id',
                "render": function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/imunizacoes/excluir/'+ 
                    	id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>';
                }               
            }
        ]
    });
    var table = $('#table-vacinas-sec').DataTable({
    	"language":{
    		"lengthMenu": "Mostrando _MENU_ registros por páginas",
    		"zeroRecords": "Nenhum registro encontrado",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": " ",
            "search": "Pesquise: ",
            "searchPlaceholder": "Descrição ou Espécie...",
            "paginate": {
                "first":      "Primeiro",
                "last":       "Ultimo",
                "next":       "Próximo",
                "previous":   "Anterior"
            } 
    	},
    	  "columnDefs":[
          	{"width":"40%", "targets": [0]},
          	{"width":"10%", "targets": [1,2,3]}
          ],
    	searching: true,
    	order: [[ 0, "asc" ]],
    	lengthMenu: [5, 10],
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '/vacinas/datatables/server',
            data: 'data'
        },
        columns: [
            {data: 'descricao'},
            {data: 'doses'},
            {data: 'intervalo'},
            {data: 'especie.nome'},
            
        ]
    });
    if ($("#msg-erro").html() != "") {
		
		$("#modal-vacina").modal('show');
		$(".invalid-feedback span").css('color', 'red');		
	}

	$("#modal-vacina").on("hidden.bs.modal", function() {
		$(".is-invalid").removeClass("is-invalid").removeAttr('style');
		$("#msg-erro").hide();
		$("#modal-vacina").modal('hide');
		
	});
	
	$(".btn-cadastrar-usuario").on("click", function(){
		$(".form-control").val("");
		$(".modal-title").text("Nova Vacina");
	});	
	$('#confirm-modal').on('show.bs.modal', function(){
		$(".modal-title").text("Excluir Imunização");
		$('#subtitulo').text('Todas aplicações que possuem essa imunização serão deletadas!');
	})
});    