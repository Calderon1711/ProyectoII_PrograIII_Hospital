package cliente.red;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Clase que gestiona los hilos del cliente para tareas de red.
//Usa un ThreadPool para evitar crear hilos nuevos constantemente.

public class GestorHilosCliente {

    private static GestorHilosCliente instance;
    private final ExecutorService pool;

    private GestorHilosCliente() {
        // Crea un pool fijo de hilos para manejar la comunicación
        pool = Executors.newFixedThreadPool(10);
    }

    public static synchronized GestorHilosCliente getInstance() {
        if (instance == null)
            instance = new GestorHilosCliente();
        return instance;
    }

    // Ejecuta una tarea en segundo plano (por ejemplo: escuchar mensajes, enviar solicitudes asíncronas)
    public void ejecutar(Runnable tarea) {
        pool.execute(tarea);
    }

     //Detiene todos los hilos del pool cuando se cierra el cliente.
    public void apagar() {
        pool.shutdownNow();
    }
}
