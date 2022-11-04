package com.booking.appbookings.controller;

import com.booking.appbookings.entities.Customer;
import com.booking.appbookings.service.impl.CustomerServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebMvcTest(controllers = CustomerController.class)
@ActiveProfiles("test")
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerServiceImpl customerService;
    private List<Customer> customerList;

    @BeforeEach
    void setUp() {
        customerList = new ArrayList<>();
        customerList.add(new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "juan@gmail.com"));
        customerList.add(new Customer(2L, "Maria", "Perez", "23232321",
                "Av. Monterrico 124", "987987123", "maria@gmail.com"));
        customerList.add(new Customer(3L, "Pedro", "Cierto", "45232323",
                "Av. Monterrico 125", "987587187", "pedro@gmail.com"));
        customerList.add(new Customer(4L, "Luis", "Perez", "90232323",
                "Av. Monterrico 126", "987917987", "luis@gmail.com"));
        customerList.add(new Customer(5L, "Willy",
                "Cierto", "23239323", "Av. Monterrico 123",
                "987987987", "willy@gmail.com"));
        customerList.add(new Customer(6L, "Juana", "Trujillo", "23382323",
                "Av. Monterrico 127", "987287987", "juana@gmail.com"));
        customerList.add(new Customer(7L, "Jose", "Valdivia", "23278323",
                "Av. Monterrico 128", "987986987", "jose@gmail.com"));
        customerList.add(new Customer(8L, "Jesus", "Espiritu", "23226323",
                "Av. Monterrico 129", "987989987", "jesus@gmail.com"));

    }

    @Test
    void findAllCustomerTest() throws Exception {
        given(customerService.getAll()).willReturn(customerList);
        mockMvc.perform(get("/api/customers")).andExpect(status().isOk());
    }

    @Test
    void findCustomerByIdTest() throws Exception {
        Long customerId = 1L;
        Customer customer = new Customer (1L, "Juan",
                "Perez", "12121212", "Av. California 876",
                "987987987", "juan@gmail.com");
        given(customerService.getById(customerId)).willReturn(Optional.of(customer));
        mockMvc.perform(get("/api/customers/{id}", customerId)).andExpect(status().isOk());
    }

    @Test
    void findByDniTest()throws Exception{
        String dni = "12121212";
        Customer customer = new Customer (1L, "Juan",
                "Perez", "12121212", "Av. California 876",
                "987987987", "juan@gmail.com");
        given(customerService.findByDni(dni)).willReturn(customer);
        mockMvc.perform(get("/api/customers/searchByDni/{dni}", dni)).andExpect(status().isOk());
    }

    @Test
    void findByLastNameTest() throws Exception {
        String lastName = "Perez";

        given(customerService.findByLastName(lastName)).willReturn(customerList.stream()
                .filter(c -> c.getLastName().equals(lastName)).collect(Collectors.toList()));
        mockMvc.perform(get("/api/customers/searchByLastName/{lastname}", lastName))
                .andExpect(status().isOk());
    }

    @Test
    void findByFirstNameTest() throws Exception {
        String firstName = "Juan";

        given(customerService.findByFirstName(firstName)).willReturn(customerList.stream()
                .filter(c -> c.getFirstName().equals(firstName)).collect(Collectors.toList()));
        mockMvc.perform(get("/api/customers/searchByFirstName/{firstname}", firstName))
                .andExpect(status().isOk());
    }

    @Test
    void findByFirstNameAndLastNameTest() throws Exception {
        String firstName = "Juan";
        String lastName = "Perez";

        given(customerService.findByFirstNameAndLastName(firstName, lastName)).willReturn(customerList.stream()
                .filter(c -> c.getFirstName().equals(firstName) && c.getLastName().equals(lastName)).collect(Collectors.toList()));
        mockMvc.perform(get("/api/customers//findByFirstNameAndLastName/{firstname}/{lastname}", firstName, lastName))
                .andExpect(status().isOk());
    }

    @Test
    void insertCustomerTest() throws Exception {
        Customer customer = new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "jp@upc.edu.pe");
        mockMvc.perform(post("/api/customers", customer)
                        .content(asJsonString(customer))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isCreated());
    }

    @Test
    void updateCustomerTest() throws Exception {
        Long id = 1L;
        Customer customer = new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "jpc@upc.edu.pe");
        given(customerService.getById(id)).willReturn(Optional.of(customer));
        mockMvc.perform(put("/api/customers/{id}", id)
                        .content(asJsonString(customer))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk());

    }

    @Test
    void deleteCustomerTest() throws Exception {
        Long id = 1L;
        Customer customer = new Customer(1L, "Juan",
                "Perez", "23232323", "Av. Monterrico 123",
                "987987987", "jp@upc.edu.pe");
        given(customerService.getById(id)).willReturn(Optional.of(customer));
        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
