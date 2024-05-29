import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
public class Main extends JFrame implements ActionListener {
    public static final int PUERTO = 8080;
    public static final String HOST = "localhost";

    JLabel nombre, entradaCliente, respuestaServidor;
    JTextField num1, resultado;
    JButton sumaButton, restaButton, multiplicacionButton, divisionButton, potenciaButton, moduloButton, enviarButton, numero0;
    String operador;
    private JButton[] numeros;
    double numero1;

    public Main() {
        setTitle("Calculadora Cliente");
        setSize(450, 400);
        setLayout(null);

        getContentPane().setBackground(new Color(255, 179, 102));

        nombre = new JLabel("Calculadora Cliente");
        nombre.setBounds(170, 10, 200, 25);
        add(nombre);

        entradaCliente = new JLabel("Entrada Cliente");
        entradaCliente.setBounds(100, 40, 250, 25);
        add(entradaCliente);

        num1 = new JTextField();
        num1.setBounds(100, 60, 250, 25);
        add(num1);

        respuestaServidor = new JLabel("Respuesta Servidor");
        respuestaServidor.setBounds(100, 90, 250, 25);
        add(respuestaServidor);

        resultado = new JTextField();
        resultado.setBounds(100, 110, 250, 25);
        resultado.setEditable(false);
        add(resultado);

        Color colorBotonNumeros = new Color(48, 172, 172);
        Color colorBotonOperadores = new Color(251, 133, 0);

        numeros = new JButton[9];
        for (int i = 0; i < 9; i++) {
            numeros[i] = new JButton(Integer.toString(i + 1));
            numeros[i].setBounds(100 + (i % 3) * 50, 150 + (i / 3) * 50, 50, 50);
            numeros[i].setBackground(colorBotonNumeros);
            numeros[i].addActionListener(this);
            add(numeros[i]);
        }
        numero0 = new JButton("0");
        numero0.setBounds(100,300,100,25);
        numero0.setBackground(colorBotonNumeros);
        numero0.addActionListener(this);
        add(numero0);

        sumaButton = new JButton("+");
        sumaButton.setBounds(250, 150, 100, 25);
        sumaButton.setBackground(colorBotonOperadores);
        sumaButton.addActionListener(this);
        add(sumaButton);

        restaButton = new JButton("-");
        restaButton.setBounds(250, 175, 100, 25);
        restaButton.setBackground(colorBotonOperadores);
        restaButton.addActionListener(this);
        add(restaButton);

        multiplicacionButton = new JButton("X");
        multiplicacionButton.setBounds(250, 200, 100, 25);
        multiplicacionButton.setBackground(colorBotonOperadores);
        multiplicacionButton.addActionListener(this);
        add(multiplicacionButton);

        divisionButton = new JButton("/");
        divisionButton.setBounds(250, 225, 100, 25);
        divisionButton.setBackground(colorBotonOperadores);
        divisionButton.addActionListener(this);
        add(divisionButton);

        potenciaButton = new JButton("^");
        potenciaButton.setBounds(250, 250, 100, 25);
        potenciaButton.setBackground(colorBotonOperadores);
        potenciaButton.addActionListener(this);
        add(potenciaButton);

        moduloButton = new JButton("%");
        moduloButton.setBounds(250, 275, 100, 25);
        moduloButton.setBackground(colorBotonOperadores);
        moduloButton.addActionListener(this);
        add(moduloButton);

        enviarButton = new JButton("ENVIAR");
        enviarButton.setBounds(200, 300, 150, 25);
        enviarButton.setBackground(colorBotonOperadores);
        enviarButton.addActionListener(this);
        add(enviarButton);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Main ventanaCliente = new Main();
        ventanaCliente.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enviarButton) {
            enviarDatos();
            return;
        } else if (e.getSource() == numero0) {
            num1.setText(num1.getText() + "0");
        } else {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == numeros[i]) {
                    num1.setText(num1.getText() + Integer.toString(i + 1));
                    break;
                }
            }
        }
        if (e.getSource() == sumaButton) {
            operador = "+";
        } else if (e.getSource() == restaButton) {
            operador = "-";
        } else if (e.getSource() == multiplicacionButton) {
            operador = "*";
        } else if (e.getSource() == divisionButton) {
            operador = "/";
        } else if (e.getSource() == potenciaButton) {
            operador = "^";
        } else if (e.getSource() == moduloButton) {
            operador = "%";
        }
    }

    private void enviarDatos() {
        try {
            numero1 = Double.parseDouble(num1.getText());

            try (Socket socket = new Socket(HOST, PUERTO);
                 BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)) {

                salida.println(numero1);
                salida.println(operador);

                double resultadoCliente = Double.parseDouble(entrada.readLine());

                resultado.setText("= " + resultadoCliente );

                salida.println(resultadoCliente);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            resultado.setText("Número no válido");
        }
    }
}