package cliente.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeChat {
    private String remitente;
    private String destinatario;
    private String contenido;
}
