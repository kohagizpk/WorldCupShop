# WorldCupShop
Projeto escolar em **Java 21, Spring Boot 4.1.2, PostgreSQL, Thymeleaf e Bootstrap**.

## Como executar
1. Instale Java 21, PostgreSQL e Maven (ou use `mvnw.cmd` no Windows).
2. No PostgreSQL, crie o banco: `CREATE DATABASE worldcupshop;` (por exemplo, no pgAdmin).
3. Configure usuário `postgres`, senha `postdba` no PostgreSQL local (ou passe `DB_PASSWORD` como variável de ambiente).
4. Na pasta do projeto, execute `mvnw.cmd spring-boot:run` no Windows ou `./mvnw spring-boot:run` no Linux.
5. Acesse http://localhost:8080.

## Administrador
Todos os novos cadastros são clientes. **Nenhum administrador é criado automaticamente.** Para promover um usuário de teste já cadastrado, execute no PostgreSQL (apenas em ambiente escolar/local):
```sql
UPDATE usuario SET tipo = 'ADMIN' WHERE nome = 'nome_do_usuario';
```
Saia e entre novamente com a conta promovida. O acesso a `/admin` exige perfil ADMIN.

## Observações
- A aplicação simula pedidos, sem cobrar pagamentos reais.
- Páginas HTML + CSS + Bootstrap + Thymeleaf; sem JavaScript escrito pelo projeto.
- Banco criado automaticamente pelo JPA a partir das entidades após criar o database `worldcupshop`.
- Imagens dos produtos são URLs informadas pelo administrador; não há upload.
- Evite publicar senhas em repositórios públicos. Troque a senha usada para desenvolvimento antes de publicar.

## Estrutura
- `model`: entidades originais (8 classes e 2 enums).
- `repository`: interfaces JpaRepository.
- `service`: regras de cadastro, consulta, estoque, carrinho e pedidos.
- `controller`: rotas e ligação com as telas.
- `resources/templates`: HTML com Thymeleaf e Bootstrap.

## Testes
Execute `.\mvnw.cmd verify` no Windows (ou `./mvnw verify` no Linux). Os testes verificam a inicialização, as páginas públicas, o redirecionamento das páginas privadas para login e a resposta para imagens inexistentes. Eles utilizam o PostgreSQL configurado em `application.properties`; o banco precisa estar disponível.

O Maven configura o agente do Mockito na inicialização da JVM de testes, evitando o carregamento dinâmico de agentes. O compartilhamento de classes (CDS) fica desativado apenas nessa JVM, por incompatibilidade com a instrumentação usada nos testes.


## Alterações visuais e CEP
O layout foi inspirado nas referências enviadas: cabeçalho azul, fundo cinza, cards e formulários brancos, com Bootstrap e CSS, **sem JavaScript**.
No cadastro, informe o CEP e clique em **Buscar CEP**; o servidor Spring consulta o ViaCEP e preenche endereço, bairro, cidade e UF. Como não há JavaScript, a consulta não ocorre automaticamente enquanto digita.
Os dados do endereço são armazenados nos novos campos do usuário: `cep`, `endereco`, `bairro`, `cidade`, `uf`, `numero` e `complemento`.

Na primeira inicialização, é criado `admin` com senha `123456` caso não exista esse nome no banco. A senha fica armazenada com BCrypt. **Troque essa senha antes de publicar o site na internet.**


## PostgreSQL

As oito entidades utilizam `GenerationType.SEQUENCE`. O banco continua chamado `worldcupshop` e `ddl-auto=update` cria as tabelas e sequências. A migração para Spring Boot 4 foi validada com PostgreSQL 18.6; PostgreSQL 9.5 não é uma versão suportada por esta configuração.

## Fotos dos produtos
No painel administrativo, selecione um arquivo JPG, PNG, GIF ou WebP (até 5 MB) ao cadastrar ou editar um produto. O servidor valida o conteúdo da imagem antes de salvar, usando o plugin TwelveMonkeys ImageIO para ler WebP. As imagens ficam em `uploads/produtos`, na pasta a partir da qual você executa o Spring Boot. O banco guarda apenas o caminho da imagem no campo `imagem`. Não apague a pasta `uploads` ao reiniciar o sistema ou atualizar o código. Sem JavaScript no frontend.

Os links Vender, Categorias e Seleções aparecem no menu apenas para administradores. O acesso direto às rotas `/admin` também exige esse perfil.

### Cadastro de categorias

Acesse **Vender → Criar categoria** ou a opção **Categorias** no menu. O administrador pode cadastrar nome e descrição, editar e excluir categorias não utilizadas. As categorias ficam disponíveis no formulário de produtos.

## Versão simplificada para estudar

- A classe `WorldcupshopApplication.java` contém somente o `main`.
- Não há métodos `@Bean` nem pacote `config`.
- O administrador inicial é cadastrado pela classe `service/AdministradorInicial.java` na primeira execução (`admin` / `123456`).
- Login simples por sessão HTTP, feito em `UsuarioController` e conferido por `service/AcessoService.java`; senha segue em hash BCrypt. **Essa autenticação didática não tem todos os mecanismos de segurança do Spring Security, como proteção CSRF integrada. Use apenas para fins escolares e em ambiente local, não em produção.**
- Os métodos de serviços e controllers usam principalmente `if`, `for`, `findById`, `findAll` e `save`.
- Mantidos os formulários HTML, upload de imagem, ViaCEP, categorias, seleções e a geração de IDs compatível com PostgreSQL 9.5.
- Execute no Windows: `.\\mvnw.cmd spring-boot:run`.
