package pacmaze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Classe principal.
 * Responsável pela leitura e escrita dos arquivos, assim como a
 * dinâmica de treinamento entre agente e ambiente.
 *
 * @author Eduardo Vieira e Sousa
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String arquivoEntrada; // Caminho do arquivo de entrada
        String arquivoSaida; // Caminho dos arquivos de saída
        String linha; // Para leitura linha a linha do arquivo
        String[] splitStr; // Para separar os valores de cada linha

        // Variáveis para execução da leitura, escrita e treinamento;
        char matrizEntrada[][];
        int nLinhas, nColunas, nrEpisodios;
        float fatorDesconto = (float) 0.9, taxaAprendizado, fatorExploracao;
        int acao, recompensa, maior;
        boolean acabou;
        float print;

        // Checa se o número de parâmetros está correto
        if (args.length != 4) {
            System.out.println("ERRO: Número incorreto de parâmetros");
            System.exit(1);
        }

        // Lê os parâmetros de entrada
        arquivoEntrada = args[0];
        taxaAprendizado = Float.parseFloat(args[1]);
        fatorExploracao = Float.parseFloat(args[2]);
        nrEpisodios = Integer.parseInt(args[3]);

        try {

            // Inicia as estruturas para leitura dos arquivos
            FileReader entrada = new FileReader(arquivoEntrada);
            BufferedReader lerArq = new BufferedReader(entrada);

            // Lê a linha inicial e separa os valores em strings utilizando o espaçamento
            // como separador
            linha = lerArq.readLine();
            splitStr = linha.trim().split("\\s+");

            // Testa se a formatação do arquivo está correta
            if (splitStr.length != 2) {
                System.out.println("\nERRO: Formato do arquivo incorreto");
                entrada.close();
                System.exit(2);
            }

            // Atribui os parâmetros da primeira linha
            nLinhas = Integer.parseInt(splitStr[0]);
            nColunas = Integer.parseInt(splitStr[1]);

            // Instancia a matriz de entrada
            matrizEntrada = new char[nLinhas][nColunas];

            // Lê a matriz de entrada
            for (int i = 0; i < nLinhas; i++) {

                linha = lerArq.readLine();

                for (int j = 0; j < nColunas; j++) {

                    matrizEntrada[i][j] = linha.charAt(j);

                }
            }

            // Arquivo lido com sucesso
            System.out.println("\nArquivo de entrada lido com sucesso.\n");
            entrada.close();

            // Inicia as variáveis de treinamento
            Estado estadoAtual, estadoAnterior;
            String[] acoes = { "R", "L", "U", "D" };
            Ambiente mundo = new Ambiente(matrizEntrada, nLinhas, nColunas);
            Agente agente = new Agente(nLinhas, nColunas, fatorDesconto, taxaAprendizado, fatorExploracao);
            float recompensaTotal, recompensaFinal;

            recompensaFinal = 0;

            // Loop de treinamento
            for (int i = 0; i < nrEpisodios; i++) {

                // Variável de parada do laço de cada episódio
                acabou = false;

                System.out.println("Episódio: " + (i + 1));

                // Reinsere o agente e reseta a compensa total
                estadoAtual = mundo.reinsereAgente();
                recompensaTotal = 0;

                while (!acabou) {

                    // Gera uma cópia do estado atual
                    estadoAnterior = estadoAtual.Clonar();

                    // Define a ação a ser executada
                    acao = agente.getAcao(estadoAtual);

                    // Executa a ação
                    mundo.mover(estadoAtual, acao);

                    // Gera a recompensa para o novo estado
                    recompensa = mundo.funcaoRecompensa(estadoAtual);

                    // Para o cálculo da média de recompensas
                    recompensaTotal += recompensa;

                    // Atualiza a Q-Table do agente
                    agente.aprender(estadoAnterior, estadoAtual, acao, recompensa);

                    // Testa se um estado final foi atingido
                    if (mundo.isFinal(estadoAtual))
                        acabou = true;

                }

                System.out.println("Recompensa total do episódio: " + recompensaTotal);
                recompensaFinal += recompensaTotal;
            }

            // Treinamento finalizado, imprime estatísticas
            System.out.println("\nAgente treinado com sucesso!");
            System.out.println("Recompensa total: " + recompensaFinal);
            System.out.println("Recompensa média: " + (float) (recompensaFinal / nrEpisodios));

            // Inicia as estruturas para escrita no arquivo
            arquivoSaida = "q.txt";
            FileWriter saida = new FileWriter(arquivoSaida);
            PrintWriter gravarArq = new PrintWriter(saida);

            // Gera o arquivo 'q.txt"
            for (int i = 0; i < nLinhas; i++) {
                for (int j = 0; j < nColunas; j++) {
                    if (matrizEntrada[i][j] == '-') {
                        for (int w = 0; w < 4; w++) {
                            print = agente.getQTable()[i][j][w];
                            gravarArq.printf(i + "," + j + "," + acoes[w] + ",");
                            gravarArq.printf(String.format(Locale.ROOT, "%.3f", print));
                            gravarArq.printf("%n");
                        }
                    }
                }
            }

            gravarArq.close();

            // Inicia as estruturas para escrita no arquivo
            arquivoSaida = "pi.txt";
            saida = new FileWriter(arquivoSaida);
            gravarArq = new PrintWriter(saida);

            // Gera o arquivo 'pi.txt"
            for (int i = 0; i < nLinhas; i++) {
                for (int j = 0; j < nColunas; j++) {
                    if (matrizEntrada[i][j] == '-') {
                        maior = agente.maiorValor(i, j);
                        gravarArq.printf(acoes[maior]);
                    } else {
                        gravarArq.print(matrizEntrada[i][j]);
                    }

                }
                gravarArq.printf("%n");
            }

            // Grava os arquivos
            gravarArq.close();
            System.out.println("\nArquivos de saída gravados.");

        } catch (IOException e) {
            System.err.printf("\nErro na abertura do arquivo: %s.\n", e.getMessage());
            System.out.println();
        }
    }
}