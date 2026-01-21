package src;

import java.io.*;
import java.util.ArrayList;

public class GestorArchivos {

    private static final String CARPETA = "Freelance Tax Tracker/datos";
    private static final String ARCHIVO = "contabilidad.csv";

    private static File obtenerArchivo() {
        String rutaProyecto = System.getProperty("user.dir");
        File carpetaDatos = new File(rutaProyecto, CARPETA);

        if (!carpetaDatos.exists()) {
            carpetaDatos.mkdir();
        }

        return new File(carpetaDatos, ARCHIVO);
    }

    public static void guardarDatos(ArrayList<Movimiento> lista) {
        try {
            File archivo = obtenerArchivo();
            System.out.println("Guardando en: " + archivo.getAbsolutePath());

            FileWriter escritorArchivo = new FileWriter(archivo);
            PrintWriter escritor = new PrintWriter(escritorArchivo);

            escritor.println("Concepto, Monto, EsGasto");

            for (Movimiento mov : lista) {
                escritor.println("\"" + mov.getConcepto() + "\"," + mov.getCosto() + "," + mov.isGasto());
            }

            escritor.close();

        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public static ArrayList<Movimiento> cargarDatos() {
        ArrayList<Movimiento> listaRecuperada = new ArrayList<>();
        File archivo = obtenerArchivo();

        if (!archivo.exists()) {
            return listaRecuperada;
        }

        try {
            FileReader lector = new FileReader(archivo);
            BufferedReader buffer = new BufferedReader(lector);

            String linea;
            boolean esPrimeraLinea = true;

            while ((linea = buffer.readLine()) != null) {
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }

                String[] partes = linea.split(",");

                if (partes.length == 3) {
                    String concepto = partes[0].replace("\"", "");
                    double monto = Double.parseDouble(partes[1]);
                    boolean esGasto = Boolean.parseBoolean(partes[2]);

                    Movimiento mov = new Movimiento(concepto, monto, esGasto);
                    listaRecuperada.add(mov);
                }
            }

            buffer.close();

        } catch (Exception e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }

        return listaRecuperada;
    }

    public static double calcularSaldoActual(ArrayList<Movimiento> lista) {
        double saldo = 0.0;
        for (Movimiento mov : lista) {
            if (mov.isGasto()) {
                saldo -= mov.getCosto();
            } else {
                saldo += mov.getCosto();
            }
        }
        return saldo;
    }
}