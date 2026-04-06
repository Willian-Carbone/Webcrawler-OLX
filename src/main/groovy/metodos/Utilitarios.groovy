package metodos

import enuns.Estados
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart


import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import java.text.Normalizer
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class Utilitarios {

    static boolean validadorEmail(String email) {
        return (email ==~ /[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}/)
    }

    static boolean validarEntrada(ArrayList<String> itens, String entrada) {
        return itens.contains(entrada)
    }

    static double convercaoValor(String valorReais) {
        double valorConvertido = valorReais.replace("R\$", "")
                .replace(".", "")
                .replace(" ", "")
                .replace(",", ".")
                .trim() as double

        return valorConvertido
    }

    static String checkEstado(String estado) {

        if (estado == null) {
            return null
        }

        String normalizado = Normalizer.normalize(estado, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll(" ", "").toUpperCase()
        if (Estados.values().any { it.name() == normalizado }) {
            return normalizado
        } else {
            return null
        }

    }





    static void criadorCsv(ArrayList<Map> linhasDaTabela, ArrayList<String> titulosColunas, String nomeArquivo, String local) {

        if(!local){local="conjunto_produtos"}


        Path raizDoProjeto = Paths.get("").toAbsolutePath()
        Path pastaDestino = raizDoProjeto.resolve("../../../").resolve(local)

        Path arquivoCompleto = pastaDestino.resolve(nomeArquivo + ".csv")

        File arquivoCsv = arquivoCompleto.toFile()

        if (!Files.exists(pastaDestino)) {
            Files.createDirectories(pastaDestino)
        }

        String cabecalho = titulosColunas.join(";")

        arquivoCsv.withWriter("UTF-8") { writer ->
            writer.writeLine(cabecalho)

            linhasDaTabela.each { infoElemento ->
                String linhaFormatada = titulosColunas.collect { titulo ->

                    String textoMapa = infoElemento[titulo] ?: ""


                    String textoLimpo = textoMapa
                            .replaceAll(/\s+/, " ")
                            .replace("\"", "\"\"")
                            .replace(";", ",")
                    return "\"${textoLimpo}\""
                }.join(";")
                writer.writeLine(linhaFormatada)
            }


        }
    }

    static File ziparPasta(Path pastaOrigem, Path destino=null) {

        Path caminhoDestino

        if(destino){
            caminhoDestino = destino.resolve("conjunto_produtos.zip")
        }

        else{
            caminhoDestino = Paths.get("").toAbsolutePath().resolve("../../../conjunto_produtos.zip")

        }


        File arquivoZipFinal = caminhoDestino.toFile()


        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(arquivoZipFinal))


        Files.walk(pastaOrigem).forEach { Path caminho ->



            String nomeNoZip = pastaOrigem.relativize(caminho).toString()

            if (Files.isDirectory(caminho)) {
                if (!nomeNoZip.isEmpty()) {
                    zos.putNextEntry(new ZipEntry(nomeNoZip + "/"))
                    zos.closeEntry()
                }
            } else {
                ZipEntry entry = new ZipEntry(nomeNoZip)
                zos.putNextEntry(entry)

                Files.copy(caminho, zos)
                zos.closeEntry()
            }
        }
        zos.close()

        return arquivoZipFinal

    }


    static boolean enviarParaInteressado(String emailDestino, File arquivoZip) {
        String emailUsuario = System.getenv("EMAIL_USER")
        String senhaApp = System.getenv("EMAIL_PASS")

        if (emailUsuario == null || senhaApp == null) {
            return false
        }

        Properties props = new Properties()
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.port", "587")
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsuario, senhaApp)
            }
        })

        try {
            Message message = new MimeMessage(session)
            message.setFrom(new InternetAddress(emailUsuario))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino))
            message.setSubject("Crawler OLX- conjunto de produtos ")

            MimeBodyPart parteAnexo = new MimeBodyPart()
            parteAnexo.attachFile(arquivoZip)

            Multipart multipart = new MimeMultipart()
            multipart.addBodyPart(parteAnexo)

            message.setContent(multipart)

            Transport.send(message)
            return true

        } catch (Exception ignored) {
            return false
        }
    }
}


