/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.RolFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Santi
 */
@Named(value = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private RolFacadeLocal rolFacadeLocal;
    
    private Usuario usuario;
    private Rol rol;
    private List<Rol> roles;
    
    public UsuarioController() {
    }
    
    @PostConstruct
    public void init(){
        usuario = new Usuario();
        rol = new Rol();
    }

    public String autenticarUsuario(){
        String redireccion = null;
        Usuario usuarioValidado = null;
        try{
           usuarioValidado =  usuarioFacadeLocal.iniciarSesion(this.usuario);
            if(usuarioValidado != null){
                //Almacenar los datos al obtener un inicio de sesión exitoso
                HttpSession sesion = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                sesion.setAttribute("usuario", usuarioValidado);
                sesion.setAttribute("rol", usuarioValidado.getRolesUsuario().get(0));
                redireccion = "pages/sigueme/sigueme?faces-redirect=true";                
            }else{
                FacesContext.getCurrentInstance().addMessage(
                    null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Error","datos incorrectos"));
            }
            
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(
                    null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error: ", "datos incorrectos"));
        }
        return redireccion; 
    }
    
    
    public String registrarUsuario(){
        FacesContext context = FacesContext.getCurrentInstance();
        String redirect = "createUser";
        try{
            this.usuario.setEstado(true);
            
            if(this.usuario.getRolesUsuario().get(0).getRolID()!=2){
                this.usuarioFacadeLocal.create(usuario);
                context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El usuario se ha registrado correctamente"));
        
            redirect = "principalUser?faces-redirect=true";
            }else{
                 context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No puede agregar otro lider"));
            }
            
            
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar el usuario"));
        }
        return redirect;
    }
    
    public List<Usuario> listarUsuarios(){
        return this.usuarioFacadeLocal.listar();
    }
    
    public void eliminarUsuario(Usuario usuario){
        FacesContext context = FacesContext.getCurrentInstance();
        this.usuario = usuario;
       
        try{
            if(usuario.getRolesUsuario().get(0).getRolID()==2){
            
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No se puede eliminar el lider")); 
            }else{
                this.usuarioFacadeLocal.editarEstado(this.usuario);
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El usuario se ha eliminado correctamente"));
            }
            
            
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se han eliminar el usuario"));
        }
        
    }
    
    public void editarUsuario(Usuario usuario){
        this.usuario = usuario;
        //return "editUser";
    }
    
    public void editarUsuario(){
        FacesContext context = FacesContext.getCurrentInstance();
        //String redirect = "editUser";
        try{
            if(this.usuario.getRolesUsuario().get(0).getRolID()!=2){
                this.usuarioFacadeLocal.edit(this.usuario);
            }else{
                context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ya existe un lider, no puedes crear otro"));
            }
            
            //redirect = "principalUser";
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Los datos se han modificado correctamente"));
            
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se han modificado los datos"));
        }
        //return redirect;
    }
    
    public String saludar(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario p = (Usuario) session.getAttribute("usuario");
           
        return "¡Hola " + p.getNombre() + "!"; 
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getRoles() {
        this.roles = this.rolFacadeLocal.findAll();
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
    
}
