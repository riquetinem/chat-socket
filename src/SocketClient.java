import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

    public static void main(String[] args) {
        try {
            final Socket cliente = new Socket("127.0.0.1", 1234);

            new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

                        //lendo mensagens do servidor
                        while (true) {
                            String mensagem = leitor.readLine();
                            if (mensagem == null || mensagem.isEmpty()) {
                                continue;
                            }
                            System.out.println("O servidor disse: " + mensagem);
                        }
                    } catch (IOException ex) {
                        System.out.println("Impossivel ler a mensagem do servidor");
                        ex.printStackTrace();
                    }

                }
            }.start();

            //escrevendo para o servidor
            PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
            BufferedReader leitorTerminal = new BufferedReader(new InputStreamReader(System.in));

            String mensagemTerminal = "";
            while (true) {
                mensagemTerminal = leitorTerminal.readLine();
                if (mensagemTerminal == null || mensagemTerminal.length() == 0) {
                    continue;
                }

                escritor.println(mensagemTerminal);

                if (mensagemTerminal.equalsIgnoreCase("/EXIT")) {
                    System.exit(0);
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("O endereço passado é inválido!");
            e.printStackTrace();
        } catch (IOException ex) {
            System.out.println("O servidor pode estar fora do ar!");
            ex.printStackTrace();
        }
    }
}
