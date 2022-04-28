package app;
import static spark.Spark.*;
import service.ProdutoService;

/**
 * @author Leonardo Barbosa Brandão
 * Ciência da Computação - PUC-Minas - COREU
 * */


public class Aplicacao {
	
	private static ProdutoService produtoService = new ProdutoService();
	
    public static void main(String[] args) {
        port(6788);
        
        staticFiles.location("/public");
        
        get("/", (request, response) -> produtoService.render(request, response, "index.html"));
        
        post("/produto/insert", (request, response) -> produtoService.insert(request, response));
        
        get("/produto/:id", (request, response) -> produtoService.get(request, response));
        
        get("/produto/list/:orderby", (request, response) -> produtoService.getAll(request, response));
        
        get("/produto/update/:id", (request, response) -> produtoService.getToUpdate(request, response));
       
        post("/produto/update/:id", (request, response) -> produtoService.update(request, response));
           
        get("/produto/delete/:id", (request, response) -> produtoService.delete(request, response));
    }
}