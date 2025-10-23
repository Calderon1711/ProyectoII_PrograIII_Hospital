package Controlador;

import Modelo.Hospital;
import Modelo.Paciente;
import Vista.Buscar_Paciente;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ControladoraBuscarPaciente {

    private  Buscar_Paciente buscarPacienteVista;
    private final Hospital hospi;
    private Paciente pacienteSeleccionado; // variable para guardar el paciente elegido

    public ControladoraBuscarPaciente(Hospital hospital) {
        this.hospi = hospital;
        // DEBUG: Verificar hospital
        if (hospital == null) {
            return;
        }
        // DEBUG: Verificar getPacientes()
        if (hospital.getPacientes() == null) {
            return;
        }
        // DEBUG: Verificar lista de pacientes
        if (hospital.getPacientes().getPacientes() == null) {
            return;
        }

        List<Paciente> listaPacientes = hospital.getPacientes().getPacientes();

        // Crear la vista
        try {
            buscarPacienteVista = new Buscar_Paciente(hospi.getPacientes());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        initController();
    }

    private void initController() {

        System.out.println("Inicializando controlador...");

        try {
            modificarColumnas();
            llenarComboPacientes();
            configurarBotonOK();
            configurarJTextField();
            configurarFiltroComboNombre();
            configurarBotonCancelar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarColumnas() {
        // Verificar que hay pacientes
        if (hospi.getPacientes() == null || hospi.getPacientes().getPacientes() == null) {
            return;
        }

        // Definir columnas
        String[] columns = {"Id", "Fecha de nacimiento", "Nombre", "Teléfono"};

        // Crear modelo vacío con las columnas
        DefaultTableModel modelo = new DefaultTableModel(columns, 0);
        List<Paciente> pacientes = hospi.getPacientes().getPacientes();

        // Agregar los pacientes al modelo
        for (int i = 0; i < pacientes.size(); i++) {
            Paciente p = pacientes.get(i);
            Object[] fila = new Object[4];
            fila[0] = p.getId();
            fila[1] = p.getFechaNacimiento();
            fila[2] = p.getNombre();
            fila[3] = p.getTelefono();
            modelo.addRow(fila);
        }

        // DEBUG: Verificar componentes de la vista
        if (buscarPacienteVista.getTable1() == null) {
            return;
        }

        if (buscarPacienteVista.getTable_Pacientes() == null) {
            return;
        }

        // Asignar el modelo a la tabla
        buscarPacienteVista.getTable1().setModel(modelo);

        // Asegurarte de que la tabla esté dentro del JScrollPane
        buscarPacienteVista.getTable_Pacientes().setViewportView(buscarPacienteVista.getTable1());

        // Refrescar la tabla
        buscarPacienteVista.getTable1().revalidate();
        buscarPacienteVista.getTable1().repaint();
        System.out.println(" Tabla refrescada");
    }

    private void llenarComboPacientes() {
        // Verificar que hay pacientes
        if (hospi.getPacientes() == null || hospi.getPacientes().getPacientes() == null) {
            return;
        }

        // DEBUG: Verificar ComboBox
        if (buscarPacienteVista.getCmb_Buscar_Paciente() == null) {
            return;
        }

        JComboBox<Paciente> combo = buscarPacienteVista.getCmb_Buscar_Paciente();
        combo.removeAllItems();

        List<Paciente> pacientes = hospi.getPacientes().getPacientes();

        for (int i = 0; i < pacientes.size(); i++) {
            Paciente p = pacientes.get(i);
            combo.addItem(p);
        }

        // Refrescar el ComboBox
        combo.revalidate();
        combo.repaint();
    }


    public void configurarBotonOK() {
        JButton botonOK = buscarPacienteVista.getBtnOk(); // tu botón OK
        JTable tabla = buscarPacienteVista.getTable1();

        botonOK.addActionListener(e -> {
            int fila = tabla.getSelectedRow(); // obtiene la fila seleccionada
            if (fila != -1) { // si hay fila seleccionada
                // Obtener el Id del paciente de la tabla
                Object idObj = tabla.getValueAt(fila, 0); // columna 0 = Id
                String id = idObj.toString();

                // Buscar el paciente en la lista usando el id
                for (Paciente p : hospi.getPacientes().getPacientes()) {
                    if (String.valueOf(p.getId()).equals(id)) {
                        pacienteSeleccionado = p;
                        JOptionPane.showMessageDialog(buscarPacienteVista, "Paciente seleccionado","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }

                // Cerrar la ventana si quieres
                buscarPacienteVista.setVisible(false);

                System.out.println("Paciente seleccionado: " + pacienteSeleccionado.getNombre());
            } else {
                JOptionPane.showMessageDialog(buscarPacienteVista, "Seleccione un paciente", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    //Para retorna el paciente seleccionado
    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void configurarJTextField() {
        buscarPacienteVista.getSugerencias_Paciente().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(DocumentEvent e) {}

            private void filtrar() {
                String texto = buscarPacienteVista.getSugerencias_Paciente().getText().toLowerCase();
                DefaultTableModel modelo = (DefaultTableModel) buscarPacienteVista.getTable1().getModel();
                modelo.setRowCount(0); // Limpia la tabla

                for (Paciente p : hospi.getPacientes().getPacientes()) {
                    if (p.getNombre().toLowerCase().startsWith(texto)) {
                        modelo.addRow(new Object[] {
                                p.getId(), p.getFechaNacimiento(), p.getNombre(), p.getTelefono()
                        });
                    }
                }
            }
        });
    }

    public void configurarFiltroComboNombre() {
        buscarPacienteVista.getCmb_Buscar_Paciente().addActionListener(e -> {
            String nombreSeleccionado = buscarPacienteVista.getCmb_Buscar_Paciente().getSelectedItem().toString().toLowerCase();
            DefaultTableModel modelo = (DefaultTableModel) buscarPacienteVista.getTable1().getModel();
            modelo.setRowCount(0); // Limpia la tabla

            for (Paciente p : hospi.getPacientes().getPacientes()) {
                if (p.getNombre().toLowerCase().equals(nombreSeleccionado)) {
                    modelo.addRow(new Object[] {
                            p.getId(), p.getFechaNacimiento(), p.getNombre(), p.getTelefono()
                    });
                }
            }
        });
    }

    public void configurarBotonCancelar() {
        buscarPacienteVista.getBtnCancel().addActionListener(e -> {
            Window ventana = SwingUtilities.getWindowAncestor(buscarPacienteVista.getBtnCancel());
            if (ventana != null) {
                ventana.dispose(); // Cierra la ventana actual
            }
        });
    }

    // Método para actualizar los datos (útil si se agregan pacientes después)
    public void actualizarDatos() {
        modificarColumnas();
        llenarComboPacientes();
    }

    public void mostrarVentana() {
        if (buscarPacienteVista == null) {
            return;
        }
        buscarPacienteVista.setVisible(true);
    }

    // Getter para acceder a la vista (opcional)
    public Buscar_Paciente getVista() {
        return buscarPacienteVista;
    }
}