package com.web.controller;

import com.web.bean.WebPassword;
import com.web.entity.WebUser;
import com.web.service.WebAuthManager;
import com.web.service.WebEntityManager;
import com.web.service.WebMailManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "DefaultController", urlPatterns = {"*.html", "", "*.ajax"}, loadOnStartup = 1)
public class DefaultController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getRequestURL().toString().contains("logout.html")) {
            logoutAction(request, response);
            
        } else if (request.getRequestURL().toString().contains("forgot.html")) {
            request.getRequestDispatcher("/WEB-INF/public/forgot.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/public/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("AJAX") != null) {
            ajaxAction(request, response);
        } else if (request.getParameter("MAIL") != null) {
            mailAction(request, response);
        } else if (request.getParameter("LOGIN") != null) {
            loginAction(request, response);
        } else if (request.getParameter("UPDATE") != null) {
            changepassAction(request, response);
        } else if (request.getParameter("FORGOT") != null) {
            forgotAction(request, response);
            
        }
    }

    private void loginAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebUser user = new WebUser(request);
        HttpSession session = request.getSession();
        WebAuthManager manager = new WebAuthManager();
        if (manager.isAuthenticated(user)) {
            user = manager.getAutenticatedUser();
            session.setAttribute("user", user);
            switch (user.getRole()) {
                case ADMIN:
                    request.setAttribute("page", "/index.saa");
                    request.getRequestDispatcher("/WEB-INF/public/ajax/redirect.jsp").forward(request, response);
                    break;
                case MANAGER:
                    request.setAttribute("page", "/index.sma");
                    request.getRequestDispatcher("/WEB-INF/public/ajax/redirect.jsp").forward(request, response);
                    break;
                case CLIENT:
                    request.setAttribute("page", "/index.sca");
                    request.getRequestDispatcher("/WEB-INF/public/ajax/redirect.jsp").forward(request, response);
                    break;
                default:
                    response.sendError(401);
                    break;
            }
        } else {
            response.sendError(401);
        }
    }

    private void forgotAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebMailManager  mailManager=null;
        try {
            mailManager = new WebMailManager();
        } catch (Exception ex) {
            Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebEntityManager<WebUser> webEntityManager=new WebEntityManager<>(WebUser.class);
        WebUser user=webEntityManager.findOne("WebUser.findByEmail",request.getParameter("email"));
        if(user!=null && mailManager!=null){
            mailManager.setSubject("recovery");
            mailManager.setTo(user.getEmail());
            mailManager.setBody("Username "+user.getUsername()+"<br> Password "+user.getPassword());
            mailManager.setFrom("Systeme Password recovery");
            mailManager.send();
            request.setAttribute("page", "/");
            request.getRequestDispatcher("/WEB-INF/public/ajax/redirect.jsp").forward(request, response);
        }else{
            response.sendError(401);
        }
    }

    private void logoutAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("login.html");
    }

    private void changepassAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        WebUser user=(WebUser)session.getAttribute("user");
        if(user!= null){
            WebPassword password=new WebPassword(request);
            password.setUser(user);
            if(password.isValid()){
                WebEntityManager manager=new WebEntityManager();
                manager.execute("WebUser.updatePassword",password.getNewPassword(),user.getEmail());
                session.invalidate();
                request.setAttribute("page", "/");
                request.getRequestDispatcher("/WEB-INF/public/ajax/redirect.jsp").forward(request, response);
            }else {
                response.sendError(401);
            }
        }
    }
    
    private void ajaxAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("CHANGEPASSWORD") != null) {
            request.getRequestDispatcher("/WEB-INF/public/ajax/password.jsp").forward(request, response);
        }
    }

    private void mailAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("NEW") != null) {
            request.getRequestDispatcher("/WEB-INF/public/ajax/mail.jsp").forward(request, response);
        }
        if (request.getParameter("SEND") != null) {
            try {
                WebMailManager manager = new WebMailManager(request);
                if (manager.send()) {
                    response.sendError(201);
                }
            } catch (Exception ex) {
                Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
