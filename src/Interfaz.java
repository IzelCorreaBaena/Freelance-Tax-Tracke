package src;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Interfaz extends JFrame {

    private JTextField campoConcepto;
    private JTextField campoMonto;
    private JCheckBox checkEsGasto;
    private JButton botonGuardar;
    private JButton botonCargar;
    private JTextArea areaResultados;
    private JLabel labelSaldoTotal;
    private ArrayList<Movimiento> listaMovimientos;

    public Interfaz() {
        listaMovimientos = new ArrayList<>();

        setTitle("Freelance Tax Tracker Pro");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelFormulario.add(new JLabel("Concepto:"));
        campoConcepto = new JTextField();
        panelFormulario.add(campoConcepto);

        panelFormulario.add(new JLabel("Monto (€):"));
        campoMonto = new JTextField();
        panelFormulario.add(campoMonto);

        panelFormulario.add(new JLabel("¿Es un gasto?"));
        checkEsGasto = new JCheckBox("Sí");
        panelFormulario.add(checkEsGasto);

        botonGuardar = new JButton("AÑADIR FACTURA");
        panelFormulario.add(botonGuardar);

        botonCargar = new JButton("CARGAR HISTORIAL");
        botonCargar.setBackground(new Color(200, 200, 255));
        panelFormulario.add(botonCargar);
        
        panelFormulario.add(new JLabel("")); 

        add(panelFormulario, BorderLayout.NORTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane barraDesplazamiento = new JScrollPane(areaResultados);
        add(barraDesplazamiento, BorderLayout.CENTER);

        JPanel panelSaldo = new JPanel();
        panelSaldo.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelSaldo.setBorder(BorderFactory.createEtchedBorder());

        labelSaldoTotal = new JLabel("Saldo Actual: 0.0 €");
        labelSaldoTotal.setFont(new Font("Arial", Font.BOLD, 16));

        panelSaldo.add(labelSaldoTotal);
        add(panelSaldo, BorderLayout.SOUTH);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarYMostrar();
            }
        });

        botonCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarHistorial();
            }
        });

        setVisible(true);
    }

    private void guardarYMostrar() {
        try {
            String concepto = campoConcepto.getText();

            if (concepto.isEmpty() || campoMonto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double monto = Double.parseDouble(campoMonto.getText());
            boolean esGasto = checkEsGasto.isSelected();

            Movimiento nuevoMov = new Movimiento(concepto, monto, esGasto);
            listaMovimientos.add(nuevoMov);

            GestorArchivos.guardarDatos(listaMovimientos);

            actualizarListaVisual();

            campoConcepto.setText("");
            campoMonto.setText("");
            checkEsGasto.setSelected(false);
            campoConcepto.requestFocus();

        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        listaMovimientos = GestorArchivos.cargarDatos();
        
        if (listaMovimientos.isEmpty()) {
             JOptionPane.showMessageDialog(this, "No se encontraron datos guardados o el archivo está vacío.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actualizarListaVisual();
             JOptionPane.showMessageDialog(this, "¡Historial recuperado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarListaVisual() {
        areaResultados.setText("");
        areaResultados.append("--- HISTORIAL DE MOVIMIENTOS ---\n\n");

        for (Movimiento mov : listaMovimientos) {
            String tipoTexto = "";

            if (mov.isGasto()) {
                tipoTexto = "GASTO   (-)";
            } else {
                tipoTexto = "INGRESO (+)";
            }

            areaResultados.append(tipoTexto + " | " + mov.getConcepto() + " | " + mov.getCosto() + " €\n");
            areaResultados.append("---------------------------------------------------\n");
        }

        double saldo = GestorArchivos.calcularSaldoActual(listaMovimientos);
        labelSaldoTotal.setText("Saldo Actual: " + saldo + " €");

        if (saldo < 0) {
            labelSaldoTotal.setForeground(Color.RED);
        } else {
            labelSaldoTotal.setForeground(new Color(0, 128, 0));
        }
    }

    public static void main(String[] args) {
        new Interfaz();
    }
}