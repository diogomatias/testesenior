# testesenior

1. Técnologias e estrutura básica
Trata-se de um projeto Spring Boot com Maven.
Para executar o mesmo é necessário seguir os passos abaixo.
-Baixar o repositório.
-Abrir o arquivo data.sql no raiz do repositório e executar os comandos em um banco Postgres na porta 5433.
-Abrir o repositório com a IDE desejada (recomendo o NetBeans).
-Clicar com o botão direito do mouse e selecionar a opção "Construir com dependências".
-Executar o projeto, é necessário possuir um servidor configurado no NetBeans.


2.Métodos HTTP da API
GET /pedidos
  params
    q: Keyword (opcional)
      RETURN Lista de pedidos que contenham algum produto/serviço com o nome similar ao passado no parâmetro.
    page: PageNumber (opcional)
      RETURN Página passada no parâmero, a mesma é uma lista de pedidos, por padrão possui o valor 0 (primeira página).
    size: PageSize (opcional)
      RETURN Indica ao sistema a quantidade de itens por página que o mesmo deve retornar, por padrão possui o valor 10.
    fields: List<String> (opcional)
      RETURN Lista de pedidos apenas com os fields passados.
RETURN Lista de pedidos.
Exemplo:
URL:http://localhost:8080/pedidos/?q=Serviço&fields=pedidosProdutosServicos,situacao&page=0&size=2
--------------------------------------------------------------------
GET /pedidos/:id
RETURN Pedido com o id passado no parâmetro.
Exemplo:
URL:http://localhost:8080/pedidos/6acb50d7-a11a-4f49-9079-91f085663c65
--------------------------------------------------------------------
POST /pedidos
RETURN Pedido que foi passado no parâmetro, após ser gravado no banco de dados.
Exemplo:
URL:http://localhost:8080/pedidos
Json: 
{
	"percentualDesconto" : 60.00,
    "situacao" : 0,
    "idsProdutosServicos" : ["67f5b5e9-4af5-4acd-b16d-5553c3de8e64",
                             "5fb019c3-44a6-44b4-ba43-d1d7ff40441f",
                             "46e6797e-a978-4b8c-a3ed-901e08e8c201"]
}
--------------------------------------------------------------------
PUT /pedidos/:id
RETURN Pedido com o id que foi passado no parâmetro, após ser atualizado no banco de dados conforme novos valores.
Exemplo:
URL:http://localhost:8080/pedidos/6acb50d7-a11a-4f49-9079-91f085663c65
Json: 
{
	"percentualDesconto" : 50.00,
    "situacao" : 0,
    "idsProdutosServicos" : ["67f5b5e9-4af5-4acd-b16d-5553c3de8e64",
                             "5fb019c3-44a6-44b4-ba43-d1d7ff40441f",
                             "46e6797e-a978-4b8c-a3ed-901e08e8c201"]
}
--------------------------------------------------------------------
DELETE /pedidos/:id
RETURN Exlui o pedido com o id passado no parâmetro.
Exemplo:
URL:http://localhost:8080/pedidos/6acb50d7-a11a-4f49-9079-91f085663c65
--------------------------------------------------------------------
GET /produtosServicos
  params
    q: Keyword (opcional)
      RETURN Lista de produto/serviço que contenham algum nome similar ao passado no parâmetro.
    page: PageNumber (opcional)
      RETURN Página passada no parâmero, a mesma é uma lista de produto/serviço, por padrão possui o valor 0 (primeira página).
    size: PageSize (opcional)
      RETURN Indica ao sistema a quantidade de itens por página que o mesmo deve retornar, por padrão possui o valor 10.
    fields: List<String> (opcional)
      RETURN Lista de produto/serviço apenas com os fields passados.
RETURN Lista de produto/serviço.
Exemplo:
URL:http://localhost:8080/produtosServicos/?q=Teste&fields=nome,valor,erwe&page=1&size=2
--------------------------------------------------------------------
GET /produtosServicos/:id
RETURN Produto/serviço com o id passado no parâmetro.
Exemplo:
URL:http://localhost:8080/produtosServicos/755f0a08-10c7-4ca4-9cdf-5fa4c0c1ab03
--------------------------------------------------------------------
POST /produtosServicos
RETURN Produto/serviço que foi passado no parâmetro, após ser gravado no banco de dados.
Exemplo:
URL:http://localhost:8080/produtosServicos
Json: 
{
    "nome": "Teste Serviço",
    "valor": 450.0,
    "ativo": true,
    "tipoEnum": "SERVICO"
}
--------------------------------------------------------------------
PUT /produtosServicos/:id
RETURN Produto/serviço com o id  que foi passado no parâmetro, após ser atualizado no banco de dados conforme novos valores.
Exemplo:
URL:http://localhost:8080/produtosServicos/755f0a08-10c7-4ca4-9cdf-5fa4c0c1ab03
Json: 
{
    "nome": "Teste Serviço",
    "valor": 300.0,
    "ativo": true,
    "tipoEnum": "SERVICO"
}
--------------------------------------------------------------------
DELETE /produtosServicos/:id
RETURN Exlui o Produto/serviço com o id passado no parâmetro.
Exemplo:
URL:http://localhost:8080/produtosServicos/755f0a08-10c7-4ca4-9cdf-5fa4c0c1ab03
--------------------------------------------------------------------
GET /pedidosProdutosServicos
  params
    q: Keyword (opcional)
      RETURN Lista de pedido e produto/serviço que contenham algum produto/serviço nome similar ao passado no parâmetro.
    page: PageNumber (opcional)
      RETURN Página passada no parâmero, a mesma é uma lista de pedido e produto/serviço, por padrão possui o valor 0 (primeira página).
    size: PageSize (opcional)
      RETURN Indica ao sistema a quantidade de itens por página que o mesmo deve retornar, por padrão possui o valor 10.
    fields: List<String> (opcional)
      RETURN Lista de pedido e produto/serviço apenas com os fields passados.
RETURN Lista de pedido e produto/serviço.
Exemplo:
URL:http://localhost:8080/pedidosProdutosServicos/?q=Teste&fields=produtoServico&page=0&size=2
--------------------------------------------------------------------
GET /pedidosProdutosServicos/:id
RETURN Pedido e produto/serviço com o id passado no parâmetro.
Exemplo:
URL:http://localhost:8080/pedidosProdutosServicos/5efe3f23-8b82-42ae-b08f-6200eff9183f
--------------------------------------------------------------------
DELETE /pedidosProdutosServicos/:id
RETURN Exlui o Produto/serviço com o id passado no parâmetro.
Exemplo:
URL:http://localhost:8080/pedidosProdutosServicos/0bc7f0e5-5066-45c1-b6ff-2bea1f21bfec
