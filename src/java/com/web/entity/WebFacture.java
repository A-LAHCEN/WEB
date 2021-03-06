package com.web.entity;

import com.web.ICollection;
import com.web.IValidation;
import com.web.exception.WebException;
import com.web.service.WebEntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;

@Entity()
@Table()
@NamedQueries(value = {
    @NamedQuery(name = "WebFacture.updateEtat", query = "UPDATE WebFacture f SET f.etat=?1 WHERE f.id=?2"),
    @NamedQuery(name = "WebFacture.finByDate", query = "SELECT u FROM WebFacture AS u WHERE u.date = ?1"),
    @NamedQuery(name = "WebFacture.finByClientAndDate", query = "SELECT u FROM WebFacture AS u WHERE u.date = ?1 AND u.client.nom LIKE CONCAT('%', ?2,'%')"),
    @NamedQuery(name = "WebFacture.finByClient", query = "SELECT u FROM WebFacture AS u WHERE u.client.nom LIKE CONCAT('%', ?1,'%') ")
})
public class WebFacture extends WebEntity implements ICollection<WebFactureItem>, IValidation {

    @Temporal(TemporalType.DATE)
    private Date date;

    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WebClient client;

    @JoinColumn(name = "tax", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WebTax tax;

    @Column()
    private boolean etat;

    @Size(min = 1)
    @OneToMany(mappedBy = "facture", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Collection<WebFactureItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "facture", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Collection<WebReglement> reglements = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    private WebType type;

    @Override
    public boolean add(WebFactureItem item) {
        item.setFacture(this);
        if (items.contains(item)) {
            items.remove(item);
        }
        return items.add(item);
    }

    @Override
    public boolean remove(WebFactureItem item) {
        return items.remove(item);
    }

    public Date getDate() {
        return date;
    }

    public WebFacture setDate(Date date) {
        this.date = date;
        return this;
    }

    public WebClient getClient() {
        return client;
    }

    public WebFacture setClient(WebClient client) {
        this.client = client;
        return this;
    }

    public WebTax getTax() {
        return tax;
    }

    public WebFacture setTax(WebTax tax) {
        this.tax = tax;
        return this;
    }

    public boolean isEtat() {
        return etat;
    }

    public WebFacture setEtat(boolean etat) {
        this.etat = etat;
        return this;
    }

    public WebType getType() {
        return type;
    }

    public WebFacture setType(WebType type) {
        this.type = type;
        return this;
    }

    public Collection<WebFactureItem> getItems() {
        return items;
    }

    public WebFacture setItems(Collection<WebFactureItem> items) {
        this.items = items;
        return this;
    }

    public Collection<WebReglement> getReglements() {
        return reglements;
    }

    public WebFacture setReglements(Collection<WebReglement> reglements) {
        this.reglements = reglements;
        return this;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @PostPersist
    public void triggerAfterInsert() throws WebException {
        if (this.type.equals(WebType.FACTURE)) {
            WebEntityManager wem = new WebEntityManager();
            if (!etat) {
                try {
                    client.setCredit(client.getCredit() + (getTotal() * tax.getValue() / 100) + getTotal());
                wem.edit(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            for (WebFactureItem item : items) {
                WebProduct product = item.getProduct();
                product.setQuantite(product.getQuantite() - item.getQuantite());
                try {
                    wem.edit(product);
                } catch (WebException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void activate(HttpServletRequest request) {
        if (request.getParameter("client") != null && !request.getParameter("client").equals("")) {
            WebClient client1 = new WebEntityManager<WebClient>(WebClient.class).find(Long.valueOf(request.getParameter("client")));
            this.client = client1;
        }
        if (request.getParameter("tax") != null && !request.getParameter("tax").equals("")) {
            WebTax tax1 = new WebEntityManager<WebTax>(WebTax.class).find(Long.valueOf(request.getParameter("tax")));
            this.tax = tax1;
        }

        this.etat = request.getParameter("etat") != null && !request.getParameter("etat").equals("");

        if (request.getParameter("date") != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                this.date = sdf.parse(request.getParameter("date"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        switch (Integer.valueOf(request.getParameter("type"))) {
            case 0:
                type = WebType.COMMANDE;
                break;
            case 1:
                type = WebType.DEVIS;
                break;
            case 2:
                type = WebType.LIVRAISON;
                break;
            case 3:
                type = WebType.FACTURE;
                break;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WebFacture) {
            WebFacture facture = (WebFacture) obj;
            if (facture.getId() == null && getId() == null) {
                return true;
            }
            return facture.getId().equals(getId());
        }
        return false;
    }

    public double getTotal() {
        double total = 0;
        for (WebFactureItem webFactureItem : items) {
            total += webFactureItem.getPrix() * webFactureItem.getQuantite();
        }
        return total;
    }

    public double getPayed() {
        double payed = 0;
        for (WebReglement reglement : getReglements()) {
            payed += reglement.getMontant();
        }
        return payed;
    }
}
