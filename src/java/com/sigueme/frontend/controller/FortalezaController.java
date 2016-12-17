/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.FortalezaDeConocimiento;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.CategoriaFacadeLocal;
import com.sigueme.backend.model.FortalezaDeConocimientoFacadeLocal;
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
@Named(value = "fortalezaController")
@ViewScoped
public class FortalezaController implements Serializable {

    @EJB
    private FortalezaDeConocimientoFacadeLocal fortalezaFacadeLocal;
    @EJB
    private CategoriaFacadeLocal categoriaFacadeLocal;
    
    private FortalezaDeConocimiento fortaleza;
    private Categoria categoria;
    private List<Categoria> categorias;
    
    public FortalezaController() {
    }
    
    @PostConstruct
    public void init(){
        fortaleza = new FortalezaDeConocimiento();
        categoria = new Categoria();
    }
    
    public String registrarFortaleza(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario p = (Usuario) session.getAttribute("usuario");
        FacesContext context = FacesContext.getCurrentInstance();
        String redirect = "createStrength";
        try{
            this.fortaleza.setUsuarioID(p);
            this.fortalezaFacadeLocal.create(fortaleza);
              
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Fortaleza de conocimiento se ha registrado correctamente"));
            redirect = "principalStrength?faces-redirect=true"; 
            
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar la Fortaleza de conocimiento"));
        }
        return redirect;
        
    }
    
    public List<FortalezaDeConocimiento> listarFortalezas(){
        return this.fortalezaFacadeLocal.listar();
    }
    
    public void eliminarFortaleza(FortalezaDeConocimiento fortaleza){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            this.fortaleza = fortaleza;
            this.fortalezaFacadeLocal.cambiarEstado(this.fortaleza);
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Fortaleza de conocimiento se ha eliminado correctamente"));
        
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo eliminar la fortaleza de conocimiento"));
        }
    }

    public void editarFortaleza(FortalezaDeConocimiento fortaleza){
        this.fortaleza = fortaleza;
        
    }
    
    public void editarFortaleza(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario p = (Usuario) session.getAttribute("usuario");
         FacesContext context = FacesContext.getCurrentInstance();
        // String redirect = "createStrength";
        try{
            this.fortaleza.setUsuarioID(p);
            this.fortalezaFacadeLocal.edit(fortaleza);
                    
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Fortaleza de conocimiento se ha modificado correctamente"));
        
           // redirect = "principalStrength";
        }catch(Exception e){
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo modificar la fortaleza de conocimiento"));
        }
        //return redirect;
    }
    
    public int verificarUsuario(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u.getUsuarioID();
    }
    
    public FortalezaDeConocimiento getFortaleza() {
        return fortaleza;
    }

    public void setFortaleza(FortalezaDeConocimiento fortaleza) {
        this.fortaleza = fortaleza;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Categoria> getCategorias() {
        this.categorias = categoriaFacadeLocal.findAll();
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
    
}
