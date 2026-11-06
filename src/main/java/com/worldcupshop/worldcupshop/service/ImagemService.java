package com.worldcupshop.worldcupshop.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagemService {
    private final Path pasta = Paths.get("uploads", "produtos").toAbsolutePath().normalize();

    public String salvar(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) return null;
        if (arquivo.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("A imagem deve ter no máximo 5 MB.");
        }
        String extensao;
        try (InputStream conteudo = arquivo.getInputStream();
             ImageInputStream entrada = ImageIO.createImageInputStream(conteudo)) {
            if (entrada == null) throw new IllegalArgumentException("Arquivo de imagem inválido.");
            Iterator<ImageReader> leitores = ImageIO.getImageReaders(entrada);
            if (!leitores.hasNext()) throw new IllegalArgumentException("Use uma imagem JPG, PNG, GIF ou WebP.");
            ImageReader leitor = leitores.next();
            try {
                leitor.setInput(entrada);
                String formato = leitor.getFormatName().toLowerCase(java.util.Locale.ROOT);
                if (!formato.equals("jpeg") && !formato.equals("jpg") && !formato.equals("png")
                        && !formato.equals("gif") && !formato.equals("webp")) {
                    throw new IllegalArgumentException("Use uma imagem JPG, PNG, GIF ou WebP.");
                }
                // Decodifica a imagem para garantir que o conteúdo é uma imagem válida.
                if (leitor.read(0) == null) throw new IllegalArgumentException("Imagem inválida.");
                extensao = formato.equals("jpeg") || formato.equals("jpg") ? "jpg" : formato;
            } finally { leitor.dispose(); }
        } catch (IOException e) {
            throw new IllegalArgumentException("Não foi possível ler a imagem.", e);
        }
        Files.createDirectories(pasta);
        String nome = UUID.randomUUID() + "." + extensao;
        try (InputStream conteudo = arquivo.getInputStream()) {
            Files.copy(conteudo, pasta.resolve(nome));
        }
        return "/imagens/" + nome;
    }

    public Path localizar(String nome) {
        if (!nome.matches("[0-9a-fA-F-]{36}\\.(jpg|png|gif|webp)")) {
            throw new IllegalArgumentException("Nome de imagem inválido.");
        }
        return pasta.resolve(nome).normalize();
    }
}
