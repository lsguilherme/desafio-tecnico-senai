### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

- **Node.js** v20.19.0
- **npm** 10.8.2

### Passos para Execução Local

1.  **Navegue até o diretório do frontend:**

    ```
    cd frontend
    ```

2.  **Instale as dependências do projeto:**
    ```
    npm install
    # ou, se preferir usar Yarn:
    # yarn install
    ```
3.  **Configure as variáveis de ambiente:**
    Crie um arquivo `.env` na raiz do diretório `frontend/`.
    Use o `env.example` abaixo como base:

    ```
    # Conteúdo do .env.example
    VITE_API_BASE_URL=http://localhost:8080/api/v1
    ```

    Certifique-se de que `VITE_API_BASE_URL` aponta para a URL do seu backend.

4.  **Inicie o servidor de desenvolvimento:**

    ```
    npm run dev
    # ou, se preferir usar Yarn:
    # yarn dev
    ```

    O aplicativo será aberto em seu navegador, em `http://localhost:5173`.

## Estrutura do Projeto

frontend/
├── public/
├── src/
│ ├── assets/
│ ├── components/
│ │ ├── layout/
│ │ ├── modals/
│ │ └── ui/
│ ├── lib/
│ ├── pages/
│ ├── services/
│ ├── types/

- **/public:** Contém arquivos estáticos que são servidos diretamente pelo servidor web, como favicon.ico e o index.html principal.
- **/src:** Contém o código-fonte principal da sua aplicação React.
- **/assets:** Armazena imagens, ícones, fontes e outros recursos estáticos que são utilizados pelos componentes.
- **/components:** Contém componentes React reutilizáveis que são utilizados em várias partes da aplicação.
- **/layout:** Componentes para a estrutura geral do layout da aplicação (cabeçalhos, barras laterais, rodapés).
- **/modals:** Modais específicos da aplicação, como formulários em modal ou diálogos de confirmação.
- **/ui:** Componentes de interface de usuário (UI) gerados e customizados via Shadcn UI, como botões, inputs e diálogos.
- **/lib:** Contém bibliotecas e utilitários compartilhados de baixo nível, que não são específicos de uma entidade ou da UI. Inclui, por exemplo, api.ts para funções de API e utils.ts para funções auxiliares gerais.
- **/pages:** Contém componentes React que representam as diferentes telas ou páginas da aplicação, geralmente associadas a rotas específicas.
- **/services:** Contém módulos responsáveis por encapsular a lógica de comunicação com as APIs do backend, como productService.ts e couponService.ts.
- **/types:** Contém as definições de interfaces e tipos TypeScript para a tipagem de dados da aplicação, incluindo DTOs e outros modelos de dados.
- **App.tsx:** O componente principal da aplicação, onde as rotas são definidas e os componentes de página são renderizados.
- **index.css:** O arquivo de estilos CSS globais da aplicação, onde as diretivas do Tailwind CSS são importadas.
- **main.tsx:** O ponto de entrada principal da aplicação React, responsável por renderizar o componente App.tsx no DOM.

## Tecnologias Utilizadas

Este frontend utiliza as seguintes tecnologias principais:

- **ReactJS**
- **Vite**
- **TypeScript**
- **Tailwind CSS**
- **Shadcn UI**
- **React Router DOM**
- **Lucide React**
- **Fetch API**
