package cadastrocliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ThreadClient extends Thread {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private JTextArea textArea;
    private JFrame frame;

    public ThreadClient(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            Boolean validate = (Boolean) in.readObject();
            Integer idUsuario = (Integer) in.readObject();

            if (validate) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String comando;
                String idPessoa;
                String idProduto;
                String quantidade;
                String valor_unitario;

                do {
                    System.out.print("L - Listar\nX - Finalizar\nE - Entrada\nS - Saída\n");
                    comando = reader.readLine().toLowerCase();

                    switch (comando) {
                        case "E":
                            out.writeObject("E");
                            System.out.print("ID da Pessoa: ");
                            idPessoa = reader.readLine();
                            out.writeObject(idPessoa);

                            System.out.print("ID do Produto: ");
                            idProduto = reader.readLine();
                            out.writeObject(idProduto);

                            System.out.print("Quantidade: ");
                            quantidade = reader.readLine();
                            out.writeObject(quantidade);

                            System.out.print("Valor Unitário: ");
                            valor_unitario = reader.readLine();
                            out.writeObject(valor_unitario);

                            break;

                        case "S":
                            out.writeObject("S");
                            System.out.print("ID da Pessoa: ");
                            idPessoa = reader.readLine();
                            out.writeObject(idPessoa);

                            System.out.print("ID do Produto: ");
                            idProduto = reader.readLine();
                            out.writeObject(idProduto);

                            System.out.print("Quantidade: ");
                            quantidade = reader.readLine();
                            out.writeObject(quantidade);

                            System.out.print("Valor Unitário: ");
                            valor_unitario = reader.readLine();
                            out.writeObject(valor_unitario);

                            break;

                        case "L":
                            out.writeObject("L");

                            try {
                                ArrayList<String> produtoList = (ArrayList<String>) in.readObject();
                                ArrayList<Integer> produtoQuantidade = (ArrayList<Integer>) in.readObject();

                                if (frame == null || !frame.isVisible()) {
                                    frame = new JFrame("Resultado");
                                    textArea = new JTextArea(20, 50);
                                    textArea.setEditable(false);
                                    frame.add(new JScrollPane(textArea));
                                    frame.pack();
                                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                                    frame.setVisible(true);

                                    SwingUtilities.invokeLater(() -> {
                                        textArea.append("Lista de Produtos:\n");

                                        for (int i = 0; i < produtoList.size(); i++) {
                                            textArea.append(produtoList.get(i) + ": " + produtoQuantidade.get(i) + "\n");
                                        }

                                        textArea.setCaretPosition(textArea.getDocument().getLength());
                                    });

                                } else {
                                    frame.setVisible(false);
                                }
                            } catch (ClassNotFoundException | IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "F":
                            out.writeObject("F");

                            System.out.println("O programa está sendo finalizado!");
                            break;

                        default:
                            System.out.println("Opção inválida!");
                            break;
                    }

                } while (!"F".equalsIgnoreCase(comando));

            } else {
                System.out.println("Usuáio e/ou senha inválido(s)!");
            }

        } catch (Exception e) {
            if (!(e instanceof java.io.EOFException)) {
                System.out.println("Thread finalizada!");
            }
        }
    }
}