import metodos.Utilitarios
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipFile

import static org.junit.jupiter.api.Assertions.*

class UtilitariosSpec {

    @TempDir
    Path tempDir


    @Test
    @DisplayName("Teste validador email")
    void testeValidadorEmail() {

        [
                [email: "teste@ans.br", valido: true],
                [email: "erro-sem-arroba", valido: false],
                [email: "admin@gov.br", valido: true],
                [email:"123",valido:false]
        ].each { cenario ->

            boolean resultado = Utilitarios.validadorEmail(cenario.email as String)
            assertEquals(cenario.valido, resultado,)
        }
    }



    @Test
    @DisplayName(" converter mapa em um arquivo CSV ")
    void testCriadorCsv() {
        ArrayList<Map>  mapasimulado = [["at1":"atributo","at2":"atributo"],["at3":"atributo","at4":"atributo"]]
        ArrayList<String> titulos=["titulo1","titulo2"]

        String nomeArquivo = "tabela_teste"
        String localDestino = tempDir.toString()


        Utilitarios.criadorCsv(mapasimulado,titulos, nomeArquivo, localDestino)

        Path arquivoCsv = tempDir.resolve(nomeArquivo + ".csv")

        assertTrue(Files.exists(arquivoCsv), "O arquivo CSV deveria ter sido criado")


        List<String> conteudo = Files.readAllLines(arquivoCsv, StandardCharsets.UTF_8)

        assertEquals(3, conteudo.size(), "O CSV deveria ter 3 linhas")



    }



    @Test
    @DisplayName("Deve retornar falso se as credenciais de e-mail não estiverem configuradas")
    void testFalhaSemConfiguracao() {

        File mockFile = new File("teste.zip")

        boolean resultado = Utilitarios.enviarParaInteressado("teste@destino.com", mockFile)

        assertFalse(resultado, "O envio não deveria ocorrer sem credenciais")
    }



    @Test
    @DisplayName("Teste conversão string real para double")
    void testeCo() {

        String valorString= "R\$ 1.999,99 "
        double valorDouble = Utilitarios.convercaoValor(valorString)

        assertEquals(valorDouble,1999.99)
    }

    @Test
    @DisplayName("Teste normalizador e verificador estado")
    void testeRetornoNomeEstado() {

        String estadoValido = "São paulo"
        String estadoInvalido="soa poula"

        String res1=Utilitarios.checkEstado(estadoValido)
        String res2=Utilitarios.checkEstado(estadoInvalido)

        assertEquals(res1,"SAOPAULO")
        assertEquals(res2,null)
    }

    @Test
    @DisplayName("Teste zip pasta")
    void testeziper(){

        Path pastaOrigem = tempDir.resolve("arquivos_para_zipar")
        Files.createDirectories(pastaOrigem)


        Path arquivoValido = pastaOrigem.resolve("relatorio.csv")
        Files.write(arquivoValido, "dados".getBytes())



        File zipGerado = Utilitarios.ziparPasta(pastaOrigem,tempDir)


        assertTrue(zipGerado.exists(), "O arquivo ZIP deveria ter sido criado")


        ZipFile zipFile = new ZipFile(zipGerado)



        assertTrue(zipFile.size()>0, "O ZIP nao pode ser vazio")



        zipFile.close()



    }



}
