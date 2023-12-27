# Loja Online

Descrição do projeto: Um aplicativo Android desenvolvido com Jetpack Compose para a disciplina de Desenvolvimento de Aplicativos Android que oferece funcionalidades de e-commerce, incluindo login de usuário, cadastro, adição e remoção de produtos do carrinho.

## Recursos

O aplicativo inclui as seguintes funcionalidades:

- **Login de Usuário**: Autenticação e gerenciamento de sessão de usuário.
- **Cadastro de Usuário**: Registro de novos usuários no aplicativo.
- **Listagem de Produtos**: Exibição de produtos disponíveis para compra.
- **Adicionar Produtos**: Funcionalidade para adicionar novos produtos ao catálogo (administradores).
- **Carrinho de Compras**: Capacidade de adicionar produtos ao carrinho de compras e remover produtos dele.

## Arquitetura

O aplicativo é construído usando a arquitetura MVVM (Model-View-ViewModel) e segue as melhores práticas de design de software e padrões de projeto.

### Principais Componentes

- **Room Database**: Utilizado para armazenamento de dados locais, gerenciando as entidades de usuários e produtos.
- **Repository Pattern**: Camada de abstração para o acesso a dados, facilitando a gestão entre fontes de dados locais e remotas.
- **Hilt**: Sistema de injeção de dependência para gerenciar as dependências do projeto de forma eficiente.
- **Jetpack Compose**: Moderna biblioteca de UI para uma construção declarativa e reativa da interface do usuário.

## Iniciando

Instruções para configurar o ambiente de desenvolvimento e executar o projeto.

### Pré-requisitos

Liste as ferramentas e bibliotecas necessárias para rodar o projeto, como Android Studio, SDKs específicos, etc.

### Instalação

Passos para clonar o repositório e configurar o ambiente de desenvolvimento.

```bash
git clone [URL do Repositório]
```

## Licença

Escolha uma licença para o seu projeto e inclua aqui. Por exemplo:

```
Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE.md para detalhes.
```

---

### Notas Adicionais

- Certifique-se de personalizar o README de acordo com as especificidades do seu projeto.
- Inclua qualquer outra informação que você acredita ser relevante, como capturas de tela do aplicativo, detalhes adicionais sobre as funcionalidades, e informações sobre futuras funcionalidades planejadas.
- Mantenha o README atualizado conforme o projeto evolui. Um README bem mantido é crucial para a compreensão e atração de outros desenvolvedores e usuários.