package com.gestion.tasking.DAO;

import java.util.List;


import com.gestion.tasking.entity.Proyecto;
import com.gestion.tasking.entity.User;


public interface ProyectoDAO {
    List<Proyecto> findByUsuario(User usuario);
    Proyecto save(Proyecto proyecto);
}



