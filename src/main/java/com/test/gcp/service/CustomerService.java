package com.test.gcp.service;

import java.util.LinkedList;
import java.util.List;

import com.test.gcp.payload.CustomerDTO;

public interface CustomerService {

     List<CustomerDTO> CUSTOMERS = new LinkedList<>();

     CustomerDTO createCustomer(CustomerDTO customerDTO);

     List<CustomerDTO> getCustomers();

     CustomerDTO getCustomersById(String customerId);

     CustomerDTO updateCustomer(String customerId, CustomerDTO customerDTO);

     void deleteCustomer(String customerId);
}
