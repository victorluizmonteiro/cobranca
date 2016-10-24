package com.monteiro.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.monteiro.cobranca.controller.model.StatusTitulo;
import com.monteiro.cobranca.controller.model.Titulo;
import com.monteiro.cobranca.repositoy.TituloFilter;
import com.monteiro.cobranca.repositoy.Titulos;
import com.monteiro.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulo")
public class TituloController {
	
	public static final String CADASTRO_VIEW = "CadastroTitulo";
	//Jpa, tem o papel do DAO
	//ModelAndView, ele carrega a View para ser enviada para o browser, mas tambem carrega outros objetos, como mensagens etc.
	
	@Autowired // Cria a instancia para utilizar o DAO, Ou seja, injeta o "Entity Manager"
	private Titulos titulos;
	
	@Autowired
	private CadastroTituloService service;
	
	
	//Metodo responsavel por encontrar retornar a View , cujo o nome é o mesmo do retornado no metodo
	@RequestMapping("/novo")
	public ModelAndView retornaPagina(){
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo());
		// Deixa o objeto acessivel na pagina html, para ser usado no thymeleaf com a tag th:object
		return mv;
	}
	
	
	//O Spring MVC neste ponto, tenta converter os campos do formulario em um model, se o name do campo no form for igual ao do model
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo,Errors errors,RedirectAttributes attributes){
		//RedirectAttributes ,cria atributos para serem redirecionados antes de um redirect ou nova requisição
		//		, caso tenha uma nova requisição
		
		if(errors.hasErrors()){
			
			return CADASTRO_VIEW;
		}
		try{
			service.salvar(titulo);
			
			attributes.addFlashAttribute("mensagem", "Titulo Cadastrado com sucesso !");
			return  "redirect:/titulo/novo"; //redirect faz um nova requisição, perdendo o valor da mensagem de sucesso
		// TRATA A EXCECÃO LANÇADA PELO DatePicker,ao informar data invalida
		}catch(IllegalArgumentException e){
			errors.rejectValue("dataVencimento",null,e.getMessage());
			return CADASTRO_VIEW;
			
		}
		
		
		
	}
	//Metodo responsavel por carregar o combo box dinamicamente sem duplicar codigo, ele cria uma list do enum
	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> listarStatusTitulo(){
		
		return Arrays.asList(StatusTitulo.values());
	}
	
	//Metodo para abrir a pagina de pesquisas
	//O ModelAttribute, faz um = new FiltroTitulo 
	// O Trecho da String, trata se ela está nula, pois a pagina nao abre se ela estiver nula
	// por isso passamos % caso ela esteja nula e carregue a pagina corretamente
	@RequestMapping
	public ModelAndView pesquisa(@ModelAttribute("filtro") TituloFilter filtro){
		
		
		List<Titulo>listar = service.buscarTitulos(filtro);
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		
		mv.addObject("titulos", listar);
		return mv;
	}
	//@PathVarieble pega o caminho da url definido, e transforma em uma variavel, que aqui será usada para buscar um titulo
	// e ja abrir o formulario preenchido com os dados
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable Long codigo){
		Titulo titulo = titulos.findOne(codigo);
		
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
		
	}
	//Vai receber do form, uma action POST, QUE É CONVERTIDA PARA DELETE NO SPRING, VINDO DE UM INPUT HIDDEN
	@RequestMapping(value="{codigo}",method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo,RedirectAttributes attributes){
		service.remover(codigo);
		
		attributes.addFlashAttribute("mensagem","Titulo excluido com sucesso !");
		
		return "redirect:/titulo";	
	}
	//@ResponseBody é utilizado para não enviar um View para o usuario, e sim o corpo da pensagem em si(texto do return)
	@RequestMapping(value = "/{codigo}/receber",method = RequestMethod.PUT )
	 public  @ResponseBody String receber(@PathVariable Long codigo){
		
		return service.receber(codigo);
		
		
	}

}
