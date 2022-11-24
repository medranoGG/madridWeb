package com.dws.web.Event;

import com.dws.web.Customer.Customer;
import com.dws.web.Customer.CustomerService;
import com.dws.web.Review.Review;
import com.dws.web.Review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CustomerService customerService;

    //EVENT

    @PostMapping("/admin/events/new")
    public String newEvent(Model model, Event e) {

        eventService.addEvent(e);
        model.addAttribute("event",e);
        return "addedEvent";
    }

    @GetMapping("/admin/events/new")
    public String newEvent(Model model, @RequestParam String name,@RequestParam String category, @RequestParam String description, @RequestParam int price) {
        Event e= new Event(name,category,description,price);
        eventService.addEvent(e);
        model.addAttribute("event",e);
        return "addedEvent";
    }

    @GetMapping("/admin/delete/events/{idEvent}")
    public String deleteEvent(Model model, @PathVariable long idEvent) {
        boolean e=eventService.deleteEvent(idEvent);
        model.addAttribute("events", eventService.getEvents());
        return "events";
    }

    @PostMapping("/admin/events/{idEvent}/update")
    public String updateEvent(Model model, @PathVariable long idEvent, Event updatedEvent) {
        Event event = eventService.getEvent(idEvent);
        updatedEvent.setIdEvent(event.getIdEvent());
        this.eventService.addUpdatedEvent(updatedEvent);
        model.addAttribute("event",updatedEvent);
        return "updatedEvent";
    }

    @GetMapping("/admin/events/{idEvent}/modify") //Para modificar evento
    public String modifyAnEvent(Model model, @PathVariable long idEvent) {
        Event e = eventService.getEvent(idEvent);
        model.addAttribute("event", e);
        return "updateEvent";
    }

    @GetMapping("/events/all/restaurants")
    public String getRestaurants(Model model, HttpServletRequest request) {
        model.addAttribute("events", eventService.getEventsFilteredByCategory("restaurante"));
        model.addAttribute("usernameee", request.isUserInRole("USER"));
        model.addAttribute("adminnn", request.isUserInRole("ADMIN"));
        return "events";
    }

    @GetMapping("/events/all/leisure")
    public String getLeisure(Model model, HttpServletRequest request) {
        model.addAttribute("events", eventService.getEventsFilteredByCategory("ocio"));
        model.addAttribute("usernameee", request.isUserInRole("USER"));
        model.addAttribute("adminnn", request.isUserInRole("ADMIN"));
        return "events";
    }

    @GetMapping("/events/all/tourism")
    public String getTourism(Model model, HttpServletRequest request) {
        model.addAttribute("events", eventService.getEventsFilteredByCategory("turismo"));
        model.addAttribute("usernameee", request.isUserInRole("USER"));
        model.addAttribute("adminnn", request.isUserInRole("ADMIN"));
        return "events";
    }

    @GetMapping("/events")
    public String catalogue(Model model, HttpServletRequest request) {
        model.addAttribute("events", eventService.getEvents());
        //System.out.println(request.isUserInRole("USER"));
        model.addAttribute("usernameee", request.isUserInRole("USER"));
        model.addAttribute("adminnn", request.isUserInRole("ADMIN"));
        return "events";
    }

    @GetMapping("/events/{idEvent}") //Para crear review
    public String getAnEvent(Model model, @PathVariable long idEvent) {
        Event e = eventService.getEvent(idEvent);
        model.addAttribute("event", e);
        return "newReview";
    }

    @PostMapping("/query/events/filtered")
    public String filterBy(Model model, float priceMin, float priceMax){
        Collection<Event> l = this.eventService.filterEvents(priceMin,priceMax);
        model.addAttribute("events", l);
        return "events";
    }

    //REVIEW

    @PostMapping("/user/event/{idEvent}/review/new")
    public String newReview(Model model, @PathVariable long idEvent, Review r, Authentication auth) {
        Event e = eventService.getEvent(idEvent);
        String email = customerService.getEmailByCustomer(auth);
        Customer c = customerService.getCustomer(email);
        c.assignReviewToACustomer(r);
        r.assignCustomer(c);
        r.setUserName(email);
        reviewService.addReviewToThisEvent(e, r);
        model.addAttribute("review", r);
        return "addedReview";
    }

    @PostMapping("/event/{idEvent}/review/filtered")
    public String filterReview(Model model, @PathVariable long idEvent, String userName){
        Event event = eventService.getEvent(idEvent);
        Collection<Review> c = this.reviewService.filterReview(event, userName);
        model.addAttribute("event",event);
        model.addAttribute("reviews", c);
        return "reviews";
    }

    @GetMapping("/user/event/{idEvent}/review/{idReview}/delete")
    public String deleteReview(Model model, @PathVariable long idEvent, @PathVariable long idReview) {
        Event e = eventService.getEvent(idEvent);
        Review r=e.getReview(idReview);
        reviewService.deleteReviewFromAnEvent(e, r);
        model.addAttribute("review", r);
        return "deletedReview";
    }

    @GetMapping("/user/events/{idEvent}/review/{idReview}/modify") //Para modificar review
    public String getAnEvent3(Model model, @PathVariable long idEvent, @PathVariable long idReview) {
        Event e = eventService.getEvent(idEvent);
        Review r=e.getReview(idReview);
        model.addAttribute("review", r);
        model.addAttribute("event", e);
        return "updateReview";
    }

    @PostMapping("/user/event/{idEvent}/review/{idReview}/update")
    public String updateReview(Model model,@PathVariable long idEvent,@PathVariable long idReview, Review updatedReview, Authentication auth) {
        Event e = eventService.getEvent(idEvent);
        Review r = e.getReview(idReview);
        String email=customerService.getEmailByCustomer(auth);
        Customer c=customerService.getCustomer(email);
        updatedReview.setUserName(c.getEmail());
        if (r != null) {
            reviewService.addUpdatedReviewToThisEvent(idReview, updatedReview);
            model.addAttribute("r",reviewService.getReview(e,idReview));
            return "review";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/event/{idEvent}/reviews")
    public String getAllReviewsOfAnEvent(Model model,@PathVariable long idEvent) {
        Event e = this.eventService.getEvent(idEvent);
        model.addAttribute("reviews", this.reviewService.getAllReviewsOfAnEvent(e));
        model.addAttribute("event", e);
        return "reviews";
    }

    @GetMapping("/event/{idEvent}/{idReview}")
    public String getAReview(Model model, @PathVariable long idEvent, @PathVariable long idReview) {
        Event e = eventService.getEvent(idEvent);
        model.addAttribute("review", reviewService.getReview(e, idReview));
        return "review";
    }

    @GetMapping("/user/perfil")
    public String getAllReviewsOfACustomer(Model model, Authentication auth, HttpServletRequest request){
        String email=customerService.getEmailByCustomer(auth);
        model.addAttribute("reviews", customerService.getAllReviewsOfACustomer(customerService.getCustomer(email)));
        return "perfil";
    }

}
