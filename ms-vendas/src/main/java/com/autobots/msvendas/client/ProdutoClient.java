package com.autobots.msvendas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-produtos", url = "http://ms-produtos:8086")
public interface ProdutoClient {

    @GetMapping("/api/v1/produtos/{idProduto}")
    Object buscarProdutoPorId(
            @PathVariable("idProduto") Long idProduto, 
            @RequestHeader("Authorization") String token
    );
}