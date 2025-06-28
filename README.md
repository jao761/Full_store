# 👕 API Vendas de Roupas

-> ⚠️ Importante: Esta é uma versão em desenvolvimento da aplicação, sendo constantemente aprimorada para um sistema de vendas de roupas mais robusto.

📋 Sobre o Projeto
API REST desenvolvida em Spring Boot para gerenciamento completo de uma loja de roupas. O sistema permite cadastrar produtos, gerenciar carrinho de compras, incluindo upload de imagens e controle de estoque.

🚀 Tecnologias Utilizadas

Java 17+
Spring Boot - Framework principal
Spring Data JPA - Persistência de dados
Spring Web - API REST
Hibernate - ORM
Bean Validation - Validação de dados
Multipart File Upload - Upload de imagens
MySQL - Banco de dados relacional
Flyway - Controle de versão do banco de dados
JUnit 5 - Framework de testes
Mockito - Framework para mocks em testes

📁 Estrutura do Projeto
src/main/java/com/joao/api_vendas_roupas/
├── controller/
│   ├── ProdutoController.java          # Endpoints de produtos
│   └── CarrinhoController.java         # Endpoints do carrinho
├── domain/
│   ├── produto/
│   │   ├── DadosAlteracaoProduto.java     
│   │   ├── DadosCadastroProduto.java      
│   │   ├── DadosDetalheProduto.java       
│   │   ├── DadosListagemProduto.java      
│   │   ├── Produto.java                   
│   │   ├── ProdutoRepository.java         
│   │   ├── ProdutoService.java            
│   │   └── UploadImagem.java              
│   └── carrinho/
│       ├── Carrinho.java                  # Entidade do carrinho
│       ├── CarrinhoRepository.java        # Repositório do carrinho
│       ├── CarrinhoService.java           # Regras de negócio do carrinho
│       ├── DadosProdutosCarrinho.java     # DTO para listagem
│       └── carrinhoProduto/
│           ├── CarrinhoProduto.java       # Entidade de associação
│           ├── CarrinhoProdutoId.java     # Chave composta
│           ├── CarrinhoProdutoRepository.java
│           ├── DadosAdicionarProduto.java
│           ├── DadosCarrinhoProduto.java
│           └── DadosDetalheProdutoCarrinho.java
├── infra/exception/
│   ├── RegraDeNegocioException.java   
│   └── TratadorDeErros.java           
└── ApiVendasRoupasApplication.java
🎯 Funcionalidades Implementadas
✅ CRUD Completo de Produtos

POST /produto/cadastrar - Cadastrar novo produto com imagem
GET /produto - Listar produtos (paginado)
GET /produto/{id} - Buscar produto por ID
PUT /produto/{id} - Atualizar produto
DELETE /produto/{id} - Deletar produto (soft delete)

✅ Sistema de Carrinho de Compras

POST /carrinho/{carrinhoId}/produto - Adicionar produto ao carrinho
GET /carrinho/{carrinhoId} - Listar produtos do carrinho
GET /carrinho/{carrinhoId}/produto/{produtoId} - Detalhar produto no carrinho
DELETE /carrinho/{carrinhoId}/produto/{produtoId} - Remover produto do carrinho
DELETE /carrinho/{carrinhoId} - Limpar carrinho completamente
PATCH /carrinho/{carrinhoId}/produto/{produtoId}/adicionar - Aumentar quantidade
PATCH /carrinho/{carrinhoId}/produto/{produtoId}/diminuir - Diminuir quantidade

✅ Funcionalidades Avançadas

Upload de Imagens - Suporte a upload de arquivos de imagem
Controle de Estoque - Validação de quantidade disponível
Cálculo Automático - Valor total do carrinho calculado automaticamente
Validações Robustas - Validação de campos obrigatórios e regras de negócio
Paginação - Listagens paginadas automaticamente
Soft Delete - Exclusão lógica de produtos

✅ Controle de Versão do Banco

Migrations automáticas com Flyway
Evolução controlada do schema
Relacionamentos complexos (Many-to-Many com atributos extras)

✅ Testes Automatizados

Testes unitários completos para ProdutoService e CarrinhoService
Cobertura de cenários positivos e negativos
Mocks para isolamento de dependências
Validação de exceções customizadas

🛠️ Como Executar
Pré-requisitos

Java 17 ou superior
Maven 3.6+
MySQL 8.0+ rodando na porta 3306

Configuração

Clone o repositório

bashgit clone <seu-repositorio>
cd api-vendas-roupas

Configure o MySQL

Certifique-se que o MySQL está rodando
O banco full_store será criado automaticamente


Configure o caminho das imagens

Crie a pasta: C:\Development\Project\projetos_pessais\Projeto_loja_roupas\img
Ou altere no application.properties:

propertiescaminho.imagens=C:\\seu\\caminho\\para\\imagens

Ajuste as credenciais do banco (se necessário)

propertiesspring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
Executando
bashmvn spring-boot:run
A API estará disponível em: http://localhost:8080
Executando os Testes
bash# Executar todos os testes
mvn test

# Executar testes com relatório de cobertura
mvn test jacoco:report
🗃️ Estrutura do Banco de Dados
O projeto utiliza Flyway para controle de versão do banco:

V1: Criação da tabela tb_produto
V2: Adição de constraints NOT NULL
V3: Adição do campo ativo para soft delete
V4: Criação da tabela tb_carrinho
V5: Criação da tabela tb_carrinho_produto (relacionamento N:N)

Relacionamentos

Carrinho ↔ Produto: Relacionamento Many-to-Many através da entidade CarrinhoProduto
Chave Composta: CarrinhoProdutoId para identificar unicamente cada item no carrinho

🧪 Cobertura de Testes
ProdutoService - Cenários Testados
✅ Cadastro de produto com validação de dados
✅ Listagem paginada com mapeamento para DTO
✅ Detalhamento por ID
✅ Atualização completa com e sem nova imagem
✅ Exclusão lógica (soft delete)
✅ Tratamento de erros para produtos não encontrados
CarrinhoService - Cenários Testados
✅ Adicionar produto ao carrinho com validação de estoque
✅ Listar produtos do carrinho com cálculo de valor total
✅ Detalhar produto específico no carrinho
✅ Remover produto do carrinho
✅ Limpar carrinho completamente
✅ Aumentar/diminuir quantidade de produtos
✅ Validação de exceções para carrinho e produtos não encontrados
Práticas de Teste Implementadas

Mockito para isolamento de dependências
ArgumentCaptor para validação de dados salvos
MockMultipartFile para simulação de upload
Cenários negativos com validação de exceções

📚 Exemplos de Uso da API
Cadastrar Produto
httpPOST /produto/cadastrar
Content-Type: multipart/form-data

anuncioNome: "Camiseta Básica"
anuncioDescricao: "Camiseta 100% algodão"
anuncioPreco: 29.90
arquivoFoto: [arquivo de imagem]
anuncioMarca: "Fashion Brand"
anuncioQuantidade: 100
Listar Produtos
httpGET /produto?page=0&size=10&sort=anuncioNome,asc
Adicionar Produto ao Carrinho
httpPOST /carrinho/1/produto
Content-Type: application/json

{
  "carrinho_id": 1,
  "produto_id": 1,
  "quantidadeProduto": 2
}
Listar Carrinho
httpGET /carrinho/1
Resposta:
json{
  "carrinho_id": 1,
  "valorCompra": 59.80,
  "produtos": [
    {
      "produtoId": 1,
      "nomeProduto": "Camiseta Básica",
      "PrecoUnitario": 29.90,
      "quantidade": 2
    }
  ]
}
🎯 Próximos Passos
Esta versão já implementa um sistema funcional de carrinho de compras. As próximas implementações incluirão:
🔐 Sistema de autenticação e autorização
🔍 Funcionalidades de busca e filtros avançados
📊 Relatórios de vendas
💳 Integração com gateways de pagamento
👤 Sistema de usuários e perfis
📦 Gestão de pedidos e status de entrega
🧪 Testes de integração e E2E
📈 Monitoramento e métricas
🐳 Containerização com Docker
🌐 Deploy em ambiente de produção
📄 Licença
Este projeto está em desenvolvimento ativo e serve como base para implementações futuras de e-commerce.

Desenvolvido com ❤️ usando Spring Boot
