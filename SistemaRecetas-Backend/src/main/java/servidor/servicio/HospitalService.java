package servidor.servicio;

import servidor.Modelo.Hospital;
import servidor.Modelo.ListaMedicamentos;
import servidor.Modelo.ListaPacientes;
import servidor.Modelo.ListaPersonal;
import servidor.Modelo.ListaRecetas;

public class HospitalService {

    private final Hospital hospital = Hospital.getInstance();

    // getters
    public ListaPersonal obtenerPersonal() {
        return hospital.getPersonal();
    }

    public ListaPacientes obtenerPacientes() {
        return hospital.getPacientes();
    }

    public ListaMedicamentos obtenerMedicamentos() {
        return hospital.getMedicamentos();
    }

    public ListaRecetas obtenerRecetas() {
        return hospital.getRecetas();
    }



    public void cargarPersonal() {
        hospital.cargarPersonal();
    }

    public void guardarPacientes() {
        hospital.guardarPacientes();
    }

    public void cargarPacientes() {
        hospital.cargarPacientes();
    }

    public void guardarMedicamentos() {
        hospital.guardarMedicamentos();
    }

    public void cargarMedicamentos() {
        hospital.cargarMedicamentos();
    }

    public void guardarRecetas() {
        hospital.guardarRecetas();
    }

    public void cargarRecetas() {
        hospital.cargarRecetas();
    }

    // login
    public Object loginPersonal(String id, String clave) {
        return hospital.loginPersonal(id, clave);
    }

}
