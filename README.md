# OLX Web Crawler
Autor: Willian Carbone Bueno

Sistema de automação desenvolvido em **Groovy** para captura de dados sobre produtos da plataforma olx e distribuição de resultados via e-mail.

 ## Funcionalidades

- **Parametrização**: captura de dados de produtos, filtado por estado, ddd , ordenação (maior e menor preço, recentes e relevantes)
- **Processamento de Dados:** Conversão automática dos dados capturados para **CSV**.
- **Distribuição:** Sistema de e-mail integrado para envio de pacote `.zip` com os dados coletados.

##  Tecnologias

- **Linguagem:** Groovy
- **Integração HTTP:** HttpBuilder-NG
- **Parser HTML:** JSoup
- **E-mail:** Jakarta Mail (Utilizando Gmail)
- **Persistência:** JSON (para gestão de e-mails)
- **Testes:** JUnit 5

##  Configuração e Variáveis de Ambiente

Para manter a segurança da  conta, o projeto utiliza variáveis de ambiente para as credenciais, salvas como EMAIL_USER e EMAIL_PASS
para que o sistema de email funcione  elas devem ser configuradas localmente

```bash
# Execute estes comandos no terminal antes de rodar o projeto:
export EMAIL_USER='seu-email@gmail.com'
export EMAIL_PASS='sua-senha-de-app-de-16-digitos'
