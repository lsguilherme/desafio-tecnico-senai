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
├── public/ # Arquivos públicos (favicon, index.html, etc)
├── src/ # Código-fonte principal
│ ├── assets/ # Imagens, ícones, fontes, etc
│ ├── components/ # Componentes reutilizáveis
│ │ ├── layout/ # Componentes para a estrutura geral do layout da aplicação (cabeçalhos, barras laterais).
│ │ ├── modals/ # Modais específicos da aplicação, como formulários ou diálogos de confirmação.
│ │ └── ui/ # Componentes de interface de usuário (UI) gerados e customizados via Shadcn UI.
│ ├── lib/ # Bibliotecas e utilitários compartilhados de baixo nível, não específicos de uma entidade ou UI.
│ ├── pages/ # Componentes React que representam as diferentes telas ou páginas da aplicação.
│ ├── services/ # Módulos responsáveis por encapsular a lógica de comunicação com as APIs do backend.
│ ├── types/ # Definições de interfaces e tipos TypeScript para a tipagem de dados da aplicação.
│ └── (outros arquivos de configuração e entrada da aplicação na raiz de `src`, como `App.tsx`, `main.tsx`, `index.css`).
├── (outros arquivos de configuração e metadados na raiz de `frontend`, como `.env.example`, `package

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
