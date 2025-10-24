package cliente.controlador;

// ControladorGeneral.java
import cliente.modelo.*;
import cliente.Vista.*;
import cliente.controlador.*;


public class ControladorGeneral {

    public void abrirVistaPorRol(Personal u) {
        if (u == null) return;

        Rol rol = u.getRol();

        switch (rol) {
            case MEDICO:
                MedicoVista vm = new MedicoVista(u);
                new ControladorMedico(vm,u);
                vm.setLocationRelativeTo(null);
                vm.setVisible(true);
                break;
            case FARMACEUTICO:
                FarmaceuticoVista vf = new FarmaceuticoVista(u);
                new ControladorFarmaceutico(vf,u);
                vf.setLocationRelativeTo(null);
                vf.setVisible(true);
                break;
            case ADMINISTRADOR:
                AdminVista va = new AdminVista(u);
                new ControladoraAdmin(va,u);
                va.setLocationRelativeTo(null);
                va.setVisible(true);
                break;
            default:
                System.err.println("Rol desconocido: " + rol);
        }
    }
}
