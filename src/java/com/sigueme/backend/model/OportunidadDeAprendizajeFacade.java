/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import com.sigueme.backend.entities.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

/**
 *
 * @author Santi
 */
@Stateless
public class OportunidadDeAprendizajeFacade extends AbstractFacade<OportunidadDeAprendizaje> implements OportunidadDeAprendizajeFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OportunidadDeAprendizajeFacade() {
        super(OportunidadDeAprendizaje.class);
    }

    @Override
    public void editarEstado(OportunidadDeAprendizaje oportunidad) {
        String consulta;

        try {
            consulta = "UPDATE OportunidadDeAprendizaje o SET o.oportunidadesUsuarios.estadoID.estadoID = 4 WHERE o.oportunidadID = ?1";
            Query query = em.createQuery(consulta);
            query.setParameter(1, oportunidad.getOportunidadID());
            query.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<OportunidadDeAprendizaje> misOportunidades(Usuario p) {

        try {
            List<OportunidadDeAprendizaje> oportunidades;
            Query query = em.createQuery("SELECT p FROM OportunidadUsuario t JOIN t.oportunidadID p WHERE t.usuarioID.usuarioID =?1 AND p.estado=true");
            query.setParameter(1, p.getUsuarioID());
            oportunidades = query.getResultList();
//            query.executeUpdate();
            return oportunidades;
        } catch (Exception n) {
            throw n;
        }

    }

/*
    @Override
    public String sugerirOportunidad() {
        StoredProcedureQuery spq = em.createNamedStoredProcedureQuery("procedimiento");
        spq.execute();
        String resultado =  spq.getOutputParameterValue("salida").toString();
        return resultado;
    }*/

    @Override
    public int diasFaltantes(OportunidadUsuario ou) {
        try {
            StoredProcedureQuery spq = em.createNamedStoredProcedureQuery("procedimientoDiasFaltantes");
            System.out.println("algo usuario" + ou.getUsuarioID().getUsuarioID());
            spq.setParameter("idUsuario", ou.getUsuarioID().getUsuarioID());
            System.out.println("algo oportunidad" + ou.getOportunidadID().getOportunidadID());
            spq.setParameter("idoportunidad", ou.getOportunidadID().getOportunidadID());
            spq.execute();
            int esultado = (int) spq.getOutputParameterValue("salida");
            System.out.println("Algo::"+esultado);
            return esultado;
        } catch (Exception ss) {
            System.out.println("Hay maracasss");
        } 
        return 0;
    }
    @Override
    public List<OportunidadDeAprendizaje> listarLider() {
        try {
            TypedQuery<OportunidadDeAprendizaje> query = getEntityManager().createNamedQuery("OportunidadDeAprendizaje.findAll", OportunidadDeAprendizaje.class);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

}
