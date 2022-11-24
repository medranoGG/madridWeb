package com.dws.web.Event;


import com.dws.web.Customer.Customer;
import com.dws.web.Review.Review;
import com.dws.web.Review.ReviewRepository;
import lombok.NoArgsConstructor;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

@Service
@NoArgsConstructor
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager entityManager;

    public void addEvent (Event event){

        //XSS//
        PolicyFactory policy= Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        event.setDescription(policy.sanitize(event.getDescription()));

        this.eventRepository.save(event);
    }

    public void assignCustomer (Customer customer, Event event){

        event.assignCustomer(customer);
        this.eventRepository.saveAndFlush(event);
    }

    public void addUpdatedEvent(Event eUP ){

        //XSS//
        PolicyFactory policy= Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        eUP.setDescription(policy.sanitize(eUP.getDescription()));

        this.eventRepository.saveAndFlush(eUP);
    }


    public Collection<Event> getEvents(){
        Collection<Event> allEvents=new HashSet<>();
        List<Event> l= eventRepository.findAll();
        for(Event e : l){
            allEvents.add(e);
        }
        return allEvents;
    }

    public Event getEvent(long idEvent){
        Optional<Event> e=eventRepository.findById(idEvent);
        if (e.isPresent()){
            return e.get();
        }
        else{
            return null;
        }
    }

    /*
    public Event getEvent(long id){
        Optional<Event> e=this.eventRepository.findById(id);
        if (e.isPresent()){
            return e.get();
        }
        else{
            return null;
        }
    }
     */

    public Long getEvent(Event e){
        return e.getId();
    }

    public boolean deleteEvent(long idEvent){
        Event e=this.eventRepository.getById(idEvent);
        if (e!=null) {
            e.cleanReviews();
            List<Review> reviewsDelete = this.reviewRepository.findByEvent(e);
            this.reviewRepository.deleteAll(reviewsDelete);
            this.eventRepository.delete(e);
            return true;
        }
        else{
            return false;
        }

    }

    public List<Event> getEventsFilteredByCategory(String category){  //Events filtered by category
        List<Event> eventsByCategory= eventRepository.findByCategory(category);
        return eventsByCategory;
    }

    public Event getEventByName(String name){
        TypedQuery<Event> query= entityManager.createQuery("SELECT e FROM Event e WHERE e.name = :name", Event.class);
        return query.setParameter("name", name).getSingleResult();
    }

    public Collection<Event> filterEvents(float priceMin, float priceMax) {

        TypedQuery<Event> query = entityManager.createQuery("SELECT e FROM Event e WHERE e.price <= :priceMax AND e.price >= :priceMin", Event.class);
        return query.setParameter("priceMin", priceMin).setParameter("priceMax", priceMax).getResultList();
    }

    public void removeCustomer(Customer c){
        for (Event e:eventRepository.findAll()){
            if (e.containsCustomer(c)){
                e.getCustomers().remove(c);
            }
        }
    }

    /*
    public Event getEventByName(String name){
        Optional<Event> e=this.eventRepository.findByName(name);
        if (e.isPresent()){
            return e.get();
        }
        else{
            return null;
        }
    }

     */

}