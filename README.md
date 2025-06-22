# 👕 API Vendas de Roupas

> **⚠️ Importante**: Esta é apenas uma versão inicial da aplicação, desenvolvida como ponto de partida para um sistema de vendas de roupas mais robusto.

## 📋 Sobre o Projeto

API REST desenvolvida em Spring Boot para gerenciamento de produtos de uma loja de roupas. O sistema permite cadastrar, listar, atualizar e deletar produtos, incluindo upload de imagens.

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Web** - API REST
- **Hibernate** - ORM
- **Bean Validation** - Validação de dados
- **Multipart File Upload** - Upload de imagens
- **MySQL** - Banco de dados relacional
- **Flyway** - Controle de versão do banco de dados
- **JUnit 5** - Framework de testes
- **Mockito** - Framework para mocks em testes

## 📁 Estrutura do Projeto

```
src/main/java/com/joao/api_vendas_roupas/
├── controller/
│   └── ProdutoController.java          # Endpoints da API
├── domain/produto/
│   ├── DadosAlteracaoProduto.java     # DTO para alteração
│   ├── DadosCadastroProduto.java      # DTO para cadastro
│   ├── DadosDetalheProduto.java       # DTO para detalhes
│   ├── DadosListagemProduto.java      # DTO para listagem
│   ├── Produto.java                   # Entidade principal
│   ├── ProdutoRepository.java         # Repositório JPA
│   ├── ProdutoService.java            # Regras de negócio
│   └── UploadImagem.java              # Serviço de upload
├── infra/exception/
│   ├── RegraDeNegocioException.java   # Exceção customizada
│   └── TratadorDeErros.java           # Global exception handler
└── ApiVendasRoupasApplication.java    # Classe principal
```

## 🎯 Funcionalidades Implementadas

### ✅ CRUD Completo de Produtos
- **POST** `/produto/cadastrar` - Cadastrar novo produto com imagem
- **GET** `/produto` - Listar produtos (paginado)
- **GET** `/produto/{id}` - Buscar produto por ID
- **PUT** `/produto/{id}` - Atualizar produto
- **DELETE** `/produto/{id}` - Deletar produto (soft delete)

### ✅ Upload de Imagens
- Suporte a upload de arquivos de imagem
- Geração automática de nomes únicos (UUID)
- Substituição de imagens em atualizações

### ✅ Validações
- Validação de campos obrigatórios
- Tratamento global de exceções
- Paginação automática nas listagens

### ✅ Controle de Versão do Banco
- Migrations automáticas com Flyway
- Evolução controlada do schema
- Soft delete implementado

### ✅ Testes Automatizados
- Testes unitários completos do Service
- Cobertura de cenários positivos e negativos
- Mocks para isolamento de dependências
- Validação de exceções customizadas

## 🛠️ Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- MySQL 8.0+ rodando na porta 3306

### Configuração

1. **Clone o repositório**
   ```bash
   git clone <seu-repositorio>
   cd api-vendas-roupas
   ```

2. **Configure o MySQL**
   - Certifique-se que o MySQL está rodando
   - O banco `full_store` será criado automaticamente (O banco não esta sendo criado automaticamente devido o uso do flyway)

3. **Configure o caminho das imagens**
   - Crie a pasta para armazenar as imagens:
   ```
   C:\Development\Project\projetos_pessais\Projeto_loja_roupas\img
   ```
   - Ou altere o caminho no `application.properties`:
   ```properties
   caminho.imagens=C:\\seu\\caminho\\para\\imagens
   ```

4. **Ajuste as credenciais do banco** (se necessário)
   ```properties
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

### Executando
```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### Executando os Testes
```bash
# Executar todos os testes
mvn test

# Executar testes com relatório de cobertura
mvn test jacoco:report
```

### Estrutura do Banco de Dados

O projeto utiliza **Flyway** para controle de versão do banco. As migrations são executadas automaticamente:

- **V1**: Criação da tabela `tb_produto`
- **V2**: Adição de constraints NOT NULL
- **V3**: Adição do campo `ativo` para soft delete

## 🧪 Cobertura de Testes

O projeto possui uma suíte robusta de testes unitários para o `ProdutoService`:

### Cenários Testados
- ✅ **Cadastro de produto** - Verifica se todos os dados são salvos corretamente
- ✅ **Listagem paginada** - Testa a paginação e mapeamento para DTO
- ✅ **Detalhamento por ID** - Busca individual de produtos
- ✅ **Atualização completa** - Atualização com e sem nova imagem
- ✅ **Exclusão lógica** - Soft delete (desativação)
- ✅ **Tratamento de erros** - Exceções para produtos não encontrados

### Práticas de Teste Implementadas
- **Mockito** para isolamento de dependências
- **ArgumentCaptor** para validação de dados salvos
- **MockMultipartFile** para simulação de upload
- **Cenários negativos** com validação de exceções

### Cadastrar Produto
```bash
POST /produto/cadastrar
Content-Type: multipart/form-data

anuncioNome: "Camiseta Básica"
anuncioDescricao: "Camiseta 100% algodão"
anuncioPreco: 29.90
arquivoFoto: [arquivo de imagem]
anuncioMarca: "Fashion Brand"
anuncioQuantidade: 100
```

### Listar Produtos
```bash
GET /produto?page=0&size=10&sort=anuncioNome,asc
```

### Buscar Produto
```bash
GET /produto/1
```

## 🎯 Próximos Passos

Esta é apenas a versão inicial do projeto. As próximas implementações incluirão:

- 🔐 Sistema de autenticação e autorização
- 🔍 Funcionalidades de busca e filtros avançados
- 📊 Relatórios de vendas
- 🛒 Sistema de carrinho de compras
- 💳 Integração com gateways de pagamento
- 🧪 Testes de integração e E2E
- 📈 Monitoramento e métricas
- 🐳 Containerização com Docker

## 📄 Licença

Este projeto está em desenvolvimento inicial e serve como base para futuras implementações.

---

**Desenvolvido com ❤️ usando Spring Boot**
