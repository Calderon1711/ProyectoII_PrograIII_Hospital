package servidor.red;

import servidor.util.ConversorJSON;
import servidor.constantes.Comandos;
import servidor.servicio.*;
import servidor.Modelo.*;

import java.io.*;
import java.net.Socket;

public class HiloCliente implements Runnable {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public HiloCliente(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    @Override
    public void run() {
        try {
            System.out.println("Cliente conectado desde: " + socket.getInetAddress().getHostAddress());

            String linea;
            while ((linea = in.readLine()) != null) {

                Mensaje mensaje = ConversorJSON.deserializar(linea, Mensaje.class);
                if (mensaje == null) {
                    System.out.println("⚠ Mensaje nulo recibido, se ignora.");
                    continue;
                }

                System.out.println("🡒 Comando recibido: " + mensaje.getComando());

                Mensaje respuesta = procesarMensaje(mensaje);

                String respJson = ConversorJSON.serializar(respuesta);
                out.println(respJson);
                // System.out.println("🡐 Respuesta enviada: " + respJson);
            }

        } catch (Exception e) {
            System.out.println("Error en HiloCliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("Cliente desconectado: " + socket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Mensaje procesarMensaje(Mensaje mensaje) {
        String comando = mensaje.getComando();

        // Respuesta base: mismo id y comando
        Mensaje resp = new Mensaje();
        resp.setId(mensaje.getId());
        resp.setComando(comando);

        try {
            MedicamentoService medicamentoService = new MedicamentoService();
            PacienteService pacienteService = new PacienteService();
            PersonalService personalService = new PersonalService();
            RecetaService recetaService = new RecetaService();
            DetalleMedicamentoService detalleService = new DetalleMedicamentoService();

            switch (comando) {

                // ========== LOGIN ==========
                case Comandos.LOGIN_PERSONAL -> {
                    String jsonDatos = (String) mensaje.getDatos();
                    String[] datos = ConversorJSON.deserializar(jsonDatos, String[].class);

                    if (datos == null || datos.length < 2) {
                        resp.setExito(false);
                        resp.setMensaje("Datos de login inválidos");
                        return resp;
                    }

                    String id = datos[0];
                    String clave = datos[1];

                    Personal usuario = personalService.obtenerTodoPersonal()
                            .stream()
                            .filter(p -> p.getId().equalsIgnoreCase(id.trim())
                                    && p.getClave().equals(clave))
                            .findFirst()
                            .orElse(null);

                    if (usuario != null) {
                        resp.setExito(true);
                        resp.setMensaje("Login exitoso");
                        resp.setResultado(ConversorJSON.serializar(usuario));
                        System.out.println("✅ Login OK para: " + id);
                    } else {
                        resp.setExito(false);
                        resp.setMensaje("Usuario o contraseña incorrectos");
                        resp.setResultado(null);
                        System.out.println("❌ Login falló para: " + id);
                    }
                }

                // ========== CAMBIAR CLAVE ==========
                case Comandos.CAMBIAR_CLAVE -> {
                    String jsonDatos = (String) mensaje.getDatos();
                    String[] datos = ConversorJSON.deserializar(jsonDatos, String[].class);

                    if (datos == null || datos.length < 2) {
                        resp.setExito(false);
                        resp.setMensaje("Datos inválidos para cambiar clave");
                        return resp;
                    }

                    String id = datos[0];
                    String nuevaClave = datos[1];

                    boolean ok = personalService.actualizarClave(id, nuevaClave);

                    if (ok) {
                        resp.setExito(true);
                        resp.setMensaje("Contraseña actualizada con éxito");
                        System.out.println("✅ Clave actualizada para: " + id);
                    } else {
                        resp.setExito(false);
                        resp.setMensaje("No se pudo actualizar la contraseña");
                        System.out.println("❌ Error actualizando clave para: " + id);
                    }
                }

                // ========== EJEMPLOS (ajusta igual que antes) ==========
                case Comandos.LISTAR_PERSONAL -> {
                    resp.setExito(true);
                    resp.setMensaje("Lista de personal");
                    resp.setResultado(ConversorJSON.serializar(personalService.obtenerTodoPersonal()));
                }

                case Comandos.LISTAR_PACIENTES -> {
                    resp.setExito(true);
                    resp.setMensaje("Lista de pacientes");
                    resp.setResultado(ConversorJSON.serializar(pacienteService.obtenerTodosPacientes()));
                }

                // Aquí agregás los demás comandos (MEDICAMENTOS, RECETAS, etc.) igual:
                // resp.setExito(true/false);
                // resp.setMensaje("...");
                // resp.setResultado(ConversorJSON.serializar(obj));

                default -> {
                    resp.setExito(false);
                    resp.setMensaje("Comando no reconocido: " + comando);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setExito(false);
            resp.setMensaje("Error procesando comando: " + e.getMessage());
        }

        return resp;
    }
}
