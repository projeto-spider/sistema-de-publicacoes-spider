# sistema-de-publicacoes-spider

O arquivo a ser baixado contém todos os arquivos necessários para front e back end, entretanto, será necessário a instalação de módulos extras, coforme explicado adiante. O banco de dados e o servidor serão executados na própria máquina, assim o projeto funcionará mesmo sem acesso à internet.

# Preparando o ambiente:
Para executar o projeto, tenha certeza de ter instalados:
- Wamp (Windows) ou Lamp (Linux). Recomenda-se entretanto, instalar o Xampp (Multiplataforma);
- Java ou OpenJDK (Versão LTS, de preferência);
- IDE java (Eclipse for Enterprises ou NetBeans);
- Maven 3.6.1 (Pode ser instalado como um plugin através da IDE);
- Node.js 8.10;
- npm 3.5.2;

# Preparando os servidores em máquina local:
BackEnd: abra sua IDE JAVA (eclipse, netbeans, etc) e importe a pasta do back-end como projeto maven. Uma vez importado, a IDE vai começar a baixar e instalar as dependências automaticamente. Após essa etapa, rode a aplicação no Eclipse [Run > Run As > Java Application] ou no Netbeans [Clean and build > Run] e aguarde a corfirmação de carregamento no console.

FrontEnd: Abra o terminal (Linux) ou prompt (Windows) e navegue para pasta do front-end, a seguir use o comando [npm install] para instalar as dependências necessárias. Caso haja uma mensegem de erro, re-execute o comando com privilégios de administrador. Depois da instalação, abra o servidor web em modo de desenvolvimento digitando o comando [npm run serve] e aguarde o servidor ser carregado.
 
# Executando a aplicação:
Abra o navegador de internet (Chrome, Firefox, etc) e certifique-se de que o javascript esteja habilitado. Acesse a aplicação digitando na barra de endereço localhost:8081/.
