package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;
import java.util.TimeZone;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter (HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String timezoneParam = req.getParameter("timezone");
        boolean validTimeZone = timezoneParam !=null || !timezoneParam.isEmpty() || Set.of(TimeZone.getAvailableIDs()).contains(timezoneParam);
        if (!validTimeZone) {
            resp.getWriter().write("Invalid timezone");
            resp.setStatus(400);
            return;
        }
        chain.doFilter(req, resp);
    }
}
