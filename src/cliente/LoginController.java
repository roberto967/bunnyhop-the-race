package cliente;

import dados.Action;
import dados.Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {
    @FXML
    public TextField portaTextLabel;

    @FXML
    public TextField nickTextLabel;

    @FXML
    public TextField ipTextLabel;

    @FXML
    private Button startButton;

    private static Socket socket;

    private static String nick;

    public static Socket getSocketClient() {
        return socket;
    }

    public static String getNick() {
        return nick;
    }

    public LoginController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.nickTextLabel.setFocusTraversable(false);
        this.ipTextLabel.setFocusTraversable(false);
        this.portaTextLabel.setFocusTraversable(false);
        this.startButton.setFocusTraversable(false);

        this.ipTextLabel.setText("localhost");
        this.portaTextLabel.setText("44444");
    }

    public void conect() throws Exception {
        boolean erro = false;

        if(!nickTextLabel.getText().isEmpty()
                && !portaTextLabel.getText().isEmpty()
                && !ipTextLabel.getText().isEmpty()) {
            System.out.println("Conectado...");

            nick = nickTextLabel.getText();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainStage.fxml"));

            Parent root = loader.load();
            MainStageController c = loader.getController();

            try {
                // conexão com o servidor
                socket = new Socket(ipTextLabel.getText(), Integer.parseInt(portaTextLabel.getText()));
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

                // enviando mensagem informando conexão(passando o nome do cliente)
                Data msg = new Data();
                msg.setNick(nick);
                msg.setMsg(" conectado!");
                msg.setAction(Action.CONECTAR);

                // instancia de ClientThread para receber dados do servidor
                ClientThread thread = new ClientThread(nick, socket, c.listViewClientes, c.placar);
                thread.setName("Thead Cliente: " + nick);
                thread.start();

                // enviando aviso de conexão
                saida.writeObject(msg);
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(startButton.getScene().getWindow());
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Erro!"));
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();

                erro = true;
            }

            //
            if(!erro) {
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                Image icon = new Image("rabbit.png");
                stage.getIcons().add(icon);
                stage.setTitle("Bunny Hop - The Game" + "       Usuário: " + getNick());
                stage.setScene(scene);
                stage.show();

                Stage loginStage = (Stage) startButton.getScene().getWindow(); // Obtendo a janela atual
                loginStage.close(); // Fechando o Stage
            }
        } else{
            //Exibe um popup avisando que algum campo está vazio, precisa de tratamento
            //caso o campo esteja inserido errado
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(startButton.getScene().getWindow());
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Algum campo está vazio!"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }
}