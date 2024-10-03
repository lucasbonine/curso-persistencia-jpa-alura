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

public class CadastroDeProduto {

	public static void main(String[] args) throws SQLException {
		EntityManager em = JPAUtil.getEntityManager(); //efetivamente inicia o H2 e o hibernate
		
		// Iniciar a console web do H2 no http://localhost:8082
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        
        Categoria categoriaCelular = new Categoria("celulares");
        
		
		Produto celular = new Produto();
		celular.setNome("Galaxy S24");
		celular.setDescricao("celular samsung galaxy s24");
		celular.setPreco(new BigDecimal(2000));
		celular.setCategoria(categoriaCelular);
		
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);
		
		em.getTransaction().begin();
		categoriaDao.cadastrar(categoriaCelular);
		produtoDao.cadastrar(celular);
		em.getTransaction().commit();
		
		//trazendo o produto
		Produto p = em.find(Produto.class, 1L);
		System.out.println(p.getId());
		System.out.println(p.getNome());
		System.out.println(p.getDescricao());
		System.out.println(p.getPreco());
		
		
		em.close();

	}

}
