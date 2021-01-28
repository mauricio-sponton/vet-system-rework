$(document).ready(function() {

	$('#form-permissoes input[type="checkbox"]').on('change', function() {
		var checkedValue = $(this).prop('checked');
		// uncheck sibling checkboxes (checkboxes on the same row)
		$(this).closest('tr').find('input[type="checkbox"]').each(function() {
			$(this).prop('checked', false);
		});
		$(this).prop("checked", checkedValue);

	});
	moment.locale('pt-BR');
    var table = $('#table-perfis').DataTable({
    	
    	"language":{
    		"lengthMenu": "Mostrando _MENU_ registros por páginas",
    		"zeroRecords": "Nenhum registro encontrado",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": " ",
            "search": "Pesquise: ",
            "searchPlaceholder": "Descrição...",
            "paginate": {
                "first":      "Primeiro",
                "last":       "Ultimo",
                "next":       "Próximo",
                "previous":   "Anterior"
            } 
    	},
    	"columnDefs":[
        	{"width":"5%", "targets": [1,2,3]}
        ],
    	searching: true,
    	order: [[ 0, "asc" ]],
    	lengthMenu: [5, 10],
        processing: true,
        serverSide: true,
        responsive: false,
        ajax: {
            url: '/perfis/datatables/server',
            data: 'data'
        },
        columns: [
            {data: 'desc'},
            {orderable: false, 
                data: 'id',
                   "render": function(id) {
                       return '<a class="btn btn-success  btn-sm btn-block" href="/perfis/editar/'+ 
                       	id +'" role="button"><i class="fas fa-edit"></i></a>';
                   }
               },
               {orderable: false,
                data: 'id',
                   "render": function(id) {
                       return '<a id="btn-del-cliente" class="btn btn-danger btn-sm btn-block" href="/perfis/excluir/'+ 
                       	id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>';
                   }               
               },
               {orderable: false,
                   data: 'id',
                      "render": function(id) {
                   	   return '<a class="btn btn-sm btn-block btn-view" href="/perfis/visualizar/'+ 
                      	id +'" role="button"><i class="fas fa-glasses"></i></a>';
                      }               
                  }
        ],
        dom: 'Bfrtip',
		buttons: [
			{
				text: 'Editar',
				attr: {
					data: 'id',
					id: 'btn-editar',
					type: 'button',
					class: "btn btn-success-new btn-sm ",
					// href:'/clientes/editar/' + id
				},
				enabled: false
			},
			{
				text: 'Excluir',
				attr: {
					id: 'btn-excluir',
					type: 'button',
					class: "btn btn-danger-new btn-sm"
				},
				enabled: false
			},
			{
				text: 'Visualizar',
				attr: {
					id: 'btn-visualizar',
					type: 'button',
					class: "btn btn-sm btn-view-new"
				},
				enabled: false
			}
		]
    });
 // acao para marcar/desmarcar botoes ao clicar na ordenacao 
	$("#table-perfis thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});
	
	// acao para marcar/desmarcar linhas clicadas 
	$("#table-perfis tbody").on('click', 'tr', function() {		
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');	
			table.buttons().disable();
		} else {			
			$('tr.selected').removeClass('selected');			
			$(this).addClass('selected');
			table.buttons().enable();
		}
	});
	
	$("#table-perfis tbody").on('click', 'tr', function(){
		var link =table.row(table.$('tr.selected')).data().id;
		$('#btn-editar').attr("href", '/perfis/editar/' + link);
		
	})
	$('#btn-editar').on('click', function(){
		var link = $(this).val();
		document.location.href = link;
	})
		
});
