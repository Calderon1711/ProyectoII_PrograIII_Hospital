package servidor.constantes;


//Clase para la comunicacion entre backend y frontend
//Esta clase lo que hace es asegurar el mismo mensaje que se envia y se recibe
//Se debe implementar en el backend

public class Comandos {

    //Mensaje de ProxyMedicamento
    public static final String LISTAR_MEDICAMENTOS = "listar_medicamento";
    public static final String AGREGAR_MEDICAMENTO = "agregar_medicamento";
    public static final String ELIMINAR_MEDICAMENTO = "eliminar_medicamento";
    public static final String CONSULTAR_MEDICAMENTO= "consultar_medicamento";
    public static final String ACTUALIZAR_MEDICAMENTO= "actualizar_medicamento";

    //Mensaje de ProxyDetalleMedicamento
    public static final String LISTAR_DETALLEMEDICAMENTOS = "listar_detallemedicamento";
    public static final String AGREGAR_DETALLEMEDICAMENTO = "agregar_detallemedicamento";
    public static final String ELIMINAR_DETALLEMEDICAMENTO = "eliminar_detallemedicamento";
    public static final String CONSULTAR_DETALLEMEDICAMENTO = "consultar_detallemedicamento";
    public static final String ACTUALIZAR_DETALLEMEDICAMENTO = "actualizar_detallemedicamento";

    //Mensajes de ProxyRecetas
    public static final String LISTAR_RECETAS = "listar_recetas";
    public static final String AGREGAR_RECETA = "agregar_receta";
    public static final String ELIMINAR_RECETA = "eliminar_receta";
    public static final String CONSULTAR_RECETA = "consultar_receta";
    public static final String ACTUALIZAR_RECETA = "actualizar_receta";

    //Mensaje de ProxyAdministrador
    public static final String LISTAR_ADMINISTRADORES = "listar_administrador";
    public static final String AGREGAR_ADMINISTRADOR = "agregar_administrador";
    public static final String ELIMINAR_ADMINISTRADOR = "eliminar_administrador";
    public static final String CONSULTAR_ADMINISTRADOR = "consultar_administrador";
    public static final String ACTUALIZAR_ADMINISTRADOR = "actualizar_administrador";

    //Mensaje de ProxyFarmaceutica
    public static final String LISTAR_FARMACEUTICAS = "listar_farmaceutica";
    public static final String AGREGAR_FARMACEUTICA = "agregar_farmaceutica";
    public static final String ELIMINAR_FARMACEUTICA = "eliminar_farmaceutica";
    public static final String CONSULTAR_FARMACEUTICA = "consultar_farmaceutica";
    public static final String ACTUALIZAR_FARMACEUTICA = "actualizar_farmaceutica";

    //Mensaje de ProxyHospital
    public static final String LISTAR_HOSPITALES = "listar_hospital";
    public static final String AGREGAR_HOSPITAL = "agregar_hospital";
    public static final String ELIMINAR_HOSPITAL = "eliminar_hospital";
    public static final String CONSULTAR_HOSPITAL = "consultar_hospital";
    public static final String ACTUALIZAR_HOSPITAL = "actualizar_hospital";

    //Mensaje de ProxyMedico
    public static final String LISTAR_MEDICOS = "listar_medico";
    public static final String AGREGAR_MEDICO = "agregar_medico";
    public static final String ELIMINAR_MEDICO = "eliminar_medico";
    public static final String CONSULTAR_MEDICO = "consultar_medico";
    public static final String ACTUALIZAR_MEDICO = "actualizar_medico";

    //Mensaje de ProxyPaciente
    public static final String LISTAR_PACIENTES = "listar_paciente";
    public static final String AGREGAR_PACIENTE = "agregar_paciente";
    public static final String ELIMINAR_PACIENTE = "eliminar_paciente";
    public static final String CONSULTAR_PACIENTE = "consultar_paciente";
    public static final String ACTUALIZAR_PACIENTE = "actualizar_paciente";

}
