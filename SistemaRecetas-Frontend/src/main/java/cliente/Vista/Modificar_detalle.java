package Vista;

import Modelo.Hospital;

import javax.swing.*;

public class Modificar_detalle extends JFrame {
    private JPanel panel1;
    private JTextField textFieldMedicamento;
    private JTextField textFieldPresentacion;
    private JTextField textFieldCodigo;
    private JComboBox comboBoxdias;
    private JComboBox comboBoxCantidad;
    private JTextField textFieldIndicaciones;
    private JButton cancelarButton;
    private JButton guardarButton;

    public JPanel getPanel1() {
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public JTextField getTextFieldPresentacion() {
        return textFieldPresentacion;
    }

    public void setTextFieldPresentacion(JTextField textFieldPresentacion) {
        this.textFieldPresentacion = textFieldPresentacion;
    }

    public JTextField getTextFieldMedicamento() {
        return textFieldMedicamento;
    }

    public void setTextFieldMedicamento(JTextField textFieldMedicamento) {
        this.textFieldMedicamento = textFieldMedicamento;
    }

    public JTextField getTextFieldCodigo() {
        return textFieldCodigo;
    }

    public void setTextFieldCodigo(JTextField textFieldCodigo) {
        this.textFieldCodigo = textFieldCodigo;
    }

    public JComboBox getComboBoxdias() {
        return comboBoxdias;
    }

    public void setComboBoxdias(JComboBox comboBoxdias) {
        this.comboBoxdias = comboBoxdias;
    }

    public JComboBox getComboBoxCantidad() {
        return comboBoxCantidad;
    }

    public void setComboBoxCantidad(JComboBox comboBoxCantidad) {
        this.comboBoxCantidad = comboBoxCantidad;
    }

    public JTextField getTextFieldIndicaciones() {
        return textFieldIndicaciones;
    }

    public void setTextFieldIndicaciones(JTextField textFieldIndicaciones) {
        this.textFieldIndicaciones = textFieldIndicaciones;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public void setCancelarButton(JButton cancelarButton) {
        this.cancelarButton = cancelarButton;
    }

    public JButton getGuardarButton() {
        return guardarButton;
    }

    public void setGuardarButton(JButton guardarButton) {
        this.guardarButton = guardarButton;
    }

    public Modificar_detalle(Hospital hospi) {
        setContentPane(panel1);
        setTitle("Modificar Detalle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300); // Tamaño más razonable
        setLocationRelativeTo(null);

    }

        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Modificar_detalle vista = new Modificar_detalle(null);
            vista.setVisible(true);
        });
    }
}
