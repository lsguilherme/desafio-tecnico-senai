### Execução Local

1. **Clone o Repositório:**

```
git clone https://github.com/lsguilherme/desafio-tecnico-senai.git
cd desafio-tecnico-senai/backend
```

2. **Compilar e Rodar o Projeto:**

Navegue até a pasta `backend` e execute o comando Maven Wrapper para rodar a aplicação Spring Boot:

```
./mvnw spring-boot:run
```

A aplicação será iniciada e estará acessível em `http://localhost:8080/api/v1`. O console do H2 estará disponível em
`http://localhost:8080/h2-console` (credenciais padrão: usuário `sa`, senha vazia).

## Estrutura do Projeto

A estrutura principal do projeto backend está organizada da seguinte forma:

```
    backend/
    ├── src/
    │   └── main/
    │       └── java/
    │           └── com/
    │               └── example/
    │                   └── desafiosenai/
    │                       ├── config/
    │                       ├── controllers/
    │                       ├── dtos/
    │                       │   ├── requests/
    │                       │   └── responses/
    │                       ├── entities/
    │                       ├── exceptions/
    │                       ├── repositories/
    │                       ├── services/
    │                       └── utils/
    ├── pom.xml
```

- **/config**: Contém classes de configuração da aplicação Spring, como a definição de prefixos de rota para os controladores.
- **/controllers**: Responsável por expor os endpoints da API REST, lidando com as requisições HTTP.
- **/dtos**: Contém os Data Transfer Objects utilizados para a comunicação entre a API e o cliente, separando requests de responses.
- **/entities**: Define os modelos de dados que representam as tabelas no banco de dados.
- **/exceptions**: Contém as classes de exceção personalizadas para tratar erros específicos da aplicação de forma padronizada.
- **/repositories**: Interfaces de acesso a dados que utilizam Spring Data JPA para interagir com o banco de dados.
- **/services**: Contém a lógica de negócio principal da aplicação, orquestrando operações e aplicando regras específicas.
- **/utils**: Classes utilitárias para funções auxiliares, como normalização de códigos.

## Tecnologias Utilizadas

As principais tecnologias e bibliotecas utilizadas neste projeto backend são:

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Web**
- **Spring Validation**
- **Hibernate**
- **H2 Database**
- **Jackson**
- **JSON Patch**
