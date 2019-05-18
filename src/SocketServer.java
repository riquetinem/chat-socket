import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void main(String[] args) {
        // inicia variavel do servidor
        ServerSocket server = null;

        // try é feito para ver se vai ter algum problema ao rodar o servidor
        try {
            System.out.println("Iniciando server!");
            // cria um novo servidor, e passa a porta que ele tera acesso
            server = new ServerSocket(1234);
            System.out.println("Servidor iniciado!");

            // cada cliente que entrar ele cai aqui dentro
            while (true) {
                Socket cliente = server.accept();
                new GerenciadorDeClientes(cliente);
            }
        } catch (IOException e) {
            System.err.println("a porta está em uso ou o servidor foi fechado!");

            // caso nao ocorra o fechamento do servidor, ele cai no catch
            try {
                // caso o server for diferente de vazio, ele fecha
                if (server != null) {
                    server.close();
                }

            } catch (IOException ex) {
            }
            e.printStackTrace();
        }
    }
}
