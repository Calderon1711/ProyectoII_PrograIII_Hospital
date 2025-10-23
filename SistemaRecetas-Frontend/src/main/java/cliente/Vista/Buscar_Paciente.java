package Vista;

import Modelo.ListaPacientes;
import Modelo.Personal;

import javax.swing.*;

public class Buscar_Paciente extends JFrame{
    private JComboBox cmb_Buscar_Paciente;
    private JTextField sugerencias_Paciente;
    private JButton btnOk;
    private JButton btnCancel;
    private JTable table1;
    private JScrollPane table_Pacientes;
    private JPanel Panel1;

    //Getters y setters
    public JComboBox getCmb_Buscar_Paciente() {
        return cmb_Buscar_Paciente;
    }

    public void setCmb_Buscar_Paciente(JComboBox cmb_Buscar_Paciente) {
        this.cmb_Buscar_Paciente = cmb_Buscar_Paciente;
    }

    public JTextField getSugerencias_Paciente() {
        return sugerencias_Paciente;
    }

    public void setSugerencias_Paciente(JTextField sugerencias_Paciente) {
        this.sugerencias_Paciente = sugerencias_Paciente;
    }

    public JButton getBtnOk() {
        return btnOk;
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(JButton btnCancel) {
        this.btnCancel = btnCancel;
    }

    public JTable getTable1() {
        return table1;
    }

    public void setTable1(JTable table1) {
        this.table1 = table1;
    }

    public JPanel getPanel1() {
        return Panel1;
    }

    public void setPanel1(JPanel panel1) {
        Panel1 = panel1;
    }

    public JScrollPane getTable_Pacientes() {
        return table_Pacientes;
    }

    public void setTable_Pacientes(JScrollPane table_Pacientes) {
        this.table_Pacientes = table_Pacientes;
    }




    //--------------------------------------------------------------


    public Buscar_Paciente(ListaPacientes u) {
        setContentPane(Panel1);
        setTitle("Agregar Paciente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Buscar_Paciente vista = new Buscar_Paciente(null);
            vista.setVisible(true);
        });
    }

}
