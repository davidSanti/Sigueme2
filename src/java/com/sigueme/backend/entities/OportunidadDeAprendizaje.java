/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Santi
 */
@Entity
@Table(name = "OportunidadesDeAprendizaje")
@XmlRootElement

@NamedQueries({
    
    @NamedQuery(name = "OportunidadDeAprendizaje.findAll", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.estado = true")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByOportunidadID", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.oportunidadID = :oportunidadID")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByNombre", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.nombre = :nombre")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByFechaInicio", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByFechaFin", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.fechaFin = :fechaFin")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByNumeroDeIntentos", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.numeroDeIntentos = :numeroDeIntentos")
})

///HMMMMM sera que no pueden haber mas de /uno
//es que eso dice que las anotaciones no se puede repetir yo creo que es como en NamedQuery toca hace que ambos esten allí
///pera andrs voy aver una cosa mentico
//sería probar quitando el anterior y pero es que el anterior ya sirve :P hay es que mirar donde hay conflicto de nombres o algo
///exacto, busquemos en san googel
//parce ponga el otro antes de los namedQUeriesaqui
//puedo dar mi opinion santiago si me scuchassi
//andres una pregunta 
////ok
//si ya tenemos un named spq por que no solo en lo que estas haciendo se lo agregas a este si me hago entender no se opino yo pero si no es correcto ok ta bien jaja
//es que estoy buscando la forma de meter en la misma anotacion ambos, porque el conflicto que hay es que no pueden haber dos anotaciones de las mismas
//lo que dices esta bien, y en efectio es lo que toca hacer, pero estoy mirando como :P
//vale listo ,es que yo lo pensaba asi y creia qu eestba mal como lo pensaba pero al menos se que no estsba  tan equivocada
//ya creo que se, miremos a ver 
//listo :D esa era parcero excelente
//bien andres /te felicito 
//ahora a ver si tanta maravilla sirve para algo :v
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name="procedimientoDiasFaltantes",resultClasses = String.class
                          ,procedureName = "diasFaltantes",parameters = {
                          @StoredProcedureParameter(mode = ParameterMode.IN,name = "idUsuario",type = Integer.class),
                          @StoredProcedureParameter(mode = ParameterMode.IN,name = "idoportunidad",type = Integer.class),
                          @StoredProcedureParameter(mode = ParameterMode.OUT,name = "salida",type = Integer.class)}),
@NamedStoredProcedureQuery(name="procedimiento",
        resultClasses = String.class,
        procedureName = "sugerirOportunidad",
        parameters = {@StoredProcedureParameter(mode = ParameterMode.OUT,name = "salida", type = String.class)})
})//mk este le puede poner cola si quiere, es con lo que se llama en el facade y debe ser diferente de este, ese dejelo asi porque al otro lado ya sirve y este
        //venga cual es cual??
        //pere pere :P
        //es como se llama en base de datos, entonces cambie el nombre en el facade por el que le vaya a poner aca



public class OportunidadDeAprendizaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "OportunidadID")
    private Integer oportunidadID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Nombre")
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "NumeroDeIntentos", insertable = false)
    private int numeroDeIntentos;
        
    @JoinColumn(name = "TipoOportunidadID", referencedColumnName = "TipoID")
    @ManyToOne(optional = false)
    private TipoDeOportunidad tipoOportunidadID;
        
    @OneToMany(mappedBy = "oportunidadID", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OportunidadUsuario> oportunidadesUsuarios;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estado", insertable = false)
    private boolean estado;
    
    
    public OportunidadDeAprendizaje() {
    }

    public OportunidadDeAprendizaje(Integer oportunidadID) {
        this.oportunidadID = oportunidadID;
    }

    public OportunidadDeAprendizaje(Integer oportunidadID, String nombre, 
                                    Date fechaInicio, Date fechaFin,
                                    String descripcion, int numeroDeIntentos,
                                    boolean estado)
    {
        this.oportunidadID = oportunidadID;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.numeroDeIntentos = numeroDeIntentos;
        this.estado = estado;
    }

    public Integer getOportunidadID() {
        return oportunidadID;
    }

    public void setOportunidadID(Integer oportunidadID) {
        this.oportunidadID = oportunidadID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumeroDeIntentos() {
        return numeroDeIntentos;
    }

    public void setNumeroDeIntentos(int numeroDeIntentos) {
        this.numeroDeIntentos = numeroDeIntentos;
    }

    public TipoDeOportunidad getTipoOportunidadID() {
        return tipoOportunidadID;
    }

    public void setTipoOportunidadID(TipoDeOportunidad tipoOportunidadID) {
        this.tipoOportunidadID = tipoOportunidadID;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<OportunidadUsuario> getOportunidadesUsuarios() {
        return oportunidadesUsuarios;
    }

    public void setOportunidadesUsuarios(List<OportunidadUsuario> oportunidadesUsuarios) {
        this.oportunidadesUsuarios = oportunidadesUsuarios;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oportunidadID != null ? oportunidadID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OportunidadDeAprendizaje)) {
            return false;
        }
        OportunidadDeAprendizaje other = (OportunidadDeAprendizaje) object;
        if ((this.oportunidadID == null && other.oportunidadID != null) || (this.oportunidadID != null && !this.oportunidadID.equals(other.oportunidadID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nOportunidadID: " + oportunidadID + 
                "Descripción: " + descripcion;
    }
    
}
