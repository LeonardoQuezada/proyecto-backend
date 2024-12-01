package com.gestion.tasking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.tasking.DAO.ProyectoDAO;
import com.gestion.tasking.entity.Proyecto;
import com.gestion.tasking.entity.User;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoDAO proyectoDAO;

   
    public List<Proyecto> listarProyectosPorUsuario(User usuario) {
        return proyectoDAO.findByUsuario(usuario);
    }
}
