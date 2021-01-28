
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
	        	{"width":"5%", "targets": [5,3,4]},
	        	{"width":"1%", "targets": [0]},
	        	{"width":"8%", "targets": [2]}
	        ],
	      
	    	searching: true,
	    	order: [
	    		[ 0, "asc" ]
	    		],
	    	lengthMenu: [5, 10],
	        processing: true,
	        serverSide: true,
	        responsive: false,
	        ajax: {
	            url: '/perfis/datatables/server',
	            data: 'data'
	        },
	        columns: [
	        	 {
	                 "className":      'details-control',
	                 "orderable":      false,
	                 "data":           null,
	                 "defaultContent": '<i class="fab fa-suse" style="color:green; vertical-align: middle;"></i>'
	             },
	            {data: 'desc'},
	            {	data : 'ativo', 
					render : function(ativo) {
						return ativo == true ? 'Sim' : 'Não';
					}
				},
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
	//	
//		// acao para marcar/desmarcar linhas clicadas 
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
		
			
		})
		$('#btn-editar').on('click', function(){
			var link = $(this).val();
			document.location.href = link;
		})
		 $('#table-perfis tbody').on('click', 'tr', function () {
				var link =table.row(table.$('tr.selected')).data().id;
				$('#btn-editar').attr("href", '/perfis/editar/' + link);
			 
		        var tr = $(this).closest('tr');
		        var row = table.row( tr );
		 
		        if ( row.child.isShown() ) {
		            // This row is already open - close it
		        	 $('div.slider', row.child()).slideUp( function () {
		                 row.child.hide();
		                 tr.removeClass('shown');
		             } );
		        	 $('#btn-editar').attr("href", '/perfis/editar/' + link);
		        }
		        else {
		        	if ( table.row( '.shown' ).length ) {
		                $('.details-control', table.row( '.shown' ).node()).click();
		        }
		            // Open this row
		            row.child( format(row.data()), 'no-padding' ).show();
		            tr.addClass('shown');
		            $('div.slider', row.child()).slideDown();
		            $('#btn-editar').attr("href", '/perfis/editar/' + link);
		        }
		    } );
	    
	});
	function format ( d ) {
	    // `d` is the original data object for the row
//	    return '<div class="slider"><table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
//	        '<tr>'+
//	            '<td>Full name:</td>'+
//	            '<td>'+d.desc+'</td>'+
//	        '</tr>'+
//	        '<tr>'+
//	            '<td>Extension number:</td>'+
//	            '<td>'+d.ativo+'</td>'+
//	        '</tr>'+
//	    '</table></div>';
		
		if(d.ativo == true){
			d.ativo = 'Sim'
		}else{
			d.ativo = 'Não'
		}
		return '<div class="slider"><span>Descrição: </span>' + '<span>'+ d.desc +'</span><br><span>Ativo: </span>' 
		+ '<span>'+ d.ativo + '</span>';
	}
