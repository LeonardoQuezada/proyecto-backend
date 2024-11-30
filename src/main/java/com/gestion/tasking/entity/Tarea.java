package com.gestion.tasking.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Tarea {
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private Integer prioridad;
    private Integer estado;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;
}
