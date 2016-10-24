package com.monteiro.cobranca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.monteiro.cobranca.controller.model.StatusTitulo;
import com.monteiro.cobranca.controller.model.Titulo;
import com.monteiro.cobranca.repositoy.TituloFilter;
import com.monteiro.cobranca.repositoy.Titulos;
//@Service indica que á classe possui regras de negocio, e tranforma ela em um componente
//permitindo ela ser injetada
@Service
public class CadastroTituloService {

	@Autowired
	private Titulos titulos;
	
	public void salvar(Titulo titulo){
		
		try{
			titulos.save(titulo);
		}catch(DataIntegrityViolationException e){
		throw new IllegalArgumentException("Formato de data inválido ");
		}
		
	}

	public void remover(Long codigo) {
		
		titulos.delete(codigo);
		
	}

	public String  receber(Long codigo) {
		Titulo titulo = titulos.findOne(codigo);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		titulos.save(titulo);
		
		return StatusTitulo.RECEBIDO.getDescricao();
		
		
		
	}

	public List<Titulo> buscarTitulos(TituloFilter filtro) {
			String descricao = filtro.getDescricao()== null  ?  "%" : filtro.getDescricao();
		
		return  titulos.findByDescricaoContaining(descricao);
		
	}
	
	
	
}
