package com.booking.appbookings.service;

import com.booking.appbookings.entities.Customer;
import com.booking.appbookings.repository.ICustomerRepository;
import com.booking.appbookings.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private ICustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void saveTest(){
        Customer customer = new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "jp@upc.edu.pe");

        given(customerRepository.save(customer)).willReturn(customer);

        Customer savedCustomer = null;
        try{
            savedCustomer = customerService.save(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat (savedCustomer).isNotNull();
        assertEquals(customer, savedCustomer);
    }

    @Test
    public void deleteTest() throws Exception {
        Long id = 1L;
        customerService.delete(id);
        verify(customerRepository, times(1)).deleteById(id);
    }

    @Test
    public void getAllTest() throws Exception {
        List<Customer> list = new ArrayList<>();
        list.add(new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "japanese@gmail.com"));
        list.add(new Customer(2L, "Maria", "Quiroz",
                "23232323", "Av. Monterrico 1234",
                "987987986", "jaa@gmail.com"));
        list.add(new Customer(3L, "Pedro", "Zavalla", "23232323",
                "Av. Monterrico 123", "987987989", "jop@gmail.com"));
        list.add(new Customer(4L, "Luis", "Perez", "23232323",
                "Av. Monterrico 123", "987987981", "jap@gmail.com"));
        list.add(new Customer(5L, "Ana", "Perez", "23232323", "Av. Monterrico 123",
                "987987982", "ja@gmail.com"));

        given(customerRepository.findAll()).willReturn(list);
        List<Customer> listExpected = customerService.getAll();
        assertEquals(list, listExpected);
    }

    @Test
    public void getByIdTest() throws Exception {
        Long id = 1L;
        Customer customer = new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "cierto@gmial.com");
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        Optional<Customer> customerExpected = customerService.getById(id);
        assertThat(customerExpected).isNotNull();
        assertEquals(Optional.of(customer), customerExpected);
        //assertEquals(customer, customerExpected.get());
    }

    @Test
    public void findByDniTest() throws Exception {
        String dni = "23232323";
        Customer customer = new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "jp@upc.edu.pe");
        given(customerRepository.findByDni(dni)).willReturn(customer);
        Customer customerExpected = customerService.findByDni(dni);
        assertThat(customerExpected).isNotNull();
        assertEquals(customer, customerExpected);
    }

    @Test
    public void findByLastNameTest() throws Exception {
        String lastName = "Perez";
        List<Customer> list = new ArrayList<>();
        list.add(new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "juan@gmail.com"));
        list.add(new Customer(2L, "Maria", "Perez", "23232323",
                "Av. Monterrico 1234", "987987986", "maria@gmail.com"));
        list.add(new Customer(3L, "Pedro", "Perez", "23232323", "Av. Monterrico 123",
                "987987989", "pedro@gmail.com"));
        list.add(new Customer(4L, "Luis", "Perez", "23232323", "Av. Monterrico 123", "987987981",
                "luis@gmail.com"));

        given(customerRepository.findByLastName(lastName)).willReturn(list);
        List<Customer> listExpected = customerService.findByLastName(lastName);
        assertEquals(list, listExpected);
    }

    @Test
    public void findByFirstNameTest() throws Exception {
        String firstName = "Juan";
        List<Customer> list = new ArrayList<>();
        list.add(new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "juan@gmail.com"));
        list.add(new Customer(2L, "Juan", "Rojas", "23232323",
                "Av. Monterrico 1234", "987987986", "maria@gmail.com"));
        list.add(new Customer(3L, "Juan", "Quispe", "23232323", "Av. Monterrico 123",
                "987987989", "pedro@gmail.com"));
        list.add(new Customer(4L, "Juan", "Cierto", "23232323", "Av. Monterrico 123", "987987981",
                "luis@gmail.com"));
        given(customerRepository.findByFirstName(firstName)).willReturn(list);
        List<Customer> listExpected = customerService.findByFirstName(firstName);
        assertEquals(list, listExpected);
    }

    @Test
    public void findByFirstNameAndLastNameTest() throws Exception {
        String firstName = "Juan";
        String lastName = "Perez";
        List<Customer> list = new ArrayList<>();
        list.add(new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987187987", "juan@gmail.com"));
        list.add(new Customer(2L, "Juan", "Perez", "23232325",
                "Av. Monterrico 124", "987987913", "maria@gmail.com"));
        list.add(new Customer(3L, "Juan", "Perez", "23232326", "Av. Monterrico 125",
                "987987919", "pedro@gmail.com"));
        list.add(new Customer(4L, "Juan", "Perez", "23232329", "Av. Monterrico 126", "987987981",
                "luis@gmail.com"));
        given(customerRepository.findByFirstNameAndLastName(firstName, lastName)).willReturn(list);
        List<Customer> listExpected = customerService.findByFirstNameAndLastName(firstName, lastName);
        assertEquals(list, listExpected);
    }
}
