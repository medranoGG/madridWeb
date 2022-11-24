package com.dws.web.Security;

import com.dws.web.Customer.CustomOAuth2User;
import com.dws.web.Customer.CustomOAuth2UserService;
import com.dws.web.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;


@Configuration
//@Order(1)
@EnableWebSecurity
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    private RepositoryUserDetailsService userDetailsService;
    @Autowired
    private CustomerService userService;
    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Override //Autorization
    protected void configure(HttpSecurity http) throws Exception{

        //Public pages
        http.authorizeRequests().antMatchers(
                "/css/**",
                "/js/**",
                "/images/**").permitAll();
        http.authorizeRequests().antMatchers("/").permitAll();
        //http.authorizeRequests().antMatchers("/index").permitAll();
        http.authorizeRequests().antMatchers("/event/**").permitAll();
        http.authorizeRequests().antMatchers("/events/**").permitAll();
        http.authorizeRequests().antMatchers("/user/**").hasRole("USER");
        http.authorizeRequests().antMatchers("/customer/**").permitAll();
        http.authorizeRequests().antMatchers("/query/events/filtered").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/logout").permitAll();
        http.authorizeRequests().antMatchers("/loginerror").permitAll();
        http.authorizeRequests().antMatchers("/oauth/**").permitAll();

        // Reject the others
        http.authorizeRequests().antMatchers("/admin/**").hasAnyRole("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();


        //Log in:
        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("email");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/");
        //http.formLogin().failureUrl("/login?error=error");
        http.formLogin().failureUrl("/loginerror");


        //Log out:
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
        //http.csrf().disable();
        //http.csrf().ignoringAntMatchers("/catalogo");
        //http.csrf().ignoringAntMatchers("/api/**");


        /*http.authorizeRequests()
                .antMatchers("/", "/login", "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService);
*/
        http.oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {

                        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();

                        userService.processOAuthPostLogin(oauthUser);

                        response.sendRedirect("/");
                    }
                });
    }
    @Bean //Declaramos una instancia, que podemos llamarla mas tarde con @Autowired (constructor de Costumer)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override //Authentication
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}

