package com.customer.customerservice.controller;


import com.customer.customerservice.entity.Customer;
import com.customer.customerservice.exception.CustomException;
import com.customer.customerservice.exception.InvalidAgeException;
import com.customer.customerservice.response.ResponseHandler;
import com.customer.customerservice.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService service;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/addCustomer")
    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {

        String strMessages = "";

        try {
            CustomException.validateDOB(customer.getCustomerDOB());
        } catch (InvalidAgeException ex) {
            strMessages = "Customer Date of Birth : " + customer.getCustomerDOB() + " - " + ex;
        }

        if (strMessages.toString() == "") {
            // save Customer details
            return ResponseHandler.generateResponse("Customer added successfully!", HttpStatus.OK, service.saveCustomer(customer));
        } else {

            return ResponseHandler.generateResponse(strMessages, HttpStatus.BAD_REQUEST, customer);
        }
    }

    @PostMapping("/addCustomers")
    public ResponseEntity<Object> addCustomers(@RequestBody List<Customer> customers) {

        String strMessages = "";
        for (Customer customer : customers) {
            try {
                CustomException.validateDOB(customer.getCustomerDOB());
            } catch (InvalidAgeException ex) {
                strMessages += "\n Customer Date of Birth : " + customer.getCustomerDOB() + " - " + ex;
            }
        }

        if (strMessages.toString() == "") {
            // save Customers details
            return ResponseHandler.generateResponse("Customers added successfully!", HttpStatus.OK, service.saveCustomers(customers));
        } else {
            return ResponseHandler.generateResponse(strMessages, HttpStatus.BAD_REQUEST, customers);
        }
    }

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        // get all Customers details
        return service.getCustomers();
    }

    @GetMapping("/customerById/{id}")
    public ResponseEntity<Object> findCustomer(@PathVariable String id) {

        try {
            int nId = Integer.parseInt(id);

            // get all Customer details
            Customer customer = service.getCustomerById(nId);
            if (customer == null) {
                return ResponseHandler.generateResponse("Given Customer id - " + nId + " not exist!", HttpStatus.NOT_FOUND, null);
            } else {
                return ResponseHandler.generateResponse("Given Customer id - " + nId + " exist!", HttpStatus.OK, customer);
            }

        } catch (NumberFormatException e) {
            return ResponseHandler.generateResponse("Please enter valid Customer Id in number format!", HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/customerByName/{customerName}")
    public List<Customer> getCustomerByCustomerName(@PathVariable String customerName) {

        // get Customer by Customer first name
        return service.getCustomerByCustomerFirstName(customerName);
    }

    @GetMapping("/customerSearch/{searchKeyWord}")
    public List<Customer> getCustomerBysearchKeyWord(@PathVariable String searchKeyWord) {

        // get Customer by Customer first name using search KeyWord
        return service.getCustomersBySearchKeyWord(searchKeyWord);
    }

    @PutMapping("/updateCustomer")
    public Customer updateCustomer(@RequestBody Customer customer) {
        // update Customer details
        return service.updateCustomer(customer);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public String deleteCustomerById(@PathVariable String id) {

        try {
            int nId = Integer.parseInt(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            Map<String, Object> gfg = new HashMap<String, Object>();

            ResponseEntity<JsonNode> result = restTemplate.exchange("http://localhost:8082/accountByCustomerId/" + id, HttpMethod.GET, entity, JsonNode.class);
            JsonNode json = result.getBody();

            JsonNode accounts = json.get("data");
            if (accounts.size() > 0) {
                return "Customer Id - " + nId + " having active account, so cont to delete";
            } else {
                //delete customer
                return service.deleteCustomerById(nId);
            }
        }catch (NumberFormatException e) {
            return "Please enter valid Customer Id in number format!";
        }
    }
}
