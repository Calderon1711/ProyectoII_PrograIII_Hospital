package Controlador;

import Modelo.*;
import Vista.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ControladorDetalleMedicamento {

    private Modificar_detalle modificarDetalleVista;
    private Hospital hospi = Hospital.getInstance();
    private Medicamento medicamento;
    private DetalleMedicamento detalle;


    public ControladorDetalleMedicamento(Hospital hospital) {

        this.hospi = hospital;


        if (hospi == null || hospi.getMedicamentos() == null || hospi.getMedicamentos().getMedicamentos() == null) {
            System.err.println("Hospital o lista de medicamentos no disponible.");
            return;
        }
        try {
            modificarDetalleVista= new Modificar_detalle(hospi);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        initController();
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


            this.medicamento = medicamento;
            this.detalle = detalle;

            JOptionPane.showMessageDialog(modificarDetalleVista,
                    "Medicamento agregado a la receta.",
                    "Medicamento",
                    JOptionPane.INFORMATION_MESSAGE);

            modificarDetalleVista.dispose();
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

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public DetalleMedicamento getDetalle() {
        return detalle;
    }

    public void setDetalle(DetalleMedicamento detalle) {
        this.detalle = detalle;
    }
}