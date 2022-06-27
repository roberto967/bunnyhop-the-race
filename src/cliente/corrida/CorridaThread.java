package cliente.corrida;

import cliente.corrida.Pontuacao;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class CorridaThread extends Thread {

    private String nome;
    private ImageView coelho;
    Pontuacao pontuacao;

    public CorridaThread(String n, ImageView c, Pontuacao p) {
        nome = n;
        coelho = c;
        pontuacao = p;
    }

    @Override
    public void run() {
        int inv = 2, cont=14;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 60; j++) {
                //MOVIMENTO DO SALTO
                Platform.runLater(() -> coelho.setLayoutX(coelho.getLayoutX() + 1));
                if (j % 30 == 0) {
                    inv++;
                }
                if (inv % 2 == 0) {
                    Platform.runLater(() -> coelho.setLayoutY(coelho.getLayoutY() + 1));
                } else {
                    Platform.runLater(() -> coelho.setLayoutY(coelho.getLayoutY() - 1));
                }
                //CONFERINDO SE PASSOU A LINHA DE CHEGADA
                cont++;
                if (cont == 565) {
                    System.out.printf(nome + " chegou!\n");
                    pontuacao.nomesCoelhos.add(nome);
                    pontuacao.quemChegou();
                    pontuacao.ganhador();
                }
                causarAtraso();
            }
            try {Thread.sleep(500);} catch (Exception e) {}
        }
        }

    public void causarAtraso() {
        int som = 0;
        for (int j = 0; j < 4000000; j++) {
            if (som % 3 == 0) {
                som = som + 2;
            }
            if (som % 2 == 0) {
                som = som + 1;
            }
            som = som + 1;
        }
    }

}