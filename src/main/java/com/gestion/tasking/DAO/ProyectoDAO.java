package com.gestion.tasking.DAO;

import com.gestion.tasking.entity.Proyecto;
import com.gestion.tasking.entity.User;

import java.util.List;


public interface ProyectoDAO {
    List<Proyecto> findByUsuario(User usuario);
}
