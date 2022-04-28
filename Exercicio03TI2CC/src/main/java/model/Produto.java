package model;
import java.util.Date;

public class Produto {
	private int id;
	private String descricao;
	private float preco;
	private int quantidade;
	private Date dataFabricacao;	
	private Date dataValidade;
	
	public Produto() {
		id = -1;
		descricao = "";
		preco = 0.00F;
		quantidade = 0;
		dataFabricacao = new Date();
		dataValidade = new Date(); 
	}

	public Produto(int id, String descricao, float preco, int quantidade, Date fabricacao, Date v) {
		setId(id);
		setDescricao(descricao);
		setPreco(preco);
		setQuantidade(quantidade);
		setDataFabricacao(fabricacao);
		setDataValidade(v);
	}		
	
	public int getID() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public float getPreco() {
		return preco;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public Date getDataValidade() {
		return dataValidade;
	}

	public Date getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(Date dataFabricacao) {
		// Pega a Data Atual
		Date agora = new Date();
		// Garante que a data de fabricaÁ„o n„o pode ser futura
		if (agora.compareTo(dataFabricacao) >= 0)
			this.dataFabricacao = dataFabricacao;
	}

	public void setDataValidade(Date dataValidade) {
			this.dataValidade = dataValidade;
	}

	/**
	 * M√©todo sobreposto da classe Object. √â executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Produto: " + descricao + "   Pre√ßo: R$" + preco + "   Quantidade.: " + quantidade + "   Fabrica√ß√£o: "
				+ dataFabricacao.toString()  + "   Data de Validade: " + dataValidade.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Produto) obj).getID());
	}	
}