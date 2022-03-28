package org.launchcode.lifeorganizer;

import org.launchcode.lifeorganizer.controllers.AuthenticationController;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/signup", "/login", "/css", "/logout", "/webjars");

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException, IllegalAccessException {

        String requestPath = request.getRequestURI();

        if (isWhitelisted(requestPath)) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (requestPath.startsWith("/admin")) {
            if(user.isAdmin()) {
                return true;
            } else {
                throw new IllegalAccessException("Path restricted to admin users");
            }
        }
            return true;
    }

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }


}
