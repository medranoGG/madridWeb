package com.dws.web.Event;

import com.dws.web.Customer.Customer;
import com.dws.web.Review.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;


@Entity
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEvent;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    //@JsonView(Customer.Basico.class)
    private String description;

    //@JsonView(Customer.Basico.class)
    private float price;

    //@JsonView(Customer.Basico.class)
    private String category;

    @JsonIgnore
    @OneToMany(cascade =CascadeType.ALL, mappedBy = "event")
    private List<Review> reviews;

    @JsonIgnore
    @ManyToMany
    private List<Customer> customers;

    public Event(String name, String category, String description, float price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }


    public long getId() {
        return this.idEvent;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public float getPrice() {
        return this.price;
    }

    public String getCategory() {
        return this.category;
    }


    public long incrementAndGetId() {  //el increment no sabemos por que es, habra que incrementar algo
        return this.idEvent++;
    }

    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getIdEvent() {
        return idEvent;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    // REVIEW

    public void addReviewToThisEvent(Review r){
        this.reviews.add(r);
    }



    public void addUpdatedReviewToThisEvent(Review r){
        this.reviews.add(r);
    }

    public Review deleteReviewOfThisEvent(Review r){
        this.reviews.remove(r);
        return r;
    }

    public Review getReview(long idReview){
        for (Review r:this.reviews){
            if (r.getIdReview()==idReview){
                return r;
            }
        }
        return null;
    }

    public List<Review> getAllReviews(){
        return this.reviews;
    }

    @Override
    public String toString() {
        return "Evento:" + "\n" + "IdEvento: " + this.idEvent + "\n" + "nombre: " +
                this.name + "\n" + "descripción: " + this.description + "\n" +
                "precio: " + this.price + "\n";
    }

    public boolean sameCategory(String category) {
        return this.category.equalsIgnoreCase(category);
    }

    @Override
    public boolean equals(Object o) {  //We compare the events by their id
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        System.out.println(idEvent + " " + event.idEvent);
        return this.idEvent == event.idEvent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvent);
    }

    public void assignCustomer(Customer c){
        if(!this.customers.contains(c)){
            this.customers.add(c);
        }
    }

    public void unassignCustomer(Customer c){
        this.customers.remove(c);
    }

    public boolean containsCustomer(Customer c){
        return this.customers.contains(c);
    }

    public void cleanReviews(){
        this.reviews.clear();
    }

}
