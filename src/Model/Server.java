package Model;

import Util.Constants;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Server {

    public static void main(String[] args) {
        ServerSocket server = null;

        // Verificar se tem algum erro com o servidor
        try {
            // cria um novo servidor, e passa a porta que ele tera acesso
            server = new ServerSocket(Constants.PORTA);
            JOptionPane.showMessageDialog(null, "Servidor iniciado!");

            while (true) {
                Socket cliente = server.accept();
                new Cliente(cliente);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "A porta está em uso ou o servidor já foi iniciado");
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
