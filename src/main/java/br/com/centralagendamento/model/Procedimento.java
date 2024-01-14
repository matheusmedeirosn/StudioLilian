package br.com.centralagendamento.model;

public enum Procedimento {
    designSobrancelhas(30),
    designHenna(40),
    designTintura(45),
    browLamination(120),
    lashLifting(100),
    micropigmentacao(300),
    hidraGloss(120),
    limpezaDePele(150),
    peelingQuimico(120),
    depilacaoBuco(10),
    depilacaoQueixo(15),
    depilacaoRosto(35)
    ;

    private final int custo;

    Procedimento(int custo) {
        this.custo = custo;
    }

    public int getCusto() {
        return custo;
    }
}

