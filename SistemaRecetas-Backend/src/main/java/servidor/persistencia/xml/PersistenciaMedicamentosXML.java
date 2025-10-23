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

public class PersistenciaMedicamentosXML {

    public static void guardar(ListaMedicamentos lista) {
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

            // Nodo raíz <medicamentos>
            Element raiz = documento.createElement("medicamentos");
            documento.appendChild(raiz);

            // Obtener lista observable
            ObservableList<Medicamento> listaObs = lista.getMedicamentos();

            for (Medicamento m : listaObs) {
                Element medicamentoElem = documento.createElement("medicamento");

                // codigo
                Element codigoElem = documento.createElement("codigo");
                codigoElem.setTextContent(m.getCodigo() != null ? m.getCodigo() : "");
                medicamentoElem.appendChild(codigoElem);

                // nombre
                Element nombreElem = documento.createElement("nombre");
                nombreElem.setTextContent(m.getNombre() != null ? m.getNombre() : "");
                medicamentoElem.appendChild(nombreElem);

                // presentacion
                Element presentacionElem = documento.createElement("presentacion");
                presentacionElem.setTextContent(m.getPresentacion() != null ? m.getPresentacion() : "");
                medicamentoElem.appendChild(presentacionElem);

                raiz.appendChild(medicamentoElem);
                raiz.appendChild(documento.createTextNode("\n"));
            }

            // Guardar XML en archivo
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File("data/ListaMedicamentos.xml"));
            transformer.transform(source, result);

            System.out.println("Medicamentos guardados en ListaMedicamentos.xml");
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

    public static ListaMedicamentos cargar() {
        ListaMedicamentos lista = new ListaMedicamentos();
        try {
            File archivo = new File("data/ListaMedicamentos.xml");
            if (!archivo.exists()) {
                System.out.println("Archivo no encontrado: ListaMedicamentos.xml — devolviendo lista vacía.");
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

            NodeList nodos = documento.getElementsByTagName("medicamento");

            for (int i = 0; i < nodos.getLength(); i++) {
                Element medElem = (Element) nodos.item(i);

                String codigo = getTagValue("codigo", medElem);
                String nombre = getTagValue("nombre", medElem);
                String presentacion = getTagValue("presentacion", medElem);

                Medicamento m = new Medicamento(nombre, presentacion, codigo);
                lista.getMedicamentos().add(m);
            }

            System.out.println("Medicamentos cargados desde ListaMedicamentos.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
