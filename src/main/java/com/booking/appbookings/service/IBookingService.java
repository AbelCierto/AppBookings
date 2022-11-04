package com.booking.appbookings.service;

import com.booking.appbookings.entities.Booking;
import java.util.Date;
import java.util.List;

public interface IBookingService extends CrudService<Booking> {
    List<Booking> findBookingByDates(Date checkin_date,Date checkout_date) throws Exception;
}
