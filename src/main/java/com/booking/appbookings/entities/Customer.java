package com.booking.appbookings.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor

@NamedQuery(name = "Customer.findByLastName", query = "select c from Customer c where c.lastName = ?1")

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstName;
    @Column(name = "lastname", nullable = false, length = 50)
    private String lastName;
    @Column(name = "dni", nullable = false, length = 8)
    private String dni;
    @Column(name = "address", nullable = true, length = 150)
    private String address;
    @Column(name = "phone", nullable = true, length = 9)
    private String phone;
    @Column(name = "email", nullable = true, length = 50)
    private String email;

    //@JsonManagedReference
    //@JsonIgnore
    //@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Booking> bookings;
}
