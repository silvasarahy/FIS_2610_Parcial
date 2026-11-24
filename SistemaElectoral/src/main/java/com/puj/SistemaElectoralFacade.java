package com.puj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaElectoralFacade {

    private final Map<String, Object> territorios;
    private final Map<String, Mesa> mesasRegistradas;
    private final Map<String, Acta> actasPorMesa;
    private final List<Incidente> incidentes;
    private final List<Reclamacion> reclamaciones;
    private final List<RegistroAuditoria> historialAuditoria;
    private Usuario usuarioAuditor;

    public SistemaElectoralFacade() {
        this(new Usuario("SYSTEM", "Sistema", "SISTEMA"));
    }

    public SistemaElectoralFacade(Usuario usuarioAuditor) {
        if (usuarioAuditor == null) {
            throw new IllegalArgumentException("El usuario auditor no puede ser nulo");
        }
        this.usuarioAuditor = usuarioAuditor;
        this.territorios = new HashMap<>();
        this.mesasRegistradas = new HashMap<>();
        this.actasPorMesa = new HashMap<>();
        this.incidentes = new ArrayList<>();
        this.reclamaciones = new ArrayList<>();
        this.historialAuditoria = new ArrayList<>();
    }

    public void registrarTerritorio(String codigo, Object territorio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del territorio es obligatorio");
        }
        if (territorio == null) {
            throw new IllegalArgumentException("La entidad territorial no puede ser nula");
        }
        territorios.put(codigo.trim(), territorio);
        registrarEventoAuditoria("REGISTRO_TERRITORIO", "Territorio registrado: " + codigo.trim());
    }

    public void registrarMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula");
        }
        String claveMesa = String.valueOf(mesa.getNumero());
        mesasRegistradas.put(claveMesa, mesa);
        registrarEventoAuditoria("REGISTRO_MESA", "Mesa registrada: " + claveMesa);
    }

    public Acta cerrarMesaYActa(String numeroMesa, String idActa) {
        if (numeroMesa == null || numeroMesa.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de mesa es obligatorio");
        }
        if (idActa == null || idActa.trim().isEmpty()) {
            throw new IllegalArgumentException("El id del acta es obligatorio");
        }
        Mesa mesa = mesasRegistradas.get(numeroMesa.trim());
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa " + numeroMesa + " no existe en el sistema");
        }
        if (actasPorMesa.containsKey(numeroMesa.trim())) {
            throw new IllegalStateException("La mesa " + numeroMesa + " ya tiene un acta registrada");
        }
        if (!"CERRADA".equals(mesa.getEstado())) {
            mesa.cerrar();
        }
        Acta acta = new Acta(idActa.trim(), mesa);
        actasPorMesa.put(numeroMesa.trim(), acta);
        registrarEventoAuditoria("CIERRE_MESA_Y_ACTA", "Mesa " + numeroMesa + " cerrada con acta " + idActa);
        return acta;
    }

    public Incidente radicarIncidente(Incidente incidente) {
        if (incidente == null) {
            throw new IllegalArgumentException("El incidente no puede ser nulo");
        }
        incidentes.add(incidente);
        registrarEventoAuditoria("INCIDENTE_RADICADO", incidente.toString());
        return incidente;
    }

    public ActaDecorator radicarReclamacion(String numeroMesa, Reclamacion reclamacion) {
        if (numeroMesa == null || numeroMesa.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de mesa es obligatorio");
        }
        if (reclamacion == null) {
            throw new IllegalArgumentException("La reclamación no puede ser nula");
        }
        if (reclamacion.getMotivo() == null || reclamacion.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de la reclamación es obligatorio");
        }
        Acta actaActual = actasPorMesa.get(numeroMesa.trim());
        if (actaActual == null) {
            throw new IllegalArgumentException("No existe un acta cerrada para la mesa " + numeroMesa);
        }
        reclamaciones.add(reclamacion);
        ActaDecorator actaImpugnada = new ActaConReclamacionDecorator(actaActual, reclamacion);
        actasPorMesa.put(numeroMesa.trim(), actaImpugnada);
        registrarEventoAuditoria("RECLAMACION_RADICADA", "Reclamación en mesa " + numeroMesa);
        return actaImpugnada;
    }

    public Map<String, Object> generarConsolidado() {
        int totalVotosEmitidos = 0;
        for (Acta acta : actasPorMesa.values()) {
            totalVotosEmitidos += acta.calcularTotalVotos();
        }
        Map<String, Object> consolidado = new HashMap<>();
        consolidado.put("totalTerritorios", territorios.size());
        consolidado.put("totalMesas", mesasRegistradas.size());
        consolidado.put("mesasCerradasConActa", actasPorMesa.size());
        consolidado.put("totalVotosEmitidos", totalVotosEmitidos);
        consolidado.put("totalIncidentes", incidentes.size());
        consolidado.put("totalReclamaciones", reclamaciones.size());
        return Collections.unmodifiableMap(consolidado);
    }

    public List<Incidente> getIncidentes() {
        return Collections.unmodifiableList(incidentes);
    }

    public List<Reclamacion> getReclamaciones() {
        return Collections.unmodifiableList(reclamaciones);
    }

    public List<RegistroAuditoria> getHistorialAuditoria() {
        return Collections.unmodifiableList(historialAuditoria);
    }

    public void setUsuarioAuditor(Usuario usuarioAuditor) {
        if (usuarioAuditor == null) {
            throw new IllegalArgumentException("El usuario auditor no puede ser nulo");
        }
        this.usuarioAuditor = usuarioAuditor;
    }

    private void registrarEventoAuditoria(String accion, String detalle) {
        String accionCompleta = accion + (detalle == null || detalle.isBlank() ? "" : " - " + detalle);
        historialAuditoria.add(new RegistroAuditoria(usuarioAuditor, accionCompleta));
    }
}