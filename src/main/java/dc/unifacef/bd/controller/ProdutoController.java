package dc.unifacef.bd.controller;

import dc.unifacef.bd.model.Produto;
import dc.unifacef.bd.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController // Ele recebe as requisições HTTP
@RequestMapping("/produtos")

public class ProdutoController {
    // Vamos usar o objeto da classe ProdutoService - Injeção de dependência
    // Vamos usar o construtor pra isso

    private ProdutoService service;
    public ProdutoController(ProdutoService service){
        this.service = service;
    }

    // Por que usar ResponseEntity? Para retornar diferentes statusCode ao FE
    @GetMapping
    public ResponseEntity<List<Produto>> listar(){
        return ResponseEntity.ok(service.listar()); // statusCode: 200 - OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Produto>> buscarPorId(@PathVariable Long id){
        Optional<Produto> prod = service.buscarPorId(id);
        if(prod.isEmpty()){
            return ResponseEntity.notFound().build();    // statusCode: 404 - Produto não existente
        }
        return ResponseEntity.ok(prod);     // statussCode: 200 - Retorna produto encontrado
    }

    @PostMapping
    public ResponseEntity<Produto> salva(@RequestBody Produto produto){
        Produto novo = service.salva(produto);
        if(novo != null){   // é pq ele retornou um produto salvo
            // Vamos montar uma URI - Uniform Resource Identifier
            URI uri = URI.create("/produtos/" + novo.getId());  // ele cria o ID automaticamente
            return ResponseEntity.created(uri).body(novo);
        }
        return ResponseEntity.noContent().build();  // statusCode: 204
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id){
        if(service.remove(id)){
            return ResponseEntity.noContent().build();  // statusCode: 204
        }
        return ResponseEntity.notFound().build();   // statusCode: 404
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id,
                                             @RequestBody Produto alterado){
        Produto resposta = service.atualiza(id, alterado);
        if(resposta != null){
            return ResponseEntity.ok(resposta);
        }
        return ResponseEntity.notFound().build();
    }
}