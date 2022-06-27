//ÚLTIMA ATUALIZAÇÃO 22/06 02:11

package cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import cliente.corrida.CorridaThread;
import dados.Action;
import dados.Data;
import cliente.corrida.Pontuacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainStageController implements Initializable {

    //Lista de usuarios conectados
    @FXML
    public ListView<String> listViewClientes;

    @FXML
    public ListView placar;
    //BOTÕES

    @FXML
    public Button btnAposta;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnReset;

    @FXML
    private Button Marrom;

    @FXML
    private Button Rosa;

    @FXML
    private Button Branco;

    @FXML
    private Button Cinza;

    @FXML
    private Button Azul;

    @FXML
    public Button disconectButton;

    //FIGURAS

    @FXML
    private Label msgAposta;

    @FXML
    private ImageView figureMarrom;

    @FXML
    private ImageView figureRosa;

    @FXML
    private ImageView figureBranco;

    @FXML
    private ImageView figureCinza;

    @FXML
    private ImageView figureAzul;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    Pontuacao p = new Pontuacao();

    @FXML

    public void botaoAposta() {
        if (figureMarrom.getLayoutX() == 14 && figureRosa.getLayoutX() == 14 && figureBranco.getLayoutX() == 14 && figureCinza.getLayoutX() == 14 && figureAzul.getLayoutX() == 14 && p.ApostaDisp == 1) {
            msgAposta.setText("Escolha o coelho que deseja apostar: ");
            botoesVisiveis();
            p.ApostaDisp = 0;
        }
    }

    public void botaoMarrom() {
        System.out.println("Apostou no Marrom");
        p.nome = "Marrom";
        botoesInvisivel();
    }

    public void botaoRosa() {
        System.out.println("Apostou no Rosa");
        p.nome = "Rosa";
        botoesInvisivel();
    }

    public void botaoBranco() {
        System.out.println("Apostou no Branco");
        p.nome = "Branco";
        botoesInvisivel();
    }

    public void botaoCinza() {
        System.out.println("Apostou no Cinza");
        p.nome = "Cinza";
        botoesInvisivel();
    }

    public void botaoAzul() {
        System.out.println("Apostou no Azul");
        p.nome = "Azul";
        botoesInvisivel();
    }

    public void botoesInvisivel() {
        Marrom.setVisible(false);
        Rosa.setVisible(false);
        Branco.setVisible(false);
        Cinza.setVisible(false);
        Azul.setVisible(false);
    }

    public void botoesVisiveis() {
        Marrom.setVisible(true);
        Rosa.setVisible(true);
        Branco.setVisible(true);
        Cinza.setVisible(true);
        Azul.setVisible(true);
    }


    public void botaoStart() {

        if (figureMarrom.getLayoutX() == 14 && figureRosa.getLayoutX() == 14 && figureBranco.getLayoutX() == 14 && figureCinza.getLayoutX() == 14 && figureAzul.getLayoutX() == 14) {
            Random random = new Random();

            botoesInvisivel();

            CorridaThread c1, c2, c3, c4, c5;

            msgAposta.setVisible(false);
            c1 = new CorridaThread("Marrom", figureMarrom, p);
            c2 = new CorridaThread("Rosa", figureRosa, p);
            c3 = new CorridaThread("Branco", figureBranco, p);
            c4 = new CorridaThread("Cinza", figureCinza, p);
            c5 = new CorridaThread("Azul", figureAzul, p);


            c1.setPriority(9);
            c2.setPriority(1);
            c3.setPriority(1);
            c4.setPriority(1);
            c5.setPriority(1);

            /*
            c1.setPriority(random.nextInt(7) + 2);
            c2.setPriority(random.nextInt(7) + 2);
            c3.setPriority(random.nextInt(7) + 2);
            c4.setPriority(random.nextInt(7) + 2);
            c5.setPriority(random.nextInt(7) + 2);*/

            c1.start();
            c2.start();
            c3.start();
            c4.start();
            c5.start();
        }

    }

    @FXML
    public void botaoReset() {
        if (figureMarrom.getLayoutX() == 674 && figureRosa.getLayoutX() == 674 && figureBranco.getLayoutX() == 674 && figureCinza.getLayoutX() == 674 && figureAzul.getLayoutX() == 674) {
            p.valor++;
            p.valor2++;
            p.ApostaDisp = 1;
            p.nomesCoelhos.clear();
            System.out.println("\nCoelhinhos em posição!\n");
            Platform.runLater(() -> figureMarrom.setLayoutX(14));
            Platform.runLater(() -> figureRosa.setLayoutX(14));
            Platform.runLater(() -> figureBranco.setLayoutX(14));
            Platform.runLater(() -> figureCinza.setLayoutX(14));
            Platform.runLater(() -> figureAzul.setLayoutX(14));
        }
    }

    @FXML
    public void disconect() {
        try {
            Data dadoSaida = new Data();
            dadoSaida.setNick(LoginController.getNick());
            dadoSaida.setMsg(" desconectado!");
            dadoSaida.setAction(Action.DESCONECTAR);

            //saída de dado
            ObjectOutputStream saida = new ObjectOutputStream(LoginController.getSocketClient().getOutputStream());
            saida.writeObject(dadoSaida); //enviando msg de desconexão ao servidor

            Stage stage = (Stage) disconectButton.getScene().getWindow(); //Obtendo a janela atual
            stage.close(); //Fechando o Stage
            System.exit(0); //finaliza a execução do cliente
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}