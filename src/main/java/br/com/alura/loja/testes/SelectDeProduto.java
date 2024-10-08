package br.com.alura.loja.testes;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import org.h2.tools.Server;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.modelo.Categoria;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.util.JPAUtil;

public class SelectDeProduto {

	public static void main(String[] args) throws SQLException, InterruptedException {
		cadastrarProdutos();

		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		
		//busca por todos produtos
		//produtoDao.buscarTodos().forEach(p -> System.out.println(p.toString()));
		
		//busca por nome de produto
		//produtoDao.buscarPorNome("Galaxy S24").forEach(p -> System.out.println(p.toString()));
		
		//busca por nome categoria
		//traz "categorias" aninhadas pois Categoria é lazy por default e o toString invoca o join
		//produtoDao.buscarPorNomeCategoria("celulares").forEach(p -> System.out.println(p.toString())); 
		
		//busca preço do produto por nome (projeção de uma coluna da entidade)
		BigDecimal precoProduto = produtoDao.buscarPrecoDoProdutoComNome("Galaxy S24");
		System.out.println("Preço do produto Galaxy S24 é: " + precoProduto);
		
		Thread.sleep(999999);

	}

	private static ProdutoDao cadastrarProdutos() throws SQLException {
		EntityManager em = JPAUtil.getEntityManager();

		Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();

		Categoria categoriaCelular = new Categoria("celulares");
		Produto produtoCelular = new Produto();
		produtoCelular.setNome("Galaxy S24");
		produtoCelular.setDescricao("Celular samsung galaxy s24");
		produtoCelular.setPreco(new BigDecimal(2000));
		produtoCelular.setCategoria(categoriaCelular);

		Categoria categoriaNotebook = new Categoria("notebooks");
		Produto produtoNotebook = new Produto();
		produtoNotebook.setNome("Notebook Lenovo");
		produtoNotebook.setDescricao("V14");
		produtoNotebook.setPreco(new BigDecimal(4000));
		produtoNotebook.setCategoria(categoriaNotebook);

		em.getTransaction().begin();

		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);

		categoriaDao.cadastrar(categoriaCelular);
		categoriaDao.cadastrar(categoriaNotebook);

		produtoDao.cadastrar(produtoCelular);
		produtoDao.cadastrar(produtoNotebook);

		em.getTransaction().commit();
		return produtoDao;
	}

}
