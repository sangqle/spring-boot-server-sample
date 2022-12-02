package com.example.springboot.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class HttpUtil {
    public static boolean outAndCloseJSON(HttpServletRequest req, HttpServletResponse resp, Object content) {
        boolean result = false;
        PrintWriter out = null;
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json; charset=UTF-8");
        try {
            out = resp.getWriter();
            out.print(content);
            result = true;
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return result;
    }
}
