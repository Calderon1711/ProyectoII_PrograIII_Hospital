package servidor.Modelo;

import java.util.List;
import java.util.ArrayList;
import servidor.persistencia.xml.*;

public class Hospital {
    private static Hospital instance;
    private ListaMedicamentos medicamentos;
    private ListaPacientes pacientes;
    private ListaPersonal personal;
    private ListaRecetas recetas;

    // 🔒 Constructor privado con validaciones seguras
    private Hospital(ListaPersonal personal, ListaPacientes pacientes,
                     ListaMedicamentos medicamentos, ListaRecetas recetas) {

        this.personal = (personal != null) ? personal : PersistenciaPersonalXML.cargar();
        if (this.personal == null) this.personal = new ListaPersonal();

        this.pacientes = (pacientes != null) ? pacientes : new ListaPacientes();
        this.medicamentos = (medicamentos != null) ? medicamentos : new ListaMedicamentos();
        this.recetas = (recetas != null) ? recetas : new ListaRecetas();
    }

    // 🔒 Constructor por defecto que garantiza que todo exista
    private Hospital() {
        this.personal = PersistenciaPersonalXML.cargar();
        if (this.personal == null) this.personal = new ListaPersonal();

        this.pacientes = new ListaPacientes();
        this.medicamentos = new ListaMedicamentos();
        this.recetas = new ListaRecetas();
    }

    // 🔁 Patrón Singleton
    public static Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital();
        }
        return instance;
    }

    // Inicialización opcional (por si se quiere construir con datos)
    public static void initialize(ListaPersonal personal, ListaPacientes pacientes,
                                  ListaMedicamentos medicamentos, ListaRecetas recetas) {
        if (instance == null) {
            instance = new Hospital(personal, pacientes, medicamentos, recetas);
        }
    }

    // Getters y setters
    public ListaPersonal getPersonal() {
        return personal;
    }

    public void setPersonal(ListaPersonal personal) {
        this.personal = personal;
    }

    public ListaRecetas getRecetas() {
        return recetas;
    }

    public void setRecetas(ListaRecetas recetas) {
        this.recetas = recetas;
    }

    public ListaPacientes getPacientes() {
        return pacientes;
    }

    public void setPacientes(ListaPacientes pacientes) {
        this.pacientes = pacientes;
    }

    public ListaMedicamentos getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(ListaMedicamentos medicamentos) {
        this.medicamentos = medicamentos;
    }

    // 🔐 Verificar credenciales del personal (login)
    public Personal loginPersonal(String id, String clave) {
        return personal.verificarCredenciales(id, clave);
    }

    // 💾 Métodos de persistencia
    public void guardarPersonal() {
        PersistenciaPersonalXML.guardar(personal);
    }

    public void cargarPersonal() {
        personal = PersistenciaPersonalXML.cargar();
        if (personal == null) personal = new ListaPersonal();
    }

    public void guardarRecetas() {
        PersistenciaRecetasXML.guardarRecetas(recetas);
    }

    public void cargarRecetas() {
        recetas = PersistenciaRecetasXML.cargarRecetas();
        if (recetas == null) recetas = new ListaRecetas();
    }

    public void guardarMedicamentos() {
        PersistenciaMedicamentosXML.guardar(medicamentos);
    }

    public void cargarMedicamentos() {
        medicamentos = PersistenciaMedicamentosXML.cargar();
        if (medicamentos == null) medicamentos = new ListaMedicamentos();
    }

    public void guardarPacientes() {
        PersistenciaPacientesXML.guardar(pacientes);
    }

    public void cargarPacientes() {
        pacientes = PersistenciaPacientesXML.cargar();
        if (pacientes == null) pacientes = new ListaPacientes();
    }
}
