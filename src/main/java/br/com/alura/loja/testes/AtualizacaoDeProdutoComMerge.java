package br.com.alura.loja.testes;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import org.h2.tools.Server;
import org.hibernate.FlushMode;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.modelo.Categoria;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.util.JPAUtil;

public class AtualizacaoDeProdutoComMerge {

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
        
        em.getTransaction().begin();
        
        categoriaDao.cadastrar(categoriaNotebook);
        produtoDao.cadastrar(produtoNotebook);
        
        //-------------- AQUI categoriaNotebook e categoriaNotebook ESTÃO EM MODO "MANAGED" POIS CHAMOU "persist" ----------
        
        em.getTransaction().commit(); //alteração no banco
        
        //-------------- AQUI ESTÁ EM MODO DETACHED pois chamou "commit" ----------
                
        produtoNotebook.setDescricao("descricao alterada");
        
        em.getTransaction().begin();
        
        produtoDao.atualizar(produtoNotebook); //<<<<----------- atualização do produtoNotebook com "merge"
        
      //-------------- AQUI ESTÁ EM MODO MANAGED POIS CHAMOU "merge" ----------
        
       imprimirProdutos(produtoDao);
       
       em.getTransaction().commit();
       
       Thread.sleep(999999); //para poder debugar

	}
	
	private static void imprimirProdutos(ProdutoDao produtoDao) {
		produtoDao.buscarTodos().forEach(p -> System.out.println(p.toString()));	
	}

}
