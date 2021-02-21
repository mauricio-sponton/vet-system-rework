// 
//$(document).ready(function () {
//	moment.locale('pt-BR');
//    var table = $('#table-internacoes').DataTable({
//    	dom:
//            "<'row'<'col-sm-12 col-md-6'l><'col-sm-12 col-md-6'<'float-md-right ml-2'B>f>>" +
//            "<'row'<'col-sm-12'tr>>" +
//            "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>>",
//            
//            buttons: [ 'csv', {
//	            'text': '<i class="fa fa-id-badge fa-fw" aria-hidden="true"></i>',
//	            'action': function (e, dt, node) {
//
//	               $(dt.table().node()).toggleClass('cards');
//	               $('.fa', node).toggleClass(['fa-table', 'fa-id-badge']);
//
//	               dt.draw('page');
//	            },
//	            'className': 'btn-sm',
//	            'attr': {
//	               'title': 'Change views',
//	            }
//	         }],
//	         'select': 'single',
//    	"language":{
//	    		"lengthMenu": "Mostrando _MENU_ registros por páginas",
//	    		"zeroRecords": "Nenhum registro encontrado",
//	            "info": "Mostrando página _PAGE_ de _PAGES_",
//	            "infoEmpty": " ",
//	            "search": "Pesquise: ",
//	            "searchPlaceholder": "Paciente ou status...",
//	            "paginate": {
//	                "first":      "Primeiro",
//	                "last":       "Ultimo",
//	                "next":       "Próximo",
//	                "previous":   "Anterior"
//	            } 
//	    	},
//	    	"columnDefs":[
//	        	{"width":"2%", "targets": [4,5, 6]}
//	        ],
//	       
//	         
//    	searching: true,
//    	order: [[ 0, "asc" ]],
//    	lengthMenu: [5, 10],
//        processing: true,
//        serverSide: true,
//        responsive: true,
//        ajax: {
//            url: '/internacoes/datatables/server',
//            data: 'data'
//        },
//        columns: [
//        	 
//        	{data: 'status', render :
//        		function(data, type, row){
//        		if(row.status == "Ativa"){
//        			return '<strong class = "ativa">'+ row.status +'</strong>';
//        		}else{
//        			return '<strong class = "encerrada">'+ row.status +'</strong>';
//        		}
//        	}
//        },
//        {data: 'animal.nome',
//       	 "render": function(data, type, row){
//       		return data .length> 10 ? data.substr( 0, 10 ) + '...' : data;
//       	 }
//           },
//            {data: 'dataEntrada', render:
//                function( data ) {
//                    return moment(data).format('L');
//                }
//            },
//            {data: 'dataSaida', render:
//                function( data, type, row ) {
//            		if(row.dataSaida == null){
//            			return "Internação em andamento"
//            		}
//                    return moment(data).format('L');
//                }
//            },
//            {orderable: false, 
//             data: 'id',
//                "render": function(id) {
//                    return '<a class="btn btn-success  btn-sm btn-block" href="/internacoes/editar/'+ 
//                    	id +'" role="button"><i class="fas fa-edit"></i></a>';
//                }
//            },
//            {orderable: false,
//             data: 'id',
//                "render": function(id) {
//                    return '<a class="btn btn-danger btn-sm btn-block" href="/internacoes/excluir/'+ 
//                    	id +'" role="button"  data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>';
//                }               
//            },
//            {orderable: false,
//                data: 'id',
//                   "render": function(id) {
//                	   return '<a class="btn btn-sm btn-block btn-view" href="/internacoes/visualizar/'+ 
//                   	id +'" role="button"><i class="fas fa-glasses"></i></a>';
//                   }               
//               }
//        ], 
//        'drawCallback': function (settings) {
//            var api = this.api();
//            var $table = $(api.table().node());
//            
//            if ($table.hasClass('cards')) {
//
//               // Create an array of labels containing all table headers
//               var labels = [];
//               $('thead th', $table).each(function () {
//                  labels.push($(this).text());
//               });
//
//               // Add data-label attribute to each cell
//               $('tbody tr', $table).each(function () {
//                  $(this).find('td').each(function (column) {
//                     $(this).attr('data-label', labels[column]);
//                  });
//               });
//
//               var max = 0;
//               $('tbody tr', $table).each(function () {
//                  max = Math.max($(this).height(), max);
//               }).height(max);
//
//            } else {
//               // Remove data-label attribute from each cell
//               $('tbody td', $table).each(function () {
//                  $(this).removeAttr('data-label');
//               });
//
//               $('tbody tr', $table).each(function () {
//                  $(this).height('auto');
//               });
//            }
//         }
//        
//    });
//   
//});    
//
