package com.worldcupshop.worldcupshop.controller;

import com.worldcupshop.worldcupshop.service.ImagemService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImagemController {
    private final ImagemService imagens;
    public ImagemController(ImagemService imagens) { this.imagens = imagens; }

    @GetMapping("/imagens/{nome:.+}")
    public ResponseEntity<FileSystemResource> mostrar(@PathVariable String nome) throws IOException {
        Path arquivo;
        try { arquivo = imagens.localizar(nome); }
        catch (IllegalArgumentException e) { return ResponseEntity.notFound().build(); }
        if (!Files.isRegularFile(arquivo)) return ResponseEntity.notFound().build();
        MediaType tipo = nome.endsWith(".png") ? MediaType.IMAGE_PNG
                : nome.endsWith(".gif") ? MediaType.parseMediaType("image/gif") : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(tipo).body(new FileSystemResource(arquivo));
    }
}
