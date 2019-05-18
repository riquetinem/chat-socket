import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

// extends Thread é para poder rodar mais de uma coisa ao mesmo tempo
public class GerenciadorDeClientes extends Thread {
    
    private Socket cliente;
    private String nomeCliente;
    private static final Map<String, GerenciadorDeClientes> clientes = new HashMap<String, GerenciadorDeClientes>();
    private BufferedReader leitor;
    private PrintWriter escritor;
    
    public GerenciadorDeClientes(Socket cliente) {
        this.cliente = cliente;
        start();
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            escritor = new PrintWriter(cliente.getOutputStream(), true);
            escritor.println("por favor escreva seu nome: ");
            String msg = leitor.readLine();
            this.nomeCliente = msg.toLowerCase().replaceAll(",", "");
            escritor.println("Olá: " + this.nomeCliente);
            
            clientes.put(this.nomeCliente, this);
            
            while (true) {
                msg = leitor.readLine();
                
                if (msg.equalsIgnoreCase("/EXIT")) {
                    this.cliente.close();
                } else if (msg.toLowerCase().startsWith("/mp")) {
                    String nomeDestinatario = msg.substring(3, msg.length());
                    System.out.println("Enviando para: " + nomeDestinatario);
                    GerenciadorDeClientes destinatario = clientes.get(nomeDestinatario);
                    
                    if (destinatario == null) {
                        escritor.println("O cliente informado nao existe!");
                    } else {
                        escritor.println("digite uma mensagem para " + destinatario.getNomeCliente());
                        destinatario.getEscritor().println(this.nomeCliente + ": " + leitor.readLine());
                    }
                } else if (msg.equals("/listar-users")) {
                    
                    StringBuffer strB = new StringBuffer();
                    
                    for (String c : clientes.keySet()) {
                        strB.append(c);
                        strB.append(", ");
                    }
                    strB.delete(strB.length() - 1, strB.length());
                    escritor.println(strB.toString());
                } else {
                    escritor.println(this.nomeCliente + ", você disse: " + msg);
                    
                }
                
            }
        } catch (IOException ex) {
            System.err.println("O cliente fechou a conexão!");
            // ex.printStackTrace();
        }
    }
    
    public PrintWriter getEscritor() {
        return escritor;
    }
    
    public String getNomeCliente() {
        return nomeCliente;
    }
    
}
