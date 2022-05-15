package pacmaze;

/**
 * Classe que implementa o estado do game Pac-Maze.
 * Representa cada um dos quadrantes da matriz de entrada
 *
 * @author Eduardo Vieira e Sousa
 */
public class Estado {

    private int x; // Linha do estado
    private int y; // Coluna do estado

    // Clona o estado
    public Estado Clonar() {
        return new Estado(x, y);
    }

    // Construtor
    public Estado(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters & Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
