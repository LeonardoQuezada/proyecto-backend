package com.gestion.tasking.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gestion.tasking.entity.Proyecto;
import com.gestion.tasking.entity.User;
import com.gestion.tasking.service.ProyectoService;
import com.gestion.tasking.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

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
}
