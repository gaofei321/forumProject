package com.kaishengit.web;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseServlet extends HttpServlet {

    public void forward(String path, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/"+path).forward(req,resp);
    }


    public void renderText(String str,HttpServletResponse resp)throws IOException{
       resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out=resp.getWriter();
        out.print(str);
        out.flush();
        out.close();
    }

    public void renderJSON(Object obj,HttpServletResponse resp)throws IOException{
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out=resp.getWriter();
        out.print(new Gson().toJson(obj));
        out.flush();
        out.close();
    }
}
