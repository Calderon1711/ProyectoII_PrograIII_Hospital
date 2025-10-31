package cliente.controlador;

import cliente.Vista.*;
import cliente.modelo.*;
import cliente.util.Alerta;
import cliente.util.ConfiguracionCliente;
import javafx.collections.ObservableList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import cliente.proxy.*;


public class ControladorMedico extends JFrame {

    //Vista
    private final MedicoVista vista;
    private final Personal usuarioActivo;
    //Proxys
    private final ProxyPersonal proxyPersonal;
    private final ProxyMedicamento proxyMedicamento;
    private final ProxyReceta proxyReceta;
    private final ProxyDetalleMedicamento proxyDetalleMedicamento;
    private final ProxyPaciente proxyPaciente;

    //Controladores
    private ControladoraBuscarPaciente controladoraBuscarPaciente;
    private ControladorDetalleMedicamento controladorDetalleMedicamento;
    private DefaultTableModel modeloTablaMedicamentos;

    // Datos cacheados
    private ObservableList<Personal> personal;
    private ObservableList<Medicamento> medicamentos;
    private ObservableList<Receta> recetas;
    private ObservableList<DetalleMedicamento> detalleMedicamentos;
    private ObservableList<Medico> medicos;

    // Control de tiempo de última actualización
    private long ultimaActualizacionPersonal = 0;
    private long ultimaActualizacionRecetas = 0;
    private long ultimaActualizacionDetalles = 0;
    private long ultimaActualizacionMedicamentos = 0;
    private long ultimaActualizacionMedicos = 0;

    //  Tiempo máximo de validez (en milisegundos)
    private static final long TIEMPO_CACHE_MS = ConfiguracionCliente.getTimedeCache(); // 5 mins


    //Para controlador general
    public ControladorMedico(MedicoVista medicoVista, Personal usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        if(medicoVista == null) {
            medicoVista= new MedicoVista(usuarioActivo);
        }
        this.vista = medicoVista;
        this.proxyPersonal = new ProxyPersonal();
        this.proxyMedicamento= new ProxyMedicamento();
        this.proxyReceta = new ProxyReceta();
        this.proxyDetalleMedicamento = new ProxyDetalleMedicamento();
        this.proxyPaciente = new ProxyPaciente();
        initController();
    }

    public void initController() {

        try {

            //Metodo general para llamar a los datos
            cargarDatosIniciales();

            //Pestana preeescribir
            configurarEventosPreescribir();
            inicializarTablaMedicamentosPreescribir();
            comboFechas();
            sincronizarComboConTextField();

            //Pestana Dashboard
            activarDatosDashboard();
            configurarEventosDashboard();
            modificarTablaDashBoard();

            //Pestana Historico
            configurarEventosHistorico();

            centrarComponentes();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // =========================================================================
    // MÉTODOS DE GESTIÓN DE DATOS Y CACHÉ
    // =========================================================================

    private ObservableList<Personal> getPersonal() {
        long ahora = System.currentTimeMillis();

        if (personal == null || (ahora - ultimaActualizacionPersonal) > TIEMPO_CACHE_MS) {
            personal = proxyPersonal.obtenerPersonal();
            ultimaActualizacionPersonal = ahora;
            Alerta.info("Medico","[INFO] Personal actualizado desde el backend.");
        } else {
            Alerta.info("Medico","[INFO] Usando personal en caché.");
        }

        return personal;
    }

    private ObservableList<Receta> getRecetas() {
        long ahora = System.currentTimeMillis();

        if (recetas == null || (ahora - ultimaActualizacionRecetas) > TIEMPO_CACHE_MS) {
            recetas = proxyReceta.obtenerRecetas();
            ultimaActualizacionRecetas = ahora;
            Alerta.info("Medico","[INFO] Recetas actualizadas desde el backend.");
        } else {
            Alerta.info("Medico","[INFO] Usando recetas en caché.");
        }

        return recetas;
    }

    private ObservableList<DetalleMedicamento> getDetalles() {
        long ahora = System.currentTimeMillis();

        if (detalleMedicamentos == null || (ahora - ultimaActualizacionDetalles) > TIEMPO_CACHE_MS) {
            detalleMedicamentos = proxyDetalleMedicamento.obtenerDetalleMedicamentos();
            ultimaActualizacionDetalles = ahora;
            Alerta.info("Medico","[INFO] Detalles medicamento actualizados desde el backend.");
        } else {
            Alerta.info("Medico","[INFO] Usando detalles medicamento en caché.");
        }

        return detalleMedicamentos;
    }

    private ObservableList<Medicamento> getMedicamentos() {
        long ahora = System.currentTimeMillis();

        if (medicamentos == null || (ahora - ultimaActualizacionMedicamentos) > TIEMPO_CACHE_MS) {
            medicamentos = proxyMedicamento.obtenerMedicamentos();
            ultimaActualizacionMedicamentos = ahora;
            Alerta.info("Medico","[INFO] Medicamentos actualizados desde el backend.");
        } else {
            Alerta.info("Medico","[INFO] Usando medicamentos en caché.");
        }

        return medicamentos;
    }

    private ObservableList<Medico> getMedicos() {
        long ahora = System.currentTimeMillis();

        if (medicos == null || (ahora - ultimaActualizacionMedicos) > TIEMPO_CACHE_MS) {
            medicos = proxyPersonal.obtenerMedicos();
            ultimaActualizacionMedicos = ahora;
            Alerta.info("Medico","[INFO] Médicos actualizados desde el backend.");
        } else {
            Alerta.info("Medico","[INFO] Usando médicos en caché.");
        }

        return medicos;
    }

    private void cargarDatosIniciales() {
        getPersonal();
        getRecetas();
        getDetalles();
        getMedicamentos();
        getMedicos();
    }



    //=============================================================
    //Pestana preescribir

    private void configurarEventosPreescribir() {

        //Agregar Paciente
        vista.getBuscarPacienteButton().addActionListener(e -> buscarPaciente());
        //Agregar Medicamento
        vista.getAgregarMedicamentoButton().addActionListener(e -> agregarMedicamento());
        //Agregar Receta
        vista.getGuardarButton().addActionListener(e -> guardarReceta());

        //===============================================
        vista.getLimpiarButton().addActionListener(e -> limpiarCampos());
        vista.getDescartarMedicamentoButton().addActionListener(e -> descartarMedicamento());
        vista.getDetallesButton().addActionListener(e -> mostrarDetalles());

    }

    private void buscarPaciente() {
        if (controladoraBuscarPaciente == null) {
            controladoraBuscarPaciente = new ControladoraBuscarPaciente();
        }
        controladoraBuscarPaciente.mostrarVentana();
    }

    public Personal obtenerMedicoAleatorio() {
        ObservableList<Personal> listaPersonal = getPersonal();
        if (listaPersonal == null || listaPersonal.isEmpty()) return null;

        List<Personal> medicos = new ArrayList<>();
        for (Personal p : listaPersonal) {
            if (p.getRol() == Rol.MEDICO) {
                medicos.add(p);
            }
        }

        if (medicos.isEmpty()) return null;
        return medicos.get(new Random().nextInt(medicos.size()));
    }


    public void comboFechas() {
        JComboBox<String> comboFechas = vista.getOpciones_Fecha_de_Retiro();
        comboFechas.removeAllItems();

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i <= 5; i++) {
            LocalDate fecha = hoy.plusDays(i);
            String diaSemana = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            String texto = fecha.format(formato) + " (" + diaSemana + ")";
            comboFechas.addItem(texto);
        }
    }


    public void sincronizarComboConTextField() {
        JComboBox<String> comboFechas = vista.getOpciones_Fecha_de_Retiro();
        JLabel campoFecha = vista.getFechaRetiro();
        vista.getFechaRetiro().setText(" "); // Espacio en blanco
        vista.getFechaRetiro().setBorder(BorderFactory.createLineBorder(Color.BLUE));

        comboFechas.addActionListener(e -> {
            String seleccion = (String) comboFechas.getSelectedItem();
            campoFecha.setText(seleccion);
        });
    }



    public void inicializarTablaMedicamentosPreescribir() {
        String[] columnas = {"Medicamento", "Presentación", "Cantidad", "Indicaciones", "Duración (días)"};
        modeloTablaMedicamentos = new DefaultTableModel(columnas, 0);
        vista.getTablaMedicamentos().setModel(modeloTablaMedicamentos);
        vista.getScrollPaneMedicamentos().setViewportView(vista.getTablaMedicamentos());
    }

    private void agregarMedicamento() {
        ControladorDetalleMedicamento ctrlDet = new ControladorDetalleMedicamento();

        ctrlDet.getModificarDetalleVista().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {

                DetalleMedicamento nuevo = ctrlDet.getDetalleGuardado();
                Medicamento med = ctrlDet.getMedicamentoGuardado();


                if (nuevo != null && med != null) {
                    proxyMedicamento.agregarMedicamento(med);
                    proxyDetalleMedicamento.agregarDetalleMedicamento(nuevo);

                    modificarTablaDashBoard();// Ahora sí, después de llenar
                    agregarMedicamentoATabla(nuevo);
                    modificarTablaMedicamentosHistorico();
                    crearGraficoMedicamentos(vista.getGraficoMedicamento());
                    mostrarDetalles();
                }
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                windowClosed(e);
            }
        });

        ctrlDet.mostrarVentana();
    }
    public void agregarMedicamentoATabla(DetalleMedicamento detalle) {
        if (detalle != null && detalle.getMedicamento() != null && modeloTablaMedicamentos != null) {
            modeloTablaMedicamentos.addRow(new Object[]{
                    detalle.getMedicamento().getNombre(),
                    detalle.getMedicamento().getPresentacion(),
                    detalle.getCantidad(),
                    detalle.getIndicacion(),
                    detalle.getDuracion()
            });
        }
    }

    private void guardarReceta() {
        try {
            Paciente paciente = (controladoraBuscarPaciente != null)
                    ? controladoraBuscarPaciente.getPacienteSeleccionado()
                    : null;
            if (paciente == null) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un paciente.");
                return;
            }

            int filaSeleccionada = vista.getTablaMedicamentos().getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un medicamento en la tabla.");
                return;
            }

            String nombre = modeloTablaMedicamentos.getValueAt(filaSeleccionada, 0).toString();
            String presentacion = modeloTablaMedicamentos.getValueAt(filaSeleccionada, 1).toString();
            int cantidad = Integer.parseInt(modeloTablaMedicamentos.getValueAt(filaSeleccionada, 2).toString());
            String indicaciones = modeloTablaMedicamentos.getValueAt(filaSeleccionada, 3).toString();
            int dias = Integer.parseInt(modeloTablaMedicamentos.getValueAt(filaSeleccionada, 4).toString());

            Medicamento med = proxyMedicamento.consultarMedicamento(nombre);

            if (med == null) {
                System.out.println(med.toString());
                JOptionPane.showMessageDialog(vista, "El medicamento seleccionado no existe en el sistema.");
                med.setPresentacion(presentacion);
                return;
            }

            DetalleMedicamento detalle = new DetalleMedicamento();
            int idDetalle = getDetalles().size() + 1;
            detalle.setMedicamento(med);
            detalle.setIdDetalle(String.valueOf(++idDetalle));
            detalle.setCantidad(cantidad);
            detalle.setIndicacion(indicaciones);
            detalle.setDuracion(dias);

            List<DetalleMedicamento> detalles = new ArrayList<>();
            detalles.add(detalle);

            String idReceta = "R" + (getRecetas().size() + 1);
            LocalDate hoy = LocalDate.now();

            String texto = vista.getFechaRetiro().getText();
            LocalDate fechaRetiro;
            if (texto != null && texto.length() >= 10) {
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fechaRetiro = LocalDate.parse(texto.substring(0, 10), formato);
            } else {
                JOptionPane.showMessageDialog(vista, "La fecha de retiro no está definida correctamente.");
                return;
            }

            Personal medico= obtenerMedicoAleatorio();

            Receta receta = new Receta(idReceta,medico, paciente, hoy, fechaRetiro, 2);
            receta.getDetalleMedicamentos().addAll(detalles);

            proxyReceta.agregarReceta(receta);

            modificarTablaRecetaHistorico();
            crearGraficoRecetas(vista.getGraficoReceta());
            JOptionPane.showMessageDialog(vista, "Receta " + receta.getId() + " guardada con éxito.");

            modeloTablaMedicamentos.setRowCount(0);
            vista.getFechaRetiro().setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al guardar receta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        String texto = vista.getFechaRetiro().getText();

        if (texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay nada que borrar", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            vista.getFechaRetiro().setText(" ");
            JOptionPane.showMessageDialog(null, "Fecha de retiro limpio", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void descartarMedicamento() {
        int fila = vista.getTablaMedicamentos().getSelectedRow();
        if (fila != -1) {
            DefaultTableModel modelo = (DefaultTableModel) vista.getTablaMedicamentos().getModel();
            modelo.removeRow(fila);
        } else {
            JOptionPane.showMessageDialog(null, "No hay medicamento seleccionado", "Descartar Medicamento", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarDetalles() {
        StringBuilder mensaje = new StringBuilder();

        for (Receta receta : getRecetas()) {
            List<DetalleMedicamento> detalles = receta.getDetalleMedicamentos();

            if (detalles != null && !detalles.isEmpty()) {
                mensaje.append("Receta de: ").append(receta.getPaciente().getNombre()).append("\n");
                for (DetalleMedicamento m : detalles) {
                    mensaje.append(" - Medicamento: ").append(m.getMedicamento().getNombre())
                            .append(", Presentación: ").append(m.getMedicamento().getPresentacion())
                            .append(" días\n");
                }
                mensaje.append("\n");
            }
        }

        if (mensaje.length() > 0) {
            JOptionPane.showMessageDialog(null, mensaje.toString(), "Detalles de Medicamentos", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No hay medicamentos registrados en las recetas.", "Detalles", JOptionPane.WARNING_MESSAGE);
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


        for (Medicamento med : getMedicamentos()) {
            vista.getCmbMedicamento().addItem(med.getNombre());
        }

        // Configurar año y mes actual por defecto
        Calendar cal = Calendar.getInstance();
        int annioActual = cal.get(Calendar.YEAR);
        int mesActual = cal.get(Calendar.MONTH); // 0-11

        vista.getCmbDesdeAnnio().setSelectedItem(String.valueOf(annioActual));
        vista.getCmbHastaAnio().setSelectedItem(String.valueOf(annioActual));
        vista.getCmbDesdeMes().setSelectedIndex(mesActual);
        vista.getCmbHastaMes().setSelectedIndex(mesActual);
    }

    public void modificarTablaDashBoard() {
        String[] columnas = {"Nombre", "Presentacion", "Codigo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Agregar cada medicamento al modelo
        for (Medicamento med : getMedicamentos()) {
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

        // Contar medicamentos por nombre
        Map<String, Integer> conteoPorNombre = new HashMap<>();
        for (Medicamento med : getMedicamentos()) {
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

        // Contar recetas por estado (usando nombre legible)
        Map<String, Integer> conteoPorEstado = new HashMap<>();
        for (Receta r1 : getRecetas()) {
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


    //==============================================================
    //Pestana Historico


    public void configurarEventosHistorico() {
        modificarTablaRecetaHistorico();
        modificarTablaMedicamentosHistorico();
        poblarComboMedicos();
        actualizarTablaRecetasPorMedico();
    }

    public void modificarTablaRecetaHistorico() {
        String[] columnas = {"Receta por paciente", "Receta por Medico", "Estado de la receta"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Agregar cada medicamento al modelo
        for (Receta med : getRecetas()) {
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
        int contador = 1;
        String[] columnas = {"Numero", "Nombre", "Presentación", "Codigo"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Medicamento med : getMedicamentos()) {
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

        Set<Medico> medicosUnicos;
        medicosUnicos = new HashSet<>();
        for (Medico r : getMedicos()) {
            medicosUnicos.add(r);
        }

        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
        for (Personal medico : getPersonal()) {
            modeloCombo.addElement(medico.getNombre());
        }

        vista.getCmbBuscarRecetasHistorico().setModel(modeloCombo);
    }

    public void actualizarTablaRecetasPorMedico() {
        vista.getCmbBuscarRecetasHistorico().addActionListener(e -> {
            String nombreSeleccionado = vista.getCmbBuscarRecetasHistorico().getSelectedItem().toString().toLowerCase();
            DefaultTableModel modelo = (DefaultTableModel) vista.getTableHistoricoRecetas().getModel();
            modelo.setRowCount(0); // Limpiar tabla

            for (Receta r :getRecetas()) {
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

    public void centrarComponentes() {
        JPanel panelcito= vista.getPanelcito();
        panelcito.setLayout(new GridBagLayout()); // distribuye con grilla flexible
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // cada componente en nueva fila
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5); // margen entre componentes

        // Recorremos y volvemos a agregar los componentes ya existentes
        Component[] comps = panelcito.getComponents();
        panelcito.removeAll();
        for (Component c : comps) {
            panelcito.add(c, gbc);
        }

        panelcito.revalidate();
        panelcito.repaint();
    }
}