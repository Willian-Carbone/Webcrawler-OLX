import enuns.Estados
import metodos.Crawler
import metodos.JsonControler
import metodos.Utilitarios

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

static void main(String[] args) {

    Scanner scan = new Scanner(System.in)

    println("Pressione 1 para baixar os dados ,  2 para editar emails de interessados, 3 para disparar relatorios a emails interessados, 4 para sair")
    String entrada = scan.nextLine()

    while (entrada != "4") {
        while (!Utilitarios.validarEntrada(["1", "2", "3"], entrada)) {
            println("Insira uma entrada valida")
            entrada = scan.nextLine()
        }

        if (entrada == "1") {
            println("informe o nome do produto")
            String nomeProduto = scan.nextLine()

            println("Deseja procurar por um estado especifico? S/N")
            String resp = scan.nextLine().toUpperCase()
            while(!Utilitarios.validarEntrada(["S","N"],resp)){
                println("Insira um valor valido")
                resp = scan.nextLine().toUpperCase()
            }

            String estadoSigla = null
            String local = null
            String filtro = null

            if (resp=="S"){
                println("Informe um estado")
                String possivelEstado = scan.nextLine()


                while(!Utilitarios.checkEstado(possivelEstado)) {
                    println("Insira um estado existente")
                    possivelEstado = scan.nextLine()
                }


                Estados estadoConfirmado = Estados.valueOf(Utilitarios.checkEstado(possivelEstado))

               estadoSigla=estadoConfirmado.getSigla()

                println("No Estado informado, há os seguintes DDS, informe um para melhor precisao, ou digite 0 se prefirir nao informar um DDD")

                Map ddsEstado=estadoConfirmado.getDdds()
                ArrayList<String> possiveisEscolhasDdd =["0"]

                ddsEstado.each {String valor,String regiao->
                    println("-"*40)
                    println("numero DDD :${valor} localização ${regiao.replace("-"," ")}")
                    possiveisEscolhasDdd.add(valor)
                }

                String dddInformado =scan.nextLine()

                while(!Utilitarios.validarEntrada(possiveisEscolhasDdd,dddInformado)){
                    println("Insira uma entrada valida")
                    dddInformado =scan.nextLine()
                }

                if(dddInformado=="0"){
                    local=null
                }
                else{
                     local=ddsEstado[dddInformado]
                }

            }




            println("Informe qual filtro deseja utilizar, 1 para menor Preço , 2 para maior preço, 3 para recentes ou 4 para não informar um filtro")
            String escolhaFiltro= scan.nextLine()

            while(!Utilitarios.validarEntrada(["1","2","3","4"],escolhaFiltro)){
                println("informe um valor válido")
            }

            if(escolhaFiltro=="1") {filtro="menorPreco"}
            else if(escolhaFiltro=="2"){ filtro="maiorPreco"}
            else if (escolhaFiltro=="3"){ filtro ="recentes"}


            println("Informe o numero de paginas que deseja incluir na sua busca, cada pagina pode conter até 50 itens")


            String numeroInformado=scan.nextLine()

            while(!(numeroInformado ==~ /^[1-9]\d*$/)){
                println("insira um numero de paginas valido maior que 0")
                numeroInformado=scan.nextLine()
            }

            Crawler c= new Crawler(nomeProduto,estadoSigla,local,filtro,numeroInformado as int)

            println("Informe o nome do arquivo csv cujo os dados do produto informado serão salvos")
            String nomeArquivo= scan.nextLine()

           ArrayList<Map> produtos= c.capturarProdutosAbaixoMedia()

            List<Double> produtoDeMaiorEMenorValor= c.montarCsvECapturarMaiorEMenorValor(produtos,nomeArquivo,null)
            Double mediacapturada = c.mediaProdutos()


            println("Dados obtidos com sucesso , a media de preço do produtos totais  é ${mediacapturada} reais, os produtos selecionados possuem preço ate esse limite , sendo o mais caro ${produtoDeMaiorEMenorValor[0]} e o mais barato ${produtoDeMaiorEMenorValor[1]} ")





        }


        if (entrada == "2") {

            TerminalEmail.terminal(scan)

        }

        if (entrada == "3") {
            Path raizDoProjeto = Paths.get("").toAbsolutePath()
            Path pastaProdutos = raizDoProjeto.resolve("../../../").resolve("conjunto_produtos")

            if (!Files.exists(pastaProdutos)) {
                println("Nenhuma requisição de dados de produto foi feita, faça ao menos uma ")
            } else {
                Map emails = JsonControler.capturarEmails()
                if (emails.size()==1) {
                    println("Nenhum email registrado,registre pelo menos um email primeiro")
                }
                File pastaZipada = Utilitarios.ziparPasta(pastaProdutos)

                emails.values().each { email ->

                    if (email instanceof Integer) {
                        return
                    }

                    println("-" * 20)

                    println("Enviando relatorio para ${email}")
                    boolean resultado = Utilitarios.enviarParaInteressado(email as String, pastaZipada)

                    if (resultado) {
                        println("Sucesso no envio")
                    } else {
                        println("Falha no envio")
                    }

                }
            }
        }




        println("Pressione 1 para baixar os dados ,  2 para editar emails de interessados, 3 para disparar relatorios a emails interessados, 4 para sair")
        entrada = scan.nextLine()

    }

}