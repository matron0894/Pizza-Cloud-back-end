package com.springproject.shavermacloud.security;

import com.springproject.shavermacloud.oauth2.MySimpleUrlAuthenticationSuccessHandler;
import com.springproject.shavermacloud.oauth2.CustomOAuth2UserService;
import com.springproject.shavermacloud.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.transaction.Transactional;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService oauthUserService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, CustomOAuth2UserService oauthUserService) {
        this.userDetailsService = userDetailsService;

        this.oauthUserService = oauthUserService;
    }


    //уст-ка пользовательского хранилища, которое можно настроить
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("buzz")
                .password("infinity")
                .authorities("ROLE_USER")
                .and()
                .withUser("woody")
                .password("bullseye")
                .authorities("ROLE_USER");

        auth.authenticationProvider(getProvider());
    }

    @Bean
    @Transactional
    public AuthenticationProvider getProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//    return new StandardPasswordEncoder("53cr3t");
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.OPTIONS).permitAll() // needed for Angular/CORS

                .antMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/ingredients").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/ingredients").hasRole("ADMIN")

                .mvcMatchers("/design", "/orders/**").hasRole("USER")
                .mvcMatchers(HttpMethod.PATCH, "/ingredients").permitAll()
                .mvcMatchers("/", "/**", "/login", "/oauth/**").permitAll()

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/design", true)

                .and()
                .httpBasic()
                .realmName("Product_Cloud")

                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")

                .and()
                .csrf()
                .ignoringAntMatchers("/h2/**", "/ingredients/**", "/design", "/orders/**", "/api/**")

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                //external server authorisation
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)

                .and()
                .successHandler(myAuthenticationSuccessHandler())
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request,
//                                                        HttpServletResponse response,
//                                                        Authentication authentication) throws IOException {
//                        System.out.println("AuthenticationSuccessHandler invoked");
//                        System.out.println("Authentication name: " + authentication.getName());
//
//                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//                        userService.processOAuthPostLogin(oauthUser.getEmail());
//
////                        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
////                        String email = oauthUser.getAttribute("email");
////                        userService.processOAuthPostLogin(email);
//                        response.sendRedirect("redirect:/design");
//                    }
//                })
//                .defaultSuccessUrl("/design", true)
        ;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }


}
