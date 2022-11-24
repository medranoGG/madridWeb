package com.dws.web.Customer;

import com.dws.web.Event.Event;
import com.dws.web.Event.EventRepository;
import com.dws.web.Review.Review;
import com.dws.web.Review.ReviewRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


@Service
@NoArgsConstructor
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    public void addClient(Customer c) {
        List<String> roles=new ArrayList<>();
        roles.add("USER");
        c.setRoles(roles);
        this.customerRepository.save(c);
    }

    public void addUpdatedClient(Customer c) {
        List<String> roles=new ArrayList<>();
        roles.add("USER");
        c.setRoles(roles);
        this.customerRepository.save(c);
    }

    public void addUpdatedClientADMIN(Customer c) {
        List<String> roles=new ArrayList<>();
        roles.add("ADMIN");
        c.setRoles(roles);
        this.customerRepository.save(c);
    }

    public Customer getClient(long id) {
        Optional<Customer> c = customerRepository.findById(id);
        if (c.isPresent()) {
            return c.get();
        } else {
            return null;
        }
    }


    public Customer getCustomer(String email) {
        Optional<Customer> c = this.customerRepository.findByEmail(email);
        if (c.isPresent()) {
            return c.get();
        } else {
            return null;
        }

    }

    public Collection<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Customer deleteCustomer(long id) {
        Customer c = customerRepository.getById(id);
        customerRepository.delete(c);
        return c;
    }

    public void updateCustomer(Customer c, long id) {
        Customer c1 = customerRepository.getById(id);
        customerRepository.delete(c1);
        customerRepository.save(c);
    }

    public boolean addEventToPlanning(long idCustomer, Event e) {
        try {
            Customer c = customerRepository.getById(idCustomer);
            c.addToPlanning(e);
            customerRepository.saveAndFlush(c);
            return true;
        }catch (Exception ex){
            return false;
        }
    }


    public Event deleteEventFromPlanning(Customer c, Event e) {
        c.deleteEvent(e);
        e.unassignCustomer(c);
        customerRepository.saveAndFlush(c);
        eventRepository.saveAndFlush(e);
        return e;
    }

    /*
    public void updateAnEvent(Customer c, long idOldEvent, Event updatedEvent) {
        c.deleteEvent(idOldEvent);
        updatedEvent.setIdEvent(idOldEvent);
        c.addToPlanning(updatedEvent);
    }
     */

    public Collection<Event> getAllEventsOfACustomer(Customer c) {
        return eventRepository.findByCustomers(c);
    }

    public Collection<Review> getAllReviewsOfACustomer(Customer c){
        return reviewRepository.findByCustomer(c);
    }

    public Event getAnEvent(Customer c, long idEvent) {
        return c.getAnEvent(idEvent);
    }

    public Event getAnEventByNoun(Customer c, String name) {
        for (Event e : getAllEventsOfACustomer(c)) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public void cleanPlanning(Customer c) {
        c.cleanPlanning();
    }

    public Customer getCustomerByEmail(String email){
        Optional<Customer> c=customerRepository.findByEmail(email);
        if (c.isPresent()){
            return c.get();
        }
        else{
            return null;
        }
    }

    public Customer getCustomerByName(String name){
        Optional<Customer> c=customerRepository.findByName(name);
        if (c.isPresent()){
            return c.get();
        }
        else{
            return null;
        }
    }

    public boolean containsCustomer(Customer customer){
        for (Customer c:this.customerRepository.findAll()){
            if (customer.getEmail().equals(c.getEmail())){
                return true;
            }
        }
        return false;
    }

    public boolean esAdmin(Customer c){
        if (c.getRoles().contains("ADMIN")){
            return true;
        }
        else{
            return false;
        }
    }
/*
    public void processOAuthPostLogin(String email) {
        Customer existUser = customerRepository.getCustomerByEmail(email);

        if (existUser == null) {
            Customer newCustomer = new Customer();
            newCustomer.setEmail(email);
            newCustomer.setName("default Google");
            newCustomer.setSurname("default Google");
            newCustomer.setAddress("default Google");
            newCustomer.setPhoneNumber("default Google");
            newCustomer.setPassword("default Google");
            List<String> roles=new ArrayList<>();
            roles.add("USER");
            newCustomer.setRoles(roles);
            customerRepository.saveAndFlush(newCustomer);
        }

    }*/

    public void processOAuthPostLogin(DefaultOidcUser customer) {
        BCryptPasswordEncoder aux = new BCryptPasswordEncoder();
        Map<String, Object> m1 = customer.getClaims();
        if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
            Customer newCustomer = new Customer();
            newCustomer.setName(m1.get("given_name").toString());
            newCustomer.setPassword(aux.encode(customer.getAccessTokenHash()));
            newCustomer.setEmail(m1.get("email").toString());
            newCustomer.setSurname(m1.get("family_name").toString());
            customerRepository.save(newCustomer);
        }
    }

    public String getEmailByCustomer(String customerName){
        Optional<Customer> c=customerRepository.findByName(customerName);
        if(c.isPresent()){
            return c.get().getEmail();
        }
        else{
            return "No existe este usuario";
        }
    }


    public String getEmailByCustomer(Authentication auth){

        if(auth.getPrincipal().getClass()== User.class){ //Interfaz
            return auth.getName();
        }

        if(auth.getPrincipal().getClass()== DefaultOidcUser.class){ //Oauth
            Optional<Customer> c=customerRepository.findByEmail((String) ((Map<String, Object>)((DefaultOidcUser)auth.getPrincipal()).getClaims()).get("email"));
            if(c.isPresent()){
                return c.get().getEmail();
            }
            else{
                return "No existe este usuario";
            }
        }
        return null;
    }




}