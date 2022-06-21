package com.springproject.prodcloud.oauth2;

import com.springproject.prodcloud.service.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private GoogleService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println("AuthenticationSuccessHandler invoked");
        System.out.println("Authentication name: " + authentication.getName());

        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        userService.processOAuthPostLogin(oauthUser.getName(), oauthUser.getEmail());

//                        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
//                        String email = oauthUser.getAttribute("email");
//                        userService.processOAuthPostLogin(email);
        response.sendRedirect("http://localhost:8080/design");
    }
}
