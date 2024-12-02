package com.gestion.tasking.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tasking.entity.Prioridad;
import com.gestion.tasking.entity.Proyecto;
import com.gestion.tasking.entity.User;
import com.gestion.tasking.repository.UserRepository;
import com.gestion.tasking.service.PrioridadService;
import com.gestion.tasking.service.ProyectoService;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private PrioridadService prioridadService;
    
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProyectoController.class);

    @PostMapping("/user")
    public ResponseEntity<?> obtenerProyectosPorUsuario(@RequestBody Map<String, Integer> request) {
        Integer usuarioId = request.get("Id");
        if (usuarioId == null) {
            logger.warn("No se proporcionó un ID de usuario en la solicitud");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El ID de usuario es requerido"));
        }

        Optional<User> optionalUsuario = userRepository.findById(usuarioId);

        if (optionalUsuario.isEmpty()) {
            logger.warn("El usuario con ID {} no existe", usuarioId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "El usuario que se intenta buscar no existe"));
        }

        User usuario = optionalUsuario.get();
        List<Proyecto> proyectos = proyectoService.listarProyectosPorUsuario(usuario);

        if (proyectos.isEmpty()) {
            logger.info("El usuario con ID {} no tiene proyectos activos", usuarioId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Este usuario no tiene proyectos activos"));
        }

        logger.info("Proyectos encontrados para el usuario con ID {}: {}", usuarioId, proyectos.size());
        return ResponseEntity.ok(proyectos);
    }
    
    
   
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarProyecto(@RequestBody Map<String, Object> input) {
        try {
            // Crear una nueva instancia de Proyecto
            Proyecto proyecto = new Proyecto();

            // Mapear los valores desde la entrada según el nuevo formato
            Integer idUsuario = (Integer) input.get("idUsuario");
            if (idUsuario == null || idUsuario <= 0) {
                return ResponseEntity.badRequest().body("El ID del usuario es requerido.");
            }
            User usuario = new User();
            usuario.setId(idUsuario);
            proyecto.setUsuario(usuario);

            proyecto.setNombreTgProyectos((String) input.get("nombreProyecto"));
            proyecto.setDescripcionTgProyectos((String) input.get("descripcionProyecto"));

            // Convertir la fechaCreacion del formato
            String fechaCreacionString = (String) input.get("fechaCreacion");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaCreacionLocalDate = LocalDate.parse(fechaCreacionString, formatter);
            
            // la hora actual para la fecha de creación
            LocalDateTime fechaCreacion = fechaCreacionLocalDate.atTime(LocalTime.now());  // Usa la hora actual
            proyecto.setFechaCreacionTgProyectos(fechaCreacion);

            // Convertir la fechaVencimiento
            String fechaVencimientoString = (String) input.get("fechaVencimiento");
            LocalDate fechaVencimiento = LocalDate.parse(fechaVencimientoString);
            proyecto.setFechaVencimientoTgProyectos(fechaVencimiento);

            // la prioridad
            Integer idPrioridad = (Integer) input.get("prioridad");
            if (idPrioridad == null || idPrioridad <= 0) {
                return ResponseEntity.badRequest().body("El ID de la prioridad es requerido.");
            }
            Prioridad prioridad = new Prioridad();
            prioridad.setIdPrioridad(idPrioridad);
            proyecto.setPrioridad(prioridad);

            // Registrar el proyec
            Proyecto nuevoProyecto = proyectoService.agregarProyecto(proyecto);

            // Crear la respuesta en el formato deseado usando LinkedHashMap
            Map<String, Object> response = new LinkedHashMap<>();  // mantener ordenado el formato de salida
            response.put("idUsuario", nuevoProyecto.getUsuario().getId());
            response.put("idProyecto", nuevoProyecto.getIdTgProyectos());
            response.put("nombreProyecto", nuevoProyecto.getNombreTgProyectos());
            response.put("descripcionProyecto", nuevoProyecto.getDescripcionTgProyectos());
            response.put("idPrioridad", nuevoProyecto.getPrioridad().getIdPrioridad());

            // Formatear la fechaCreacion para que tenga la hora correcta
            DateTimeFormatter responseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaCreacionFormatted = nuevoProyecto.getFechaCreacionTgProyectos().format(responseFormatter);
            response.put("fechaCreacion", fechaCreacionFormatted);

            response.put("fechaVencimiento", nuevoProyecto.getFechaVencimientoTgProyectos());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar el proyecto: " + e.getMessage());
        }
    }

    
}
