package com.dws.web.Security;

import com.dws.web.Customer.Customer;
import com.dws.web.Customer.CustomerRepository;
import com.dws.web.Event.Event;
import com.dws.web.Event.EventRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseUsersLoader {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EventRepository eventRepository;


    @PostConstruct
    private void initDatabase() {

        //eventRepository.save(new Event("e1","Restaurante","e1",34));
        //customerRepository.save(new Customer("user2","surname","user2@user2.es","666-666-666", "pass2","calle 2","USER" ));
        //customerRepository.save(new Customer("user","surname","user@user.es","666-666-666","pass","calle 1","USER" ));
        customerRepository.save(new Customer("admin","admin","admin@admin.es","666-666-666","adminpass","calle A","ADMIN"));

        if (customerRepository.findByEmail("admin@admin.com") == null) {
            Customer ad = new Customer("admin", "-", "admin@admin.com", "-", "admin123", "-");
            ad.makeAdmin();
            customerRepository.save(ad);
        }

        //customerRepository.save(new Customer("user", "user", "user@user.es", "626-206-725",  passwordEncoder.encode("pass"), "CasaUser", "USER"));
        //customerRepository.save(new Customer("admin", "admin", "admin@admin.es", "626-206-725", passwordEncoder.encode("adminpass"), "CasaAdmin", "USER", "ADMIN"));
    }


}
