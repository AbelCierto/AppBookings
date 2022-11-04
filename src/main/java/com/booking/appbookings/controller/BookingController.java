package com.booking.appbookings.controller;

import com.booking.appbookings.entities.Booking;
import com.booking.appbookings.entities.Customer;
import com.booking.appbookings.service.IBookingService;
import com.booking.appbookings.service.ICustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Api(tags = "Booking", value = "Web Service RESTful of Bookings")
public class BookingController {
    private final IBookingService bookingService;

    private final ICustomerService customerService;

    public BookingController(IBookingService bookingService, ICustomerService customerService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Bookings List", notes = "Method for list bookings")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Bookings founds - DAOS"),
            @ApiResponse(code = 404, message = "Bookings Not Found"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<List<Booking>> findAllBookings(){
        try {
            List<Booking> bookings = bookingService.getAll();
            if(bookings.size()>0)
                return new ResponseEntity<>(bookings, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search Booking by Id", notes = "Method for find a booking by id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Booking found"),
            @ApiResponse(code = 404, message = "Booking Not Found"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })

    public ResponseEntity<Booking> findBookingById(@PathVariable("id")Long id){
        try {
            Optional<Booking> booking = bookingService.getById(id);
            if(!booking.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(booking.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/searchBetweenDates", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search Booking by Dates", notes = "Method for List bookings between dates")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Bookings found"),
            @ApiResponse(code = 404, message = "Bookings Not Found"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<List<Booking>> findBookingBetweenDates (
            @RequestParam(name = "checkin_date") String checkin_string,
            @RequestParam(name = "checkout_date") String checkout_string) {
       try {
           Date checkin_date = ParseDate(checkin_string);
           Date checkout_date = ParseDate(checkout_string);
           List<Booking> bookings = bookingService.findBookingByDates(checkin_date, checkout_date);
           if(bookings.size()>0)
               return new ResponseEntity<>(bookings, HttpStatus.OK);
           else
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    public static Date ParseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date result = null;
        try {
            result = format.parse(date);
        } catch (Exception ex) {

        }
        return result;
    }
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Register one booking of the customer", notes = "Method for register bookings")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Booking Created"),
            @ApiResponse(code = 404, message = "Booking Not Created"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<Booking> insertBooking(@PathVariable("id")Long id, @Valid @RequestBody Booking booking){
        try {
           Optional<Customer> customer = customerService.getById(id);
           if (customer.isPresent()) {
               booking.setCustomer(customer.get());
               Booking bookingNew = bookingService.save(booking);
               return ResponseEntity.status(HttpStatus.CREATED).body(bookingNew);
           }
           else {
               return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
           }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update data of the booking", notes = "Method for update bookings")
    @ApiResponses({
            @ApiResponse(code = 20, message = "Booking Updated"),
            @ApiResponse(code = 404, message = "Booking Not Updated"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<Booking> updateBooking (@PathVariable("id")Long idBooking, @Valid @RequestBody Booking booking){
        try {
            Optional<Booking> bookingOld = bookingService.getById(idBooking);
            if(!bookingOld.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else {
                booking.setId(idBooking);
                bookingService.save(booking);
                return new ResponseEntity<>(booking, HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //DELETE
    //http://localhost:8080/api/bookings/3
    //Object JSON
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Booking by Id", notes = "Method for delete bookings")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Booking Deleted"),
            @ApiResponse(code = 404, message = "Booking Not Deleted"),
            @ApiResponse(code = 501, message = "Internal Server Error")
    })
    public ResponseEntity<Booking> deleteBooking (@PathVariable("id")Long id){
        try {
            Optional<Booking> bookingDelete = bookingService.getById(id);
            if(bookingDelete.isPresent()){
                bookingService.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
