package com.test.gcp.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.test.gcp.exception.ResourceFoundException;
import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.CustomerDTO;
import com.test.gcp.service.CustomerService;

@Component
public class CustomerValidation {

    public void validateAddCustomer(final CustomerDTO customerDTO) {

        Optional<CustomerDTO> optionalCustomerDTO = CustomerService.CUSTOMERS.stream().filter(e -> e.getCustomerName().equalsIgnoreCase(customerDTO.getCustomerName())).findFirst();
        if (optionalCustomerDTO.isPresent()) {
            throw new ResourceFoundException("Name", customerDTO.getCustomerName());
        }

    }

    public CustomerDTO validateUpdateCustomer(final CustomerDTO customerDTO, final String customerId) {

        Optional<CustomerDTO> presentCustomerDTO = CustomerService.CUSTOMERS.stream().filter(e -> e.getCustomerId().equals(customerId)).findFirst();
        if (!presentCustomerDTO.isPresent()) {
            throw new ResourceNotFoundException("customerId", customerId);
        } else {
            Optional<CustomerDTO> optionalCustomerDTO = CustomerService.CUSTOMERS.stream().filter(e -> e.getCustomerName().equalsIgnoreCase(customerDTO.getCustomerName()) && !e.getCustomerId().equals(customerId)).findFirst();
            if (optionalCustomerDTO.isPresent()) {
                throw new ResourceFoundException("Name", customerDTO.getCustomerName());
            }
        }
        return presentCustomerDTO.get();
    }
}
