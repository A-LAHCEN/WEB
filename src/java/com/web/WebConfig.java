package com.web;

import com.web.entity.WebApplication;
import com.web.entity.WebCategory;
import com.web.entity.WebClient;
import com.web.entity.WebFournisseur;
import com.web.entity.WebManager;
import com.web.entity.WebProduct;
import com.web.entity.WebStock;
import com.web.service.WebEntityManager;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;

public class WebConfig {

    private static String driver, url, username, password;

    private static String jspDirectiry = "";

    private static String dataDirectory = "";

    public static String getDriver() {
        return driver;
    }

    public static void setDriver(String driver) {
        WebConfig.driver = driver;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        WebConfig.url = url;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        WebConfig.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        WebConfig.password = password;
    }

    public static String getJspDirectiry() {
        return jspDirectiry;
    }

    public static void setJspDirectiry(String jspDirectiry) {
        WebConfig.jspDirectiry = jspDirectiry;
    }

    public static String getDataDirectory() {
        return dataDirectory;
    }

    public static void setDataDirectory(String dataDirectory) {
        WebConfig.dataDirectory = dataDirectory;
    }

    public static void initialize(ServletContext servletContext) {
        WebConfig.setDriver("org.hsqldb.jdbcDriver");
        WebConfig.setPassword("ADMIN");
        WebConfig.setUsername("ADMIN");
        WebConfig.setUrl("jdbc:hsqldb:file:" + servletContext.getRealPath("/WEB-INF/data") + java.io.File.separator + "data.db;hsqldb.lock_file=false;;shutdown=true;");
        WebEntityManager<WebApplication> wm = new WebEntityManager(WebApplication.class);
        List<WebApplication> app = wm.findEntities();
        /*if (!app.isEmpty()) {
         WebApplication application = app.get(0);
         if(application.getServer()!=null 
         && application.getDatabase()!=null 
         && application.getUsername()!=null){
         WebConfig.setDriver("org.postgresql.Driver");
         WebConfig.setPassword(application.getPassword());
         WebConfig.setUsername(application.getUsername());
         WebConfig.setUrl("jdbc:postgresql://"+application.getServer()+":" + application.getPort()+"/"+application.getDatabase());
         }
         }*/
        /*WebConfig.setDriver("org.postgresql.Driver");
         WebConfig.setPassword("ADMIN");
         WebConfig.setUsername("postgres");
         WebConfig.setUrl("jdbc:postgresql://127.0.0.1:5432/db");
         */

        /*WebConfig.setDriver("com.mysql.jdbc.Driver");
         WebConfig.setPassword("");
         WebConfig.setUsername("root");
         WebConfig.setUrl("jdbc:mysql://127.0.0.1:3306/db");
         */
        if (Boolean.valueOf(servletContext.getInitParameter("DEV"))) {
            WebEntityManager emanager = new WebEntityManager();
            try {

                for (int i = 0; i < 50; i++) {
                    WebClient client = new WebClient()
                            .setAdress("ADRESSE" + i)
                            .setCodePostal("CLIENT" + i)
                            .setEmail("CLIENT" + i + "@hotmail.com")
                            .setNom("CLIENT" + i)
                            .setPays("MAROC" + i)
                            .setFax("002125000000" + i)
                            .setIdentifiantFiscal("IFC" + i)
                            .setTelephoneFixe("002125000000" + i)
                            .setTelephoneMobile("002126000000" + i)
                            .setVille("RABAT");
                    WebManager manager = new WebManager()
                            .setAdress("ADRESSE e" + i)
                            .setCodePostal("E " + i)
                            .setEmail("E" + i + "@hotmail.com")
                            .setNom("E " + i)
                            .setPays("MAROC")
                            .setTelephoneFixe("002125000010" + i)
                            .setTelephoneMobile("002126000010" + i)
                            .setVille("SALE" + i);
                    WebFournisseur fournisseur = new WebFournisseur()
                            .setAdress("ADRESSE F" + i)
                            .setCodePostal("CLIENT" + i)
                            .setEmail("fournisseur" + i + "@hotmail.com")
                            .setNom("fournisseur" + i)
                            .setPays("MAROC" + i)
                            .setFax("002125100000" + i)
                            .setIdentifiantFiscal("IF" + i)
                            .setTelephoneFixe("002125100020" + i)
                            .setTelephoneMobile("002126100020" + i)
                            .setVille("RABAT");
                    WebCategory category = new WebCategory()
                            .setNom("CATEGORIE" + i);
                    WebProduct product = new WebProduct()
                            .setCategory(category)
                            .setDescription("DESCRIPTION" + i)
                            .setLibele("PRODUIT" + i)
                            .setPrix(i * i + 10);
                    WebStock stock = new WebStock()
                            .setFournisseur(fournisseur)
                            .setDate(new Date())
                            .setPrix(i * i + 10)
                            .setQuantite(i * 5)
                            .setRemarque("REMARQUE" + i)
                            .setProduct(product);

                    emanager.create(fournisseur);
                    emanager.create(client);
                    emanager.create(manager);
                    emanager.create(category);
                    emanager.create(product);
                    emanager.create(stock);
                }

            } catch (Exception e) {
            }

        }

        try {
            WebManager manager = new WebManager()
                    .setAdress("ADMIN")
                    .setCodePostal("ADMIN")
                    .setEmail("ADMIN")
                    .setNom("ADMIN")
                    .setPays("ADMIN")
                    .setTelephoneFixe("ADMIN")
                    .setTelephoneMobile("ADMIN")
                    .setVille("ADMIN")
                    .setAdmin(true);
            new WebEntityManager(WebManager.class).create(manager);
        } catch (Exception e) {
        }
    }
}
