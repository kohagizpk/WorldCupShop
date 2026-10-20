package com.worldcupshop.worldcupshop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class CepService {

    public JsonNode consultar(String cep) {
        String numeros = cep == null ? "" : cep.replaceAll("[^0-9]", "");
        if (numeros.length() != 8) {
            throw new IllegalArgumentException("Digite um CEP com 8 números.");
        }
        try {
            String url = "https://viacep.com.br/ws/" + numeros + "/json/";
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest pedido = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(6)).GET().build();
            HttpResponse<String> resposta = cliente.send(pedido, HttpResponse.BodyHandlers.ofString());
            if (resposta.statusCode() != 200) {
                throw new IllegalArgumentException("Não foi possível consultar o CEP.");
            }
            JsonNode dados = new ObjectMapper().readTree(resposta.body());
            if (dados.path("erro").asBoolean(false)) {
                throw new IllegalArgumentException("CEP não encontrado.");
            }
            return dados;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Não foi possível consultar o ViaCEP.");
        }
    }
}
