package servidor.red;
import servidor.util.ConversorJSON;
import servidor.constantes.Comandos;
import servidor.servicio.*; // Para llamar los Service MedicamentoService, PacienteService)
import servidor.Modelo.*;
import java.io.*;
import java.net.Socket;

//la funcion de esta clase es que cada vez que el servidor acepta uan conexion de una clientes, crea un nuevo hilo

public class HiloCliente implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    //contructor
    public HiloCliente(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            System.out.println("Cliente conectado desde: " + socket.getInetAddress().getHostName());

            //Escuchar mensajes mientrtas el cliente esta conectado
            String mensajeJSON;

            while((mensajeJSON=in.readLine()) != null){
             //pasamos el mensaje de JSON a un objeto mensaje
             Mensaje mensaje= ConversorJSON.deserializar(mensajeJSON, Mensaje.class);
                System.out.println("comando recibido: "+mensaje.getComando());

                //Procesamos el mensaje segun el comando
                Mensaje respuesta = procesarMensaje(mensaje);//se crea abajo
              //enviamos la respuesta al cliente
            String respuestaJSON= ConversorJSON.serializar(respuesta);
            out.println(respuestaJSON);
            }
        }catch (Exception e){
            System.out.println("Error en hiloCliente: " + e.getMessage());
        }finally {
            try{
                socket.close();
                System.out.println("Cliente desconectado: " + socket.getInetAddress());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private Mensaje procesarMensaje(Mensaje mensaje){
        try{
            String comando= mensaje.getComando();

            //Instanciamos los servicios que vamos a usar
            MedicamentoService medicamentoService = new MedicamentoService();
            PacienteService pacienteService = new PacienteService();
            PersonalService personalService = new PersonalService();
            RecetaService recetaService = new RecetaService();
            DetalleMedicamentoService detalleService = new DetalleMedicamentoService();

            switch (comando){
                case Comandos.LISTAR_MEDICAMENTOS -> {
                    return new Mensaje(true, "Lista de medicamentos",
                            ConversorJSON.serializar(medicamentoService.obtenerMedicamentos()));
                }
                case Comandos.AGREGAR_MEDICAMENTO -> {
                    Medicamento med = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Medicamento.class);
                    medicamentoService.agregarMedicamento(med);
                    return new Mensaje(true, "Medicamento agregado con éxito", null);
                }
                case Comandos.ELIMINAR_MEDICAMENTO -> {
                    Medicamento med = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Medicamento.class);
                    medicamentoService.eliminarMedicamento(med.getCodigo());
                    return new Mensaje(true, "Medicamento eliminado con éxito", null);
                }
                case Comandos.CONSULTAR_MEDICAMENTO -> {
                    Medicamento med = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Medicamento.class);
                    Medicamento encontrado = medicamentoService.consultarMedicamento(med.getCodigo());
                    return new Mensaje(true, "Medicamento consultado",
                            ConversorJSON.serializar(encontrado));
                }
                case Comandos.ACTUALIZAR_MEDICAMENTO -> {
                    Medicamento med = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Medicamento.class);
                    medicamentoService.actualizarMedicamento(med);
                    return new Mensaje(true, "Medicamento actualizado", null);
                }

                // ----------------- DetalleMedicamento -----------------
                case Comandos.LISTAR_DETALLEMEDICAMENTOS -> {
                    return new Mensaje(true, "Lista de detalles de medicamentos",
                            ConversorJSON.serializar(detalleService.listarDetalles()));
                }
                case Comandos.AGREGAR_DETALLEMEDICAMENTO -> {
                    DetalleMedicamento detalle = ConversorJSON.deserializar(mensaje.getObjeto().toString(), DetalleMedicamento.class);
                    detalleService.agregarDetalle(detalle);
                    return new Mensaje(true, "Detalle agregado con éxito", null);
                }
                case Comandos.ELIMINAR_DETALLEMEDICAMENTO -> {
                    DetalleMedicamento detalle = ConversorJSON.deserializar(mensaje.getObjeto().toString(), DetalleMedicamento.class);
                    detalleService.eliminarDetalle(detalle.getIdDetalle(), detalle.getMedicamento().getCodigo());
                    return new Mensaje(true, "Detalle eliminado con éxito", null);
                }
                case Comandos.CONSULTAR_DETALLEMEDICAMENTO -> {
                    DetalleMedicamento detalle = ConversorJSON.deserializar(mensaje.getObjeto().toString(), DetalleMedicamento.class);
                    DetalleMedicamento encontrado = detalleService.consultarDetalleMedicamento(detalle.getIdDetalle(), detalle.getMedicamento().getCodigo());
                    return new Mensaje(true, "Detalle consultado", ConversorJSON.serializar(encontrado));
                }
                case Comandos.ACTUALIZAR_DETALLEMEDICAMENTO -> {
                    DetalleMedicamento detalle = ConversorJSON.deserializar(mensaje.getObjeto().toString(), DetalleMedicamento.class);
                    detalleService.actualizarDetalle(detalle);
                    return new Mensaje(true, "Detalle actualizado", null);
                }
                //------------------Login---------------------
                case Comandos.LOGIN_PERSONAL -> {
                    String[] datos = ConversorJSON.deserializar(mensaje.getObjeto().toString(), String[].class);
                    String id = datos[0];
                    String clave = datos[1];
                    Personal usuario = personalService.obtenerTodoPersonal()
                            .stream()
                            .filter(p -> p.getId().equals(id) && p.getClave().equals(clave))
                            .findFirst().orElse(null);
                    return new Mensaje(usuario != null, usuario != null ? "Login exitoso" : "Credenciales incorrectas",
                            usuario != null ? ConversorJSON.serializar(usuario) : null);
                }

                case Comandos.CAMBIAR_CLAVE -> {
                    String[] datos = ConversorJSON.deserializar(mensaje.getObjeto().toString(), String[].class);
                    boolean ok = personalService.actualizarClave(datos[0], datos[1]);
                    return new Mensaje(ok, ok ? "Contraseña actualizada" : "Error al actualizar", null);
                }

                // ----------------- Recetas -----------------
                case Comandos.LISTAR_RECETAS -> {
                    return new Mensaje(true, "Lista de recetas",
                            ConversorJSON.serializar(recetaService.listarRecetas()));
                }
                case Comandos.AGREGAR_RECETA -> {
                    Receta receta = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Receta.class);
                    recetaService.agregarReceta(receta, receta.getDetalleMedicamentos());
                    return new Mensaje(true, "Receta agregada con éxito", null);
                }
                case Comandos.ELIMINAR_RECETA -> {
                    Receta receta = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Receta.class);
                    recetaService.eliminarReceta(receta.getId());
                    return new Mensaje(true, "Receta eliminada con éxito", null);
                }
                case Comandos.CONSULTAR_RECETA -> {
                    Receta receta = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Receta.class);
                    Receta encontrado = recetaService.consultarReceta(receta.getId());
                    return new Mensaje(true, "Receta consultada", ConversorJSON.serializar(encontrado));
                }
                case Comandos.ACTUALIZAR_RECETA -> {
                    Receta receta = ConversorJSON.deserializar(mensaje.getObjeto().toString(), Receta.class);
                    recetaService.actualizarEstado(receta.getId(), receta.getEstado());
                    return new Mensaje(true, "Receta actualizada", null);
                }

                // ----------------- Personal -----------------
                case Comandos.Listar_Personal -> {
                    return new Mensaje(true, "Lista de personal",
                            ConversorJSON.serializar(personalService.obtenerTodoPersonal()));
                }

                // ----------------- Pacientes -----------------
                case Comandos.LISTAR_PACIENTES -> {
                    return new Mensaje(true, "Lista de pacientes",
                            ConversorJSON.serializar(pacienteService.obtenerTodosPacientes()));
                }


                default -> {
                    return new Mensaje(false, "Comando no reconocido: " + comando, null);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            return new Mensaje(false, "Error procesando comando: " + e.getMessage(),null);
        }
    }
}
