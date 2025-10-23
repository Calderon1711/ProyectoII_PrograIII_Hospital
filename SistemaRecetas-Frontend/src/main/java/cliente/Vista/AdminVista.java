package Vista;

import Modelo.Personal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminVista extends JFrame {

    private JPanel JPanelPrincipal;
    private JTabbedPane PestannaGlobal;
    private JPanel PanelMedico;
    private JPanel PanelFarmaceutico;
    private JPanel Pacientes;
    private JPanel Medicamentos1;
    private JPanel PanelDashboard;
    private JPanel Datos;
    private JLabel Desde;
    private JComboBox cmbDesdeAnnio;
    private JComboBox cmbDesdeMes;
    private JLabel Hasta;
    private JComboBox cmbHastaAnio;
    private JComboBox cmbHastaMes;
    private JComboBox cmbMedicamento;
    private JButton btnGenerarRango;
    private JButton btnLimpiar;
    private JPanel Formarto;
    private JButton medicamentosButton;
    private JPanel graficoRecetaFormato;
    private JButton recetasButton;
    private JButton btnAgregarMesButton;
    private JButton btnQuitarMesButton;
    private JTable tblDatos;
    private JPanel FormatoPanelMedico;
    private JButton BotonLimpiar;
    private JButton BotonGuardar;
    private JTextField CampoNombreMedico;
    private JButton BotonBorrar;
    private JTable TablaListado;
    private JTextField CampoIDMEdico;
    private JTextField CampoEspecialidadMEdico;
    private JTextField CampoNombreBusquedaMedico;
    private JButton BuscarBoton;
    private JButton BotonReporte;
    private JLabel TextoMedico;
    private JLabel TextoNombre;
    private JLabel TextoBusqueda;
    private JLabel TextoListado;
    private JScrollPane ScrollTabla;
    private JLabel TextoId;
    private JLabel TextoEspecialidad;
    private JLabel TextoNombre2;
    private JLabel fotoHospital;
    private JPanel PanelAcercaDe;
    private JTable tableHistoricoRecetas;
    private JButton ButtonBuscarHistorico;
    private JComboBox cmbBuscarRecetasHistorico;
    private JTable tableHistoricoMedicamentos;
    private JPanel Historial;
    private JPanel FormatoFarmaceutico;
    private JTextField campoNombreFarma;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JTextField campoEspecialidadFarma;
    private JButton borrarButton;
    private JTextField campoIdFarma;
    private JButton buscarButton;
    private JButton reporteButton;
    private JTable TablaListadofarma;
    private JPanel FormatoPacientes;
    private JSplitPane DivisorPantalla;
    private JPanel PanelTExto;
    private JPanel PanelTabla;
    private JTable TablaPacientes;
    private JScrollPane FormatoTabla;
    private JLabel TextoListadoPaciente;
    private JLabel TextoPacientes;
    private JLabel BuscarTexto;
    private JTextField campoBuscarPaciente;
    private JButton BotonBuscarPaciente;
    private JPanel PanelInsertarFarma;
    private JPanel PanelBusquedaFarma;
    private JPanel PanelTablaFarma;
    private JPanel PanelPrincipalMedicamento;
    private JPanel PanelInsertar;
    private JPanel PanelBusqueda;
    private JPanel PanelTablaMedicamentos;
    private JLabel CodigoMedicamento;
    private JLabel Presentacion;
    private JLabel NombreMedicamento;
    private JTextField CampoNombreMedicamento;
    private JButton Boton_LimpiarMedicamento;
    private JButton BTNBorrarMedicamento;
    private JTextField CampoCodigoMedicamento;
    private JTextField CampoPresentacionMedicamento;
    private JButton btnGuardarMedicamento;
    private JLabel TextoBusquedaMedicamento;
    private JLabel NombreMedicamentoB;
    private JTextField CampoNombreMEdicamento;
    private JButton BotonBuscarMedicamento;
    private JButton ReporteMedicamento;
    private JLabel TXTListado;
    private JLabel MEdicamento;
    private JTable TablaMedicamento;
    private JLabel TextoIDFarma;
    private JLabel TextoFarma;
    private JLabel NombreTxtFarma;
    private JLabel BusquedaFarmTxt;
    private JLabel NombreFarmatxt;
    private JLabel LIstadoFarmatxt;
    private JTextField campoBusquedaFarma;
    private JLabel TxtIDPAciente;
    private JTextField CampoIdPAciente;
    private JLabel txtNombrePaciente;
    private JTextField campoNombrePaciente;
    private JButton btnGuardarPaciente;
    private JButton btnLimpiarPaciente;
    private JButton BorrarPAciente;
    private JLabel txtFechaPaciente;
    private JLabel txtTelefonopaciente;
    private JTextField campoTelefonoPaciente;
    private JTextField campoFechaNacimiento;
    private JScrollPane ScrollPaneDashBoard;
    private JPanel GraficoMedicamento;
    private JPanel graficoReceta;
    private JScrollPane ScrollPaneHistoricoRecetas;
    private JScrollPane ScrollPaneHistoricoMedicamentos;
    private DefaultTableModel modeloMedicos;
    private DefaultTableModel modeloFarmaceuticos;
    private DefaultTableModel modeloPacientes;
    private DefaultTableModel modeloMedicamentos;

    public AdminVista(Personal u) {
        setContentPane(JPanelPrincipal);
        setTitle("Login");// le pongo titulo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //para q cuando la aplicacion cierre el programa tambnien
        setSize(1000, 900);// tamanno de la ventana
        setLocationRelativeTo(null);
        inicializarTablaMedicos();
        inicializarTablaFarmaceuticos();
        inicializarTablaPacientes();
        inicializarTablaMEdicamentos();
        btnAgregarMesButton = new JButton("Agregar Mes");
        btnQuitarMesButton = new JButton("Quitar Mes");
    }

    private void inicializarTablaMedicos() {
        // Columnas de la tabla
        String[] columnas = {"ID", "Nombre", "Especialidad"};

        // Modelo vacío
        modeloMedicos = new DefaultTableModel(null, columnas);

        TablaListado.setModel(modeloMedicos);
        JTableHeader header1 = TablaListado.getTableHeader();
        header1.setFont(new Font("Arial", Font.BOLD, 17));
    }

    private void inicializarTablaFarmaceuticos(){

        String[] columnas = {"ID", "Nombre"};
        modeloFarmaceuticos = new DefaultTableModel(null, columnas);
        TablaListadofarma.setModel(modeloFarmaceuticos);
        JTableHeader header2 = TablaListadofarma.getTableHeader();
        header2.setFont(new Font("Arial", Font.BOLD, 17));

    }

    private void inicializarTablaPacientes(){
        // Definir columnas (sin filas todavía)
        String[] columnas = {"ID", "Nombre", "Fecha Nacimiento", "Teléfono"};
        modeloPacientes = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //  evita edición directa en la vista
            }
        };
        TablaPacientes.setModel(modeloPacientes);

        // Personalizar encabezado
        JTableHeader header = TablaPacientes.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 17));
        header.setReorderingAllowed(false); //  evitar que arrastren columnas

        // Ajustes visuales de la tabla
        TablaPacientes.setRowHeight(25); // altura de filas
        TablaPacientes.setFont(new Font("Arial", Font.PLAIN, 15));
        TablaPacientes.setSelectionBackground(new Color(184, 207, 229)); // color al seleccionar
        TablaPacientes.setSelectionForeground(Color.BLACK); // color de texto al seleccionar
    }

    private void inicializarTablaMEdicamentos(){
        // Definir columnas (sin filas todavía)
        String[] columnas = {"Codigo", "Nombre", "Presentacion"};
        modeloMedicamentos = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //  evita edición directa en la vista
            }
        };
        TablaMedicamento.setModel(modeloMedicamentos);

        // Personalizar encabezado
        JTableHeader header = TablaMedicamento.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 17));
        header.setReorderingAllowed(false); //  evitar que arrastren columnas

        // Ajustes visuales de la tabla
        TablaMedicamento.setRowHeight(25); // altura de filas
        TablaMedicamento.setFont(new Font("Arial", Font.PLAIN, 15));
        TablaMedicamento.setSelectionBackground(new Color(184, 207, 229)); // color al seleccionar
        TablaMedicamento.setSelectionForeground(Color.BLACK); // color de texto al seleccionar
    }

    //  Getters para que la controladora pueda acceder
    public JTable getTablaMedicos() {
        return TablaListado;
    }
    public DefaultTableModel getModeloMedicos() {
        return modeloMedicos;
    }

    public JTable getTablaFarmaceuticos() {return TablaListadofarma;}
    public DefaultTableModel getModeloFarmaceuticos() {return modeloFarmaceuticos;}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminVista ventana = new AdminVista(null);
            ventana.setVisible(true);
        });
    }

    //gets y sets
    public JPanel getJPanelPrincipal() { return JPanelPrincipal; }
    public void setJPanelPrincipal(JPanel JPanelPrincipal) { this.JPanelPrincipal = JPanelPrincipal; }

    public JTabbedPane getPestannaGlobal() { return PestannaGlobal; }
    public void setPestannaGlobal(JTabbedPane pestannaGlobal) { PestannaGlobal = pestannaGlobal; }

    public JPanel getPanelMedico() { return PanelMedico; }
    public void setPanelMedico(JPanel panelMedico) { PanelMedico = panelMedico; }

    public JPanel getPanelFarmaceutico() { return PanelFarmaceutico; }
    public void setPanelFarmaceutico(JPanel panelFarmaceutico) { PanelFarmaceutico = panelFarmaceutico; }

    public JPanel getPacientes() { return Pacientes; }
    public void setPacientes(JPanel pacientes) { Pacientes = pacientes; }

    public JPanel getMedicamentos1() { return Medicamentos1; }
    public void setMedicamentos1(JPanel medicamentos1) { Medicamentos1 = medicamentos1; }

    public JPanel getPanelDashboard() { return PanelDashboard; }
    public void setPanelDashboard(JPanel panelDashboard) { PanelDashboard = panelDashboard; }

    public JPanel getDatos() { return Datos; }
    public void setDatos(JPanel datos) { Datos = datos; }

    public JPanel getMedicamentos() { return Formarto; }
    public void setMedicamentos(JPanel medicamentos) { Formarto = medicamentos; }

    public JPanel getRecetas() { return graficoRecetaFormato; }
    public void setRecetas(JPanel recetas) { graficoRecetaFormato = recetas; }

    public JPanel getFormatoPanelMedico() { return FormatoPanelMedico; }
    public void setFormatoPanelMedico(JPanel formatoPanelMedico) { FormatoPanelMedico = formatoPanelMedico; }

    public JPanel getPanelAcercaDe() { return PanelAcercaDe; }
    public void setPanelAcercaDe(JPanel panelAcercaDe) { PanelAcercaDe = panelAcercaDe; }

    public JPanel getHistorial() { return Historial; }
    public void setHistorial(JPanel historial) { Historial = historial; }

    public JPanel getFormatoFarmaceutico() { return FormatoFarmaceutico; }
    public void setFormatoFarmaceutico(JPanel formatoFarmaceutico) { FormatoFarmaceutico = formatoFarmaceutico; }

    public JPanel getFormatoPacientes() { return FormatoPacientes; }
    public void setFormatoPacientes(JPanel formatoPacientes) { FormatoPacientes = formatoPacientes; }

    public JPanel getPanelTExto() { return PanelTExto; }
    public void setPanelTExto(JPanel panelTExto) { PanelTExto = panelTExto; }

    public JPanel getPanelTabla() { return PanelTabla; }
    public void setPanelTabla(JPanel panelTabla) { PanelTabla = panelTabla; }

    public JPanel getPanelInsertarFarma() { return PanelInsertarFarma; }
    public void setPanelInsertarFarma(JPanel panelInsertarFarma) { PanelInsertarFarma = panelInsertarFarma; }

    public JPanel getPanelBusquedaFarma() { return PanelBusquedaFarma; }
    public void setPanelBusquedaFarma(JPanel panelBusquedaFarma) { PanelBusquedaFarma = panelBusquedaFarma; }

    public JPanel getPanelTablaFarma() { return PanelTablaFarma; }
    public void setPanelTablaFarma(JPanel panelTablaFarma) { PanelTablaFarma = panelTablaFarma; }

    public JPanel getPanelPrincipalMedicamento() { return PanelPrincipalMedicamento; }
    public void setPanelPrincipalMedicamento(JPanel panelPrincipalMedicamento) { PanelPrincipalMedicamento = panelPrincipalMedicamento; }

    public JPanel getPanelInsertar() { return PanelInsertar; }
    public void setPanelInsertar(JPanel panelInsertar) { PanelInsertar = panelInsertar; }

    public JPanel getPanelBusqueda() { return PanelBusqueda; }
    public void setPanelBusqueda(JPanel panelBusqueda) { PanelBusqueda = panelBusqueda; }

    public JPanel getPanelTablaMedicamentos() { return PanelTablaMedicamentos; }
    public void setPanelTablaMedicamentos(JPanel panelTablaMedicamentos) { PanelTablaMedicamentos = panelTablaMedicamentos; }

    //botones


    public JButton getBotonBuscarPaciente() {
        return BotonBuscarPaciente;
    }

    public void setBotonBuscarPaciente(JButton botonBuscarPaciente) {
        BotonBuscarPaciente = botonBuscarPaciente;
    }

    public JButton getBorrarPAciente() {
        return BorrarPAciente;
    }

    public void setBorrarPAciente(JButton borrarPAciente) {
        BorrarPAciente = borrarPAciente;
    }

    public JButton getBtnLimpiarPaciente() {
        return btnLimpiarPaciente;
    }

    public void setBtnLimpiarPaciente(JButton btnLimpiarPaciente) {
        this.btnLimpiarPaciente = btnLimpiarPaciente;
    }

    public JButton getBtnGuardarPaciente() {
        return btnGuardarPaciente;
    }

    public void setBtnGuardarPaciente(JButton btnGuardarPaciente) {
        this.btnGuardarPaciente = btnGuardarPaciente;
    }

    public JButton getBtnGenerarRango() { return btnGenerarRango; }
    public void setBtnGenerarRango(JButton btnGenerarRango) { this.btnGenerarRango = btnGenerarRango; }

    public JButton getBtnLimpiar() { return btnLimpiar; }
    public void setBtnLimpiar(JButton btnLimpiar) { this.btnLimpiar = btnLimpiar; }

    public JButton getMedicamentosButton() { return medicamentosButton; }
    public void setMedicamentosButton(JButton medicamentosButton) { this.medicamentosButton = medicamentosButton; }

    public JButton getRecetasButton() { return recetasButton; }
    public void setRecetasButton(JButton recetasButton) { this.recetasButton = recetasButton; }

    public JButton getBtnAgregarMesButton() { return btnAgregarMesButton; }
    public void setBtnAgregarMesButton(JButton btnAgregarMesButton) { this.btnAgregarMesButton = btnAgregarMesButton; }

    public JButton getBtnQuitarMesButton() { return btnQuitarMesButton; }
    public void setBtnQuitarMesButton(JButton btnQuitarMesButton) { this.btnQuitarMesButton = btnQuitarMesButton; }

    public JButton getBotonLimpiar() { return BotonLimpiar; }
    public void setBotonLimpiar(JButton botonLimpiar) { BotonLimpiar = botonLimpiar; }

    public JButton getBotonGuardar() { return BotonGuardar; }
    public void setBotonGuardar(JButton botonGuardar) { BotonGuardar = botonGuardar; }

    public JButton getBotonBorrar() { return BotonBorrar; }
    public void setBotonBorrar(JButton botonBorrar) { BotonBorrar = botonBorrar; }

    public JButton getBuscarBoton() { return BuscarBoton; }
    public void setBuscarBoton(JButton buscarBoton) { BuscarBoton = buscarBoton; }

    public JButton getBotonReporte() { return BotonReporte; }
    public void setBotonReporte(JButton botonReporte) { BotonReporte = botonReporte; }

    public JButton getButtonBuscarHistorico() { return ButtonBuscarHistorico; }
    public void setButtonBuscarHistorico(JButton buttonBuscarHistorico) { ButtonBuscarHistorico = buttonBuscarHistorico; }

    public JButton getGuardarButton() { return guardarButton; }
    public void setGuardarButton(JButton guardarButton) { this.guardarButton = guardarButton; }

    public JButton getLimpiarButton() { return limpiarButton; }
    public void setLimpiarButton(JButton limpiarButton) { this.limpiarButton = limpiarButton; }

    public JButton getBorrarButton() { return borrarButton; }
    public void setBorrarButton(JButton borrarButton) { this.borrarButton = borrarButton; }

    public JButton getBuscarButton() { return buscarButton; }
    public void setBuscarButton(JButton buscarButton) { this.buscarButton = buscarButton; }

    public JButton getReporteButton() { return reporteButton; }
    public void setReporteButton(JButton reporteButton) { this.reporteButton = reporteButton; }

    public JButton getBotonBuscar() { return BotonBuscarPaciente; }
    public void setBotonBuscar(JButton botonBuscar) { BotonBuscarPaciente = botonBuscar; }

    public JButton getBoton_LimpiarMedicamento() { return Boton_LimpiarMedicamento; }
    public void setBoton_LimpiarMedicamento(JButton boton_LimpiarMedicamento) { Boton_LimpiarMedicamento = boton_LimpiarMedicamento; }

    public JButton getBTNBorrarMedicamento() { return BTNBorrarMedicamento; }
    public void setBTNBorrarMedicamento(JButton BTNBorrarMedicamento) { this.BTNBorrarMedicamento = BTNBorrarMedicamento; }

    public JButton getBtnGuardarMedicamento() { return btnGuardarMedicamento; }
    public void setBtnGuardarMedicamento(JButton btnGuardarMedicamento) { this.btnGuardarMedicamento = btnGuardarMedicamento; }

    public JButton getBotonBuscarMedicamento() { return BotonBuscarMedicamento; }
    public void setBotonBuscarMedicamento(JButton botonBuscarMedicamento) { BotonBuscarMedicamento = botonBuscarMedicamento; }

    public JButton getReporteMedicamento() { return ReporteMedicamento; }
    public void setReporteMedicamento(JButton reporteMedicamento) { ReporteMedicamento = reporteMedicamento; }

    //tablas
    public JTable getTblDatos() { return tblDatos; }
    public void setTblDatos(JTable tblDatos) { this.tblDatos = tblDatos; }

    public JTable getTablaListado() { return TablaListado; }
    public void setTablaListado(JTable tablaListado) { TablaListado = tablaListado; }

    public JTable getTableHistoricoRecetas() { return tableHistoricoRecetas; }
    public void setTableHistoricoRecetas(JTable tableHistoricoRecetas) { this.tableHistoricoRecetas = tableHistoricoRecetas; }

    public JTable getTableHistoricoMedicamentos() { return tableHistoricoMedicamentos; }
    public void setTableHistoricoMedicamentos(JTable tableHistoricoMedicamentos) { this.tableHistoricoMedicamentos = tableHistoricoMedicamentos; }

    public JTable getTablaListadofarma() { return TablaListadofarma; }
    public void setTablaListadofarma(JTable tablaListadofarma) { TablaListadofarma = tablaListadofarma; }

    public JTable getTablaPacientes() { return TablaPacientes; }
    public void setTablaPacientes(JTable tablaPacientes) { TablaPacientes = tablaPacientes; }

    public JTable getTablaMedicamento() { return TablaMedicamento; }
    public void setTablaMedicamento(JTable tablaMedicamento) { TablaMedicamento = tablaMedicamento; }

    public JLabel getDesde() {
        return Desde;
    }

    public void setDesde(JLabel desde) {
        Desde = desde;
    }

    //textfields


    public JTextField getCampoBuscarPaciente() {
        return campoBuscarPaciente;
    }

    public void setCampoBuscarPaciente(JTextField campoBuscarPaciente) {
        this.campoBuscarPaciente = campoBuscarPaciente;
    }

    public JTextField getCampoIdPAciente() {
        return CampoIdPAciente;
    }

    public void setCampoIdPAciente(JTextField campoIdPAciente) {
        CampoIdPAciente = campoIdPAciente;
    }

    public JTextField getCampoBusquedaFarma() {
        return campoBusquedaFarma;
    }
    public void setCampoBusquedaFarma(JTextField campoBusquedaFarma) {
        this.campoBusquedaFarma = campoBusquedaFarma;
    }

    public JTextField getCampoNombreMedico() { return CampoNombreMedico; }
    public void setCampoNombreMedico(JTextField campoNombre) { CampoNombreMedico = campoNombre; }

    public JTextField getCampoIDMEdico() { return CampoIDMEdico; }
    public void setCampoIDMEdico(JTextField campoID) { CampoIDMEdico = campoID; }

    public JTextField getCampoEspecialidadMEdico() { return CampoEspecialidadMEdico; }
    public void setCampoEspecialidadMEdico(JTextField textField3) { this.CampoEspecialidadMEdico = textField3; }

    public JTextField getCampoNombreBusquedaMedico() { return CampoNombreBusquedaMedico; }
    public void setCampoNombreBusquedaMedico(JTextField campoNombre2) { CampoNombreBusquedaMedico = campoNombre2; }

    public JTextField getCampoNombreFarma() { return campoNombreFarma; }
    public void setCampoNombreFarma(JTextField campoNombreFarma) { this.campoNombreFarma = campoNombreFarma; }

    public JTextField getCampoEspecialidadFarma() { return campoEspecialidadFarma; }
    public void setCampoEspecialidadFarma(JTextField campoEspecialidadFarma) { this.campoEspecialidadFarma = campoEspecialidadFarma; }

    public JTextField getCampoIdFarma() { return campoIdFarma; }
    public void setCampoIdFarma(JTextField campoIdFarma) { this.campoIdFarma = campoIdFarma; }

    public JTextField getCampoNombreMedicamento() { return CampoNombreMedicamento; }
    public void setCampoNombreMedicamento(JTextField campoNombreMedicamento) { CampoNombreMedicamento = campoNombreMedicamento; }

    public JTextField getCampoCodigoMedicamento() { return CampoCodigoMedicamento; }
    public void setCampoCodigoMedicamento(JTextField campoCodigoMedicamento) { CampoCodigoMedicamento = campoCodigoMedicamento; }

    public JTextField getCampoPresentacionMedicamento() { return CampoPresentacionMedicamento; }
    public void setCampoPresentacionMedicamento(JTextField campoPresentacionMedicamento) { CampoPresentacionMedicamento = campoPresentacionMedicamento; }

    public JTextField getCampoNombreMEdicamento() { return CampoNombreMEdicamento; }
    public void setCampoNombreMEdicamento(JTextField campoNombreMEdicamento) { CampoNombreMEdicamento = campoNombreMEdicamento; }

    public JTextField getCampoFechaNacimiento() {
        return campoFechaNacimiento;
    }

    public void setCampoFechaNacimiento(JTextField campoFechaNacimiento) {
        this.campoFechaNacimiento = campoFechaNacimiento;
    }

    public JTextField getCampoTelefonoPaciente() {
        return campoTelefonoPaciente;
    }

    public void setCampoTelefonoPaciente(JTextField campoTelefonoPaciente) {
        this.campoTelefonoPaciente = campoTelefonoPaciente;
    }

    public JTextField getCampoNombrePaciente() {
        return campoNombrePaciente;
    }

    public void setCampoNombrePaciente(JTextField campoNombrePaciente) {
        this.campoNombrePaciente = campoNombrePaciente;
    }

    //modelos

    public void setModeloMedicos(DefaultTableModel modeloMedicos) { this.modeloMedicos = modeloMedicos; }


    public void setModeloFarmaceuticos(DefaultTableModel modeloFarmaceuticos) { this.modeloFarmaceuticos = modeloFarmaceuticos; }

    public DefaultTableModel getModeloPacientes() { return modeloPacientes; }
    public void setModeloPacientes(DefaultTableModel modeloPacientes) { this.modeloPacientes = modeloPacientes; }

    public DefaultTableModel getModeloMedicamentos() { return modeloMedicamentos; }
    public void setModeloMedicamentos(DefaultTableModel modeloMedicamentos) { this.modeloMedicamentos = modeloMedicamentos; }


    //dashboard


    public JComboBox getCmbDesdeAnnio() {
        return cmbDesdeAnnio;
    }

    public void setCmbDesdeAnnio(JComboBox cmbDesdeAnnio) {
        this.cmbDesdeAnnio = cmbDesdeAnnio;
    }

    public JComboBox getCmbDesdeMes() {
        return cmbDesdeMes;
    }

    public void setCmbDesdeMes(JComboBox cmbDesdeMes) {
        this.cmbDesdeMes = cmbDesdeMes;
    }

    public JLabel getHasta() {
        return Hasta;
    }

    public void setHasta(JLabel hasta) {
        Hasta = hasta;
    }

    public JComboBox getCmbHastaAnio() {
        return cmbHastaAnio;
    }

    public void setCmbHastaAnio(JComboBox cmbHastaAnio) {
        this.cmbHastaAnio = cmbHastaAnio;
    }

    public JComboBox getCmbHastaMes() {
        return cmbHastaMes;
    }

    public void setCmbHastaMes(JComboBox cmbHastaMes) {
        this.cmbHastaMes = cmbHastaMes;
    }

    public JScrollPane getScrollTabla() {
        return ScrollTabla;
    }

    public void setScrollTabla(JScrollPane scrollTabla) {
        ScrollTabla = scrollTabla;
    }

    public JComboBox getCmbBuscarRecetasHistorico() {
        return cmbBuscarRecetasHistorico;
    }

    public void setCmbBuscarRecetasHistorico(JComboBox cmbBuscarRecetasHistorico) {
        this.cmbBuscarRecetasHistorico = cmbBuscarRecetasHistorico;
    }

    public JComboBox getCmbMedicamento() {
        return cmbMedicamento;
    }

    public void setCmbMedicamento(JComboBox cmbMedicamento) {
        this.cmbMedicamento = cmbMedicamento;
    }

    public JScrollPane getScrollPaneDashBoard() {
        return ScrollPaneDashBoard;
    }

    public void setScrollPaneDashBoard(JScrollPane scrollPaneDashBoard) {
        ScrollPaneDashBoard = scrollPaneDashBoard;
    }

    public JPanel getGraficoReceta() {
        return graficoReceta;
    }

    public void setGraficoReceta(JPanel graficoReceta) {
        this.graficoReceta = graficoReceta;
    }

    public JPanel getGraficoMedicamento() {
        return GraficoMedicamento;
    }

    public void setGraficoMedicamento(JPanel graficoMedicamento) {
        GraficoMedicamento = graficoMedicamento;
    }

    public JScrollPane getScrollPaneHistoricoRecetas() {
        return ScrollPaneHistoricoRecetas;
    }

    public void setScrollPaneHistoricoRecetas(JScrollPane scrollPaneHistoricoRecetas) {
        ScrollPaneHistoricoRecetas = scrollPaneHistoricoRecetas;
    }

    public JScrollPane getScrollPaneHistoricoMedicamentos() {
        return ScrollPaneHistoricoMedicamentos;
    }

    public void setScrollPaneHistoricoMedicamentos(JScrollPane scrollPaneHistoricoMedicamentos) {
        ScrollPaneHistoricoMedicamentos = scrollPaneHistoricoMedicamentos;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
