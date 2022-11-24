
package com.dws.web.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
@Order(1)
public class CustomRestSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**");

        // Private endpoints

        //createAnEvent
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/admin/events/new").hasRole("ADMIN");
        //createAReview
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/event/{idEvent}/review/new").hasRole("USER");
        //createACustomer
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/customer/new").hasAnyRole("USER", "ADMIN");
        //addEventsToPlanning
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/events/{idEvent}/new/{email}").hasRole("USER");

        //modifyAnEvent
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/admin/events/{idEvent}/update").hasRole("ADMIN");
        //modifyACustomer
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/user/customer/update/{email}").hasRole("USER");
        //modifyYourOwnCustomer (HAY QUE IMPLEMENTARLO TODAVIA)
        //http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/admin/customer/update/{email}").hasAnyRole("ADMIN", "USER");
        //modifyAReview
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/event/{idEvent}/review/update/{idReview}").hasRole("USER");

        //deleteAnEvent
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/admin/events/{idEvent}/delete").hasRole("ADMIN");
        //deleteEventFromPlanning
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/user/planning/{email}/delete/{idEvent}").hasRole("USER");
        //deleteCustomer
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/admin/customer/delete/{email}").hasRole("ADMIN");
        //deleteReview
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/user/event/{idEvent}/review/{idReview}/delete").hasRole("USER");

        //QueryPriceMinPriceMax
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/events/filtered").hasAnyRole("ADMIN", "USER");
        //QuerySearchReview
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/event/{idEvent}/review/filtered").hasAnyRole("ADMIN", "USER");

        //getAllEvents
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/events").hasAnyRole("ADMIN", "USER");
        //getAnEvent
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/events/{idEvent}").hasAnyRole("ADMIN", "USER");
        //getEventsFilteredByCategory
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/events/category/{category}").hasAnyRole("ADMIN", "USER");

        //getAllReviewsOfAnEvent
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/event/{idEvent}/reviews").hasAnyRole("ADMIN", "USER");
        //getAReview
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/event/{idEvent}/review/{idReview}").hasAnyRole("ADMIN", "USER");
        //getAllReviewsOfACustomer (HAY QUE IMPLEMENTARLO)
        //http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/admin/events/category/{category}").hasRole("ADMIN");

        //getAllCustomers
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/admin/customers").hasRole("ADMIN");
        //getACustomer
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/admin/customer/{email}").hasRole("ADMIN");

        //getAllEventsFromPlanning
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/planning/{email}").hasRole("USER");
        //getAnEventFromPlanning
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/events/{email}/{idEvent}").hasRole("USER");
        //getAnEventsFilteredByCategoryPlanning
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/planning/{email}/category/{category}").hasRole("USER");

        http.authorizeRequests().antMatchers("/api/user/**").hasRole("USER");
        http.authorizeRequests().antMatchers("/api/event/**").hasRole("USER");
        http.authorizeRequests().antMatchers("/api/events/**").hasRole("USER");
        http.authorizeRequests().antMatchers("/api/user/**").hasRole("USER");
        http.authorizeRequests().antMatchers("/").hasRole("USER");

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf().disable();

        // Enable Basic Authentication
        http.httpBasic();

        // Disable Form login Authentication
        http.formLogin().disable();

        // Avoid creating session (because every request has credentials)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

}
