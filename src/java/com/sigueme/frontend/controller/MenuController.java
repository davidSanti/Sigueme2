/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Permiso;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.PermisoFacadeLocal;
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
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Santi
 */
@Named(value = "menuController")
@SessionScoped
public class MenuController implements Serializable {

    @EJB
    private PermisoFacadeLocal permisoFacadeLocal;
    private List<Permiso> permisos;
    private MenuModel model;
    private Usuario usuario;   
    private UsuarioFacadeLocal usuarioFacadeLocal;
    
    public MenuController() {
    }
    
    @PostConstruct
    public void init(){
        model = new DefaultMenuModel();
        this.listarPermisos();
        this.establecerPermisos();
    }
    
    public void listarPermisos(){
        HttpSession sesion = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Rol r = (Rol) sesion.getAttribute("rol");
        permisos = r.getPermisosRol();
    }
    
    public void establecerPermisos(){
        for (Permiso permiso : permisos) {
            if(permiso.getTipo()){
                if(permiso.getDependencia() == null){
                    DefaultMenuItem item = new DefaultMenuItem(permiso.getDescripcion());
                    model.addElement(item);
                }
            }else{
                DefaultSubMenu firtsSubMenu = new DefaultSubMenu(permiso.getDescripcion());
                for (Permiso permiso1 : permisos) {
                    Permiso subPermiso = permiso1.getDependencia();
                    if(subPermiso != null){
                        if(subPermiso.getPermisoID() == permiso.getPermisoID()){
                            DefaultMenuItem item = new DefaultMenuItem(permiso1.getDescripcion());
                            item.setUrl(permiso1.getUrl());
                            firtsSubMenu.addElement(item);
                        }
                    } 
                    
                }
                model.addElement(firtsSubMenu);
            }
        }
    }
    
      public void verificarSesion(){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
           Usuario usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
           
           if(usuario == null){
               context.getExternalContext().redirect("/algo.xhtml");
           }
        }catch(Exception e){
            //Log para guardar codigo de error
        }
    }    
      
    public void cerrarSesion(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
    }
    
    public String mostrarNombreUsuario(){
        HttpSession sesion = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario u = (Usuario) sesion.getAttribute("usuario");
        return u.getNombre();
    }
    
    public Usuario leerDatos(){
        HttpSession sesion = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario usua = (Usuario) sesion.getAttribute("usuario");
        this.usuario = usua;
        enviarDatos();
        return usuario;
    }
    
    public String enviarDatos(){
        return "/pages/users/modifyCount?faces-redirect=true";
    }
    
    public void editarUsuario(){
        FacesContext context = FacesContext.getCurrentInstance();
     
        try{
            this.usuarioFacadeLocal.edit(this.usuario);
    
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Los datos se han modificado correctamente"));
            
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se han modificado los datos"));
        }
     
    }
        
    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

    public Usuario getUsuario() {
        
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
   
}
    
    
