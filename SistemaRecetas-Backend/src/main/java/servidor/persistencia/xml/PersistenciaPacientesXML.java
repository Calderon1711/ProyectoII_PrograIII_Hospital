package servidor.persistencia.xml;

import servidor.Modelo.*;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;

public class PersistenciaPacientesXML {

    public static void guardar(ListaPacientes lista) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Seguridad básica
            try {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                factory.setXIncludeAware(false);
                factory.setExpandEntityReferences(false);
            } catch (ParserConfigurationException ignored) {}

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();

            // Nodo raíz <pacientes>
            Element raiz = documento.createElement("pacientes");
            documento.appendChild(raiz);

            // Obtener lista observable
            ObservableList<Paciente> listaObs = lista.getPacientes();

            for (Paciente p : listaObs) {
                Element pacienteElem = documento.createElement("paciente");

                // id
                Element idElem = documento.createElement("id");
                idElem.setTextContent(p.getId() != null ? p.getId() : "");
                pacienteElem.appendChild(idElem);

                // nombre
                Element nombreElem = documento.createElement("nombre");
                nombreElem.setTextContent(p.getNombre() != null ? p.getNombre() : "");
                pacienteElem.appendChild(nombreElem);

                // fechaNacimiento
                Element fechaElem = documento.createElement("fechaNacimiento");
                fechaElem.setTextContent(p.getFechaNacimiento() != null ? p.getFechaNacimiento().toString() : "");
                pacienteElem.appendChild(fechaElem);

                // telefono
                Element telefonoElem = documento.createElement("telefono");
                telefonoElem.setTextContent(String.valueOf(p.getTelefono()));
                pacienteElem.appendChild(telefonoElem);

                raiz.appendChild(pacienteElem);
                raiz.appendChild(documento.createTextNode("\n"));
            }

            // Guardar XML en archivo
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File("data/ListaPacientes.xml"));
            transformer.transform(source, result);

            System.out.println("Pacientes guardados en ListaPacientes.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTagValue(String tag, Element element) {
        NodeList lista = element.getElementsByTagName(tag);
        if (lista != null && lista.getLength() > 0) {
            Node nodo = lista.item(0);
            if (nodo != null) {
                return nodo.getTextContent();
            }
        }
        return null;
    }

    public static ListaPacientes cargar() {
        ListaPacientes lista = new ListaPacientes();
        try {
            File archivo = new File("data/ListaPacientes.xml");
            if (!archivo.exists()) {
                System.out.println("Archivo no encontrado: ListaPacientes.xml — devolviendo lista vacía.");
                return lista;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                factory.setXIncludeAware(false);
                factory.setExpandEntityReferences(false);
            } catch (ParserConfigurationException ignored) {}

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(archivo);
            documento.getDocumentElement().normalize();

            NodeList nodos = documento.getElementsByTagName("paciente");

            for (int i = 0; i < nodos.getLength(); i++) {
                Element pacienteElem = (Element) nodos.item(i);

                String id = getTagValue("id", pacienteElem);
                String nombre = getTagValue("nombre", pacienteElem);
                String fechaText = getTagValue("fechaNacimiento", pacienteElem);
                String telefonoText = getTagValue("telefono", pacienteElem);

                LocalDate fechaNacimiento = null;
                if (fechaText != null && !fechaText.isEmpty()) {
                    fechaNacimiento = LocalDate.parse(fechaText);
                }

                int telefono = 0;
                if (telefonoText != null && !telefonoText.isEmpty()) {
                    try {
                        telefono = Integer.parseInt(telefonoText);
                    } catch (NumberFormatException ignored) {}
                }

                Paciente p = new Paciente(telefono, fechaNacimiento, nombre, id);
                lista.getPacientes().add(p);
            }

            System.out.println("Pacientes cargados desde ListaPacientes.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
