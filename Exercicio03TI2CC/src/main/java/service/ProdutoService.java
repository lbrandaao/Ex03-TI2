package service;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.net.URL;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;
import java.text.SimpleDateFormat;

/**
 * @author Leonardo Barbosa Brandão
 * Ciência da Computação - PUC-Minas - COREU
 * */

public class ProdutoService {
		
	// Attributes
	private ProdutoDAO produtoDAO = new ProdutoDAO();
	private String homePage;
	private SimpleDateFormat formato;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
		
	// Constructor
	public ProdutoService() {
		homePage = renderContent("/public/index.html");
		formato = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	/**
	 * Method to get the content of a html file
	 * @param htmlFilme html file name to render
	 * */
	private String renderContent(String htmlFile) {
	    try {
	        URL url = getClass().getResource(htmlFile);
	        Path path = Paths.get(url.toURI());
	        return new String(Files.readAllBytes(path), Charset.forName("UTF8"));
	    
	    } catch (IOException | URISyntaxException e) { }
	    return null;
	}
	
	private String makeHTMLTable(int orderBy) {
		List<Produto> produtos;
		if (orderBy == FORM_ORDERBY_ID) {                 	produtos = produtoDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		produtos = produtoDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			produtos = produtoDAO.getOrderByPreco();
		} else {											produtos = produtoDAO.get();
		}
		
		String table = "";
		for (Produto p : produtos) {
			table += "<tr>"
					+ "		<td class=\"product-info\">"+ p.getID() +"</td>"
					+ "   	<td class=\"product-info\">"+ p.getDescricao() +"</td>"
					+ "  	<td class=\"product-info\">"+ p.getPreco() +"</td>"
					+ " 	<td class=\"product-info\"><i class=\"far fa-regular fa-file detail-icon\" alt="+p.getID()+"></i></td>"
					+ " 	<td class=\"product-info\"><i class=\"far fa-edit edit-icon\" alt="+p.getID()+"></i></td>"
					+ "  	<td class=\"product-info\"><i class=\"fas fa-window-close remove-icon\" alt="+p.getID()+"></i></td>"
					+ "</tr>";
		}
		
		return table;
	}
	
	public Object render(Request request, Response response, String htmlFile) {
		return homePage;
	}
	
	public Object insert(Request request, Response response) throws Exception {
		String descricao = request.queryParams("descricao");
		float preco = Float.parseFloat(request.queryParams("preco"));
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		Date dataFabricacao = formato.parse(request.queryParams("dataFabricacao"));
		Date dataValidade = formato.parse(request.queryParams("dataValidade"));
		
		Produto produto = new Produto(-1, descricao, preco, quantidade, dataFabricacao, dataValidade);
		
		if(produtoDAO.insert(produto) == true) {
            response.status(201); // 201 Created
		} else {
			response.status(404); // 404 Not found
		}
		
		return homePage;
	}
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
	    String table = makeHTMLTable(orderBy);
		response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return table;
	}
	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Produto produto = (Produto) produtoDAO.get(id);
		String resp;
		
		if (produto != null) {
			response.status(200);
			resp = "ID: " + produto.getID() + "\n"
					+"Descrição: " + produto.getDescricao() + "\n"
					+"Preço: " + produto.getPreco() + "\n"
					+"Quantidade: " + produto.getQuantidade() + "\n"
					+"Data de Fabricação: " + produto.getDataFabricacao().toString() + "\n"
					+"Data de Validade: " + produto.getDataValidade().toString() + "\n";
		}
		else {
			response.status(404);
			resp = "Produto não encontrado.";
		}
		
		return resp;
	}
	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Produto produto = (Produto) produtoDAO.get(id);
		String HTMLform = "";
		
		if (produto != null) {
			response.status(200); // success
			HTMLform = "<h4>Atualizar Produto</h4>\r\n"
					+ "            <form class=\"container\" action=\"/produto/update/"+produto.getID()+"\" method=\"post\" id=\"form\">\r\n"
					+ "                <div class=\"row\">\r\n"
					+ "                    <div class=\"col-4\">\r\n"
					+ "                        <label for=\"descricao\">Descrição do Produto</label>\r\n"
					+ "                        <input type=\"text\" name=\"descricao\" id=\"descricao\" value=\""+produto.getDescricao()+"\">\r\n"
					+ "                    </div>\r\n"
					+ "                    <div class=\"col-3\">\r\n"
					+ "                        <label for=\"preco\">Preço do Produto</label>\r\n"
					+ "                        <input class=\"input-number\" type=\"text\" name=\"preco\" id=\"preco\" value=\""+produto.getPreco()+"\">\r\n"
					+ "                    </div>\r\n"
					+ "                    <div class=\"col-4\">\r\n"
					+ "                        <label for=\"quantidade\">Quantidade do Produto</label>\r\n"
					+ "                        <input class=\"input-number\" type=\"number\" name=\"quantidade\" id=\"quantidade\" value=\""+produto.getQuantidade()+"\">\r\n"
					+ "                    </div>\r\n"
					+ "                </div>\r\n"
					+ "                \r\n"
					+ "                <div class=\"row\">\r\n"
					+ "                    <div class=\"col-4\">\r\n"
					+ "                        <label for=\"dataFabricacao\">Data de Fabricação</label>\r\n"
					+ "                        <input type=\"date\" name=\"dataFabricacao\" id=\"dataFabricacao\" value=\""+produto.getDataFabricacao()+"\">\r\n"
					+ "                    </div>\r\n"
					+ "                    <div class=\"col-4\">\r\n"
					+ "                        <label for=\"dataValidade\">Data de Validade</label>\r\n"
					+ "                        <input type=\"date\" name=\"dataValidade\" id=\"dataValidade\" value=\""+produto.getDataValidade()+"\">\r\n"
					+ "                    </div>\r\n"
					+ "                    <div class=\"col-2\">\r\n"
					+ "                        <button class=\"btn btn-primary\" type=\"submit\" id=\"btnSubmit\">Atualizar</button>\r\n"
					+ "                    </div>\r\n"
					+ "                </div>\r\n"
					+ "            </form>";
        } else {
        	response.status(404); // 404 Not found
        	HTMLform = "<h4>ERRO AO TENTAR ATUALIZAR ESTE PRODUTO</h4>";
        }

		return HTMLform;
	}
	
	public Object update(Request request, Response response) throws Exception {
        int id = Integer.parseInt(request.params(":id"));
		Produto produto = produtoDAO.get(id);
		String resp;

        if (produto != null) {
        	produto.setDescricao(request.queryParams("descricao"));
        	produto.setPreco(Float.parseFloat(request.queryParams("preco")));
        	produto.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
        	produto.setDataFabricacao(formato.parse(request.queryParams("dataFabricacao")));
        	produto.setDataValidade(formato.parse(request.queryParams("dataValidade")));
        	produtoDAO.update(produto);
        	response.status(200); // success
        	resp = homePage;
        } else {
            response.status(404); // 404 Not found
            resp = "<h4>Produto (ID \" + produto.getId() + \") não encontrado!</h4>";
        }
        
        return resp;
	}
	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);
        String resp = "";       

        if (produto != null) {
            produtoDAO.delete(id);
            response.status(200); // success
            resp = homePage;
        } else {
            response.status(404); // 404 Not found
            resp = "<h4>Produto (ID \" + produto.getId() + \") não encontrado!</h4>";
        }
		
        return resp;
	}
}