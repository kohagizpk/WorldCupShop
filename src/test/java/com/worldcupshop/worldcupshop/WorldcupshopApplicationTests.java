package com.worldcupshop.worldcupshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EnumSource;
import com.worldcupshop.worldcupshop.model.TipoUsuario;
import com.worldcupshop.worldcupshop.model.Usuario;
import com.worldcupshop.worldcupshop.repository.UsuarioRepository;
import com.worldcupshop.worldcupshop.service.ImagemService;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
class WorldcupshopApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UsuarioRepository usuarios;

	@Autowired
	private ImagemService imagens;

	@Test
	void contextLoads() {
	}

	@ParameterizedTest
	@ValueSource(strings = {"/login", "/cadastro", "/produtos"})
	void paginasPublicasRenderizam(String rota) throws Exception {
		mvc.perform(get(rota)).andExpect(status().isOk());
	}

	@ParameterizedTest
	@ValueSource(strings = {"/admin", "/carrinho", "/pedidos", "/perfil"})
	void paginasPrivadasExigemLogin(String rota) throws Exception {
		mvc.perform(get(rota)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));
	}

	@Test
	void imagemInexistenteRetorna404() throws Exception {
		mvc.perform(get("/imagens/invalida.jpg")).andExpect(status().isNotFound());
	}

	@Test
	void visitanteNaoVeOpcoesAdministrativas() throws Exception {
		mvc.perform(get("/login")).andExpect(status().isOk())
				.andExpect(content().string(not(containsString("href=\"/admin/"))));
	}

	@ParameterizedTest
	@EnumSource(TipoUsuario.class)
	@Transactional
	void menuEPermissoesRespeitamPerfil(TipoUsuario tipo) throws Exception {
		Usuario usuario = new Usuario();
		usuario.setNome("teste-menu-" + UUID.randomUUID());
		usuario.setSenha("senha-de-teste");
		usuario.setTipo(tipo);
		usuarios.saveAndFlush(usuario);
		MockHttpSession sessao = new MockHttpSession();
		sessao.setAttribute("usuarioId", usuario.getId());
		boolean admin = tipo == TipoUsuario.ADMIN;
		mvc.perform(get("/produtos").session(sessao)).andExpect(status().isOk())
				.andExpect(content().string(admin
						? allOf(containsString("href=\"/admin/produtos\""),
								containsString("href=\"/admin/categorias\""),
								containsString("href=\"/admin/selecoes\""))
						: not(containsString("href=\"/admin/"))));
		mvc.perform(get("/admin/produtos/criar").session(sessao))
				.andExpect(admin ? status().isOk() : status().isForbidden());
	}

	@Test
	void aceitaEServeWebp() throws Exception {
		// Imagem WebP VP8 de 1 x 1 pixel; valida a decodificação, não só a extensão.
		byte[] webp = Base64.getDecoder().decode("UklGRiIAAABXRUJQVlA4IBYAAAAwAQCdASoBAAEADsD+JaQAA3AAAAAA");
		String url = imagens.salvar(new MockMultipartFile("imagemArquivo", "produto.WEBP", "image/webp", webp));
		try {
			assertTrue(url.endsWith(".webp"));
			mvc.perform(get(url)).andExpect(status().isOk())
					.andExpect(content().contentType("image/webp"))
					.andExpect(content().bytes(webp));
		} finally {
			Files.deleteIfExists(imagens.localizar(url.substring("/imagens/".length())));
		}
	}

	@Test
	void rejeitaWebpFalso() {
		MockMultipartFile arquivo = new MockMultipartFile("imagemArquivo", "falso.webp", "image/webp", new byte[] {1, 2, 3});
		assertThrows(IllegalArgumentException.class, () -> imagens.salvar(arquivo));
	}

	@Test
	void rejeitaImagemAcimaDeCincoMb() {
		MockMultipartFile arquivo = new MockMultipartFile("imagemArquivo", "grande.webp", "image/webp", new byte[5 * 1024 * 1024 + 1]);
		assertThrows(IllegalArgumentException.class, () -> imagens.salvar(arquivo));
	}

}
