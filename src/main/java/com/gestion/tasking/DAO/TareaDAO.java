package com.gestion.tasking.DAO;

import java.util.List;

import com.gestion.tasking.entity.Tarea;



public interface TareaDAO {
    Tarea registrarTarea(int idProyecto, String nombre, String descripcion, 
                         Integer prioridad, Integer estado, String fechaVencimiento) throws Exception;
    
    
    
    List<Tarea> obtenerTareasPorProyecto(int idProyecto);

    
}

