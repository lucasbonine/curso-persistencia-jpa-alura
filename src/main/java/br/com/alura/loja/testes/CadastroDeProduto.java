package br.com.alura.loja.testes;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import br.com.alura.loja.Produto;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.util.JPAUtil;

public class CadastroDeProduto {

	public static void main(String[] args) {
		Produto celular = new Produto();
		celular.setNome("Galaxy S24");
		celular.setDescricao("celular samsung galaxy s24");
		celular.setPreco(new BigDecimal(2000));
		

		EntityManager em = JPAUtil.getEntityManager();
		
		ProdutoDao produtoDao = new ProdutoDao(em);
		
		em.getTransaction().begin();
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
