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

public class InsertDeProduto {

	public static void main(String[] args) throws SQLException {
		
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
		
		/*
		 * após chamado o persist, a entidade torna-se managed e
		 *  qualquer alteração no objeto vai alterar o seu estado 
		 *  na base assim que commitado
		 */
		
		produtoCelular.setNome("Galaxy S23"); //faz update SQL porém dentro da sessão do hibernate
		produtoCelular.setDescricao("Celular samsung galaxy s24");
		
		//encerra a transação e é necessário abrir outra para realizar alterações no DB
		em.getTransaction().commit(); 
		
		//Trazendo o produto (não é necessário transação)
		Produto p = em.find(Produto.class, 1L);
		System.out.println(p.toString());
		
		
		em.close();

	}

}
