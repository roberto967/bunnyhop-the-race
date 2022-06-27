package cliente.corrida;

import cliente.LoginController;
import dados.Action;
import dados.Data;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pontuacao{

    public int valor = 1;
    public int valor2 = 1;
    public int ApostaDisp = 1;
    public String nome;
    public ArrayList<String> nomesCoelhos = new ArrayList<>();

    public synchronized void quemChegou()
    {
        if(valor == 1)
        {
            System.out.println(nomesCoelhos.get(0) + " Chegou primeiro\n");
            if(nomesCoelhos.get(0).equals(nome))
            {
                System.out.println("Seu coelho " + nomesCoelhos.get(0) + " chegou primeiro\n");
            }
        }
        valor = 0;
    }

    public synchronized void ganhador() {
        if(valor2 == 1)
        {
            if(nomesCoelhos.get(0).equals(nome))
            {
                try{
                    Data dadoSaida = new Data();
                    dadoSaida.setNick(LoginController.getNick());
                    dadoSaida.setPoints(100);
                    dadoSaida.setAction(Action.WIN);

                    //saída de dado
                    ObjectOutputStream saida = new ObjectOutputStream(LoginController.getSocketClient().getOutputStream());
                    saida.writeObject(dadoSaida); //enviando msg de desconexão ao servidor
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        valor2 = 0;
    }
}
