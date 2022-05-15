package pacmaze;

import java.util.Random;

/**
 * Classe que implementa o mundo do game Pac-Maze.
 * O ambiente modela o mapa do jogo, aplicando as ações e retornando
 * as recompensas para o agente
 *
 * @author Eduardo Vieira e Sousa
 */
public class Ambiente {

    private int numLinhas; // Número de linhas do mapa
    private int numColunas; // Número de colunas do mapa
    private char[][] mapa; // Matriz de caracteres representando o mapa

    /**
     * Seleciona um espaço vazio para inserção do Pac-Man.
     *
     * @return Estado vazio
     */
    public Estado reinsereAgente() {

        int x, y;
        Random r = new Random();

        // Sorteia as coordenadas x e y até encontrar um estado vazio
        while (true) {

            // Exclui primeiros e últimos (paredes que cercam o labirinto
            x = r.nextInt(this.numLinhas - 2) + 1;
            y = r.nextInt(this.numColunas - 2) + 1;

            if (this.mapa[x][y] == '-') {
                return new Estado(x, y);
            }
        }
    }

    /**
     * Executa a ação sobre o mundo.
     *
     * @param estado Estado sobre o qual a ação será executada
     * @param acao   Inteiro indicando a ação: 0 (R), 1 (L), 2 (U) e 3 (D)
     *
     */
    public void mover(Estado estado, int acao) {

        int x, y;

        // Recupera as coordenadas do estado
        x = estado.getX();
        y = estado.getY();

        // Aplica a mudança no estado de acordo com a ação desejada
        switch (acao) {
            case 0: // RIGHT
                y++;
                break;
            case 1: // LEFT
                y--;
                break;
            case 2: // UP
                x--;
                break;
            case 3: // DOWN
                x++;
                break;
        }

        // Se não for uma parede, aplica a ação
        // Caso contrário o estado não muda
        if (this.mapa[x][y] != '#') {
            estado.setX(x);
            estado.setY(y);
        }
    }

    /**
     * Testa se o estado é um estado final.
     *
     * @param estado Estado a ser analisado
     *
     * @return Verdadeiro caso seja estado final, falso caso contrário
     */
    public boolean isFinal(Estado estado) {

        int x, y;

        // Recupera as coordenadas do estado
        x = estado.getX();
        y = estado.getY();

        // Se colidiu com um fantasma
        if (this.mapa[x][y] == '&') {
            System.out.println("Ah! fantasma!");
            return true;
        }

        // Se encontrou uma pílula
        if (this.mapa[x][y] == '0') {
            System.out.println("Oba, pílula!");
            return true;
        }

        return false;
    }

    /**
     * Retorna a recompensa para o estado dado.
     *
     * @param estado Estado a ser analisado
     *
     * @return Recompensa
     */
    public int funcaoRecompensa(Estado estado) {

        int x, y;

        // Recupera as coordenadas do estado
        x = estado.getX();
        y = estado.getY();

        // Recompensa para o fantasma
        if (this.mapa[x][y] == '&')
            return -10;

        // Recompensa para a pílula
        if (this.mapa[x][y] == '0')
            return 10;

        // Recompensa pelo estado vazio
        return -1;
    }

    // Construtor
    public Ambiente(char[][] matrizEntrada, int numLinhas, int numColunas) {
        this.numLinhas = numLinhas;
        this.numColunas = numColunas;
        this.mapa = matrizEntrada;
    }

    // Getters & Setters
    public int getNumLinhas() {
        return numLinhas;
    }

    public void setNumLinhas(int numLinhas) {
        this.numLinhas = numLinhas;
    }

    public int getNumColunas() {
        return numColunas;
    }

    public void setNumColunas(int numColunas) {
        this.numColunas = numColunas;
    }

    public char[][] getMapa() {
        return mapa;
    }

    public void setMapa(char[][] mapa) {
        this.mapa = mapa;
    }

}
