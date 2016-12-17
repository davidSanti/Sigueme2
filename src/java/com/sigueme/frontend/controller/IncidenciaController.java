/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.ErrorFrecuente;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.CategoriaFacadeLocal;
import com.sigueme.backend.model.ErrorFrecuenteFacadeLocal;
import com.sigueme.backend.model.IncidenciaFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
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
@Named(value = "incidenciaController")
@ViewScoped
public class IncidenciaController implements Serializable {

     
    @EJB
    private IncidenciaFacadeLocal incidenciaFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB 
    private CategoriaFacadeLocal categoriaFacadeLocal;
    @EJB
    private ErrorFrecuenteFacadeLocal errorFrecuenteFacadeLocal;
    
    private Usuario usuario;
    private Categoria categoria;
    private Incidencia incidencia;
    private ErrorFrecuente errorFrecuente;
    
    private List<Usuario> usuarios;
    private List<Categoria> categorias;
    private List<Incidencia> incidencias;
    private List<ErrorFrecuente> errores;

    public IncidenciaController() {
    }
    
    @PostConstruct
    public void init(){
        usuario = new Usuario();
        categoria = new Categoria();
        incidencia = new Incidencia();
        errorFrecuente = new ErrorFrecuente();
        
    }
    
        
    public String registrarIncidencia(){
        FacesContext context = FacesContext.getCurrentInstance();
        String redirect = "createIncident";
        try{
            this.incidenciaFacadeLocal.create(incidencia);
                    
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La incidencia se ha registrado correctamente"));
        
            redirect = "principalIncident?faces-redirect=true";
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar la incidencia"));
        }
        return redirect;
        
    }
    
    public void eliminarIncidencia(Incidencia incidencia){
        FacesContext context = FacesContext.getCurrentInstance();
        this.incidencia = incidencia;
        try{
            this.incidenciaFacadeLocal.editarEstado(this.incidencia);
                      
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La incidencia se ha eliminado correctamente"));
        
            }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo eliminar la incidencia"));
        }
        
    }

    public void editarIncidencia(Incidencia incidencia){
        this.incidencia = incidencia;
        //return "editIncident";
        
    }

    public void editarIncidencia(){
        FacesContext context = FacesContext.getCurrentInstance();
       // String redirect = "editIncident";
        try{
            
            this.incidenciaFacadeLocal.edit(this.incidencia);
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La incidencia se ha modificado correctamente") );
            //redirect = "principalIncident";
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "La incidencia no se pudo modificar") );
        }
       // return redirect;
    }
    
    public List<Incidencia> listarIncidencia(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario u = (Usuario) session.getAttribute("usuario");
        if(verificarRol()){
            return this.incidenciaFacadeLocal.listarIncidenciasPropias(u);
        }
        return this.incidenciaFacadeLocal.listar();
    }
    
    public boolean verificarRol(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Rol r = (Rol) session.getAttribute("rol");
        boolean bandera = false;
        
        if(r.getRolID() == 1){
            bandera = true;
        }
        return bandera;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public ErrorFrecuente getErrorFrecuente() {
        return errorFrecuente;
    }
    public void setErrorFrecuente(ErrorFrecuente errorFrecuente) {
        this.errorFrecuente = errorFrecuente;
    }

    public List<Usuario> getUsuarios() {
        usuarios = usuarioFacadeLocal.listarAgentes();
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Categoria> getCategorias() {
        categorias = categoriaFacadeLocal.findAll();
        return categorias;
    }

    public void setServicios(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Incidencia> getIncidencias() {
        incidencias = incidenciaFacadeLocal.findAll();
        return incidencias;
    }

    public void setIncidencias(List<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }

    public List<ErrorFrecuente> getErrores() {
        errores = errorFrecuenteFacadeLocal.findAll();
        return errores;
    }

    public void setErrores(List<ErrorFrecuente> errores) {
        this.errores = errores;
    }
    
}
