package br.com.alura.loja.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.alura.loja.modelo.Produto;

public class ProdutoDao {
	
	private EntityManager em;
	
	public ProdutoDao(EntityManager em) {
		this.em = em;
	}
	
	public void cadastrar(Produto produto) {
		em.persist(produto);
	}
	
	public Produto atualizar(Produto produto) {
		return em.merge(produto);
	}
	
	public List<Produto> buscarTodos() {
		String jpql = "SELECT p FROM Produto p";
		return em.createQuery(jpql, Produto.class).getResultList();
	}
	
	public void remover(Produto produto) {
		if(!em.contains(produto))
			throw new RuntimeException("Produto não está managed para ser removido!");

		this.em.remove(produto);
	}
	
	public Produto buscarPorId(Long id) {
		return this.em.find(Produto.class, id);
	}
	
	public List<Produto> buscarPorNome(String nome) {
		String jpql = "Select p from Produto p where nome = :nome";

		return em.createQuery(jpql, Produto.class).setParameter("nome", nome).getResultList();
	}
	
	public List<Produto> buscarPorNomeCategoria(String nomeCategoria) {
		String jpql = "Select p from Produto p where p.categoria.nome = :nomeCategoria";

		return em.createQuery(jpql, Produto.class).setParameter("nomeCategoria", nomeCategoria).getResultList();
	}
	
	public BigDecimal buscarPrecoDoProdutoComNome(String nome) {
		String jpql = "Select p.preco from Produto p where p.nome = :nome";
		return em.createQuery(jpql, BigDecimal.class).setParameter("nome", nome).getSingleResult();
	}
	
	

}
