package servidor;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static int qtdClientes = 0;

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(44444);
        System.out.println("A porta 44444 foi aberta!");
        System.out.println("Aguardando conexão dos clientes...");

        while (true){
            Socket socket;
            socket = serverSocket.accept();

            qtdClientes++;

            //mostra o ip do cliente conectado
            System.out.println("Cliente "+ socket.getInetAddress().getHostAddress() + " conectado.");

            ServerThread thread = new ServerThread(socket);
            thread.setName("Server thread: " + qtdClientes);
            thread.start();
        }
    }
}