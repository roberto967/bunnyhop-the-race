package cliente;

import dados.Action;
import dados.Data;
import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {
    protected Socket socket;
    private String nick;
    boolean sair = false;
    private ListView<String> lista;
    private ListView<String> placar;

    public ClientThread(String nick, Socket socket, ListView<String> lista, ListView<String> placar) {
        this.socket = socket;
        this.nick = nick;
        this.lista = lista;
        this.placar = placar;
    }

    @Override
    public void run() {
        try {
            while (!sair) {
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Data dadoEntrada = (Data) entrada.readObject();

                Action action = dadoEntrada.getAction();

                switch (action) {
                    case CONECTAR -> {
                        conectar(dadoEntrada);
                        recebeVitoria(dadoEntrada);
                    }
                    case DESCONECTAR -> desconectar(dadoEntrada);
                    case WIN -> recebeVitoria(dadoEntrada);
                    case CLIENT_LIST -> atualizarClientes(dadoEntrada);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void conectar(Data dadoEntrada) {
        System.out.println(dadoEntrada.getMsg());
    }

    public void desconectar(Data dadoEntrada)  throws IOException {
        if(dadoEntrada.getNick().equals(this.nick)){
            this.socket.close();
            this.sair = true;
        }
    }

    public void recebeVitoria(Data dadoEntrada) {
        ArrayList<String> attPlacar = dadoEntrada.getPlacar();

        Platform.runLater(() -> {
            placar.getItems().clear();
            placar.getItems().addAll(attPlacar);
        });
    }

    public void atualizarClientes(Data dadoEntrada) {
        ArrayList<String> clientesConectados = dadoEntrada.getClientesConectados();

        Platform.runLater(() -> {
            lista.getItems().clear();
            lista.getItems().addAll(clientesConectados);
        });
    }
}