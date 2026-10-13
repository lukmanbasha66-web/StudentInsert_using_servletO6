
package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static java.lang.System.out;

@WebServlet("/insertStudent")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String course = request.getParameter("course");

        String url = "jdbc:mysql://localhost:3306/lukmaan";
        String username = "root";
        String password = "manager";

        String sql = "INSERT INTO student (id, name, age, course) VALUES (?, ?, ?, ?)";

        try (var out = response.getWriter()) {

            try (Connection con =
                         DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setInt(3, age);
                ps.setString(4, course);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2>Student inserted successfully!</h2>");
                    out.println("<p>ID: " + id + "</p>");
                    out.println("<p>Name: " + name + "</p>");
                    out.println("<p>Age: " + age + "</p>");
                    out.println("<p>Course: " + course + "</p>");
                }

            } } catch (SQLException e) {
            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            getServletContext().log("Student insertion failed", e);

            out.println("<h2>Database error. Check the server log.</h2>");

        }
    }
}
