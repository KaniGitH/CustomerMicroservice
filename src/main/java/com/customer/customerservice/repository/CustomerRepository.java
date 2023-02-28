package com.customer.customerservice.repository;

import com.customer.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findBycustomerFirstName(String customerFirstName);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.customerFirstName) like LOWER(CONCAT('%', ?1,'%')) or LOWER(c.customerLastName) like LOWER(CONCAT('%', ?1,'%')) order by c.customerFirstName asc" )
    List<Customer> findByCustomerFirstNameOrderByCustomerFirstNameAsc(String searchKeyWord);
}
