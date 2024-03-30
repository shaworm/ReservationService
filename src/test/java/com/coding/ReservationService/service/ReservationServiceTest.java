package com.coding.ReservationService.service;

import com.coding.ReservationService.model.MobilePhone;
import com.coding.ReservationService.model.Tester;
import org.junit.jupiter.api.Test;

import javax.naming.NameNotFoundException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService reservationService = new ReservationService();

    String wrongPhoneName = "wrong";
    Tester tester1 = new Tester("first1", "last1", "first1.last1@test.com");
    Tester tester2 = new Tester("first2", "last2", "first2.last2@test.com");
    String iPhoneX = "iPhone X";
    String ssg8 = "Samsung Galaxy S8";

    @Test
    void reservePhoneWrongName() {
        assertThrows(NameNotFoundException.class,
                () -> {reservationService.reservePhone(wrongPhoneName, null);},
                "No entry found for the phone name:" + wrongPhoneName);
    }

    @Test
    void reservePhoneUniqueName() throws NameNotFoundException {
        MobilePhone mobilePhone = reservationService.reservePhone(iPhoneX, tester1);
        assertEquals(tester1, mobilePhone.getBookedBy());
        assertEquals(false, mobilePhone.isAvailable());
        assertEquals(iPhoneX, mobilePhone.getName());
        assertNotNull(mobilePhone.getBookedDate());
    }

    @Test
    void reservePhoneMultipleUnitsAllAvailable() throws NameNotFoundException {
        MobilePhone mobilePhone = reservationService.reservePhone(ssg8, tester1);
        assertEquals(tester1, mobilePhone.getBookedBy());
        assertEquals(false, mobilePhone.isAvailable());
        assertEquals(ssg8, mobilePhone.getName());
        assertNotNull(mobilePhone.getBookedDate());
    }

    @Test
    void reservePhoneMultipleUnitsOneAvailable() throws NameNotFoundException {
        reservationService.reservePhone(ssg8, tester2);
        MobilePhone mobilePhone = reservationService.reservePhone(ssg8, tester1);
        assertEquals(tester1, mobilePhone.getBookedBy());
        assertEquals(false, mobilePhone.isAvailable());
        assertEquals(ssg8, mobilePhone.getName());
        assertNotNull(mobilePhone.getBookedDate());
    }

    @Test
    void reservePhoneMultipleUnitsNoneAvailable() throws NameNotFoundException {
        reservationService.reservePhone(ssg8, tester2);
        reservationService.reservePhone(ssg8, tester2);
        MobilePhone mobilePhone = reservationService.reservePhone(ssg8, tester1);
        assertNull(mobilePhone);
    }

    @Test
    void reservePhoneUnavailable() throws NameNotFoundException {
        reservationService.reservePhone(iPhoneX, tester1);
        MobilePhone mobilePhone = reservationService.reservePhone(iPhoneX, tester2);
        assertNull(mobilePhone);
    }

    @Test
    void returnPhoneWrongName() {
        assertThrows(NameNotFoundException.class,
                () -> {reservationService.returnPhone(wrongPhoneName, null);},
                "No entry found for the phone name:" + wrongPhoneName);
    }

    @Test
    void returnPhoneUniqueName() throws NameNotFoundException {
        reservationService.reservePhone(iPhoneX, tester1);
        MobilePhone mobilePhone = reservationService.returnPhone(iPhoneX, tester1);
        assertNull(mobilePhone.getBookedBy());
        assertEquals(true, mobilePhone.isAvailable());
        assertEquals(iPhoneX, mobilePhone.getName());
        assertNull(mobilePhone.getBookedDate());
    }

    @Test
    void returnPhoneUniqueNameWrongTester() throws NameNotFoundException {
        reservationService.reservePhone(iPhoneX, tester1);
        MobilePhone mobilePhone = reservationService.returnPhone(iPhoneX, tester2);
        assertNull(mobilePhone);
    }

    @Test
    void returnPhoneMultipleUnitsAllAvailable() throws NameNotFoundException {
        MobilePhone mobilePhone = reservationService.returnPhone(ssg8, tester1);
        assertNull(mobilePhone);
    }

    @Test
    void returnPhoneMultipleUnitsOneReserved() throws NameNotFoundException {
        reservationService.reservePhone(ssg8, tester1);
        MobilePhone mobilePhone = reservationService.returnPhone(ssg8, tester1);
        assertNull(mobilePhone.getBookedBy());
        assertEquals(true, mobilePhone.isAvailable());
        assertEquals(ssg8, mobilePhone.getName());
        assertNull(mobilePhone.getBookedDate());
        Map<String, List<MobilePhone>> allPhones = ReservationService.mobilePhones;
        assertEquals(2, allPhones.get(ssg8).stream().filter(mp -> mp.isAvailable()).count());
    }

    @Test
    void returnPhoneMultipleUnitsAllReserved() throws NameNotFoundException {
        reservationService.reservePhone(ssg8, tester1);
        reservationService.reservePhone(ssg8, tester2);
        MobilePhone mobilePhone = reservationService.returnPhone(ssg8, tester1);
        assertNull(mobilePhone.getBookedBy());
        assertEquals(true, mobilePhone.isAvailable());
        assertEquals(ssg8, mobilePhone.getName());
        assertNull(mobilePhone.getBookedDate());
        Map<String, List<MobilePhone>> allPhones = ReservationService.mobilePhones;
        assertEquals(1, allPhones.get(ssg8).stream().filter(mp -> mp.isAvailable()).count());
    }

}