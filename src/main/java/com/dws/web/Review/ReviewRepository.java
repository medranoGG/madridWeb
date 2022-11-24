package com.dws.web.Review;

import com.dws.web.Customer.Customer;
import com.dws.web.Event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByEvent(Event event);
    List<Review> findByCustomer(Customer customer);
}

