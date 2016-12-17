/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.EstadoOportunidad;
import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import com.sigueme.backend.entities.TipoDeOportunidad;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.EstadoOportunidadFacadeLocal;
import com.sigueme.backend.model.OportunidadDeAprendizajeFacadeLocal;
import com.sigueme.backend.model.OportunidadUsuarioFacadeLocal;
import com.sigueme.backend.model.TipoDeOportunidadFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Santi
 */
@Named(value = "oportunidadController")
@ViewScoped
public class OportunidadController implements Serializable {

    @EJB
    private OportunidadDeAprendizajeFacadeLocal oportunidadFacadeLocal;
    @EJB
    private TipoDeOportunidadFacadeLocal tipoFacadeLocal;
    @EJB
    private OportunidadUsuarioFacadeLocal oportunidadUsuarioFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private EstadoOportunidadFacadeLocal estadoFacadeLocal;

    private OportunidadDeAprendizaje oportunidad;
    private OportunidadUsuario oportunidadUsuario;
    private Usuario usuario;
    private EstadoOportunidad estadoOportunidad;

    private List<TipoDeOportunidad> tiposDeOportunidad;
    private List<Usuario> usuarios;
    private List<EstadoOportunidad> estadosOportunidad;
    private List<Usuario> pruebaUsuarios;
    
    
    public OportunidadController() {
    }

    @PostConstruct
    public void init() {
        oportunidad = new OportunidadDeAprendizaje();
        oportunidadUsuario = new OportunidadUsuario();
        usuario=new Usuario();

    }
    
    
    public String registrarOportunidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        String redirect = "createOportunity";
        try {
            this.oportunidadFacadeLocal.create(oportunidad);
            Usuario u;
            for (int i = 0; i < usuarios.size(); i++) {
                OportunidadUsuario ou = new OportunidadUsuario();
                u = usuarios.get(i);
                ou.setOportunidadID(oportunidad);
                ou.setUsuarioID(usuario);
                ou.setUsuarioID(u);
                this.oportunidadUsuarioFacadeLocal.create(ou);
            }

            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Oportunidad se ha registrado correctamente"));

            redirect = "principalOportunity?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar la oportunidad"));
        }
        return redirect;
    }
    
    

    public List<OportunidadDeAprendizaje> listarOportunidades() {
        
         HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
         usuario = (Usuario) session.getAttribute("usuario");
         
         if(usuario.getRolesUsuario().get(0).getRolID()==1){
             return this.oportunidadFacadeLocal.misOportunidades(usuario);
         }else{
             return this.oportunidadFacadeLocal.listarLider();
         }
    }//buena pregunta jajaja
    /*
    public String sugerencias(){
        return this.oportunidadFacadeLocal.sugerirOportunidad();
    }
    */
       
    public void calificarOportunidad(OportunidadUsuario p){
        this.oportunidadUsuario=p;
    }
    
    public void calificarOportunidad(){
        try{
            this.oportunidadUsuarioFacadeLocal.edit(oportunidadUsuario);
               FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La oportunidad se ha calificado correctamente"));
        }catch(Exception e){
               FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "La oportunidad no se ha podido calificar"));
        }
    }
    
    
    public void editarOportunidadUsuario(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuario = (Usuario) session.getAttribute("usuario");
        try{
            this.oportunidadUsuario.setOportunidadID(this.oportunidad);
            this.oportunidadUsuario.setUsuarioID(usuario);
            this.oportunidadUsuarioFacadeLocal.editarOportunidadUsuario(oportunidadUsuario);
            
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha enviado correctamente la oportunidad"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(
                    null,new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo enviar la oportunidad") );
        }
        
       
    }
       
    public void editarOportunidad(OportunidadDeAprendizaje oportunidad) {
        this.oportunidad = oportunidad;
        usuarios = new ArrayList<>();
        for (OportunidadUsuario oportunidadesUsuario : oportunidad.getOportunidadesUsuarios()) {
            usuarios.add(oportunidadesUsuario.getUsuarioID());
        }
        
    }
    

    public void editarOportunidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        String redirect = "editOportunity";
            
        try {
            for (Usuario usu :usuarios) {
                boolean bandera = false;
                for (OportunidadUsuario ou: oportunidad.getOportunidadesUsuarios()) {
                    if(ou.getUsuarioID().getUsuarioID() == usu.getUsuarioID()){
                        bandera = true;
                    }
                }
                if(!bandera){
                    OportunidadUsuario opUsuario = new OportunidadUsuario();
                    opUsuario.setOportunidadID(oportunidad);
                    opUsuario.setUsuarioID(usu);
                    oportunidad.getOportunidadesUsuarios().add(opUsuario);
                }
            }
     
            for (int i = 0; i < oportunidad.getOportunidadesUsuarios().size(); i++) {
                boolean bandera = true;
                for (Usuario usuario : usuarios) {
                    if(usuario.getUsuarioID() == oportunidad.getOportunidadesUsuarios().get(i).getUsuarioID().getUsuarioID()){
                        bandera = false;
                    }
                }
                if(bandera){
                    oportunidad.getOportunidadesUsuarios().remove(i);
                    i--;
                }
            }
            
            this.oportunidadFacadeLocal.edit(oportunidad);
          
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Oportunidad se ha modificado correctamente"));

            redirect = "principalOportunity";
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo modificar la Oportunidad"));
        }
        //return redirect;
    }

    public void eliminarOportunidad(OportunidadDeAprendizaje oportunidad) {
        this.oportunidad = oportunidad;
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            
            this.oportunidad.setEstado(false);
            this.oportunidadFacadeLocal.edit(this.oportunidad);

            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Oportunidad se ha eliminado correctamente"));

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo eliminar la Oportunidad"));
        }
    }

    public List<OportunidadUsuario> listarOportunidadesUsuarios() {
        return this.oportunidadUsuarioFacadeLocal.findAll();
    }
   
    public int calcularDias(OportunidadUsuario ou){
       return this.oportunidadFacadeLocal.diasFaltantes(ou);
   }
    //ese método va es en el facade de oportunidadUsuario 
    public OportunidadDeAprendizaje getOportunidad() {
        return oportunidad;
    }

    public void setOportunidad(OportunidadDeAprendizaje oportunidad) {
        this.oportunidad = oportunidad;
    }

    public OportunidadUsuario getOportunidadUsuario() {
        return oportunidadUsuario;
    }////Ppoeoxoocooooooooooooeooooooolente  LA :Dmagia
    //excelente andres ufff brutal 
    //que mas hacemos? parce cosas importantes :
    //1. ya se como hacer que me liste solo los agentes para asignar oportunidades e incidnecais
    //2. los permisos del usuario a la bd
    // y ya?
    //nos quedaría faltando una cosita de la validacion de las fecha de las oportunidades
    //y venga en dodne es que llamamo el metodo de sugerir oportunidad? jjaajmiRA//lo puede poner donde tu corazon considere jajajaj
    //ajjajajaja se da garra andres ajajajjajaj att:kathe
    //
    

    public void setOportunidadUsuario(OportunidadUsuario oportunidadUsuario) {
        this.oportunidadUsuario = oportunidadUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoOportunidad getEstadoOportunidad() {
        return estadoOportunidad;
    }

    public void setEstadoOportunidad(EstadoOportunidad estadoOportunidad) {
        this.estadoOportunidad = estadoOportunidad;
    }

    public List<TipoDeOportunidad> getTiposDeOportunidad() {
        this.tiposDeOportunidad = tipoFacadeLocal.findAll();
        return tiposDeOportunidad;
    }

    public void setTiposDeOportunidad(List<TipoDeOportunidad> tiposDeOportunidad) {
        this.tiposDeOportunidad = tiposDeOportunidad;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<EstadoOportunidad> getEstadosOportunidad() {
        this.estadosOportunidad = estadoFacadeLocal.findAll();
        return estadosOportunidad;
    }

    public void setEstadosOportunidad(List<EstadoOportunidad> estadosOportunidad) {
        this.estadosOportunidad = estadosOportunidad;
    }


    
    
      
}
