package com.web.entity;

import com.web.exception.WebException;
import com.web.service.WebEntityManager;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
@Entity
@Table
public class WebReglement extends WebEntity{
    
    private double montant;
    
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WebFacture facture;
    
    @JoinColumn(name = "mode", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WebReglementMode mode;
    
    private String ref;
    
    @Temporal(TemporalType.DATE)
    private Date date;

    public WebReglement(HttpServletRequest request) {
        super(request);
        if (request.getParameter("mode") != null && !request.getParameter("mode").equals("")) {
            WebReglementMode mode1 = new WebEntityManager<WebReglementMode>(WebReglementMode.class).find(Long.valueOf(request.getParameter("mode")));
            this.mode = mode1;
        }
        if (request.getParameter("facture") != null && !request.getParameter("facture").equals("")) {
            WebFacture facture1 = new WebEntityManager<WebFacture>(WebFacture.class).find(Long.valueOf(request.getParameter("facture")));
            this.facture = facture1;
        }
        if (request.getParameter("montant") != null && !request.getParameter("montant").equals("")) {
            this.montant = Double.valueOf(request.getParameter("montant"));
        }
        if (request.getParameter("ref") != null && !request.getParameter("ref").equals("")) {
            this.ref = (request.getParameter("ref"));
        }
        date=new Date();
    }

    public WebReglement() {
    }
    
    public WebReglementMode getMode() {
        return mode;
    }

    public WebReglement setMode(WebReglementMode mode) {
        this.mode = mode;
        return this;
    }

    public double getMontant() {
        return montant;
    }

    public WebReglement Montant(double prix) {
        this.montant = prix;
        return this;
    }

    public WebFacture getFacture() {
        return facture;
    }

    public WebReglement setFacture(WebFacture facture) {
        this.facture = facture;
        return this;
    }

    public Date getDate() {
        return date;
        
    }

    public WebReglement setDate(Date date) {
        this.date = date;
        return this;
    }
    
    @PostPersist
    public void triggerAfterInsert() throws WebException{
        WebEntityManager wem=new WebEntityManager();
        WebClient  client=getFacture().getClient().setCredit(getFacture().getClient().getCredit()-montant);
        wem.edit(client);
        
    }

    public String getRef() {
        return ref;
    }

    public WebReglement setRef(String ref) {
       this.ref = ref;
       return this;
    }
    
    
}