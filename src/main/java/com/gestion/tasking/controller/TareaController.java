package com.gestion.tasking.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tasking.entity.Tarea;
import com.gestion.tasking.model.AuthResponse;
import com.gestion.tasking.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarTarea(@RequestBody Tarea tarea) {
        try {
            // Validaciones
            if (tarea.getIdProyecto() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse(400, "El ID del proyecto es obligatorio y debe ser mayor a cero."));
            }
            if (tarea.getNombre() == null || tarea.getNombre().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse(400, "El nombre de la tarea es obligatorio."));
            }
            if (tarea.getDescripcion() == null || tarea.getDescripcion().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse(400, "La descripción de la tarea es obligatoria."));
            }
            if (tarea.getPrioridad() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse(400, "La prioridad de la tarea es obligatoria."));
            }
            if (tarea.getEstado() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse(400, "El estado de la tarea es obligatorio."));
            }
            if (tarea.getFechaVencimiento() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse(400, "La fecha de vencimiento es obligatoria."));
            }

            // Llamar al servicio para registrar la tarea
            Tarea nuevaTarea = tareaService.registrarTarea(
                tarea.getIdProyecto(),
                tarea.getNombre(),
                tarea.getDescripcion(),
                tarea.getPrioridad(),
                tarea.getEstado(),
                tarea.getFechaVencimiento()
            );

            // Retornar la tarea registrada con la fecha formateada
            return ResponseEntity.ok(nuevaTarea);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    

    @PostMapping("/proyecto")
    public ResponseEntity<?> obtenerTareasPorProyecto(@RequestBody Map<String, Integer> request) {
        int idProyecto = request.get("idProyecto");  // Extraemos el idProyecto del cuerpo del JSON

        if (idProyecto <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(400, "El ID del proyecto debe ser mayor a cero."));
        }

        List<Tarea> tareas = tareaService.obtenerTareasPorProyecto(idProyecto);

        if (tareas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse(404, "No se encontraron tareas para este proyecto."));
        }

        // Excluir campo idProyecto
        List<Map<String, Object>> tareasResponse = new ArrayList<>();
        for (Tarea tarea : tareas) {
            Map<String, Object> tareaData = new HashMap<>();
            tareaData.put("idTarea", tarea.getIdTarea());
            tareaData.put("nombre", tarea.getNombre());
            tareaData.put("descripcion", tarea.getDescripcion());
            tareaData.put("prioridad", tarea.getPrioridad());
            tareaData.put("estado", tarea.getEstado());
            tareaData.put("fechaVencimiento", tarea.getFechaVencimiento());
            tareaData.put("fechaCreacion", tarea.getFechaCreacion()); // Incluir fechaCreacion en la respuesta
            tareasResponse.add(tareaData);
        }

        return ResponseEntity.ok(tareasResponse); // Respuesta con fechaCreacion
    }




}
