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

    }
}
