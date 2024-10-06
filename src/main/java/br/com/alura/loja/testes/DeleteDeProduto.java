package br.com.alura.loja.testes;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.h2.tools.Server;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.modelo.Categoria;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.util.JPAUtil;

public class DeleteDeProduto {

	public static void main(String[] args) throws SQLException, InterruptedException {
		/*
		 * Efetivamente inicia o H2 e o hibernate, 
		 * criando as tabelas defininas nas anotações JPA
		 */
		EntityManager em = JPAUtil.getEntityManager();
		
		//Inicia a console web do H2 (http://localhost:8082)
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        
        Categoria categoriaCelular = new Categoria("celulares");

		
		Produto produtoCelular = new Produto();
		produtoCelular.setNome("Galaxy S24");
		produtoCelular.setDescricao("Celular samsung galaxy s24");
		produtoCelular.setPreco(new BigDecimal(2000));
		produtoCelular.setCategoria(categoriaCelular);
		
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);
		
		em.getTransaction().begin();
		
		categoriaDao.cadastrar(categoriaCelular); //chama o persist
		produtoDao.cadastrar(produtoCelular); //chama o persist
		
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		try {
			produtoDao.remover(produtoCelular);			
		}catch (RuntimeException e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}
		em.getTransaction().commit();

		
		Thread.sleep(999999);
	}

}
