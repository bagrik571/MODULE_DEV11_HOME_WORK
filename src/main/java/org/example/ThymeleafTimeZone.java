package org.example;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

@WebServlet("/time")
    public class ThymeleafTimeZone extends HttpServlet {
        private TemplateEngine engine;
        private String currentTime;

        @Override
        public void init() throws ServletException {
            engine = new TemplateEngine();

            FileTemplateResolver resolver = new FileTemplateResolver();
            resolver.setPrefix("/Users/bagri/java16/MODULE_DEV11_HOME_WORK/src/main/webapp/templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML5");
            resolver.setOrder(engine.getTemplateResolvers().size());
            resolver.setCacheable(false);
            engine.addTemplateResolver(resolver);
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("text/html");

            String timezoneParam = req.getParameter("timezone");
            System.out.println("Received timezone parameter: " + timezoneParam);

            // Відновлюємо символ "+" перед передачею до методу ZoneId.of()
            if (timezoneParam != null) {
                timezoneParam = timezoneParam.replace(" ", "+");
            }

            boolean validTimeZone = timezoneParam !=null || !timezoneParam.isEmpty() || Set.of(TimeZone.getAvailableIDs()).contains(timezoneParam);
            if (validTimeZone){
                resp.addCookie(new Cookie("lastTimezone", timezoneParam));
            }
            resp.addCookie(new Cookie("lastTimezone", timezoneParam));
            Cookie[] cookies = req.getCookies();
            if ((timezoneParam == null || timezoneParam.isEmpty()) && cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lastTimezone".equals(cookie.getName())) {
                        timezoneParam = cookie.getValue();
                        break;
                    }
                }
            }
            ZoneId zoneId = (timezoneParam != null && !timezoneParam.isEmpty()) ? ZoneId.of(timezoneParam) : ZoneId.of("UTC");
            System.out.println("ZoneId: " + zoneId);

            currentTime = ZonedDateTime.now(zoneId).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

            Context zoneContext = new Context(
                    req.getLocale(),
                    Map.of("queryParamZoneId", zoneId, "queryParamCurrentTime", currentTime)
            );

            engine.process("time", zoneContext, resp.getWriter());
            resp.getWriter().close();
        }
    }
