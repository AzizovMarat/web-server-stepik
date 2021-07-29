package com.stepikcourse.webserveronjava18.controller;

import com.stepikcourse.webserveronjava18.entity.RequestObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Controller
public class DefaultController {

    @GetMapping("/*")
    public String getPage(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        RequestObject pageVariables = createPageVariables(request);

        model.addAttribute("requestObject", pageVariables);

        return "page";
    }

    @GetMapping("/mirror{key}")
    public void getKey(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println(request.getParameter("key"));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PostMapping("/*")
    public String postPage(Model model, HttpServletRequest request, HttpServletResponse response) {

        RequestObject pageVariables = createPageVariables(request);
        pageVariables.setMessage(request.getParameter("message"));

        if (pageVariables.getMessage() == null || pageVariables.getMessage().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }

        model.addAttribute("requestObject", pageVariables);

        return "page";
    }

    private static RequestObject createPageVariables(HttpServletRequest request) {
        RequestObject requestObject = new RequestObject();
        requestObject.setMethod(request.getMethod());
        requestObject.setUrl(request.getRequestURL().toString());
        requestObject.setPathInfo(request.getServletPath()); //в исходнике на stepik - request.getPathInfo() не выдает то что необходимо
        requestObject.setSessionId(request.getSession().getId());
        Map<String, String[]> map = request.getParameterMap();

        StringBuilder strParameters = new StringBuilder();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            strParameters.append("{")
                    .append(entry.getKey())
                    .append(" : ")
                    .append(Arrays.toString(entry.getValue()))
                    .append("}");
        }
        requestObject.setParameters(strParameters.toString()); //в исходнике на stepik - request.getParameterMap().toString() выдает адрес, а не содержание
        return requestObject;
    }

}