package pacmaze;

import java.util.Random;

/**
 * Classe que implementa o agente do game Pac-Maze.
 * O agente tem como algoritmo de aprendizado o Q-Learning.
 * Já a estratégia de exploração adotada é a Épsilon-Greedy.
 *
 * @author Eduardo Vieira e Sousa
 */
public class Agente {

    private float fatorDesconto; // Gama
    private float taxaAprendizado; // Alfa
    private float fatorExploracao; // Épsilon
    private float qTable[][][]; // Q-Table

    /**
     * Seleciona uma ação utilizando a estratégia E-Greedy.
     *
     * @param estado Estado sobre o qual a ação será executada
     *
     * @return Inteiro indicando a ação: 0 (R), 1 (L), 2 (U) e 3 (D)
     */
    public int getAcao(Estado estado) {

        int x, y, acao;
        Random r = new Random();

        // E-Greedy
        if ((r.nextFloat()) < this.fatorExploracao) {

            // Seleciona ação aleatória
            acao = r.nextInt(4);
        } else {
            x = estado.getX();
            y = estado.getY();

            // Seleciona ação com maior valor
            acao = maiorValor(x, y);
        }

        return acao;
    }

    /**
     * Retorna a ação com maior valor para o estado dado.
     *
     * @param x linha
     * @param y coluna
     *
     * @return Inteiro indicando a ação: 0 (R), 1 (L), 2 (U) e 3 (D)
     */
    public int maiorValor(int x, int y) {

        float valorAcao[] = this.qTable[x][y];
        int acao = 0;

        // Itera na terceira dimensão procurando a ação com maior valor
        for (int i = 1; i < valorAcao.length; i++) {
            if (valorAcao[acao] < valorAcao[i])
                acao = i;
        }

        return acao;
    }

    /**
     * Atualiza a Q-Table do agente.
     *
     * @param estadoAnterior estado de antes da execução da ação
     * @param estadoAtual    estado após a execução da ação
     * @param acao           número da ação executada
     * @param recompensa     recompensa obtida
     *
     */
    public void aprender(Estado estadoAnterior, Estado estadoAtual, int acao, int recompensa) {

        int xAnterior, yAnterior, xAtual, yAtual, maior;
        float valorAtual, valorAnterior;

        // Recupera as coordenadas do estado anterior
        xAnterior = estadoAnterior.getX();
        yAnterior = estadoAnterior.getY();

        // Recupera as coordenadas do estado atual
        xAtual = estadoAtual.getX();
        yAtual = estadoAtual.getY();

        // Valor do estado anterior na tabela
        valorAnterior = this.qTable[xAnterior][yAnterior][acao];

        // Seleciona o maior valor do estado atual
        maior = maiorValor(xAtual, yAtual);

        // Calcula o valor para o estado atual
        valorAtual = this.fatorDesconto * this.qTable[xAtual][yAtual][maior];

        // Equação de Bellman
        this.qTable[xAnterior][yAnterior][acao] += this.taxaAprendizado * (recompensa + (valorAtual - valorAnterior));
    }

    // Construtor
    public Agente(int nLinhas, int nColunas, float fatorDesconto, float taxaAprendizado, float fatorExploracao) {
        this.fatorDesconto = fatorDesconto;
        this.taxaAprendizado = taxaAprendizado;
        this.fatorExploracao = fatorExploracao;
        this.qTable = new float[nLinhas][nColunas][4];
    }

    // Getters & Setters
    public float getFatorDesconto() {
        return fatorDesconto;
    }

    public void setFatorDesconto(float fatorDesconto) {
        this.fatorDesconto = fatorDesconto;
    }

    public float getTaxaAprendizado() {
        return taxaAprendizado;
    }

    public void setTaxaAprendizado(float taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
    }

    public float getFatorExploracao() {
        return fatorExploracao;
    }

    public void setFatorExploracao(float fatorExploracao) {
        this.fatorExploracao = fatorExploracao;
    }

    public float[][][] getQTable() {
        return qTable;
    }

    public void setqTable(float[][][] qTable) {
        this.qTable = qTable;
    }
}
