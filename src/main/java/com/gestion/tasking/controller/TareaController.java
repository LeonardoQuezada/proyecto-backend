package com.gestion.tasking.controller;

import com.gestion.tasking.entity.Tarea;
import com.gestion.tasking.model.AuthResponse;
import com.gestion.tasking.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
