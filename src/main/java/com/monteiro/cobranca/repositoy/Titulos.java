package com.monteiro.cobranca.repositoy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monteiro.cobranca.controller.model.Titulo;

public interface Titulos extends JpaRepository<Titulo, Long> {
	// Spring data Jpa ja cria a query com o like , utilizando o Containing
	List<Titulo>findByDescricaoContaining(String descricao);

}
