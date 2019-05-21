import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class ClienteView extends javax.swing.JFrame {

    private PrintWriter escritor;
    private BufferedReader leitor;

    public ClienteView() {
        adicionandoScroll();
        initComponents();

        txaChat.setEditable(false);
        this.setLocationRelativeTo(null);

        txaMsg.requestFocus();

        String[] usuarios = new String[]{};
        preencherListaUsuarios(usuarios);
    }

    private void adicionandoScroll() {
        JScrollPane scroll = new JScrollPane(txaChat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollLista = new JScrollPane(jltUsuarios, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void preencherListaUsuarios(String[] usuarios) {
        DefaultListModel modelo = new DefaultListModel();
        jltUsuarios.setModel(modelo);

        for (String usuario : usuarios) {
            modelo.addElement(usuario);
        }
    }

    private void prepararMsg() {
        txaMsg.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override

            public void keyPressed(KeyEvent e) {
                enviarMsg(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void enviarMsg(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txaChat.getText().isEmpty()) {
                return;
            }
            Object usuario = jltUsuarios.getSelectedValue();
            if (usuario != null) {
                // Adicionando mensagem no chat
                txaChat.append("Eu: ");
                txaChat.append(txaMsg.getText());
                txaChat.append("\n");

                escritor.println(Constants.MENSAGEM + usuario);
                escritor.println(txaMsg.getText());

                txaMsg.setText("");
                e.consume();
            } else {
                txaChat.append("[TODOS] Eu: ");
                txaChat.append(txaMsg.getText());
                txaChat.append("\n");

                String[] usuariosLista = new String[10];

                for (int i = 0; i < jltUsuarios.getModel().getSize(); i++) {
                    usuariosLista[i] = String.valueOf(jltUsuarios.getModel().getElementAt(i));
                    System.out.println(usuariosLista[i]);
                }

                sendToAll(usuariosLista, txaMsg.getText());

                txaMsg.setText("");
                e.consume();
            }
        }
    }

    public void sendToAll(String[] usuarios, String msg) {
        for (String usuario : usuarios) {
            escritor.println(Constants.MENSAGEM + usuario);
            escritor.println("[TODOS]" + txaMsg.getText());
        }
    }

    public void iniciarChat() {
        try {
            final Socket cliente = new Socket(Constants.SERVIDOR, Constants.PORTA);
            escritor = new PrintWriter(cliente.getOutputStream(), true);
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(ClienteView.this, "Servidor indisponível no momento! Tente novamente mais tarde.");
        }
    }

    private void inicarLeitor() {
        // lendo mensagens do servidor
        try {
            while (true) {
                String mensagem = leitor.readLine();
                if (mensagem == null || mensagem.isEmpty()) {
                    continue;
                }

                // recebe a mensagem
                if (mensagem.equals(Constants.LISTA_USUARIOS)) {
                    String[] usuarios = leitor.readLine().split(",");
                    preencherListaUsuarios(usuarios);
                } else if (mensagem.equals(Constants.LOGIN)) {
                    String user = JOptionPane.showInputDialog("Digite o seu user: ");
                    escritor.println(user);
                } else if (mensagem.equals(Constants.LOGIN_NEGADO)) {
                    JOptionPane.showMessageDialog(ClienteView.this, "Usuário já logado");
                } else if (mensagem.equals(Constants.LOGIN_ACEITO)) {
                    atualizarListaUsuarios();
                    txaMsg.requestFocus();
                } else {
                    txaChat.append(mensagem);
                    txaChat.append("\n");
                    txaChat.setCaretPosition(txaChat.getDocument().getLength());
                }
            }

        } catch (IOException e) {
            System.out.println("Impossivel ler a mensagem do servidor");
        }
    }

    private void atualizarListaUsuarios() {
        escritor.println(Constants.LISTA_USUARIOS);
    }

    private DefaultListModel getListaUsuarios() {
        return (DefaultListModel) jltUsuarios.getModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txaChat = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaMsg = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jltUsuarios = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CHAT");
        setName("CHAT"); // NOI18N

        txaChat.setColumns(20);
        txaChat.setRows(5);
        txaChat.setName("txaChat"); // NOI18N
        jScrollPane1.setViewportView(txaChat);

        txaMsg.setColumns(20);
        txaMsg.setRows(5);
        txaMsg.setName("txaMsg"); // NOI18N
        jScrollPane2.setViewportView(txaMsg);

        jltUsuarios.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jltUsuarios.setName("jltOnline"); // NOI18N
        jScrollPane3.setViewportView(jltUsuarios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(lookAndFeel);
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClienteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClienteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClienteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClienteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        ClienteView cliente = new ClienteView();
        cliente.setVisible(true);
        cliente.iniciarChat();
        cliente.prepararMsg();
        cliente.inicarLeitor();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList jltUsuarios;
    private javax.swing.JTextArea txaChat;
    private javax.swing.JTextArea txaMsg;
    // End of variables declaration//GEN-END:variables
}
