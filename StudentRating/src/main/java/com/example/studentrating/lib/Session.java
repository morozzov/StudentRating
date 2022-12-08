package com.example.studentrating.lib;

import javax.servlet.http.HttpSession;

public class Session {

    public static void setSession(HttpSession request, Long id, String type, boolean council) {
        request.setAttribute("id", id);
        request.setAttribute("type", type);
        request.setAttribute("council", council);
    }

    public static String isAuthorize(HttpSession request) {
        if (request.getAttribute("id") != null && request.getAttribute("type") != null)
            return request.getAttribute("type").toString();
        else return "";
    }

    public static void signOut(HttpSession request) {
        request.setAttribute("id", null);
        request.setAttribute("type", null);
        request.setAttribute("council", null);
    }

    public static Long getSessionId(HttpSession request) {

        return (Long) request.getAttribute("id");
    }

    public static String getSessionType(HttpSession request) {
        return (String) request.getAttribute("type");
    }
}
