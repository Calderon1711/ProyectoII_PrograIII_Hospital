package cliente.controlador;

import cliente.Vista.AdminVista.AdminVista;
import cliente.modelo.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import cliente.proxy.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;


public class ControladoraAdmin implements ActionListener {
    private final AdminVista vista;
    private final Personal usuarioAdmin;
    private final ProxyPersonal proxyPersonal;
    private final ProxyPaciente proxyPaciente;
    private final ProxyMedicamento proxyMedicamento;
    private final ProxyReceta proxyReceta;

    public ControladoraAdmin(AdminVista vista, Personal usuarioAdmin) {
        this.vista = vista;
        this.usuarioAdmin = usuarioAdmin;

        // Inicializar proxies
        this.proxyPersonal = new ProxyPersonal();
        this.proxyPaciente = new ProxyPaciente();
        this.proxyMedicamento = new ProxyMedicamento();
        this.proxyReceta = new ProxyReceta();

        // Asignar eventos a botones principales (sin duplicar)
        vista.getBotonGuardar().addActionListener(this);
        vista.getBotonLimpiar().addActionListener(this);
        vista.getBotonBorrar().addActionListener(this);
        vista.getBuscarBoton().addActionListener(this);
        vista.getBotonReporte().addActionListener(this);

        vista.getGuardarButton().addActionListener(this);
        vista.getLimpiarButton().addActionListener(this);
        vista.getBorrarButton().addActionListener(this);
        vista.getBuscarButton().addActionListener(this);
        vista.getReporteButton().addActionListener(this);

        vista.getBtnGuardarMedicamento().addActionListener(this);
        vista.getBoton_LimpiarMedicamento().addActionListener(this);
        vista.getBTNBorrarMedicamento().addActionListener(this);
        vista.getBotonBuscarMedicamento().addActionListener(this);
        vista.getReporteMedicamento().addActionListener(this);

        vista.getBtnGuardarPaciente().addActionListener(this);
        vista.getBtnLimpiarPaciente().addActionListener(this);
        vista.getBorrarPAciente().addActionListener(this);
        vista.getBotonBuscarPaciente().addActionListener(this);

        // Cargar datos iniciales
        actualizarTablaMedicos();
        actualizarTablaFarma();
        actualizarTablaPaciente();
        actualizarTablaMedicamentos();

        // Configurar dashboard e histórico
        initController();

        System.out.println("Administrador logueado: " + usuarioAdmin.getNombre());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // ==== MÉDICOS ====
        if (source == vista.getBotonGuardar()) {
            guardarMedico();
        } else if (source == vista.getBotonLimpiar()) {
            limpiarMedico();
        } else if (source == vista.getBotonBorrar()) {
            borrarMedico();
        } else if (source == vista.getBuscarBoton()) {
            buscarMedico();
        } else if (source == vista.getBotonReporte()) {
            reporteMedico();
            actualizarTablaMedicos();
        }

        // ==== FARMACÉUTICOS ====
        else if (source == vista.getGuardarButton()) {
            guardarFarma();
        } else if (source == vista.getLimpiarButton()) {
            limpiarFarma();
        } else if (source == vista.getBorrarButton()) {
            borrarFarma();
        } else if (source == vista.getBuscarButton()) {
            buscarFarma();
        } else if (source == vista.getReporteButton()) {
            reporteFarma();
            actualizarTablaFarma();
        }

        // ==== PACIENTES ====
        else if (source == vista.getBtnGuardarPaciente()) {
            guardarPaciente();
        } else if (source == vista.getBtnLimpiarPaciente()) {
            limpiarPaciente();
        } else if (source == vista.getBorrarPAciente()) {
            borrarPaciente();
        } else if (source == vista.getBotonBuscarPaciente()) {
            buscarPaciente();
        }

        // ==== MEDICAMENTOS ====
        else if (source == vista.getBtnGuardarMedicamento()) {
            guardarMedicamento();
        } else if (source == vista.getBoton_LimpiarMedicamento()) {
            limpiarMedicamento();
        } else if (source == vista.getBTNBorrarMedicamento()) {
            borrarMedicamento();
        } else if (source == vista.getBotonBuscarMedicamento()) {
            buscarMedicamento();
        } else if (source == vista.getReporteMedicamento()) {
            reporteMedicamento();
            actualizarTablaMedicamentos();
        }
    }



    public void initController() {

        try {

            //Pestana Dashboard
            activarDatosDashboard();
            configurarEventosDashboard();
            modificarTablaDashBoard();

            //Pestana Historico
            configurarEventosHistorico();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void guardarMedico() {
        String id = vista.getCampoIDMEdico().getText();
        String nombre = vista.getCampoNombreMedico().getText();
        String especialidad = vista.getCampoEspecialidadMEdico().getText();

        if (id.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
            return;
        }

        Medico medicoNuevo = new Medico(nombre, id, id, especialidad, Rol.MEDICO);

        try {
            // Llamamos al proxy para insertar
            Personal resultado = proxyPersonal.agregarPersonal(medicoNuevo);

            if (resultado != null) {
                JOptionPane.showMessageDialog(null, "Médico guardado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Médico no se pudo guardar (puede que ya exista o haya un error).");
            }

            limpiarMedico();
            actualizarTablaMedicos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el médico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    private void limpiarMedico() {
        vista.getCampoIDMEdico().setText("");
        vista.getCampoNombreMedico().setText("");
        vista.getCampoEspecialidadMEdico().setText("");
        vista.getCampoNombreBusquedaMedico().setText("");
    }

    private void borrarMedico() {
        int fila = vista.getTablaListado().getSelectedRow();
        if (fila >= 0) {
            String id = (String) vista.getTablaListado().getValueAt(fila, 0);
            Personal eliminado = proxyPersonal.eliminarPersonal(Integer.parseInt(id));

            if (eliminado != null)
                JOptionPane.showMessageDialog(null, "Médico eliminado");
            else
                JOptionPane.showMessageDialog(null, "Error al eliminar médico");

            actualizarTablaMedicos();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un médico para borrar");
        }
    }




    private void actualizarTablaMedicos() {
        List<Personal> lista = proxyPersonal.obtenerPersonal()
                .stream()
                .filter(p -> p instanceof Medico)
                .toList();

        DefaultTableModel model = vista.getModeloMedicos();
        model.setRowCount(0);
        lista.forEach(p -> {
            Medico m = (Medico) p;
            model.addRow(new Object[]{m.getId(), m.getNombre(), m.getEspecialidad()});
        });
    }

    private void buscarMedico() {
        String nombre = vista.getCampoNombreBusquedaMedico().getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre");
            return;
        }

        List<Personal> lista = proxyPersonal.obtenerPersonal()
                .stream()
                .filter(p -> p instanceof Medico && p.getNombre().equalsIgnoreCase(nombre))
                .toList();

        DefaultTableModel model = vista.getModeloMedicos();
        model.setRowCount(0);
        lista.forEach(p -> {
            Medico m = (Medico) p;
            model.addRow(new Object[]{m.getId(), m.getNombre(), m.getEspecialidad()});
        });
    }

    private void reporteMedico() {
        int fila = vista.getTablaListado().getSelectedRow();

        if (fila >= 0) {
            // Obtiene el ID del médico seleccionado
            String idStr = (String) vista.getTablaListado().getValueAt(fila, 0);
            int id;

            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Consulta el médico usando el proxy
            Personal p = proxyPersonal.consultarPersonal(id);

            // Verifica y muestra la información
            if (p != null && p instanceof Medico) {
                Medico m = (Medico) p;
                JOptionPane.showMessageDialog(
                        vista,
                        "=== REPORTE DE MÉDICO ===\n" +
                                "ID: " + m.getId() + "\n" +
                                "Nombre: " + m.getNombre() + "\n" +
                                "Especialidad: " + m.getEspecialidad() + "\n" +
                                "Rol: " + m.getRol(),
                        "Reporte de Médico",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(vista, "El elemento seleccionado no es un médico.");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un médico de la tabla para generar reporte.");
        }
    }



    //=============================Logica Farma============================================//
    private void guardarFarma() {
        String idF = vista.getCampoIdFarma().getText();
        String nombreF = vista.getCampoNombreFarma().getText();

        if (idF.isEmpty() || nombreF.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
            return;
        }

        // Crear nuevo farmacéutico
        Farmaceuta farmaNuevo = new Farmaceuta(nombreF, idF, idF, Rol.FARMACEUTICO);

        // Enviar al backend mediante el proxy
        Personal respuesta = proxyPersonal.agregarPersonal(farmaNuevo);

        if (respuesta != null) {
            JOptionPane.showMessageDialog(null, "Farmacéutico guardado correctamente.");
            limpiarFarma();
            actualizarTablaFarma();
        } else {
            JOptionPane.showMessageDialog(null, "Error: No se pudo guardar el farmacéutico.");
        }
    }

    private void limpiarFarma() {
       vista.getCampoIdFarma().setText("");
       vista.getCampoNombreFarma().setText("");
       vista.getCampoBusquedaFarma().setText("");
    }

    private void borrarFarma() {
        int fila = vista.getTablaFarmaceuticos().getSelectedRow();

        if (fila >= 0) {
            String idStr = (String) vista.getTablaFarmaceuticos().getValueAt(fila, 0);
            int id;

            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llamada al proxy (no devuelve boolean)
            proxyPersonal.eliminarPersonal(id);

            // Mensaje y actualización de la vista
            JOptionPane.showMessageDialog(vista, "Farmacéutico eliminado correctamente.");
            actualizarTablaFarma();

        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un farmacéutico para borrar.");
        }
    }


    private void actualizarTablaFarma() {
        DefaultTableModel tableModel = vista.getModeloFarmaceuticos();
        tableModel.setRowCount(0);

        // Traer todos los farmacéuticos desde el backend
        List<Personal> lista = proxyPersonal.obtenerPersonal();
        if (lista != null) {
            for (Personal p : lista) {
                if (p instanceof Farmaceuta) {
                    Farmaceuta f = (Farmaceuta) p;
                    tableModel.addRow(new Object[]{
                            f.getId(),
                            f.getNombre()
                    });
                }
            }
        }
    }


    private void buscarFarma() {
        String nombre = vista.getCampoBusquedaFarma().getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre para buscar.");
            return;
        }

        DefaultTableModel modelo = vista.getModeloFarmaceuticos();
        modelo.setRowCount(0); // limpiar tabla

        List<Personal> lista = proxyPersonal.obtenerPersonal();
        boolean encontrado = false;

        if (lista != null) {
            for (Personal p : lista) {
                if (p instanceof Farmaceuta) {
                    Farmaceuta f = (Farmaceuta) p;
                    if (f.getNombre().equalsIgnoreCase(nombre)) {
                        modelo.addRow(new Object[]{
                                f.getId(),
                                f.getNombre()
                        });
                        encontrado = true;
                    }
                }
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró un farmacéutico con ese nombre.");
        }
    }


    private void reporteFarma() {
        int fila = vista.getTablaFarmaceuticos().getSelectedRow();

        if (fila >= 0) {
            String idStr = (String) vista.getTablaFarmaceuticos().getValueAt(fila, 0);
            int id;

            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Personal p = proxyPersonal.consultarPersonal(id);

            if (p != null && p instanceof Farmaceuta) {
                Farmaceuta f = (Farmaceuta) p;
                JOptionPane.showMessageDialog(
                        vista,
                        "=== REPORTE DE FARMACÉUTICO ===\n" +
                                "ID: " + f.getId() + "\n" +
                                "Nombre: " + f.getNombre() + "\n" +
                                "Rol: " + f.getRol(),
                        "Reporte de Farmacéutico",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(vista, "El elemento seleccionado no es un farmacéutico.");
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un farmacéutico de la tabla para generar reporte.");
        }
    }

//=============================================Logica PAciente============================

    private void guardarPaciente() {
        String id = vista.getCampoIdPAciente().getText().trim();
        String nombre = vista.getCampoNombrePaciente().getText().trim();
        String fechaStr = vista.getCampoFechaNacimiento().getText().trim();
        String telStr = vista.getCampoTelefonoPaciente().getText().trim();

        // Validar campos vacíos
        if (id.isEmpty() || nombre.isEmpty() || fechaStr.isEmpty() || telStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor complete todos los campos.");
            return;
        }

        // Validar formato de fecha
        LocalDate fechaNacimiento;
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fechaNacimiento = LocalDate.parse(fechaStr, formato);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(vista,
                    "La fecha debe tener formato yyyy-MM-dd",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar teléfono
        int telefono;
        try {
            telefono = Integer.parseInt(telStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "El teléfono debe ser un número.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear objeto Paciente
        Paciente paciente = new Paciente(telefono, fechaNacimiento, nombre, id);

        try {
            // Enviar al backend por proxy
            proxyPaciente.agregarPaciente(paciente);

            // Si no lanza excepción, consideramos éxito
            JOptionPane.showMessageDialog(vista, "Paciente guardado correctamente.");
            limpiarPaciente();
            actualizarTablaPaciente();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo guardar el paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void limpiarPaciente() {
    vista.getCampoIdPAciente().setText("");
    vista.getCampoNombrePaciente().setText("");
    vista.getCampoFechaNacimiento().setText("");
    vista.getCampoTelefonoPaciente().setText("");
    }

    // =================== ELIMINAR PACIENTE ===================
    private void borrarPaciente() {
        int fila = vista.getTablaPacientes().getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Seleccione un paciente para eliminar.");
            return;
        }

        try {
            // obtener el id desde la tabla y convertirlo a int
            String idStr = vista.getTablaPacientes().getValueAt(fila, 0).toString();
            int id = Integer.parseInt(idStr);

            // llamar al proxy (no devuelve nada)
            proxyPaciente.eliminarPaciente(id);

            // actualizar interfaz
            JOptionPane.showMessageDialog(vista, "Paciente eliminado correctamente.");
            actualizarTablaPaciente();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El ID del paciente no es un número válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar el paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }



    // =================== ACTUALIZAR TABLA ===================
    private void actualizarTablaPaciente() {
        DefaultTableModel tableModel = vista.getModeloPacientes();
        tableModel.setRowCount(0);

        List<Paciente> pacientes = proxyPaciente.obtenerPacientes();
        if (pacientes != null) {
            for (Paciente p : pacientes) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getFechaNacimiento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        p.getTelefono()
                });
            }
        }
    }



    // =================== BUSCAR PACIENTE ===================
    private void buscarPaciente() {
        String nombre = vista.getCampoBuscarPaciente().getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un nombre para buscar.");
            return;
        }

        DefaultTableModel modelo = vista.getModeloPacientes();
        modelo.setRowCount(0);

        List<Paciente> lista = proxyPaciente.obtenerPacientes();
        boolean encontrado = false;

        if (lista != null) {
            for (Paciente p : lista) {
                if (p.getNombre().equalsIgnoreCase(nombre)) {
                    modelo.addRow(new Object[]{
                            p.getId(),
                            p.getNombre(),
                            p.getFechaNacimiento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            p.getTelefono()
                    });
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(vista, "No se encontró un paciente con ese nombre.");
        }
    }


    //======================================logica medicamento ==============================================//

    // =================== GUARDAR MEDICAMENTO ===================
    private void guardarMedicamento() {
        String idStr = vista.getCampoCodigoMedicamento().getText().trim();
        String nombre = vista.getCampoNombreMedicamento().getText().trim();
        String presentacion = vista.getCampoPresentacionMedicamento().getText().trim();

        if (idStr.isEmpty() || nombre.isEmpty() || presentacion.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor complete todos los campos.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr); // Si el código es numérico
            Medicamento medicamento = new Medicamento(nombre, presentacion, String.valueOf(id));

            proxyMedicamento.agregarMedicamento(medicamento); // No retorna nada

            JOptionPane.showMessageDialog(vista, "Medicamento guardado correctamente.");
            limpiarMedicamento();
            actualizarTablaMedicamentos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                    "El código del medicamento debe ser numérico.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al guardar el medicamento: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }



    private void limpiarMedicamento() {
        vista.getCampoCodigoMedicamento().setText("");
        vista.getCampoNombreMedicamento().setText("");
        vista.getCampoPresentacionMedicamento().setText("");
        vista.getCampoNombreMEdicamento().setText("");
    }

    // =================== ELIMINAR MEDICAMENTO ===================
    private void borrarMedicamento() {
        int fila = vista.getTablaMedicamento().getSelectedRow();

        if (fila >= 0) {
            String idStr = (String) vista.getTablaMedicamento().getValueAt(fila, 0);

            try {
                int id = Integer.parseInt(idStr);
                proxyMedicamento.eliminarMedicamento(id); // No retorna nada

                JOptionPane.showMessageDialog(vista, "Medicamento eliminado correctamente.");
                actualizarTablaMedicamentos();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista,
                        "El código del medicamento debe ser numérico.",
                        "Error de formato",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista,
                        "Error al eliminar el medicamento: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un medicamento para borrar.");
        }
    }




    // =================== ACTUALIZAR TABLA ===================
    private void actualizarTablaMedicamentos() {
        DefaultTableModel tableModel = vista.getModeloMedicamentos();
        tableModel.setRowCount(0);

        List<Medicamento> lista = proxyMedicamento.obtenerMedicamentos();
        if (lista != null) {
            for (Medicamento med : lista) {
                tableModel.addRow(new Object[]{
                        med.getCodigo(),
                        med.getNombre(),
                        med.getPresentacion()
                });
            }
        }
    }


    // =================== BUSCAR MEDICAMENTO ===================
    private void buscarMedicamento() {
        String codigo = vista.getCampoCodigoMedicamento().getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un código o nombre de medicamento para buscar.");
            return;
        }

        DefaultTableModel modelo = vista.getModeloMedicamentos();
        modelo.setRowCount(0);

        List<Medicamento> lista = proxyMedicamento.obtenerMedicamentos();
        boolean encontrado = false;

        if (lista != null) {
            for (Medicamento med : lista) {
                if (med.getNombre().equalsIgnoreCase(codigo) || med.getCodigo().equalsIgnoreCase(codigo)) {
                    modelo.addRow(new Object[]{
                            med.getCodigo(),
                            med.getNombre(),
                            med.getPresentacion()
                    });
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(vista, "No se encontró un medicamento con ese nombre o código.");
        }
    }



    // =================== REPORTE DE MEDICAMENTO ===================
    private void reporteMedicamento() {
        int fila = vista.getTablaMedicamento().getSelectedRow();

        if (fila >= 0) {
            // Obtenemos el nombre desde la tabla (columna 1, según tu tabla)
            String nombre = (String) vista.getTablaMedicamento().getValueAt(fila, 1);

            // Consultamos el medicamento usando su nombre
            Medicamento med = proxyMedicamento.consultarMedicamento(nombre);

            if (med != null) {
                JOptionPane.showMessageDialog(vista,
                        "Código: " + med.getCodigo() + "\n" +
                                "Nombre: " + med.getNombre() + "\n" +
                                "Presentación: " + med.getPresentacion(),
                        "Reporte de Medicamento",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista,
                        "No se encontró el medicamento con nombre " + nombre,
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(vista,
                    "Seleccione un medicamento de la tabla para generar reporte.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }



    //========================================================================
    //Pestana Dasboard


    public void activarDatosDashboard() {
        inicializarComponentesDashboard();
        modificarTablaDashBoard();
        crearGraficoMedicamentos(vista.getGraficoMedicamento());
        crearGraficoRecetas(vista.getGraficoReceta());
    }

    public void configurarEventosDashboard() {
        vista.getBtnGenerarRango().addActionListener(e -> generarRango());
        vista.getBtnAgregarMesButton().addActionListener(e -> agregarMes());
        vista.getBtnQuitarMesButton().addActionListener(e -> quitarMes());
    }

    public void inicializarComponentesDashboard() {
        // Inicializar combobox con años (2020-2030)
        for (int i = 2020; i <= 2030; i++) {
            vista.getCmbDesdeAnnio().addItem(String.valueOf(i));
            vista.getCmbHastaAnio().addItem(String.valueOf(i));
        }

        // Inicializar combobox con meses
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        for (String mes : meses) {
            vista.getCmbDesdeMes().addItem(mes);
            vista.getCmbHastaMes().addItem(mes);
        }

        // ======= Inicializar combobox de medicamentos =======
        List<Medicamento> lista = proxyMedicamento.obtenerMedicamentos();
        if (lista != null) {
            for (Medicamento med : lista) {
                vista.getCmbMedicamento().addItem(med.getNombre());
            }
        }

        // ======= Configurar año y mes actual por defecto =======
        Calendar cal = Calendar.getInstance();
        int añoActual = cal.get(Calendar.YEAR);
        int mesActual = cal.get(Calendar.MONTH); // 0-11

        vista.getCmbDesdeAnnio().setSelectedItem(String.valueOf(añoActual));
        vista.getCmbHastaAnio().setSelectedItem(String.valueOf(añoActual));
        vista.getCmbDesdeMes().setSelectedIndex(mesActual);
        vista.getCmbHastaMes().setSelectedIndex(mesActual);
    }


    public void modificarTablaDashBoard() {
        List<Medicamento> lista = proxyMedicamento.obtenerMedicamentos();
        String[] columnas = {"Nombre", "Presentación", "Código"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        if (lista != null) {
            // Agregar cada medicamento al modelo
            for (Medicamento med : lista) {
                Object[] fila = {
                        med.getNombre(),
                        med.getPresentacion(),
                        med.getCodigo()
                };
                modelo.addRow(fila);
            }
        }

        vista.getTblDatos().setModel(modelo);
        vista.getScrollPaneDashBoard().setViewportView(vista.getTblDatos());
    }


    public void crearGraficoMedicamentos(JPanel panel) {

        List<Medicamento> listaMedicamentos = proxyMedicamento.obtenerMedicamentos();

        // Contar medicamentos por nombre
        Map<String, Integer> conteoPorNombre = new HashMap<>();
        if (listaMedicamentos != null) {
            for (Medicamento med : listaMedicamentos) {
                String nombre = med.getNombre();
                conteoPorNombre.put(nombre, conteoPorNombre.getOrDefault(nombre, 0) + 1);
            }
        }

        // Todo lo demás del gráfico queda igual...
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : conteoPorNombre.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Medicamentos por Nombre",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        chart.setBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 300));

        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }


    public void crearGraficoRecetas(JPanel panel) {

        List<Receta> recetas = proxyReceta.obtenerRecetas();

        Map<String, Integer> conteoPorEstado = new HashMap<>();
        if (recetas != null) {
            for (Receta r1 : recetas) {
                int estadoInt = r1.getEstado();
                String estadoNombre = obtenerNombreEstado(estadoInt);
                conteoPorEstado.put(estadoNombre, conteoPorEstado.getOrDefault(estadoNombre, 0) + 1);
            }
        }


        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : conteoPorEstado.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Recetas por Estado",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        chart.setBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 300));

        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }


    private String obtenerNombreEstado(int estado) {
        switch (estado) {
            case 1:
                return "Confeccionada";
            case 2:
                return "En Proceso";
            case 3:
                return "Lista";
            case 4:
                return "Entregada";
            default:
                return "Desconocido";
        }
    }

    public void generarRango() {
        String desdeAño = (String) vista.getCmbDesdeAnnio().getSelectedItem();
        String desdeMes = (String) vista.getCmbDesdeMes().getSelectedItem();
        String hastaAño = (String) vista.getCmbHastaAnio().getSelectedItem();
        String hastaMes = (String) vista.getCmbHastaMes().getSelectedItem();
        String medicamento = (String) vista.getCmbMedicamento().getSelectedItem();

        // Validar que la fecha desde sea menor o igual que la fecha hasta
        if (!validarRangoFechas(desdeAño, desdeMes, hastaAño, hastaMes)) {
            JOptionPane.showMessageDialog(null,
                    "La fecha 'Desde' debe ser menor o igual que la fecha 'Hasta'",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensaje = String.format(
                "Generando reporte:\nDesde: %s %s\nHasta: %s %s\nMedicamento: %s",
                desdeMes, desdeAño, hastaMes, hastaAño, medicamento
        );

        JOptionPane.showMessageDialog(null, mensaje, "Generar Reporte", JOptionPane.INFORMATION_MESSAGE);

    }

    private boolean validarRangoFechas(String desdeAño, String desdeMes, String hastaAño, String hastaMes) {
        int añoDesde = Integer.parseInt(desdeAño);
        int añoHasta = Integer.parseInt(hastaAño);

        if (añoDesde > añoHasta) {
            return false;
        }

        if (añoDesde == añoHasta) {
            int mesDesde = vista.getCmbDesdeMes().getSelectedIndex();
            int mesHasta = vista.getCmbHastaMes().getSelectedIndex();
            return mesDesde <= mesHasta;
        }

        return true;
    }


    private void agregarMes() {
        // Lógica para agregar un mes a la tabla
        DefaultTableModel model = (DefaultTableModel) vista.getTblDatos().getModel();
        Object[] nuevoMes = {"Nuevo Mes", 0.0, 0.0};
        model.addRow(nuevoMes);

        JOptionPane.showMessageDialog(vista, "Mes agregado a la tabla", "Agregar Mes", JOptionPane.INFORMATION_MESSAGE);
    }

    private void quitarMes() {
        // Lógica para quitar el mes seleccionado de la tabla
        int selectedRow = vista.getTblDatos().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor, seleccione un mes para quitar",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) vista.getTblDatos().getModel();
        model.removeRow(selectedRow);

        JOptionPane.showMessageDialog(vista, "Mes quitado de la tabla", "Quitar Mes", JOptionPane.INFORMATION_MESSAGE);
    }


    public void configurarEventosHistorico() {
        modificarTablaRecetaHistorico();
        modificarTablaMedicamentosHistorico();
        poblarComboMedicos();
        actualizarTablaRecetasPorMedico();
    }

    public void modificarTablaRecetaHistorico() {
        // Ahora obtenemos las recetas desde el proxy
        List<Receta> lista = proxyReceta.obtenerRecetas();

        String[] columnas = {"Receta por Paciente", "Receta por Médico", "Estado de la Receta"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        if (lista != null) {
            for (Receta receta : lista) {
                Object[] fila = {
                        receta.getPaciente().getNombre(),
                        receta.getPersonal().getNombre(),
                        obtenerNombreEstado(receta.getEstado())
                };
                modelo.addRow(fila);
            }
        }

        vista.getTableHistoricoRecetas().setModel(modelo);
        vista.getScrollPaneHistoricoRecetas().setViewportView(vista.getTableHistoricoRecetas());
    }


    public void modificarTablaMedicamentosHistorico() {
        // Obtener medicamentos desde el proxy
        List<Medicamento> lista = proxyMedicamento.obtenerMedicamentos();
        int contador = 1;
        String[] columnas = {"Número", "Nombre", "Presentación", "Código"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (lista != null) {
            for (Medicamento med : lista) {
                Object[] fila = {
                        contador++,
                        med.getNombre(),
                        med.getPresentacion(),
                        med.getCodigo(),
                };
                modelo.addRow(fila);
            }
        }

        JTable tabla = vista.getTableHistoricoMedicamentos();
        tabla.setModel(modelo);
        vista.getScrollPaneHistoricoMedicamentos().setViewportView(tabla);
    }


    private void poblarComboMedicos() {
        // Obtener recetas desde el proxy
        List<Receta> listaRecetas = proxyReceta.obtenerRecetas();
        Set<String> medicosUnicos = new HashSet<>();

        if (listaRecetas != null) {
            for (Receta r : listaRecetas) {
                medicosUnicos.add(r.getPersonal().getNombre());
            }
        }

        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
        for (String nombreMedico : medicosUnicos) {
            modeloCombo.addElement(nombreMedico);
        }

        vista.getCmbBuscarRecetasHistorico().setModel(modeloCombo);
    }


    public void actualizarTablaRecetasPorMedico() {
        vista.getCmbBuscarRecetasHistorico().addActionListener(e -> {
            String nombreSeleccionado = vista.getCmbBuscarRecetasHistorico()
                    .getSelectedItem()
                    .toString()
                    .toLowerCase();

            DefaultTableModel modelo = (DefaultTableModel) vista.getTableHistoricoRecetas().getModel();
            modelo.setRowCount(0); // Limpiar tabla

            List<Receta> recetas = proxyReceta.obtenerRecetas();
            if (recetas != null) {
                for (Receta r : recetas) {
                    String nombreMedico = r.getPersonal().getNombre().toLowerCase();
                    if (nombreMedico.equals(nombreSeleccionado)) {
                        modelo.addRow(new Object[]{
                                r.getPaciente().getNombre(),
                                r.getPersonal().getNombre(),
                                obtenerNombreEstado(r.getEstado())
                        });
                    }
                }
            }
        });
    }




}
