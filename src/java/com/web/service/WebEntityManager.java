package com.web.service;

import com.web.WebConfig;
import com.web.entity.WebEntity;
import com.web.exception.WebException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class WebEntityManager<T> implements Serializable {

    
    private final Class clss;

    public void execute(String name, Object... params) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction=em.getTransaction();
        Query query;
        try {
            transaction.begin();
            query = em.createNamedQuery(name);
            for (int i = 1; i <= params.length; i++) {
                query.setParameter(i, params[i - 1]);
            }
            query.executeUpdate();
            transaction.commit();
        } finally {
            em.close();
        }
    }
    
    public T findOne(String name, Object... params) {
        EntityManager em = getEntityManager();
        Query query;

        try {
            query = em.createNamedQuery(name);
            for (int i = 1; i <= params.length; i++) {
                query.setParameter(i, params[i - 1]);
            }
            List<T> list = query.getResultList();
            if (list.size() > 0) {
                return list.get(0);
            }
            return null;
        } finally {
            em.close();
        }

    }

    public List<T> findAll(String name, Object... params) {
        EntityManager em = getEntityManager();
        Query query;
        try {
            query = em.createNamedQuery(name);
            for (int i = 1; i <= params.length; i++) {
                query.setParameter(i, params[i - 1]);
            }
            return query.getResultList();
        } finally {
            em.close();
        }

    }

    public WebEntityManager() {
        this(WebEntity.class);
    }

    public WebEntityManager(Class clss) {
        Map properties = new HashMap();
        properties.put("javax.persistence.jdbc.driver", WebConfig.getDriver());
        properties.put("javax.persistence.jdbc.url", WebConfig.getUrl());
        properties.put("javax.persistence.jdbc.user", WebConfig.getUsername());
        properties.put("javax.persistence.jdbc.password", WebConfig.getPassword());
        properties.put("javax.persistence.schema-generation.database.action", "create");
        this.emf = Persistence.createEntityManagerFactory("WebProject", properties);
        this.clss = clss;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(T entity) {

        EntityManager em = null;
        try {
            em = getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        } catch (Exception e) {
           
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(T entity) throws WebException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.merge(entity);
            transaction.commit();
        } catch (OptimisticLockException ex) {
            
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = 02L;
                if (find(id) == null) {
                    throw new WebException("Objecthe entity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws WebException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            T entity;
            try {
                entity = (T) em.getReference(clss, id);
            } catch (EntityNotFoundException enfe) {
                throw new WebException("Objecthe entity with id " + id + " no longer exists.");
            }
            em.remove(entity);
            transaction.commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<T> findEntities() {
        return findEntities(true, -1, -1);
    }

    public List<T> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<T> findEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb=em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root c = cq.from(clss);
            cq.select(c);
            cq.orderBy(cb.desc(c.get("version")));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
                
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public T find(Long id) {
        EntityManager em = getEntityManager();
        try {
            return (T) em.find(clss, id);
        } finally {
            em.close();
        }
    }

    public int getCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Object> rt = cq.from(clss);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
