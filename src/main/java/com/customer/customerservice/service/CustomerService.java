package com.customer.customerservice.service;


import com.customer.customerservice.entity.Customer;
import com.customer.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public Customer saveCustomer(Customer customer) {
        // save Customer detail
        return repository.save(customer);
    }

    public List<Customer> saveCustomers(List<Customer> customers) {
        // save all Customers detail
        return repository.saveAll(customers);
    }

    public List<Customer> getCustomers() {
        // get all Customers detail
        return repository.findAll();
    }

    public Customer getCustomerById(int id) {
        // get Customer detail by Customer id
        return repository.findById(id).orElse(null);
    }

    public List<Customer> getCustomerByCustomerFirstName(String cusName) {
        // get Customer detail by Customer first name
        return repository.findBycustomerFirstName(cusName);
    }

    public List<Customer> getCustomersBySearchKeyWord(String searchKeyWord) {
        // get Customer detail by Search keyword on customerfirstname and customerlastname
        return repository.findByCustomerFirstNameOrderByCustomerFirstNameAsc(searchKeyWord.toLowerCase());
    }

    public String deleteCustomerById(int customerId) {
        // delete Customer by Customer id
        repository.deleteById(customerId);
        return "Customer Id - " + customerId + " is deleted";
    }

    public Customer updateCustomer(Customer customer) {
        // get existing Customer
        Customer existingCustomer = repository.findById(customer.getCustomerId()).orElse(null);
        if (existingCustomer != null) {
            // update all fields of Customer
            existingCustomer.setCustomerFirstName(customer.getCustomerFirstName());
            existingCustomer.setCustomerLastName(customer.getCustomerLastName());
            existingCustomer.setCustomerGender(customer.getCustomerGender());
            existingCustomer.setCustomerDOB(customer.getCustomerDOB());
            existingCustomer.setCustomerAddress1(customer.getCustomerAddress1());
            existingCustomer.setCustomerAddress2(customer.getCustomerAddress2());
            existingCustomer.setCustomerPan(customer.getCustomerPan());
            existingCustomer.setCustomerAadhar(customer.getCustomerAadhar());
            existingCustomer.setCustomerMobileNumber(customer.getCustomerMobileNumber());
            existingCustomer.setCustomerMailId(customer.getCustomerMailId());
        }
        // save updated changes
        return repository.save(existingCustomer);
    }
}
