package cliente.controlador;

import cliente.modelo.*;
import cliente.proxy.*;
import cliente.Vista.Modificar_detalle;
import cliente.util.Alerta;
import javafx.collections.ObservableList;
import lombok.Data;

import javax.swing.*;

@Data
public class ControladorDetalleMedicamento {

    private Modificar_detalle modificarDetalleVista;
    private Medicamento medicamento;
    private ProxyHospital proxyHospital;
    private ProxyDetalleMedicamento proxyDetalle;

    public ControladorDetalleMedicamento() {
        try {
            proxyHospital = new ProxyHospital();
            proxyDetalle = new ProxyDetalleMedicamento();

            Hospital hospi = proxyHospital.obtenerHospital(); // ahora el hospital viene del backend
            if (hospi == null) {
                JOptionPane.showMessageDialog(null, "No se pudo obtener información del hospital desde el servidor.");
                return;
            }

            modificarDetalleVista = new Modificar_detalle(hospi);
            initController();

        } catch (Exception e) {
            e.printStackTrace();
            Alerta.error("Detalle Medicamento","Error inicializando el controlador" + e.getMessage());
        }
    }


    private void initController() {
        System.out.println(" Inicializando controlador...");

        try {
            iniciarcomboBox();
            configurarBotonGuardar();
            cancelar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarcomboBox() {
        modificarDetalleVista.getComboBoxCantidad().removeAllItems();
        modificarDetalleVista.getComboBoxdias();

        for (int i = 0; i <= 10; i++) {
            modificarDetalleVista.getComboBoxCantidad().addItem(i);
        }

        for (int j = 1; j <= 30; j++) {
            modificarDetalleVista.getComboBoxdias().addItem(j);
        }
    }

    public void configurarBotonGuardar() {
        modificarDetalleVista.getGuardarButton().addActionListener(e -> {
            try {
                String nombre = modificarDetalleVista.getTextFieldMedicamento().getText().trim();
                String presentacion = modificarDetalleVista.getTextFieldPresentacion().getText().trim();
                String codigo = modificarDetalleVista.getTextFieldCodigo().getText().trim();
                String indicacion = modificarDetalleVista.getTextFieldIndicaciones().getText().trim();

                Integer cantidad = (Integer) modificarDetalleVista.getComboBoxCantidad().getSelectedItem();
                Integer duracion = (Integer) modificarDetalleVista.getComboBoxdias().getSelectedItem();

                if (nombre.isEmpty() || presentacion.isEmpty() || codigo.isEmpty() || indicacion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor completa todos los campos obligatorios.");
                    return;
                }

                // Crear objetos
                Medicamento medicamento = new Medicamento();
                medicamento.setCodigo(codigo);
                medicamento.setNombre(nombre);
                medicamento.setPresentacion(presentacion);

                DetalleMedicamento detalle = new DetalleMedicamento();
                detalle.setCantidad(cantidad);
                detalle.setMedicamento(medicamento);
                detalle.setIdDetalle(codigo);
                detalle.setDuracion(duracion);
                detalle.setIndicacion(indicacion);

                // 🔹 Nuevo: enviar al backend mediante el Proxy
                DetalleMedicamento detalleGuardado = proxyDetalle.agregarDetalleMedicamento(detalle);

                if (detalleGuardado != null) {
                    JOptionPane.showMessageDialog(modificarDetalleVista,
                            "Medicamento agregado correctamente al servidor.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    modificarDetalleVista.dispose();
                } else {
                    JOptionPane.showMessageDialog(modificarDetalleVista,
                            "Error al agregar medicamento en el servidor.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
            }
        });
    }

    public void cancelar(){
        modificarDetalleVista.getCancelarButton().addActionListener(e -> {
            modificarDetalleVista.dispose();
        });
    }

    public void actualizarDatos() {
        iniciarcomboBox();
        configurarBotonGuardar();
        cancelar();
    }

    public void mostrarVentana() {
        if (modificarDetalleVista == null) {
            return;
        }
        modificarDetalleVista.setVisible(true);
    }
    public Modificar_detalle getVista() {
        return modificarDetalleVista;
    }

    public void cargarDetallesDesdeServidor() {
        try {
            ObservableList<DetalleMedicamento> lista = proxyDetalle.obtenerDetalleMedicamentos();
            if (lista.isEmpty()) {
                System.out.println("No hay detalles registrados en el servidor.");
            } else {
                System.out.println("Detalles obtenidos: " + lista.size());
            }
            /// Falta ver donde van los detalles
            // Aquí podrías llenar una JTable o ComboBox con esos datos.

        } catch (Exception e) {
            System.err.println("Error al obtener detalles desde el servidor: " + e.getMessage());
        }
    }


}