package servidor;

import dados.Action;
import dados.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread{
    private  Socket socket;
    private static Map<String, Socket> clientesMap = new HashMap<>();
    private static Map<String, Integer> clientPointsMap = new HashMap<>(); //modificado HOJE
    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        boolean exit = false;

        try{
            while (!exit){
                //recebendo dado do cliente
                ObjectInputStream entradaDadoGenerico = new ObjectInputStream(socket.getInputStream());
                Data dadoEntrada = (Data) entradaDadoGenerico.readObject();
                // acao do cliente
                Action action = dadoEntrada.getAction();

                switch (action) {
                    case CONECTAR -> {
                        System.out.println("Cliente: " + dadoEntrada.getNick() + dadoEntrada.getMsg());
                        conectar(dadoEntrada);
                        enviarClientesConectados();
                        enviaPlacar();
                    }
                    case WIN -> {
                        atualizarPontuacao(dadoEntrada);
                        enviaPlacar();
                    }
                    case DESCONECTAR -> {
                        System.out.println("Cliente: " + dadoEntrada.getNick() + dadoEntrada.getMsg());
                        desconectar(dadoEntrada);
                        enviarClientesConectados();
                        exit = true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void conectar(Data data) {
        clientesMap.put(data.getNick(), socket);
        if (!clientPointsMap.containsKey(data.getNick())){
            clientPointsMap.put(data.getNick(), data.getPoints());
        }
    }

    public synchronized void desconectar(Data data) {
        clientesMap.remove(data.getNick());
    }

    public synchronized void enviarClientesConectados() throws IOException {
        ArrayList<String> clientesConectados = new ArrayList<>();

        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            clientesConectados.add(cliente.getKey());
        }

        Data dadoClientesConectados = new Data();
        dadoClientesConectados.setAction(Action.CLIENT_LIST);
        dadoClientesConectados.setClientesConectados(clientesConectados);

        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
            saida.writeObject(dadoClientesConectados);
        }
    }

    public synchronized void atualizarPontuacao(Data data) throws IOException {
        int ptsAtual;

        for (Map.Entry<String, Integer> cliente : clientPointsMap.entrySet()) {
            if (data.getNick().equals(cliente.getKey())) {
                ptsAtual = cliente.getValue();
                ptsAtual += data.getPoints();
                cliente.setValue(ptsAtual);
            }
        }
    }

    public synchronized void enviaPlacar () throws IOException {
        ArrayList<String> placar = new ArrayList<>();

        // ordena a pontuação do maior para o menor
        List<Map.Entry<String, Integer>> list = new ArrayList<>(clientPointsMap.entrySet());
        Comparator<Map.Entry<String, Integer>> comp = Map.Entry.comparingByValue();
        list.sort(comp.reversed());

        //coloca os dados no array para saída
        int cont = 1;
        for (Map.Entry<String, Integer> l : list) {
            placar.add(cont + "º  " + l.getKey() + " | pontos: " + l.getValue());
            cont++;
        }

        //coloca os dados na saída
        Data dadoClientesConectados = new Data();
        dadoClientesConectados.setAction(Action.WIN);
        dadoClientesConectados.setPlacar(placar);

        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
            saida.writeObject(dadoClientesConectados);
        }
    }
}