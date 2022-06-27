package dados;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private String nick;
    private int points;
    private String msg;
    private Action action;
    private ArrayList<String> clientesConectados;

    private ArrayList<String> placar;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getPoints() { return points; }

    public void setPoints(int points) { this.points = points; }
    public ArrayList<String> getClientesConectados() {
        return clientesConectados;
    }

    public void setClientesConectados(ArrayList<String> clientesConectados) {
        this.clientesConectados = clientesConectados;
    }

    public ArrayList<String> getPlacar() { return placar; }

    public void setPlacar(ArrayList<String> placar) { this.placar = placar; }
}