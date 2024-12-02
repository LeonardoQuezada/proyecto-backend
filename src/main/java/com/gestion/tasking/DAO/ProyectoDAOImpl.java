package com.gestion.tasking.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.tasking.entity.Proyecto;
import com.gestion.tasking.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class ProyectoDAOImpl implements ProyectoDAO {

    @PersistenceContext
    private EntityManager entityManager;

  
    @Override
    public List<Proyecto> findByUsuario(User usuario) {
        String jpql = "SELECT p FROM Proyecto p WHERE p.usuario.id = :usuarioId";
        TypedQuery<Proyecto> query = entityManager.createQuery(jpql, Proyecto.class);
        query.setParameter("usuarioId", usuario.getId());

        return query.getResultList();
    }
    
    @Override
    public Proyecto save(Proyecto proyecto) {
        if (proyecto.getIdTgProyectos() == null) {
            // Crear un nuevo proyecto
            entityManager.persist(proyecto);
            return proyecto;
        } else {
            // Actualizar un proyecto existente
            return entityManager.merge(proyecto);
        }
    }



}
