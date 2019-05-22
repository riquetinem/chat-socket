package Model;

import Util.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Cliente extends Thread {

    private Socket cliente;
    private String nomeCliente;
    private BufferedReader leitor;
    private PrintWriter escritor;
    private static final Map<String, Cliente> clientes = new HashMap<String, Cliente>();

    public Cliente(Socket cliente) {
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

            efetuarLogin();

            String msg;
            while (true) {
                msg = leitor.readLine();

                if (msg.startsWith(Constants.MENSAGEM)) {
                    String nomeDestinario = msg.substring(Constants.MENSAGEM.length(), msg.length());
                    Cliente destinario = clientes.get(nomeDestinario);

                    System.out.println("");

                    if (destinario != null) {
                        destinario.getEscritor().println(this.nomeCliente + ": " + leitor.readLine());
                    }

                    // lista o nome de todos os clientes logados
                } else if (msg.equals(Constants.LISTA_USUARIOS)) {
                    atualizarListaUsuarios(this);
                } else {
                    // escritor.println(this.nomeCliente + ", você disse: " + msg);
                }
            }

        } catch (IOException e) {
            System.err.println("o cliente fechou a conexao");
            clientes.remove(this.nomeCliente);
            e.printStackTrace();
        }
    }

    private synchronized void efetuarLogin() throws IOException {

        while (true) {
            escritor.println(Constants.LOGIN);
            this.nomeCliente = leitor.readLine().toLowerCase().replaceAll(",", "");
            if (this.nomeCliente.equalsIgnoreCase(null) || this.nomeCliente.isEmpty()) {
                escritor.println(Constants.LOGIN_NEGADO);
            } else if (clientes.containsKey(this.nomeCliente)) {
                escritor.println(Constants.LOGIN_NEGADO);
            } else {
                escritor.println(Constants.LOGIN_ACEITO);
                escritor.println("olá " + this.nomeCliente);
                clientes.put(this.nomeCliente, this);
                for (String cliente : clientes.keySet()) {
                    atualizarListaUsuarios(clientes.get(cliente));
                }
                break;
            }
        }
    }

    private void atualizarListaUsuarios(Cliente cliente) {
        StringBuffer str = new StringBuffer();
        for (String c : clientes.keySet()) {
            if (cliente.getNomeCliente().equals(c)) {
                continue;
            }

            str.append(c);
            str.append(",");
        }
        if (str.length() > 0) {
            str.delete(str.length(), str.length());
        }
        cliente.getEscritor().println(Constants.LISTA_USUARIOS);
        cliente.getEscritor().println(str.toString());
    }

    public PrintWriter getEscritor() {
        return escritor;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }
}
