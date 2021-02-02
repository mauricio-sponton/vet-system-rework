$(document).ready(function() {
		$(".btn-cadastrar-usuario").on('click',function(){
			$("#modal-funcionario").modal('show');
			$(".modal-title").text("Editar Dados");
		})
			
		if ($("#msg-erro").html() != "") {
			$("#modal-funcionario").modal('show');
			$("#msg-erro").css("color", "red");
			$(".is-invalid").css("border", "1px solid red");
		}

		$("#modal-funcionario").on("hidden.bs.modal", function() {
			$(".is-invalid").removeClass("is-invalid").removeAttr('style');
			$("#msg-erro").hide();
			$("#modal-funcionario").modal('hide');
			
		});
		$(function(){
			$('.teste').each(function() {
				if($(this).is(":checked")){
					$(this).closest('tr').find('.inicio, .fim').attr("readonly", "readonly");
					$(this).closest('tr').addClass("adiciona-tr");
				}
			$('.teste').on('change', function(){	
				if($(this).is(":checked")){
					$(this).closest('tr').find('.inicio, .fim').attr("readonly", "readonly");
					$(this).closest('tr').addClass("adiciona-tr");
				}else{
					if($(this).not(":checked")){
						$(this).closest('tr').find('.inicio, .fim').val();
						$(this).closest('tr').removeClass("adiciona-tr");
						$(this).closest('tr').find('.inicio, .fim').removeAttr("readonly");
					}
				}
			 })
			})		
		});
});