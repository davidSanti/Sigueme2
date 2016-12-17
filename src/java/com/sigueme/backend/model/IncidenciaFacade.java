/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Santi
 */
@Stateless
public class IncidenciaFacade extends AbstractFacade<Incidencia> implements IncidenciaFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IncidenciaFacade() {
        super(Incidencia.class);
    }

    @Override
    public void editarEstado(Incidencia inci) {
        String consulta;
       
       try{
           consulta = "UPDATE Incidencia i SET i.estado = false WHERE i.incidenciaID = ?1";
           Query query = em.createQuery(consulta);
           query.setParameter(1, inci.getIncidenciaID());
           query.executeUpdate();
       }catch(Exception e){
           throw  e;
       }
    }
    
    @Override
    public List<Incidencia> listar() {
        TypedQuery<Incidencia> query = getEntityManager().createNamedQuery("Incidencia.findAll", Incidencia.class);
        return  query.getResultList();
    }

    @Override
    public List<Incidencia> listarIncidenciasPropias(Usuario usuario) {
        try {
            TypedQuery<Incidencia> query = getEntityManager().createNamedQuery("Incidencia.listarPropias", Incidencia.class);
            query.setParameter("usuarioID", usuario.getUsuarioID());
            return  query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
    
    
}
