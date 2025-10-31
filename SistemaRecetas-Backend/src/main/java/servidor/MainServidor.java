package servidor;

import servidor.red.ServidorSocket;
import java.io.IOException;

public class MainServidor {
    public static void main(String[] args) throws IOException {
        ServidorSocket servidor = new ServidorSocket(5000, 10);
        servidor.iniciar();
    }
}

