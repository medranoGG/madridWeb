package com.dws.web.Review;

import com.dws.web.Customer.Customer;
import com.dws.web.Event.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
public class Review {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonView(Customer.Basico.class)
    private long idReview;

    //@JsonView(Customer.Basico.class)
    private String userName;

    //@JsonView(Customer.Basico.class)
    private String message;

    @ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Event event;

    @JsonIgnore
    @ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;


    public Review(String message){
        this.message=message;
    }

    public long getIdReview() {
        return this.idReview;
    }

    public long getIdEvent(){
        return this.event.getIdEvent();
    }

    public void setIdReview(long idReview) {
        this.idReview = idReview;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Review{" +
                "idReview=" + idReview +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


    public void assignEvent(Event e){
        this.event=e;
    }

    public void unassignEvent(){this.event=null;}

    public void assignCustomer(Customer c){
        this.customer=c;
    }

    public void unassignCustomer(){
        this.customer=null;
    }

    public Customer getCustomer(){
        return this.customer;
    }



}