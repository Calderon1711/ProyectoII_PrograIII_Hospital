package cliente.controlador;

import cliente.constantes.Comandos;
import cliente.modelo.*;
import cliente.Vista.Buscar_Paciente;
import cliente.proxy.ProxyPaciente;
import javafx.collections.ObservableList;
import lombok.Data;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Data
public class ControladoraBuscarPaciente {

    private Buscar_Paciente buscarPacienteVista;
    private final ProxyPaciente proxyPaciente;
    private Paciente pacienteSeleccionado; // variable para guardar el paciente elegido

    private void inicializarVista() {
        try {
            // Traemos los pacientes desde el servidor
            ObservableList<Paciente> listaPacientes = proxyPaciente.obtenerPacientes();

            if (listaPacientes == null || listaPacientes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay pacientes disponibles en el servidor.");
                return;
            }

            // Creamos la vista usando los pacientes
            buscarPacienteVista = new Buscar_Paciente(listaPacientes);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al inicializar la vista: " + e.getMessage());
        }
    }

    public ControladoraBuscarPaciente() {
        this.proxyPaciente = new ProxyPaciente();
        try {
            initController();
            inicializarVista();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        initController();
    }

    private void initController() {

        System.out.println("Inicializando controlador...");

        try {
            cargarPacientes();
            configurarBotonOK();
            configurarJTextField();
            configurarFiltroComboNombre();
            configurarBotonCancelar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarPacientes() {
        try {
            ObservableList<Paciente> pacientes = proxyPaciente.obtenerPacientes();
            if (pacientes == null || pacientes.isEmpty()) {
                JOptionPane.showMessageDialog(buscarPacienteVista, "No hay pacientes registrados", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            modificarColumnas(pacientes);
            llenarComboPacientes(pacientes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modificarColumnas(List<Paciente> pacientes) {
        String[] columnas = {"Id", "Fecha de nacimiento", "Nombre", "Teléfono"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Paciente p : pacientes) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getFechaNacimiento(),
                    p.getNombre(),
                    p.getTelefono()
            });
        }

        buscarPacienteVista.getTable1().setModel(modelo);
        buscarPacienteVista.getTable_Pacientes().setViewportView(buscarPacienteVista.getTable1());
        buscarPacienteVista.getTable1().revalidate();
        buscarPacienteVista.getTable1().repaint();
    }


    private void llenarComboPacientes(ObservableList<Paciente> pacientes) {
        JComboBox<Paciente> combo = buscarPacienteVista.getCmb_Buscar_Paciente();
        combo.removeAllItems();

        for (Paciente p : pacientes) {
            combo.addItem(p);
        }

        combo.revalidate();
        combo.repaint();
    }


    private void configurarBotonOK() {
        JButton botonOK = buscarPacienteVista.getBtnOk();
        JTable tabla = buscarPacienteVista.getTable1();

        botonOK.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                Object idObj = tabla.getValueAt(fila, 0);
                int id = Integer.parseInt(idObj.toString());

                pacienteSeleccionado = proxyPaciente.consultarPaciente(id);

                if (pacienteSeleccionado != null) {
                    JOptionPane.showMessageDialog(buscarPacienteVista, "Paciente seleccionado: " + pacienteSeleccionado.getNombre(),
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    buscarPacienteVista.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(buscarPacienteVista, "Seleccione un paciente", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void configurarJTextField() {
        buscarPacienteVista.getSugerencias_Paciente().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrar(); }

            @Override
            public void removeUpdate(DocumentEvent e) { filtrar(); }

            @Override
            public void changedUpdate(DocumentEvent e) {}

            private void filtrar() {
                String texto = buscarPacienteVista.getSugerencias_Paciente().getText().toLowerCase();
                List<Paciente> pacientes = proxyPaciente.obtenerPacientes();

                DefaultTableModel modelo = (DefaultTableModel) buscarPacienteVista.getTable1().getModel();
                modelo.setRowCount(0);

                for (Paciente p : pacientes) {
                    if (p.getNombre().toLowerCase().startsWith(texto)) {
                        modelo.addRow(new Object[]{
                                p.getId(), p.getFechaNacimiento(), p.getNombre(), p.getTelefono()
                        });
                    }
                }
            }
        });
    }

    private void configurarFiltroComboNombre() {
        buscarPacienteVista.getCmb_Buscar_Paciente().addActionListener(e -> {
            Object seleccionado = buscarPacienteVista.getCmb_Buscar_Paciente().getSelectedItem();
            if (seleccionado == null) return;

            String nombreSeleccionado = seleccionado.toString().toLowerCase();
            List<Paciente> pacientes = proxyPaciente.obtenerPacientes();

            DefaultTableModel modelo = (DefaultTableModel) buscarPacienteVista.getTable1().getModel();
            modelo.setRowCount(0);

            for (Paciente p : pacientes) {
                if (p.getNombre().toLowerCase().equals(nombreSeleccionado)) {
                    modelo.addRow(new Object[]{
                            p.getId(), p.getFechaNacimiento(), p.getNombre(), p.getTelefono()
                    });
                }
            }
        });
    }

    private void configurarBotonCancelar() {
        buscarPacienteVista.getBtnCancel().addActionListener(e -> {
            Window ventana = SwingUtilities.getWindowAncestor(buscarPacienteVista.getBtnCancel());
            if (ventana != null) {
                ventana.dispose();
            }
        });
    }

    public void mostrarVentana() {
        if (buscarPacienteVista == null) {
            return;
        }
        buscarPacienteVista.setVisible(true);
    }

}
