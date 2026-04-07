package metodos


import groovyx.net.http.HttpBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


import static groovyx.net.http.HttpBuilder.configure

class Crawler {
    private ArrayList<Document> paginas
    private HttpBuilder http

    Crawler(String produto, String siglaEstado, String local, String filtro, int numeroPaginas) {

        String filtroUrl
        String localUrl
        String estadoUrl

        String produtoUrl = produto.replace(" ", "+")
        this.paginas = new ArrayList<Document>()

        if (!local) {
            localUrl = ""
        } else {
            localUrl = "/${local}"
        }

        if (!siglaEstado) {
            estadoUrl = "brasil"
        } else {
            estadoUrl = "estado-${siglaEstado}"
        }

        if (!filtro) {
            filtroUrl = ""
        } else if (filtro == "recentes") {
            filtroUrl = "&sf=1"
        } else if (filtro == "menorPreco") {
            filtroUrl = "&sp=1"
        } else {
            filtroUrl = "&sp=5"
        }


        this.http = configure {
            request.uri = 'https://www.olx.com.br'
            request.headers['User-Agent'] = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:124.0) Gecko/20100101 Firefox/124.0"
            request.headers['Accept-Language'] = 'pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3'
            request.headers['Connection'] = 'keep-alive'
            request.headers['Upgrade-Insecure-Requests'] = '1'
            request.headers['Accept'] = '*/*'
        }

        http.get()

        String referencia = 'https://www.olx.com.br/'

        for (int i = 1; i <= numeroPaginas; i++) {
            String urlIndicadorPag

            if (i == 1) {
                urlIndicadorPag = ""
            } else {
                urlIndicadorPag = "&o=${i}"
            }

            String urlPagina = "https://www.olx.com.br/${estadoUrl}${localUrl}?q=${produtoUrl}${filtroUrl}${urlIndicadorPag}"

            String htmlBrutoPaginaAtual = this.http.get {
                request.uri = urlPagina
                request.headers['Referer'] = referencia

            }

            if (!paginas.isEmpty() && htmlBrutoPaginaAtual == paginas.last().html()) {
                break
            }

            this.paginas.add(Jsoup.parse(htmlBrutoPaginaAtual))

            referencia = urlPagina

        }


    }

    private double capturarValorProduto(Element produto) {
        String valorString = produto.select("h3.typo-body-large.olx-adcard__price.font-semibold").text()

        if (!valorString) {
            return 0
        }
        double valorDouble = Utilitarios.convercaoValor(valorString)
        return valorDouble

    }

    private Map<String, String> capturarInfosProduto(Element produto) {

        Map<String, String> infoProduto = [:]

         String titulo= produto.select("h2.typo-body-large.olx-adcard__title.font-semibold").text()
        String link= produto.select("a.olx-adcard__link").attr("abs:href")
        String valor = produto.select("h3.typo-body-large.olx-adcard__price.font-semibold").text()
       String local = produto.select("p.typo-caption.olx-adcard__location").textNodes()[1] as String

        infoProduto["Titulo"] = titulo.length() >1? titulo : "Dado não informado"
        infoProduto["Link"] = link.length() >1? link : "Dado não informado"
        infoProduto["Valor"] = valor.length() >1? valor : "Dado não informado"
        infoProduto["Local"] = local.length() >1? local : "Dado não informado"


        return infoProduto

    }


    double mediaProdutos() {
        int totalProdutos = 0
        double soma = 0


        paginas.each { p ->
            Elements produtos = p.select("section.olx-adcard.olx-adcard__horizontal ")
            produtos.each { produto ->

                totalProdutos++
                soma += capturarValorProduto(produto)

            }

        }
        double media = soma / totalProdutos
        return (media.round(2))
    }


     ArrayList<Map> capturarProdutosAbaixoMedia(){


        double media=mediaProdutos()
        ArrayList<Map> produtosValidos = []



        paginas.each { p ->

            Elements produtos = p.select("section.olx-adcard.olx-adcard__horizontal ")
            produtos.each { produto ->

                double valorProduto = capturarValorProduto(produto)

                if (valorProduto <= media) {
                    Map infoDoProduto = capturarInfosProduto(produto)
                    produtosValidos.add(infoDoProduto)
                }


            }

        }


        return produtosValidos


    }

    List<Double> montarCsvECapturarMaiorEMenorValor(ArrayList<Map> produtos,String nomeArquivo,String local){

        ArrayList<String> tituloColunas=["Titulo","Link","Valor","Local"]
        Utilitarios.criadorCsv(produtos,tituloColunas,nomeArquivo,local)

        ArrayList<Double> valoresDosProdutos=[]

        produtos.each {produto->

            String valorEmString = produto["Valor"]
            Double valorDouble = Utilitarios.convercaoValor(valorEmString)
            valoresDosProdutos.add(valorDouble)

        }

        List<Double> MaiorEMenorValor = Utilitarios.capturarMaiorEMenorValor(valoresDosProdutos)

        return MaiorEMenorValor

    }








}

