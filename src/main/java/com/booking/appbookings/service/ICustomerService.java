package com.booking.appbookings.service;
import com.booking.appbookings.entities.Customer;

import java.util.List;

public interface ICustomerService extends CrudService<Customer> {
    Customer findByDni(String dni) throws Exception;
    List<Customer> findByLastName(String lastName) throws Exception;
    List<Customer> findByFirstNameAndLastName(String firstName, String lastName) throws Exception;
    List<Customer> findByFirstName(String firstName) throws Exception;


}
