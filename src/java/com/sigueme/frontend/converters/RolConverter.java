/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.model.RolFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Santi
 */
@FacesConverter (value = "rolConverter")
@ViewScoped
public class RolConverter implements Converter{
    
    @EJB
    private RolFacadeLocal rolFacadeLocal;

    public RolConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<Rol> roles = this.rolFacadeLocal.findAll();
        for (Rol objeto : roles) {
            if (objeto.getRolID() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Rol) objeto).getRolID().toString();
        } else {
            return "";
        }
    }

    
}
