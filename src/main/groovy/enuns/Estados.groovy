package enuns

enum Estados {
    ACRE("ac", ["68": "acre"]),
    ALAGOAS("al", ["82": "alagoas"]),
    AMAPA("ap", ["96": "amapa"]),
    AMAZONAS("am", ["92": "regiao-de-manaus", "97": "leste-do-amazonas"]),
    BAHIA("ba", ["71": "grande-salvador", "73": "sul-da-bahia", "74": "regiao-de-juazeiro-e-jacobina", "75": "regiao-de-feira-de-santana-e-alagoinhas", "77": "regiao-de-vitoria-da-conquista-e-barreiras"]),
    CEARA("ce", ["85": "fortaleza-e-regiao", "88": "regiao-de-juazeiro-do-norte-e-sobral"]),
    DISTRITOFEDERAL("df", ["61": "distrito-federal-e-regiao"]),
    ESPIRITOSANTO("es", ["27": "norte-do-espirito-santo", "28": "sul-do-espirito-santo"]),
    GOIAS("go", ["62": "grande-goiania-e-anapolis", "64": "regiao-de-rio-verde-e-caldas-novas"]),
    MARANHAO("ma", ["98": "regiao-de-sao-luis", "99": "regiao-de-imperatriz-e-caxias"]),
    MATOGROSSO("mt", ["65": "regiao-de-cuiaba", "66": "regiao-de-rondonopolis-e-sinop"]),
    MATOGROSSODOSUL("ms", ["67": "mato-grosso-do-sul"]),
    MINASGERAIS("mg", ["31": "belo-horizonte-e-regiao", "32": "juiz-de-fora-e-sudeste", "33": "governador-valadares", "34": "uberlandia-e-triangulo", "35": "pocos-de-caldas-e-sul", "37": "divinopolis", "38": "montes-claros"]),
    PARA("pa", ["91": "belem-e-regiao", "93": "regiao-de-santarem", "94": "regiao-de-maraba"]),
    PARAIBA("pb", ["83": "paraiba"]),
    PARANA("pr", ["41": "regiao-de-curitiba-e-paranagua", "42": "regiao-de-ponta-grossa-e-guarapuava", "43": "regiao-de-londrina", "44": "regiao-de-maringa", "45": "regiao-de-foz-do-iguacu-e-cascavel", "46": "regiao-de-francisco-beltrao-e-pato-branco"]),
    PERNAMBUCO("pe", ["81": "grande-recife", "87": "regiao-de-petrolina-e-garanhuns"]),
    PIAUI("pi", ["86": "regiao-de-teresina-e-parnaiba", "89": "regiao-de-picos-e-floriano"]),
    RIODEJANEIRO("rj", ["21": "rio-de-janeiro-e-regiao", "22": "norte-do-estado-do-rio", "24": "serra-angra-dos-reis-e-regiao"]),
    RIOGRANDEDONORTE("rn", ["84": "rio-grande-do-norte"]),
    RIOGRANDEDOSUL("rs", ["51": "regioes-de-porto-alegre-torres-e-santa-cruz-do-sul", "53": "regioes-de-pelotas-rio-grande-e-bage", "54": "regioes-de-caxias-do-sul-e-passo-fundo", "55": "regioes-de-santa-maria-uruguaiana-e-cruz-alta"]),
    RONDONIA("ro", ["69": "rondonia"]),
    RORAIMA("rr", ["95": "roraima"]),
    SANTACATARINA("sc", ["47": "norte-de-santa-catarina", "48": "florianopolis-e-regiao", "49": "oeste-de-santa-catarina"]),
    SAOPAULO("sp", ["11": "sao-paulo-e-regiao", "12": "vale-do-paraiba-e-litoral-norte", "13": "baixada-santista-e-litoral-sul", "14": "regiao-de-bauru-e-marilia", "15": "regiao-de-sorocaba", "16": "regiao-de-ribeirao-preto", "17": "regiao-de-sao-jose-do-rio-preto", "18": "regiao-de-presidente-prudente", "19": "grande-campinas"]),
    SERGIPE("se", ["79": "sergipe"]),
    TOCANTINS("to", ["63": "tocantins"])

    String sigla
    Map<String, String> ddds

    Estados(String sigla, Map<String, String> ddds) {
        this.sigla = sigla
        this.ddds = ddds
    }
}