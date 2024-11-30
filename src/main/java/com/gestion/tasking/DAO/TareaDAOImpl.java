package com.gestion.tasking.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.gestion.tasking.entity.Tarea;



@Repository
public class TareaDAOImpl implements TareaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Tarea registrarTarea(int idProyecto, String nombre, String descripcion, 
                                  Integer prioridad, Integer estado, String fechaVencimiento) throws Exception {
         
         // Llamar al stored procedure para registrar la tarea
         String procedureCall = "{CALL RegistrarTarea(?, ?, ?, ?, ?, ?)}";
         
         try {
             jdbcTemplate.update(procedureCall, idProyecto, nombre, descripcion, prioridad, estado, fechaVencimiento);
         } catch (DataAccessException e) {
             // Comprobamos si el errorCode es 45000 (tarea duplicada)
             if (e.getMessage().contains("45000")) {
                 // Lanzar excepción con el código de error y el mensaje conciso
                 throw new Exception("{\"error\": 1644, \"message\": \"Ya existe una tarea con el mismo nombre en este proyecto.\"}");
             } else {
                 // Si es otro tipo de error, lanzar la excepción original
                 throw new Exception("{\"error\": 500, \"message\": \"Error al registrar la tarea: " + e.getMessage() + "\"}");
             }
         }

         // Si la tarea se registra correctamente
         Tarea tarea = new Tarea();
         tarea.setIdProyecto(idProyecto);
         tarea.setNombre(nombre);
         tarea.setDescripcion(descripcion);
         tarea.setPrioridad(prioridad);
         tarea.setEstado(estado);
         tarea.setFechaVencimiento(java.time.LocalDate.parse(fechaVencimiento));

         return tarea;
     }
}