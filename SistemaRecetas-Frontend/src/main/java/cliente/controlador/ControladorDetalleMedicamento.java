package cliente.controlador;

import cliente.modelo.*;
import cliente.proxy.*;
import cliente.Vista.*;
import cliente.util.Alerta;
import javafx.collections.ObservableList;
import lombok.Data;

import javax.swing.*;

@Data
public class ControladorDetalleMedicamento {

    //Vista
    private Modificar_detalle modificarDetalleVista;

    //Variables para aguardar lo que digite el usuario
    private Medicamento medicamentoGuardado;
    private DetalleMedicamento detalleGuardado;

    //Proxys que se usan
    private ProxyDetalleMedicamento proxyDetalle;
    private ProxyMedicamento proxyMedicamento;

    //Lista para enllenar la ventana
    ObservableList<Medicamento> medicamentos;
    ObservableList<DetalleMedicamento> detalleMedicamentos;

    public ControladorDetalleMedicamento() {
        try {
            proxyMedicamento = new ProxyMedicamento();
            proxyDetalle = new ProxyDetalleMedicamento();

            medicamentos = proxyMedicamento.obtenerMedicamentos();
            detalleMedicamentos = proxyDetalle.obtenerDetalleMedicamentos();

            if (medicamentos == null) {
                JOptionPane.showMessageDialog(null, "No se pudo obtener información de los medicamentos desde el servidor.");
                return;
            }

            if (detalleMedicamentos == null) {
                JOptionPane.showMessageDialog(null, "No se pudo obtener información de los detalleMedicamentos desde el servidor.");
                return;
            }


            modificarDetalleVista = new Modificar_detalle();
            initController();

        } catch (Exception e) {
            e.printStackTrace();
            Alerta.error("Agregar Medicamento", "Error inicializando el controlador" + e.getMessage());
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
                medicamentoGuardado.setCodigo(codigo);
                medicamentoGuardado.setNombre(nombre);
                medicamentoGuardado.setPresentacion(presentacion);

                detalleGuardado.setCantidad(cantidad);
                detalleGuardado.setMedicamento(medicamentoGuardado);
                detalleGuardado.setIdDetalle(codigo);
                detalleGuardado.setDuracion(duracion);
                detalleGuardado.setIndicacion(indicacion);

                proxyMedicamento.agregarMedicamento(medicamentoGuardado);
                proxyDetalle.agregarDetalleMedicamento(detalleGuardado);

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

    public void cancelar() {
        modificarDetalleVista.getCancelarButton().addActionListener(e -> {
            modificarDetalleVista.dispose();
        });
    }

    public void mostrarVentana() {
        if (modificarDetalleVista == null) {
            return;
        }
        modificarDetalleVista.setVisible(true);
    }
}