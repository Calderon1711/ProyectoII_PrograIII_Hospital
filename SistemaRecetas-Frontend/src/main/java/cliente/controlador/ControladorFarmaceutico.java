package Controlador;

import Modelo.*;
import Vista.FarmaceuticoVista;
import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;



public class ControladorFarmaceutico {
    private FarmaceuticoVista vista;
    private ListaRecetas listaRecetas;
    private static Hospital hospital=Hospital.getInstance();
    private ListaRecetas listaRecetas2;

    public ControladorFarmaceutico(FarmaceuticoVista vista, Personal personalFarma) {
        this.vista = vista;
        this.listaRecetas = hospital.getRecetas();

        System.out.println("Farmaceutico Vista Iniciada");

        this.vista.onBuscar(this::buscarRecetas);
        this.vista.onLimpiar(this::limpiarTabla);
        this.vista.onRefrescar(this::refrescarTabla);
        this.vista.onProceso(this::iniciarProceso);
        this.vista.onLista(this::marcarLista);
        this.vista.onEntregar(this::entregarReceta);

        refrescarTabla();

    }


    private void iniciarProceso(){
        FarmaceuticoVista.RecetaRow row=vista.getRecetaSeleccionada();
        if(row==null){
            JOptionPane.showMessageDialog(vista, "Seleccione una receta", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Receta r=listaRecetas.getRecetaPorID(row.id);
        if (r == null ) {
            JOptionPane.showMessageDialog(vista, "No se encontro la receta seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(r.getEstado()!=2){
            JOptionPane.showMessageDialog(vista, "Solo se pueden poner en PROCESO recetas CONFECCIONADAS", "Regla de negocio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!estaEnVentanaRetiro(r.getFechaRetiro(),3)){
            JOptionPane.showMessageDialog(vista, "La receta no esta dentro de la ventana de retiro (+3 días).", "Regla de negocio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        r.setEstado(1);
        vista.setMensaje("Receta #"+r.getId()+"marcada como EN PROCESO.");
        refrescarTablaConservarSeleccion(r.getId());
    }

    private void marcarLista(){
        FarmaceuticoVista.RecetaRow row=vista.getRecetaSeleccionada();
        if(row==null){
            JOptionPane.showMessageDialog(vista, "Seleccione una receta", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Receta r=listaRecetas.getRecetaPorID(row.id);
        if (r == null ) {
            JOptionPane.showMessageDialog(vista, "No se encontro la receta seleccionda", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(r.getEstado()!=1){
            JOptionPane.showMessageDialog(vista, "Solo se pueden marcar como LISTA las recetas en PROCESO.", "Regla de negocio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        r.setEstado(3);
        vista.setMensaje("Receta #"+r.getId()+" ENTREGADA al paciente.");
        refrescarTablaConservarSeleccion(r.getId());
    }

    private void entregarReceta(){
        FarmaceuticoVista.RecetaRow row=vista.getRecetaSeleccionada();
        if(row==null){
            JOptionPane.showMessageDialog(vista, "Seleccione una receta", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Receta r=listaRecetas.getRecetaPorID(row.id);
        if (r == null ) {
            JOptionPane.showMessageDialog(vista, "No se encontro la receta seleccionda", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(r.getEstado()!=3){
            JOptionPane.showMessageDialog(vista, "Solo se pueden ENTREGAR recetas que esten LISTAS", "Regla de negocio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        r.setEstado(4);
        vista.setMensaje("Receta #"+r.getId()+" ENTREGADA al paciente.");
        refrescarTablaConservarSeleccion(r.getId());
    }

    private void buscarRecetas() {
        String texto = vista.getTextoBuscar();
        String filtro = vista.getTipoFiltro();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un texto para buscar.");
            return;
        }

        List<Receta> base = new ArrayList<>(listaRecetas.getRecetas());
        String q = texto.toLowerCase(Locale.ROOT).trim();

        List<Receta> filtradas = switch (filtro == null ? "" : filtro.toLowerCase(Locale.ROOT)) {
            // Busca por ID de RECETA (#Receta)
            case "id receta", "#receta", "codigo", "receta" -> base.stream()
                    .filter(r -> r.getId() != null && r.getId().toLowerCase(Locale.ROOT).contains(q))
                    .collect(Collectors.toList());

            // 👉 NUEVO: busca por ID de PACIENTE (tu combo dice "ID Paciente")
            case "id paciente", "paciente id", "id" -> base.stream()
                    .filter(r -> r.getPaciente() != null
                            && r.getPaciente().getId() != null
                            && r.getPaciente().getId().toLowerCase(Locale.ROOT).contains(q))
                    .collect(Collectors.toList());

            // Por nombre del paciente (si algún día agregas la opción)
            case "paciente", "nombre" -> base.stream()
                    .filter(r -> r.getPaciente() != null
                            && r.getPaciente().getNombre() != null
                            && r.getPaciente().getNombre().toLowerCase(Locale.ROOT).contains(q))
                    .collect(Collectors.toList());

            // Si no coincide, no filtra
            default -> base;
        };

        boolean fConf = vista.isEstadoConfeccionada();
        boolean fProc = vista.isEstadoProceso();
        boolean fLista = vista.isEstadoLista();

        if (fConf || fProc || fLista) {
            filtradas = filtradas.stream()
                    .filter(r ->
                            (fConf && r.getEstado() == 2) ||
                                    (fProc && r.getEstado() == 1) ||
                                    (fLista && r.getEstado() == 3))
                    .collect(Collectors.toList());
        }

        setTablaDesdeModelo(filtradas);
    }
    private void refrescarTabla() {
        LocalDate hoy=LocalDate.now();
        List<Receta> activas= listaRecetas.getRecetas().stream()
                .filter(r->r.getEstado()==2||r.getEstado()==1||r.getEstado()==3)
                .filter(r->estaEnVentanaRetiro(r.getFechaRetiro(),3))
                .collect(Collectors.toList());
        setTablaDesdeModelo(activas);
        vista.setMensaje("Mostrando recetas activas para retiro alrededor de hoy ("+hoy+")");
    }
    private void limpiarTabla() {
        vista.setRecetas(new ArrayList<>());
        vista.setMensaje("Tabla limpiada");
    }

    private void refrescarTablaConservarSeleccion(String idReceta){
        List<FarmaceuticoVista.RecetaRow>filas= construirFilas(listaRecetas.getRecetas());
        vista.setRecetas(filas);
    }
    private void setTablaDesdeModelo(List<Receta> recetas) {
        List<FarmaceuticoVista.RecetaRow>filas= construirFilas(recetas);
        vista.setRecetas(filas);
    }
    private List<FarmaceuticoVista.RecetaRow> construirFilas(List<Receta> recetas) {
        List<FarmaceuticoVista.RecetaRow> filas=new ArrayList<>();
        for(Receta r: recetas){
            String estado=r.obtenerNombreEstado(r.getEstado());
            String medico=(r.getPersonal()!=null && r.getPersonal().getNombre()!=null)
                    ?r.getPersonal().getNombre():"";
            String paciente= (r.getPaciente()!=null &&r.getPaciente().getNombre()!=null)
                    ?r.getPaciente().getNombre():"";

            List<FarmaceuticoVista.DetalleRow>detRows=new ArrayList<>();
            if(r.getDetalleMedicamentos()!=null){
                for(DetalleMedicamento d: r.getDetalleMedicamentos()){
                    String codigo= d.getMedicamento()!=null?d.getMedicamento().getCodigo():"";
                    String medNom= d.getMedicamento()!=null?d.getMedicamento().getCodigo():"";
                    String present= d.getMedicamento()!=null?d.getMedicamento().getPresentacion():"";
                    int cant=d.getCantidad();
                    String indic= d.getIndicacion()!=null?d.getIndicacion():"";
                    int dias=d.getDuracion();
                    detRows.add(new FarmaceuticoVista.DetalleRow(codigo,medNom,present,cant,indic,dias));
                }
            }
            filas.add(new FarmaceuticoVista.RecetaRow(
                    r.getId(),
                    (r.getPaciente()!=null? r.getPaciente().getId():""),
                    paciente,
                    r.getFechaPrescripcion(),
                    r.getFechaRetiro(),
                    estado,
                    medico,
                    detRows
            ));
        }
        return filas;
    }
    private boolean estaEnVentanaRetiro(LocalDate fechaRetiro, int dias){
        if(fechaRetiro==null)return false;
        LocalDate hoy= LocalDate.now();
        LocalDate desde= hoy.minusDays(dias);
        LocalDate hasta= hoy.plusDays(dias);
        return !fechaRetiro.isBefore(desde) && fechaRetiro.isAfter(hasta);
    }
}
