package servidor.red;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorSocket {
    private final int puerto;
    private final ExecutorService pool;
    private boolean activo= true;

    public ServidorSocket(int puerto,int maxConexiones) {
        this.puerto = puerto;
        this.pool = Executors.newFixedThreadPool(maxConexiones);
    }

    public void iniciar() throws IOException {
        try(ServerSocket servidor= new ServerSocket(puerto))  {
            System.out.println("Servidor iniciado en el puerto "+puerto);
            while (activo) {
                Socket cliente= servidor.accept();
                System.out.println("Cliente conectado desde "+cliente.getInetAddress());

                //Creo un nuevo hilo por cada cliente
                HiloCliente hilo= new  HiloCliente(cliente);
                pool.execute(hilo);
            }

        }catch(IOException e){
            System.err.println("Error al iniciar el servidor"+e.getMessage());

        }finally{
            pool.shutdown();
        }
    }

    public void detener(){
        activo= false;
        pool.shutdown();
        System.out.println("Servidor Detenido");
    }



}
