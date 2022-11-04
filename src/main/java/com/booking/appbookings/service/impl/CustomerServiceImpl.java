package com.booking.appbookings.service.impl;

import com.booking.appbookings.entities.Customer;
import com.booking.appbookings.repository.ICustomerRepository;
import com.booking.appbookings.service.ICustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)

public class CustomerServiceImpl implements ICustomerService {
    private final ICustomerRepository customerRepository;
    public CustomerServiceImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Customer save(Customer customer) throws Exception {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getAll() throws Exception {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getById(Long id) throws Exception {
        return customerRepository.findById(id);
    }

    @Override
    public Customer findByDni(String dni) throws Exception {
        return customerRepository.findByDni(dni);
    }

    @Override
    public List<Customer> findByLastName(String lastName) throws Exception {
        return customerRepository.findByLastName(lastName);
    }

    @Override
    public List<Customer> findByFirstNameAndLastName(String firstName, String lastName) throws Exception {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public List<Customer> findByFirstName(String firstName) throws Exception {
        return customerRepository.findByFirstName(firstName);
    }
}
