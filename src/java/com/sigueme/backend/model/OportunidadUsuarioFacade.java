/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Santi
 */
@Stateless
public class OportunidadUsuarioFacade extends AbstractFacade<OportunidadUsuario> implements OportunidadUsuarioFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OportunidadUsuarioFacade() {
        super(OportunidadUsuario.class);
    }
    
    
    @Override
    public void editarOportunidadUsuario(OportunidadUsuario algo){
        try{
            Query query = em.createQuery("UPDATE OportunidadUsuario u SET u.descripcion=:descripcion WHERE u.usuarioID.usuarioID=:usuario and u.oportunidadID.oportunidadID=:oportunidad");
            query.setParameter("descripcion", algo.getDescripcion());
            query.setParameter("usuario", algo.getUsuarioID().getUsuarioID());
            query.setParameter("oportunidad", algo.getOportunidadID().getOportunidadID());
            query.executeUpdate();
            System.out.println("Excelente");
        }catch(Exception e){
            System.out.println("algo paso");
        }
    }

    @Override
    public void eliminarOportnidad(OportunidadDeAprendizaje oportunidad) {
        try{
            Query query = em.createQuery("UPDATE OportunidadUsuario u SET u.estadoID.estadoID = 4 WHERE u.oportunidadID.oportunidadID = ?1");
            query.setParameter(1, oportunidad.getOportunidadID());
            query.executeUpdate();
        }catch(Exception e){
            throw e;
        }
    }                                      
}
