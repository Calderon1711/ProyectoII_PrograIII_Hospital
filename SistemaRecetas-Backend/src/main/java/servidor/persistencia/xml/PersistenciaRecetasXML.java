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

public class PersistenciaRecetasXML {

    private static final String RUTA_ARCHIVO = "data/ListaRecetas.xml";

    // ================== GUARDAR ==================
    public static void guardarRecetas(ListaRecetas lista) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("ListaRecetas");
            doc.appendChild(root);

            for (Receta receta : lista.getRecetas()) {
                Element recetaElement = doc.createElement("Receta");
                recetaElement.setAttribute("id", receta.getId());

                // Paciente
                Element pacienteElement = doc.createElement("Paciente");
                pacienteElement.setAttribute("id", receta.getPaciente().getId());
                pacienteElement.setAttribute("nombre", receta.getPaciente().getNombre());
                recetaElement.appendChild(pacienteElement);

                // Personal (médico)
                Element personalElement = doc.createElement("Personal");
                personalElement.setAttribute("id", receta.getPersonal().getId());
                personalElement.setAttribute("nombre", receta.getPersonal().getNombre());
                recetaElement.appendChild(personalElement);

                // Fechas y estado
                Element fechaPrescripcion = doc.createElement("FechaPrescripcion");
                fechaPrescripcion.setTextContent(receta.getFechaPrescripcion().toString());
                recetaElement.appendChild(fechaPrescripcion);

                Element fechaRetiro = doc.createElement("FechaRetiro");
                if (receta.getFechaRetiro() != null) {
                    fechaRetiro.setTextContent(receta.getFechaRetiro().toString());
                }
                recetaElement.appendChild(fechaRetiro);

                Element estado = doc.createElement("Estado");
                estado.setTextContent(String.valueOf(receta.getEstado()));
                recetaElement.appendChild(estado);

                // Detalles de medicamentos
                Element detallesElement = doc.createElement("Detalles");
                for (DetalleMedicamento detalle : receta.getDetalleMedicamentos()) {
                    Element detalleElement = doc.createElement("DetalleMedicamento");
                    detalleElement.setAttribute("idDetalle", detalle.getIdDetalle());
                    detalleElement.setAttribute("cantidad", String.valueOf(detalle.getCantidad()));
                    detalleElement.setAttribute("duracion", String.valueOf(detalle.getDuracion()));
                    detalleElement.setAttribute("indicacion", detalle.getIndicacion());

                    // Medicamento dentro del detalle
                    Medicamento med = detalle.getMedicamento();
                    Element medElement = doc.createElement("Medicamento");
                    medElement.setAttribute("codigo", med.getCodigo());
                    medElement.setAttribute("nombre", med.getNombre());
                    medElement.setAttribute("presentacion", med.getPresentacion());

                    detalleElement.appendChild(medElement);
                    detallesElement.appendChild(detalleElement);
                }
                recetaElement.appendChild(detallesElement);

                root.appendChild(recetaElement);
            }

            // Guardar en archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(RUTA_ARCHIVO));
            transformer.transform(source, result);

            System.out.println(" Recetas guardadas en " + RUTA_ARCHIVO);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    // ================== CARGAR ==================
    public static ListaRecetas cargarRecetas() {
        ListaRecetas lista = new ListaRecetas();
        try {
            File archivo = new File(RUTA_ARCHIVO);
            if (!archivo.exists()) {
                return lista; // Si no existe el XML, devolvemos lista vacía
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);
            doc.getDocumentElement().normalize();

            NodeList recetasNodes = doc.getElementsByTagName("Receta");

            for (int i = 0; i < recetasNodes.getLength(); i++) {
                Element recetaElement = (Element) recetasNodes.item(i);

                String id = recetaElement.getAttribute("id");

                // Paciente
                Element pacienteElement = (Element) recetaElement.getElementsByTagName("Paciente").item(0);
                Paciente paciente = new Paciente();
                paciente.setId(pacienteElement.getAttribute("id"));
                paciente.setNombre(pacienteElement.getAttribute("nombre"));

                // Personal (médico)
                Element personalElement = (Element) recetaElement.getElementsByTagName("Personal").item(0);
                Personal personal = new Medico();
                personal.setId(personalElement.getAttribute("id"));
                personal.setNombre(personalElement.getAttribute("nombre"));

                // Fechas y estado
                LocalDate fechaPrescripcion = LocalDate.parse(
                        recetaElement.getElementsByTagName("FechaPrescripcion").item(0).getTextContent()
                );
                String fechaRetiroStr = recetaElement.getElementsByTagName("FechaRetiro").item(0).getTextContent();
                LocalDate fechaRetiro = fechaRetiroStr == null || fechaRetiroStr.isEmpty() ? null : LocalDate.parse(fechaRetiroStr);

                int estado = Integer.parseInt(recetaElement.getElementsByTagName("Estado").item(0).getTextContent());

                Receta receta = new Receta(id, personal, paciente, fechaPrescripcion, fechaRetiro, estado);

                // Detalles de medicamentos
                NodeList detallesNodes = recetaElement.getElementsByTagName("DetalleMedicamento");
                for (int j = 0; j < detallesNodes.getLength(); j++) {
                    Element detalleElement = (Element) detallesNodes.item(j);

                    String idDetalle = detalleElement.getAttribute("idDetalle");
                    int cantidad = Integer.parseInt(detalleElement.getAttribute("cantidad"));
                    int duracion = Integer.parseInt(detalleElement.getAttribute("duracion"));
                    String indicacion = detalleElement.getAttribute("indicacion");

                    // Medicamento dentro del detalle
                    Element medElement = (Element) detalleElement.getElementsByTagName("Medicamento").item(0);
                    Medicamento med = new Medicamento(
                            medElement.getAttribute("nombre"),
                            medElement.getAttribute("presentacion"),
                            medElement.getAttribute("codigo")
                    );

                    DetalleMedicamento detalle = new DetalleMedicamento(med, idDetalle, cantidad, duracion, indicacion);
                    receta.insertarDetalleMedicamento(detalle);
                }

                lista.insertarReceta(receta);
            }

            System.out.println(" Recetas cargadas desde " + RUTA_ARCHIVO);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
