package cliente;

import cliente.controlador.ControladorDetalleMedicamento;

public class MainCliente {
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando aplicación...");

            // Crea e inicia el controlador
            ControladorDetalleMedicamento controlador = new ControladorDetalleMedicamento();
            controlador.mostrarVentana();

            System.out.println("Ventana de detalle de medicamento abierta correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }
}
