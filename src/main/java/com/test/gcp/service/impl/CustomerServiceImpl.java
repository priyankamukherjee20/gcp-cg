package com.test.gcp.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.CustomerDTO;
import com.test.gcp.service.CustomerService;
import com.test.gcp.util.CustomerValidation;

@Service
public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

  @Autowired
  private CustomerValidation customerValidation;

  @Override
  public CustomerDTO createCustomer(final CustomerDTO customerDTO) {

    logger.log(Level.INFO, () -> "createCustomer method starts ===>>> " + customerDTO);

    customerValidation.validateAddCustomer(customerDTO);
    String customerId = String.valueOf(CUSTOMERS.size() + 1);
    customerDTO.setCustomerId(customerId);
    CUSTOMERS.add(customerDTO);

    logger.log(Level.INFO, () -> "createCustomer method ends ===>>> " + customerDTO);
    return customerDTO;
  }

  @Override
  public List<CustomerDTO> getCustomers() {
    logger.log(Level.INFO, () -> "getCustomers method");
    return CUSTOMERS;
  }

  @Override
  public CustomerDTO getCustomersById(final String customerId) {

    logger.log(Level.INFO, () -> "getCustomersById method starts ===>>> " + customerId);
    CustomerDTO presentCustomerDTO = null;
    Optional<CustomerDTO> customerDTO = CUSTOMERS.stream()
        .filter(e -> e.getCustomerId().equals(customerId)).findFirst();
    if (customerDTO.isPresent()) {
      presentCustomerDTO = customerDTO.get();
    } else {
      throw new ResourceNotFoundException("customerId", customerId);
    }
    logger.log(Level.INFO, "getCustomersById method ends ===>>> " + presentCustomerDTO);
    return presentCustomerDTO;
  }

  @Override
  public CustomerDTO updateCustomer(final String customerId,
      final CustomerDTO customerDTO) {

    logger.log(Level.INFO, () -> "updateCustomer method starts ===>>> " + customerId);

    CustomerDTO customerDTO2 = customerValidation.validateUpdateCustomer(customerDTO,
        customerId);
    CUSTOMERS.remove(customerDTO2);
    customerDTO.setCustomerId(customerId);
    CUSTOMERS.add(customerDTO);
    logger.log(Level.INFO, () -> "updateCustomer method ends ===>>> " + CUSTOMERS);
    return customerDTO;
  }

  @Override
  public void deleteCustomer(final String customerId) {

    logger.log(Level.INFO, () -> "deleteCustomer method starts ===>>> " + customerId);

    Optional<CustomerDTO> customerDTO = CUSTOMERS.stream()
        .filter(e -> e.getCustomerId().equals(customerId)).findFirst();
    if (customerDTO.isPresent()) {
      CUSTOMERS.remove(customerDTO.get());
    } else {
      throw new ResourceNotFoundException("customerId", customerId);
    }
    logger.log(Level.INFO, () -> "deleteCustomer method ends ===>>> " + CUSTOMERS);
  }
}
