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

public class PersistenciaPersonalXML {

    public static void guardar(ListaPersonal lista) {
        try{
            //Factory para crear DocumentBuilder(DOM)
            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();//factory

            //esto solo son medidas de seguridad(no es necesario esto es por si cargamos un archivo malicioso o asi  )
            try{
                //Este evita que alguien meta definiciones externas peligrosas
                Factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                //esta bloquea las entidades externas generales
                Factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                //esta es casi lo mismo q la de arriba
                Factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                //esta deshabilita incluir otros xml dentro del doc
                Factory.setXIncludeAware(false);
                //evita que se resuelvan cosas raras automáticamente.
                Factory.setExpandEntityReferences(false);
            }catch(ParserConfigurationException ignored){}

                DocumentBuilder Builder = Factory.newDocumentBuilder();//DOM
                Document documento = Builder.newDocument(); // Crea un nuevo documento XML en memoria.

                //Nodo Raiz <Personal>
                Element raiz = documento.createElement("personal");//<personal>
                documento.appendChild(raiz);//inserto la raiz en el documento

                //Se Toma la ObservableList
                ObservableList<Personal> listaobs= lista.getPersonal();
                //recorre toda la lista
                for(Personal p : listaobs){
                    Element personaElem=documento.createElement("persona"); //<persona>

                    //guardar el tipo
                    //-------------------*
                    if (p.tipo() != null) {
                        personaElem.setAttribute("tipo", p.tipo());
                    } else {
                        personaElem.setAttribute("tipo", "");
                    }

                    //-----------------------------*

                    //guardar id
                    //--------------------------------------*
                    Element idElem=documento.createElement("id");
                    if (p.getId() != null) {
                        idElem.setTextContent(p.getId());
                    } else {
                        idElem.setTextContent("");
                    }
                    personaElem.appendChild(idElem);
                    //-----------------------------------*

                    //guardar nombre
                    //--------------------------------------*
                    Element nombreElem=documento.createElement("nombre");
                    if (p.getNombre() != null) {
                        nombreElem.setTextContent(p.getNombre());
                    }else {
                        nombreElem.setTextContent("");
                    }
                    personaElem.appendChild(nombreElem);
                    //--------------------------------------*

                    //guardarClave
                    //*--------------------------------------*
                    Element claveElem=documento.createElement("clave");
                    if(p.getClave() != null) {
                        claveElem.setTextContent(p.getClave());
                    }else{
                        claveElem.setTextContent("");
                    }
                    personaElem.appendChild(claveElem);
                    //-------------------------------------------*

                    //guardarRol
                    //---------------------------------------------*
                    Element rolElem= documento.createElement("rol");
                    if(p.getRol() != null) {
                        rolElem.setTextContent(p.getRol().toString());
                    }else {
                        rolElem.setTextContent("");
                    }
                    personaElem.appendChild(rolElem);
                    //-----------------------------------------------*

                    // Campo exclusivo de Medico
                    if (p instanceof Medico) {
                        Medico m = (Medico) p;
                        Element espElem = documento.createElement("especialidad");
                        if(((Medico) p).getEspecialidad() != null) {
                            espElem.setTextContent(((Medico) p).getEspecialidad());
                        }else{
                            espElem.setTextContent("");
                        }
                        personaElem.appendChild(espElem);
                    }

                    raiz.appendChild(personaElem);
                    Text salto = documento.createTextNode("\n");
                    raiz.appendChild(salto);
                }
            // Transformador para escribir el DOM en archivo
            TransformerFactory tf = TransformerFactory.newInstance();// crea una fabrica es responsable de convertir un documento DOM a un xml
            Transformer transformer = tf.newTransformer();//el que toma el Document y lo convierta en un archivo
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//Le indica al transformador que indente el XML al escribirlo
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");//codifique el archivo en UTF-8.
            // Propiedad para controlar cantidad de espacios en la indentación (funciona en Xalan/Apache)
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");//controla cuántos espacios se usan por nivel de indentación.

            DOMSource source = new DOMSource(documento); //es un contenedor que le dice al transformador que se va transformar
            StreamResult result = new StreamResult(new File("data/ListaPersonal.xml"));//indica dónde se va a escribir el XML.
            transformer.transform(source, result);//aca se transforma

            System.out.println(" Personal guardado en " + "ListaPersonal.xml");
        }catch(ParserConfigurationException | TransformerException e){
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


    public static ListaPersonal cargar() {
        ListaPersonal lista = new ListaPersonal();
        try {
            //verificamos
            File archivo = new File("data/ListaPersonal.xml");
            if (!archivo.exists()) {
                System.out.println(" Archivo no encontrado: " + "ListaPersonal.xml" + " — devolviendo lista vacía.");
                return lista;
            }

            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance(); //fabrica
            // seguridad básica
            try {
                Factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                Factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                Factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                Factory.setXIncludeAware(false);
                Factory.setExpandEntityReferences(false);
            } catch (ParserConfigurationException ignored) {}

            DocumentBuilder Builder = Factory.newDocumentBuilder(); //DOM
            Document documento = Builder.parse(archivo);//lee el archivo XML y lo convierte en un objeto Document
            documento.getDocumentElement().normalize(); //limpia nodos vacíos y espacios innecesarios en el árbol XML.

            NodeList nodos = documento.getElementsByTagName("persona");
            //corremos todos los nodos <persona>
            for (int i = 0; i < nodos.getLength(); i++) {
                Element personaElem= (Element) nodos.item(i);
                //leemos los datos
                String tipo= personaElem.getAttribute("tipo");
                String id= getTagValue("id",personaElem);
                String nombre = getTagValue("nombre",personaElem);
                String clave = getTagValue("clave",personaElem);
                String rolText = getTagValue( "rol",personaElem);
                Rol rol = null;

                //esto es para convertir el<Rol> a un valor de enum ya q se guardo como ""
                //-------------------------------------------------*
                if (rolText != null && !rolText.isEmpty()) {
                    try { rol = Rol.valueOf(rolText);
                    } catch (IllegalArgumentException ignored) {
                        rol = null;
                    }
                }
                //------------------------------------------------------*
                //creamos los de personal haciendo un dynamic cast
                Personal p =null;
                if("Medico".equalsIgnoreCase(tipo)||rol== Rol.MEDICO){
                    String especialidad = getTagValue("especialidad",personaElem);
                    p=new Medico(nombre,id,clave,especialidad,Rol.MEDICO);
                } else if ("Farmaceuta".equalsIgnoreCase(tipo)||rol==Rol.FARMACEUTICO) {
                    p= new Farmaceuta(nombre,id,clave,rol);
                } else if ("Administrador".equalsIgnoreCase(tipo)||rol== Rol.ADMINISTRADOR) {
                    p=new Administrador(nombre,id,clave,rol);
                }else{
                    System.err.println(" Tipo no reconocido en XML (fila " + i + "): tipo='" + tipo + "', rol='" + rolText + "'. Entrada ignorada.");
                    continue;
                }
                lista.getPersonal().add(p);
            }

            System.out.println(" Personal cargado desde " + "PersonalXML");
        }catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
