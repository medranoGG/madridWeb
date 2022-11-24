package com.dws.web.Customer;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByName(String name);

    @Query("SELECT u FROM Customer u WHERE u.email = :email")
    public Customer getCustomerByEmail(@Param("email") String email);

}


