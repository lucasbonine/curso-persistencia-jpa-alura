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

public class UpdateDeProdutoComMerge {

	public static void main(String[] args) throws SQLException, InterruptedException {
		/*
		 * Efetivamente inicia o H2 e o hibernate, 
		 * criando as tabelas defininas nas anotações JPA
		 */
		EntityManager em = JPAUtil.getEntityManager();
		
		
		//Inicia a console web do H2 (http://localhost:8082)
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        
        Categoria categoriaNotebook = new Categoria("notebook");
        
        Produto produtoNotebook = new Produto();
        produtoNotebook.setNome("V14");
        produtoNotebook.setDescricao("Lenovo V14");
        produtoNotebook.setCategoria(categoriaNotebook);
        produtoNotebook.setPreco(new BigDecimal(4000));
        
        CategoriaDao categoriaDao = new CategoriaDao(em);        
        ProdutoDao produtoDao = new ProdutoDao(em);
        
        //------ Início transação -----
        em.getTransaction().begin();
        
        categoriaDao.cadastrar(categoriaNotebook); //persist
        produtoDao.cadastrar(produtoNotebook); //persist
        //categoriaNotebook e produtoNotebook 
        //estão no estado managed
        
        em.getTransaction().commit();
        //---------- Fim transação ---------
        
        verificarEstadoObjetos(em, categoriaNotebook, produtoNotebook);
        produtoNotebook.setDescricao("descricao alterada 1");
        
        //------ Início transação -----
        em.getTransaction().begin();
        em.clear(); //volta tudo para detached
        
      //"merge" devolve uma instancia "managed" para um objeto que estava detached
        Produto produtoAlterado =  produtoDao.atualizar(produtoNotebook); 
        produtoAlterado.setDescricao("descricao alterada 2");
        
        imprimirProdutos(produtoDao);

        em.getTransaction().commit();
        //---------- Fim transação ---------
       
        Thread.sleep(999999);
	}

	private static void verificarEstadoObjetos(EntityManager em, Categoria categoriaNotebook, Produto produtoNotebook) {
		if(em.contains(produtoNotebook))
        	System.out.println("Objeto produtoNotebook está managed.");
        else
        	System.out.println("Objeto produtoNotebook está detached.");
        
        if(em.contains(categoriaNotebook))
        	System.out.println("Objeto categoriaNotebook está managed.");
        else
        	System.out.println("Objeto categoriaNotebook está detached.");
        
        if(em.getTransaction().isActive())
        	System.out.println("Há transação ativa!");
        else
        	System.out.println("Não há transação ativa!");
	}
	
	private static void imprimirProdutos(ProdutoDao produtoDao) {
		produtoDao.buscarTodos().forEach(p -> System.out.println(p.toString()));	
	}

}
