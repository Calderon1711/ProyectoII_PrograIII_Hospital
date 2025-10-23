package Vista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Modelo.Usuario;
import Modelo.Personal;

public class FarmaceuticoVista extends JFrame {
    private JPanel rootPanel1;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltro;
    private JButton botBuscar;
    private JButton botLimpiar;
    private JCheckBox checkConfeccionada;
    private JCheckBox checkProceso;
    private JCheckBox checkLista;
    private JLabel lblVentana;
    private JButton botIniProceso;
    private JButton botLista;
    private JButton botEntregar;
    private JButton botRefrescar;
    private JLabel lblUsuario;
    private JLabel lblCantidad;
    private JLabel lblListo;
    private JTable tabDetalles;
    private JPanel controlpanel;
    private JTable tabRecetas;



    private final RecetaTableModel recetasModel = new RecetaTableModel();
    private final DetalleTableModel detallesModel = new DetalleTableModel();

    public FarmaceuticoVista(Personal u) {
        setContentPane(rootPanel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Farmacéutico");
        setSize(1000, 800);
        initUi();
        setLocationRelativeTo(null);
    }

    private void initUi() {
        // Filtros
        cmbFiltro.removeAllItems();
        cmbFiltro.addItem("ID Paciente");
        cmbFiltro.addItem("#Receta");
        checkConfeccionada.setSelected(true);
        checkProceso.setSelected(true);
        checkLista.setSelected(true);
        lblVentana.setText("Rango de retiro permitido: hoy ± 3 días");

        // Tablas
        tabRecetas.setModel(recetasModel);
        tabDetalles.setModel(detallesModel);
        tabRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabRecetas.getSelectionModel().addListSelectionListener(this::onRecetaSeleccionada);

        actualizarBotones(null);
    }

    private void onRecetaSeleccionada(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tabRecetas.getSelectedRow();
        RecetaRow sel = (row >= 0) ? recetasModel.getRow(row) : null;
        actualizarBotones(sel);

        if (sel != null) {
            detallesModel.setRows(sel.detalles);
            lblListo.setText("Receta " + sel.id + " seleccionada");
            // Si añadiste campos de fecha visibles:
            // txtFechaConf.setText(sel.fechaConfeccion != null ? sel.fechaConfeccion.toString() : "");
            // txtFechaRet.setText(sel.fechaRetiro != null ? sel.fechaRetiro.toString() : "");
        } else {
            detallesModel.setRows(List.of());
            lblListo.setText(" ");
            // txtFechaConf.setText("");
            // txtFechaRet.setText("");
        }
    }

    private void actualizarBotones(RecetaRow r) {
        if (r == null) {
            botIniProceso.setEnabled(false);
            botLista.setEnabled(false);
            botEntregar.setEnabled(false);
            return;
        }
        switch (r.estado) {
            case "CONFECCIONADA" -> {
                botIniProceso.setEnabled(true);
                botLista.setEnabled(false);
                botEntregar.setEnabled(false);
            }
            case "PROCESO" -> {
                botIniProceso.setEnabled(false);
                botLista.setEnabled(true);
                botEntregar.setEnabled(false);
            }
            case "LISTA" -> {
                botIniProceso.setEnabled(false);
                botLista.setEnabled(false);
                botEntregar.setEnabled(true);
            }
            default -> { // ENTREGADA u otros estados no accionables
                botIniProceso.setEnabled(false);
                botLista.setEnabled(false);
                botEntregar.setEnabled(false);
            }
        }
    }

    // ==== Getters para Controller ====
    public String getTextoBuscar() { return txtBuscar.getText().trim(); }
    public String getTipoFiltro() { return (String) cmbFiltro.getSelectedItem(); }
    public boolean isEstadoConfeccionada() { return checkConfeccionada.isSelected(); }
    public boolean isEstadoProceso() { return checkProceso.isSelected(); }
    public boolean isEstadoLista() { return checkLista.isSelected(); }

    public void setUsuario(String usuario) { lblUsuario.setText("Usuario: " + usuario); }
    public void setMensaje(String mensaje) { lblListo.setText(mensaje); }

    public void setRecetas(List<RecetaRow> filas) {
        recetasModel.setRows(filas);
        lblCantidad.setText("Recetas: " + filas.size());
        if (!filas.isEmpty()) tabRecetas.setRowSelectionInterval(0, 0);
    }

    public RecetaRow getRecetaSeleccionada() {
        int i = tabRecetas.getSelectedRow();
        return (i >= 0) ? recetasModel.getRow(i) : null;
    }

    public void onBuscar(Runnable r) { botBuscar.addActionListener(e -> r.run()); }
    public void onLimpiar(Runnable r) { botLimpiar.addActionListener(e -> r.run()); }
    public void onProceso(Runnable r) { botIniProceso.addActionListener(e -> r.run()); }
    public void onLista(Runnable r) { botLista.addActionListener(e -> r.run()); }
    public void onEntregar(Runnable r) { botEntregar.addActionListener(e -> r.run()); }
    public void onRefrescar(Runnable r) { botRefrescar.addActionListener(e -> r.run()); }


    public static class RecetaRow {
        public final String id;
        public final String pacienteId;
        public final String paciente;
        public final LocalDate fechaConfeccion;
        public final LocalDate fechaRetiro;
        public final String estado;   // CONFECCIONADA / PROCESO / LISTA / ENTREGADA
        public final String medico;
        public final List<DetalleRow> detalles;

        public RecetaRow(String id, String pacienteId,String paciente, LocalDate fechaConfeccion,
                         LocalDate fechaRetiro, String estado, String medico,
                         List<DetalleRow> detalles) {
            this.id = id;
            this.pacienteId = pacienteId;
            this.paciente = paciente;
            this.fechaConfeccion = fechaConfeccion;
            this.fechaRetiro = fechaRetiro;
            this.estado = estado;
            this.medico = medico;
            this.detalles = detalles;
        }
    }

    public static class DetalleRow {
        public final String codigo;
        public final String medicamento;
        public final String presentacion;
        public final int cantidad;
        public final String indicaciones;
        public final int dias;

        public DetalleRow(String codigo, String medicamento, String presentacion,
                          int cantidad, String indicaciones, int dias) {
            this.codigo = codigo;
            this.medicamento = medicamento;
            this.presentacion = presentacion;
            this.cantidad = cantidad;
            this.indicaciones = indicaciones;
            this.dias = dias;
        }
    }

    // ==== TableModels ====
    private static class RecetaTableModel extends AbstractTableModel {
        // 6 columnas (incluye F. Retiro)
        private final String[] cols = {"#Receta","Paciente ID", "Paciente", "F. Confección", "F. Retiro", "Estado", "Médico"};
        private final List<RecetaRow> rows = new ArrayList<>();

        public void setRows(List<RecetaRow> nuevas) {
            rows.clear();
            rows.addAll(nuevas);
            fireTableDataChanged();
        }

        public RecetaRow getRow(int i) { return rows.get(i); }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }

        @Override
        public Object getValueAt(int r, int c) {
            RecetaRow x = rows.get(r);
            return switch (c) {
                case 0 -> x.id;
                case 1->x.pacienteId;
                case 2 -> x.paciente;
                case 3 -> x.fechaConfeccion;
                case 4 -> x.fechaRetiro;
                case 5 -> x.estado;
                case 6 -> x.medico;
                default -> "";
            };
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return switch (c) {
                case 3, 4 -> LocalDate.class;
                default -> String.class;
            };
        }
    }

    private static class DetalleTableModel extends AbstractTableModel {
        private final String[] cols = {"Código", "Medicamento", "Presentación", "Cantidad", "Indicaciones", "Días"};
        private final List<DetalleRow> rows = new ArrayList<>();

        public void setRows(List<DetalleRow> nuevas) {
            rows.clear();
            rows.addAll(nuevas);
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }

        @Override
        public Object getValueAt(int r, int c) {
            DetalleRow x = rows.get(r);
            return switch (c) {
                case 0 -> x.codigo;
                case 1 -> x.medicamento;
                case 2 -> x.presentacion;
                case 3 -> x.cantidad;
                case 4 -> x.indicaciones;
                case 5 -> x.dias;
                default -> "";
            };
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return switch (c) {
                case 3, 5 -> Integer.class;
                default -> String.class;
            };
        }
    }
}