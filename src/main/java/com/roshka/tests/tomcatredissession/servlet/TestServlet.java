package com.roshka.tests.tomcatredissession.servlet;

import com.roshka.tests.tomcatredissession.model.SessionObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TestServlet", urlPatterns = "/test")
public class TestServlet extends HttpServlet  {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        SessionObject so = null;
        if (session.isNew()) {
            System.err.println("New session with ID: " + session.getId());
            so = new SessionObject();
            session.setAttribute("so", so);
        } else {
            so = (SessionObject) session.getAttribute("so");
        }

        PrintWriter writer = resp.getWriter();
        if (so != null) {
            resp.setContentType("application/json");
            writer.write("" +
                    "{" +
                    "\"status\": \"" + so.getStatus() + "\", " +
                    "\"counter\": " + so.getCounter() + ", " +
                    "\"soc\": {" +
                    "\"asdf\": " + so.getSessionObjectChild().getCreated() + "\"" +
                            "}" +
                    "}");
            so.setCounter(so.getCounter() + 1);
            so.setStatus("RUNNING " + so.getCounter());
        } else {
            writer.write("{\"error\": \"so is null\"}");
        }
    }
}
