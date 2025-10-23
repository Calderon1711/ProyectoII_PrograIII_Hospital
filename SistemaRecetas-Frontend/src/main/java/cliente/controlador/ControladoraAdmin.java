package Controlador;

import Modelo.*;
import Vista.AdminVista;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

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
    private AdminVista vista;
    private Personal modelo;
    private static Hospital hospi=Hospital.getInstance();

    public ControladoraAdmin(AdminVista vista, Personal modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // ======= Asignar acciones a los botones =======
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

        vista.getBotonBuscar().addActionListener(this);
        vista.getBtnGuardarPaciente().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getBorrarPAciente().addActionListener(this);

        actualizarTablaMedicos();
        actualizarTablaFarma();
        actualizarTablaPaciente();
        actualizarTablaMedicamentos();
        initController();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // ==== Médicos ====
        if (source == vista.getBotonGuardar()) {
            guardarMedico();
            hospi.guradarPersonal();
        } else if (source == vista.getBotonLimpiar()) {
            limpiarMedico();
        } else if (source == vista.getBotonBorrar()) {
            borrarMedico();
            hospi.guradarPersonal();
        } else if (source == vista.getBuscarBoton()) {
            buscarMedico();
        } else if (source == vista.getBotonReporte()) {
            reporteMedico();
            actualizarTablaMedicos();
        }

        // ==== Farmacéuticos ====
        else if (source == vista.getGuardarButton()) {
            guardarFarma();
            hospi.guradarPersonal();
        } else if (source == vista.getLimpiarButton()) {
            limpiarFarma();
        } else if (source == vista.getBorrarButton()) {
            borrarFarma();
            hospi.guradarPersonal();
        } else if (source == vista.getBuscarButton()) {
            buscarFarma();
        } else if (source == vista.getReporteButton()) {
            reporteFarma();
            actualizarTablaMedicos();
        }

        // ==== Pacientes ====
          else if (source == vista.getBtnGuardarPaciente()) {
            guardarPaciente();
            hospi.guardarPacientes();
        } else if (source == vista.getBtnLimpiarPaciente()) {
            limpiarPaciente();
        } else if (source == vista.getBorrarPAciente()) {
            borrarPaciente();
            hospi.guardarPacientes();
        } else if (source == vista.getBotonBuscarPaciente()) {
            buscarPaciente();
        }

             // ==== Medicamentos ====
        else if (source == vista.getBtnGuardarMedicamento()) {
            guardarMedicamento();
            hospi.guardarMedicamentos();
        } else if (source == vista.getBoton_LimpiarMedicamento()) {
            limpiarMedicamento();
        } else if (source == vista.getBTNBorrarMedicamento()) {
            borrarMedicamento();
            hospi.guardarMedicamentos();
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

        Medico medicoNuevo = new Medico(nombre,id,id,especialidad,Rol.MEDICO);
        boolean verificador = hospi.getPersonal().existePersonalConEseID(medicoNuevo.getId());
        if( hospi.getPersonal().insertarPersonal(medicoNuevo,verificador)==true) {
            JOptionPane.showMessageDialog(null, "Médico guardado correctamente.");
        }else{
            JOptionPane.showMessageDialog(null, "Médico No se puede guardar.");
        }

        limpiarMedico();
        actualizarTablaMedicos();
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
            hospi.getPersonal().eliminar(id);
            actualizarTablaMedicos();
            JOptionPane.showMessageDialog(null, "Médico eliminado.");
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un médico para borrar.");
        }
    }

    private void actualizarTablaMedicos() {
        DefaultTableModel tableModel = vista.getModeloMedicos();
        tableModel.setRowCount(0);

        // Traemos solo los médicos de la lista general
        for (Personal p : hospi.getPersonal().obtenerPersonalPorTipo("Medico")) {
            if (p instanceof Medico) {
                Medico m = (Medico) p;
                tableModel.addRow(new Object[]{
                        m.getId(),
                        m.getNombre(),
                        m.getEspecialidad()
                });
            }
        }
    }

    private void buscarMedico() {
        String nombre = vista.getCampoNombreBusquedaMedico().getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre para buscar.");
            return;
        }

        DefaultTableModel modelo = vista.getModeloMedicos();
        modelo.setRowCount(0); // limpiar tabla

        for (Personal p : hospi.getPersonal().obtenerPersonalPorTipo("Medico")) {
            if (p instanceof Medico) {
                Medico m = (Medico) p;
                if (m.getNombre().equalsIgnoreCase(nombre)) {
                    modelo.addRow(new Object[]{
                            m.getId(),
                            m.getNombre(),
                            m.getEspecialidad()
                    });
                }
            }
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No se encontró un médico con ese nombre.");
        }
    }

    private void reporteMedico() {
        int fila = vista.getTablaListado().getSelectedRow();
        if (fila >= 0) {
            String id = (String) vista.getTablaListado().getValueAt(fila, 0);
            Personal p = hospi.getPersonal().getPersonalPorID(id);

            if (p != null && p instanceof Medico) {
                JOptionPane.showMessageDialog(null, p.toString());
            } else {
                JOptionPane.showMessageDialog(null, "El elemento seleccionado no es un médico.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un médico de la tabla para generar reporte.");
        }
    }

    //=============================Logica Farma============================================//
    private void guardarFarma() {
        String idF = vista.getCampoIdFarma().getText();
        String nombreF = vista.getCampoNombreFarma().getText();


        if (idF.isEmpty() || nombreF.isEmpty() ) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
            return;
        }

        Farmaceuta farmaNuevo = new Farmaceuta(nombreF,idF,idF,Rol.FARMACEUTICO);
        boolean verificador = hospi.getPersonal().existePersonalConEseID(farmaNuevo.getId());
        if( hospi.getPersonal().insertarPersonal(farmaNuevo,verificador)==true) {
            JOptionPane.showMessageDialog(null, "Farmaceuta guardado correctamente.");
        }else{
            JOptionPane.showMessageDialog(null, "Farmaceuta No se puede guardar.");
        }

        limpiarFarma();
        actualizarTablaFarma();
    }

    private void limpiarFarma() {
       vista.getCampoIdFarma().setText("");
       vista.getCampoNombreFarma().setText("");
       vista.getCampoBusquedaFarma().setText("");
    }

    private void borrarFarma() {
        int fila = vista.getTablaFarmaceuticos().getSelectedRow();
        if (fila >= 0) {
            String id = (String) vista.getTablaFarmaceuticos().getValueAt(fila, 0);
            hospi.getPersonal().eliminar(id);
            actualizarTablaFarma();
            JOptionPane.showMessageDialog(null, "Farmacéutico eliminado.");
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un Farmacéutico para borrar.");
        }
    }

    private void  actualizarTablaFarma() {
        DefaultTableModel tableModel = vista.getModeloFarmaceuticos();
        tableModel.setRowCount(0);

        // Traemos solo los médicos de la lista general
        for (Personal p : hospi.getPersonal().obtenerPersonalPorTipo("Farmaceuta")) {
            if (p instanceof Farmaceuta) {
                Farmaceuta f = (Farmaceuta) p;
                tableModel.addRow(new Object[]{
                        f.getId(),
                        f.getNombre()
                });
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

        for (Personal p : hospi.getPersonal().obtenerPersonalPorTipo("Farmaceuta")) {
            if (p instanceof Farmaceuta) {
                Farmaceuta f = (Farmaceuta) p;
                if (f.getNombre().equalsIgnoreCase(nombre)) {
                    modelo.addRow(new Object[]{
                            f.getId(),
                            f.getNombre()
                    });
                }
            }
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No se encontró un farmacéutico con ese nombre.");
        }
    }

    private void reporteFarma() {
        int fila = vista.getTablaFarmaceuticos().getSelectedRow();
        if (fila >= 0) {
            String id = (String) vista.getTablaFarmaceuticos().getValueAt(fila, 0);
            Personal p = hospi.getPersonal().getPersonalPorID(id);

            if (p != null && p instanceof Farmaceuta) {
                JOptionPane.showMessageDialog(null, p.toString());
            } else {
                JOptionPane.showMessageDialog(null, "El elemento seleccionado no es un Farmaceuta.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un Farmaceuta de la tabla para generar reporte.");
        }
    }
//=============================================Logica PAciente============================

    private void guardarPaciente() {
        String id = vista.getCampoIdPAciente().getText();
        String nombre = vista.getCampoNombrePaciente().getText();
        String fechaStr = vista.getCampoFechaNacimiento().getText();
        LocalDate fechaNacimiento = null;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            fechaNacimiento = LocalDate.parse(fechaStr, formato);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null,
                    "La fecha debe tener formato yyyy-MM-dd",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return; // salir o manejar error
        }

        String telStr = vista.getCampoTelefonoPaciente().getText();
        int telefono = 0;
        try {
            telefono = Integer.parseInt(telStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El teléfono debe ser un número",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return; // salir o manejar error
        }


        if (id.isEmpty() || nombre.isEmpty() || fechaNacimiento==null||telefono==0) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
            return;
        }

        Paciente paciente = new Paciente(telefono,fechaNacimiento,nombre,id);
        boolean verificador = hospi.getPacientes().existeAlguienConEseID(paciente.getId());
        if( hospi.getPacientes().insertarPaciente(paciente,verificador)==true) {
            JOptionPane.showMessageDialog(null, "Paciente guardado correctamente.");
        }else{
            JOptionPane.showMessageDialog(null, "Paciente No se puede guardar.");
        }

        limpiarPaciente();
        actualizarTablaPaciente();
    }

    private void limpiarPaciente() {
    vista.getCampoIdPAciente().setText("");
    vista.getCampoNombrePaciente().setText("");
    vista.getCampoFechaNacimiento().setText("");
    vista.getCampoTelefonoPaciente().setText("");
    }

    private void borrarPaciente() {
        int fila = vista.getTablaPacientes().getSelectedRow();
        if (fila >= 0) {
            String id = (String) vista.getTablaPacientes().getValueAt(fila, 0);
            hospi.getPacientes().eliminar(id);
            actualizarTablaPaciente();
            JOptionPane.showMessageDialog(null, "Paciente eliminado.");
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un paciente para borrar.");
        }
    }

    private void actualizarTablaPaciente() {
        DefaultTableModel tableModel = vista.getModeloPacientes();
        tableModel.setRowCount(0); // Limpiamos la tabla

        // Recorremos la lista de pacientes
        for (Paciente p : hospi.getPacientes().getPacientes()) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getFechaNacimiento(),
                    p.getTelefono()
            });
        }
    }


    private void buscarPaciente() {
        String nombre = vista.getCampoBuscarPaciente().getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre para buscar.");
            return;
        }

        DefaultTableModel modelo = vista.getModeloPacientes();
        modelo.setRowCount(0); // limpiar tabla

        boolean encontrado = false;

        for (Paciente p : hospi.getPacientes().getPacientes()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        p.getTelefono(),
                });
                encontrado = true;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró un paciente con ese nombre.");
        }
    }

    //======================================logica medicamento ==============================================//

    private void guardarMedicamento() {
        String id = vista.getCampoCodigoMedicamento().getText();
        String nombre = vista.getCampoNombreMedicamento().getText();
        String presentacion = vista.getCampoPresentacionMedicamento().getText();

        if (id.isEmpty() || nombre.isEmpty() || presentacion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
            return;
        }

       Medicamento medicamento = new Medicamento(nombre, presentacion, id);

        if( hospi.getMedicamentos().insertarMedicamento(medicamento)==true) {
            JOptionPane.showMessageDialog(null, "Medicamento guardado correctamente.");
        }else{
            JOptionPane.showMessageDialog(null, "Medicamento No se puede guardar.");
        }

        limpiarMedicamento();
        actualizarTablaMedicamentos();
    }

    private void limpiarMedicamento() {
        vista.getCampoCodigoMedicamento().setText("");
        vista.getCampoNombreMedicamento().setText("");
        vista.getCampoPresentacionMedicamento().setText("");
        vista.getCampoNombreMEdicamento().setText("");
    }

    private void borrarMedicamento() {
        int fila = vista.getTablaMedicamento().getSelectedRow();
        if (fila >= 0) {
            String id = (String) vista.getTablaMedicamento().getValueAt(fila, 0);
            hospi.getMedicamentos().eliminar(id);
            actualizarTablaMedicamentos();
            JOptionPane.showMessageDialog(null, "Medicamento eliminado.");
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un Medicamento para borrar.");
        }
    }

    private void actualizarTablaMedicamentos() {
        DefaultTableModel tableModel = vista.getModeloMedicamentos();
        tableModel.setRowCount(0); // limpiar tabla

        // Recorremos la lista de medicamentos del hospital
        for (Medicamento med : hospi.getMedicamentos().getMedicamentos()) {
            tableModel.addRow(new Object[]{
                    med.getCodigo(),
                    med.getNombre(),
                    med.getPresentacion()
            });
        }
    }

    private void buscarMedicamento() {
        String codigo = vista.getCampoCodigoMedicamento().getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre de medicamento para buscar.");
            return;
        }

        DefaultTableModel modelo = vista.getModeloMedicamentos();
        modelo.setRowCount(0); // limpiar tabla

        boolean encontrado = false;

        for (Medicamento med : hospi.getMedicamentos().getMedicamentos()) {
            if (med.getNombre().equalsIgnoreCase(codigo)) {
                modelo.addRow(new Object[]{
                        med.getCodigo(),
                        med.getNombre(),
                        med.getPresentacion()
                });
                encontrado = true;
            }
        }
        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró un medicamento con ese nombre.");
        }
    }


    private void reporteMedicamento() {
        int fila = vista.getTablaMedicamento().getSelectedRow();
        if (fila >= 0) {
            String id = (String) vista.getTablaMedicamento().getValueAt(fila, 0);
            Medicamento med = hospi.getMedicamentos().getMedicamento(id);

            if (med != null) {
                JOptionPane.showMessageDialog(null, med.toString(),
                        "Reporte de Medicamento",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el medicamento con ID " + id);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un medicamento de la tabla para generar reporte.");
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

        // Inicializar combobox de medicamentos

        List<Medicamento> lista = hospi.getMedicamentos().getMedicamentos();

        for (Medicamento med : lista) {
            vista.getCmbMedicamento().addItem(med.getNombre());
        }

        // Configurar año y mes actual por defecto
        Calendar cal = Calendar.getInstance();
        int añoActual = cal.get(Calendar.YEAR);
        int mesActual = cal.get(Calendar.MONTH); // 0-11

        vista.getCmbDesdeAnnio().setSelectedItem(String.valueOf(añoActual));
        vista.getCmbHastaAnio().setSelectedItem(String.valueOf(añoActual));
        vista.getCmbDesdeMes().setSelectedIndex(mesActual);
        vista.getCmbHastaMes().setSelectedIndex(mesActual);
    }

    public void modificarTablaDashBoard() {
        List<Medicamento> lista = hospi.getMedicamentos().getMedicamentos();
        String[] columnas = {"Nombre", "Presentacion", "Codigo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Agregar cada medicamento al modelo
        for (Medicamento med : lista) {
            Object[] fila = {
                    med.getNombre(),
                    med.getPresentacion(),
                    med.getCodigo()
            };
            modelo.addRow(fila);
        }

        vista.getTblDatos().setModel(modelo);
        vista.getScrollPaneDashBoard().setViewportView(vista.getTblDatos());
    }

    public void crearGraficoMedicamentos(JPanel panel) {
        List<Medicamento> listaMedicamentos = hospi.getMedicamentos().getMedicamentos();

        // Contar medicamentos por nombre
        Map<String, Integer> conteoPorNombre = new HashMap<>();
        for (Medicamento med : listaMedicamentos) {
            String nombre = med.getNombre();
            conteoPorNombre.put(nombre, conteoPorNombre.getOrDefault(nombre, 0) + 1);
        }

        // Crear dataset para gráfico de pastel
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : conteoPorNombre.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Crear gráfico de pastel
        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Medicamentos por Nombre",
                dataset,
                true,
                true,
                false
        );

        // Mostrar porcentaje en etiquetas
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}")); // Ej: Paracetamol: 25%

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
        List<Receta> recetas = hospi.getRecetas().getRecetas();

        // Contar recetas por estado (usando nombre legible)
        Map<String, Integer> conteoPorEstado = new HashMap<>();
        for (Receta r1 : recetas) {
            int estadoInt = r1.getEstado(); // Suponiendo que esto devuelve el int
            String estadoNombre = obtenerNombreEstado(estadoInt);
            conteoPorEstado.put(estadoNombre, conteoPorEstado.getOrDefault(estadoNombre, 0) + 1);
        }

        // Crear dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : conteoPorEstado.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Crear gráfico
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
        List<Receta> lista = hospi.getRecetas().getRecetas();
        String[] columnas = {"Receta por paciente", "Receta por Medico", "Estado de la receta"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Agregar cada medicamento al modelo
        for (Receta med : lista) {
            Object[] fila = {

                    med.getPaciente(),
                    med.getPersonal(),
                    obtenerNombreEstado(med.getEstado())
            };
            modelo.addRow(fila);
        }

        // Asignar modelo a la tabla
        vista.getTableHistoricoRecetas().setModel(modelo);
        vista.getScrollPaneHistoricoRecetas().setViewportView(vista.getTableHistoricoRecetas());
    }

    public void modificarTablaMedicamentosHistorico() {
        List<Medicamento> lista = hospi.getMedicamentos().getMedicamentos();
        int contador = 1;
        String[] columnas = {"Numero", "Nombre", "Presentación", "Codigo"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Medicamento med : lista) {
            Object[] fila = {
                    contador,
                    med.getNombre(),
                    med.getPresentacion(),
                    med.getCodigo(),
            };

            modelo.addRow(fila);
            contador++;
        }

        JTable tabla = vista.getTableHistoricoMedicamentos();
        tabla.setModel(modelo);
        vista.getScrollPaneHistoricoMedicamentos().setViewportView(tabla);
    }

    private void poblarComboMedicos() {
        List<Receta> listaRecetas = hospi.getRecetas().getRecetas();
        Set<Personal> medicosUnicos = new HashSet<>();
        for (Receta r : listaRecetas) {
            medicosUnicos.add(r.getPersonal());
        }

        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
        for (Personal medico : medicosUnicos) {
            modeloCombo.addElement(medico.getNombre());
        }

        vista.getCmbBuscarRecetasHistorico().setModel(modeloCombo);
    }

    public void actualizarTablaRecetasPorMedico() {
        vista.getCmbBuscarRecetasHistorico().addActionListener(e -> {
            String nombreSeleccionado = vista.getCmbBuscarRecetasHistorico().getSelectedItem().toString().toLowerCase();
            DefaultTableModel modelo = (DefaultTableModel) vista.getTableHistoricoRecetas().getModel();
            modelo.setRowCount(0); // Limpiar tabla

            for (Receta r : hospi.getRecetas().getRecetas()) {
                String nombreMedico = r.getPersonal().getNombre().toLowerCase();
                if (nombreMedico.equals(nombreSeleccionado)) {
                    modelo.addRow(new Object[]{
                            r.getPaciente().getNombre(),
                            r.getPersonal().getNombre(),
                            r.getEstado()
                    });
                }
            }
        });
    }



}
