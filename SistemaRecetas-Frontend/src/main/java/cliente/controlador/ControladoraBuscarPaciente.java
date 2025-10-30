package cliente.controlador;

import cliente.modelo.Paciente;
import cliente.Vista.Buscar_Paciente;
import cliente.proxy.ProxyPaciente;
import cliente.util.Alerta;
import cliente.util.ConfiguracionCliente;
import javafx.collections.ObservableList;
import lombok.Data;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Data
public class ControladoraBuscarPaciente {

    private Buscar_Paciente buscarPacienteVista;
    private final ProxyPaciente proxyPaciente;
    private Paciente pacienteSeleccionado;
    private ObservableList<Paciente> listaPacientes;

    // Variables para control de caché
    private long ultimaActualizacionPacientes = 0;
    private static final long TIEMPO_CACHE_MS = ConfiguracionCliente.getTimeout(); // 5 minutos de caché

    public ControladoraBuscarPaciente() {
        this.proxyPaciente = new ProxyPaciente();
        inicializarVista();
        initController();
        cargarPacientesAsync(); // carga inicial asíncrona
    }

    private void inicializarVista() {
        try {
            buscarPacienteVista = new Buscar_Paciente();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al inicializar la vista: " + e.getMessage());
        }
    }

    private void initController() {
        System.out.println("Inicializando controlador...");
        try {
            configurarBotonCancelar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Nuevo método general para obtener pacientes con caché

    private ObservableList<Paciente> getPacientesConCache() {
        long ahora = System.currentTimeMillis();

        // si nunca se cargaron o ya pasó el tiempo de cache
        if (listaPacientes == null || (ahora - ultimaActualizacionPacientes) > TIEMPO_CACHE_MS) {
            System.out.println("[INFO] Actualizando lista de pacientes desde backend...");
            listaPacientes = proxyPaciente.obtenerPacientes();
            ultimaActualizacionPacientes = ahora;

            if (listaPacientes == null || listaPacientes.isEmpty()) {
                Alerta.error("Buscar Paciente", "No se pudo obtener pacientes desde el servidor.");
            } else {
                Alerta.info("Buscar Paciente", "[INFO] Lista de pacientes actualizada desde el backend.");
            }
        } else {
            System.out.println("[INFO] Usando pacientes desde caché (última actualización reciente)");
            Alerta.info("Buscar Paciente", "[INFO] Usando pacientes en caché.");
        }

        return listaPacientes;
    }

    // 🟢 Ajuste en carga asíncrona para que use el método con caché
    private void cargarPacientesAsync() {
        SwingWorker<ObservableList<Paciente>, Void> worker = new SwingWorker<>() {
            @Override
            protected ObservableList<Paciente> doInBackground() {
                try {
                    // ahora usa el método con caché
                    return getPacientesConCache();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    listaPacientes = get();
                    if (listaPacientes == null || listaPacientes.isEmpty()) {
                        Alerta.error("Buscar Paciente", "No se pudo obtener pacientes desde el servidor.");
                        return;
                    }
                    modificarColumnas();
                    llenarComboPacientes();
                    configurarBotonOK();
                    configurarJTextField();
                    configurarFiltroComboNombre();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alerta.error("Buscar Paciente", "Error al cargar pacientes: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void modificarColumnas() {
        if (listaPacientes == null || listaPacientes.isEmpty()) {
            Alerta.error("Modificar Columnas", "Error en la lista de pacientes");
            return;
        }

        String[] columnas = {"Id", "Fecha de nacimiento", "Nombre", "Teléfono"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Paciente p : listaPacientes) {
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

    private void llenarComboPacientes() {
        if (listaPacientes == null || listaPacientes.isEmpty()) {
            return;
        }

        JComboBox<Paciente> combo = buscarPacienteVista.getCmb_Buscar_Paciente();
        combo.removeAllItems();

        for (Paciente p : listaPacientes) {
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
                    JOptionPane.showMessageDialog(buscarPacienteVista,
                            "Paciente seleccionado: " + pacienteSeleccionado.getNombre(),
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    buscarPacienteVista.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(buscarPacienteVista,
                        "Seleccione un paciente", "Aviso", JOptionPane.WARNING_MESSAGE);
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
                DefaultTableModel modelo = (DefaultTableModel) buscarPacienteVista.getTable1().getModel();
                modelo.setRowCount(0);

                for (Paciente p : listaPacientes) {
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

            DefaultTableModel modelo = (DefaultTableModel) buscarPacienteVista.getTable1().getModel();
            modelo.setRowCount(0);

            for (Paciente p : listaPacientes) {
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
        if (buscarPacienteVista != null) {
            buscarPacienteVista.setVisible(true);
        }
    }
}
