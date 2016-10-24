//Pega o id do modal, e quando clicar no botao excluir, executa um evento
$('#confirmacaoExclusaoModal').on('show.bs.modal',function(event){
	// Pega o botão que disparou o evento
	var button = $(event.relatedTarget);
	//recupera os valores do atributo tipo  data do botao que disparou o evento
	var codigoTitulo = button.data('codigo');
	
	var descricao = button.data('descricao');
	
	//tranforma o modal em objeto Jquery
	var modal = $(this);
	
	//busca o form dentro do modal
	var form = modal.find('form');
	
	//pega o valor do campo data-base, do modal 
	var action = form.data('base')
	
	if(!action.endsWith('/')){
		action += "/";
	}
	
	form.attr('action',action + codigoTitulo);
	//Busco a classe modal-body e dentro dela o span, inserindo texto dentro nela atravez o .html
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o titulo <strong>' 
			+ descricao + '</strong> ?')
	
	
	
	
});

//função vai rodar logo apos carregar a pagina
$(function(){
	//procura por rel=tooltip na pagina, e executa uma ação
	$('[rel=tooltip]' ).tooltip();
	//busca na página todos os campos que tenham a classe js-currency e aplica a mascara
	$('.js-currency').maskMoney({decimal:',',thousands:'.'});
	
	$('.js-atualizar-status').on('click',function(event){
		//não executa o comportamento padrão do link,que é redirecionar para outra pagina
		event.preventDefault();
		//pega o botao que está sendo submetido .currentTarget é usado pois o botao  é um link
		var botaoReceber = $(event.currentTarget);
		//busca a propriedade href do botao, cuja classe é js-atualizar-status e recupera o valor dentro dele
		var urlReceber = botaoReceber.attr('href');
		//Função responsavel por fazer a requisição no servidor
		var response = $.ajax({
			url: urlReceber,
			type: 'PUT'
		});
		//se deu ok na requisição,executo uma ação com a resposta do servidor
		// o parametro e da função, ele é o retorno da requisição, ou seja, o que colocamos no retorno do metodo com
		//@ResponseBody (metodo receber)
		response.done(function(e){
			//busca o atributo do tipo data do botão
			var codigoTitulo = botaoReceber.data('codigo');
			//busca o data-role  que tenha  o codigo do titulo especifico na pagina
			//Depois substitui a classe do titulo selecionado
			$('[data-role=' + codigoTitulo + ']').html('<span class="label label-success">' + e+  '</span>');
			botaoReceber.hide();
		});
	});
	
	
	
	
	
})