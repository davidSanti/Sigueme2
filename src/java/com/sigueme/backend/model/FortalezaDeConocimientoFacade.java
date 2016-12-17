/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.FortalezaDeConocimiento;
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
public class FortalezaDeConocimientoFacade extends AbstractFacade<FortalezaDeConocimiento> implements FortalezaDeConocimientoFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FortalezaDeConocimientoFacade() {
        super(FortalezaDeConocimiento.class);
    }
    
     @Override
     public List<FortalezaDeConocimiento> listar(){
         TypedQuery<FortalezaDeConocimiento> query;
         query = getEntityManager().createNamedQuery("FortalezaDeConocimiento.findAll", FortalezaDeConocimiento.class);
         return query.getResultList();
     }
     
     @Override
     public void cambiarEstado(FortalezaDeConocimiento fortaleza){
        String consulta;
       
       try{
           consulta = "UPDATE FortalezaDeConocimiento f SET f.estado = false WHERE f.fortalezaID  = ?1";
           Query query = em.createQuery(consulta);
           query.setParameter(1, fortaleza.getFortalezaID());
           query.executeUpdate();
       }catch(Exception e){
           throw  e;
       }
         
     }
}
