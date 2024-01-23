/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.register;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;


/**
 *
 * @author Yogeshwar_Info
 */
@MultipartConfig  //this annotation is important it tells jvm that we in this servlet we are using multimedia content or data without this annotation we cannot use multipart content
public class Register extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            // fetching data data from form

            String name = request.getParameter("user_name");
            String email = request.getParameter("user_email");
            String password = request.getParameter("user_password");
            
            Part part = request.getPart("image");
            String filename = part.getSubmittedFileName();
//            out.println(filename);

           

            // connecting jdbc
            try {
                
                Thread.sleep(3000);
                //load the class
                Class.forName("com.mysql.cj.jdbc.Driver");

                //createing connection;
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "tarun@1234");

                //creating queery;
                String q = "insert into data(name, password, email, imageName) values(?,?,?,?)";

                PreparedStatement pstm = con.prepareStatement(q);
                pstm.setString(1, name);
                pstm.setString(2, password);
                pstm.setString(3, email);
                pstm.setString(4, filename);

                pstm.executeUpdate();
                
                //upload..
                
                InputStream is = part.getInputStream();
                byte [] data = new byte[is.available()];
                String fileSeparator = FileSystems.getDefault().getSeparator();
                
                is.read(data);
                String path = request.getRealPath("/")+"img"+fileSeparator+filename;
//                out.println(path);
                FileOutputStream fos = new FileOutputStream(path);
                
                fos.write(data);
                fos.close();

                out.println("Done");

                //close connection
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
                out.println("error");
            }

            
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
