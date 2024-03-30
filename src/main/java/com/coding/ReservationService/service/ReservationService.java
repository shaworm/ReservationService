package com.coding.ReservationService.service;

import com.coding.ReservationService.model.MobilePhone;
import com.coding.ReservationService.model.Tester;
import lombok.Synchronized;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NameNotFoundException;
import java.time.Instant;
import java.util.*;

public class ReservationService {

    public static Map<String, List<MobilePhone>> mobilePhones = new HashMap<>();

    static {
        mobilePhones.put("Samsung Galaxy S9", Arrays.asList(new MobilePhone("Samsung Galaxy S9", true, null, null)));
        mobilePhones.put("Samsung Galaxy S8", Arrays.asList(new MobilePhone("Samsung Galaxy S8", true, null, null),
                                                            new MobilePhone("Samsung Galaxy S8", true, null, null)));
        mobilePhones.put("Motorola Nexus 6", Arrays.asList(new MobilePhone("Motorola Nexus 6", true, null, null)));
        mobilePhones.put("Oneplus 9", Arrays.asList(new MobilePhone("Oneplus 9", true, null, null)));
        mobilePhones.put("Apple iPhone 13", Arrays.asList(new MobilePhone("Apple iPhone 13", true, null, null)));
        mobilePhones.put("Apple iPhone 12", Arrays.asList(new MobilePhone("Apple iPhone 12", true, null, null)));
        mobilePhones.put("Apple iPhone 11", Arrays.asList(new MobilePhone("Apple iPhone 11", true, null, null)));
        mobilePhones.put("iPhone X", Arrays.asList(new MobilePhone("iPhone X", true, null, null)));
        mobilePhones.put("Nokia 3310", Arrays.asList(new MobilePhone("Nokia 3310", true, null, null)));
    }

    @Transactional
    @Synchronized
    public MobilePhone reservePhone(String name, Tester tester) throws NameNotFoundException {
        List<MobilePhone> mobilePhonesByName = mobilePhones.get(name);
        if (mobilePhonesByName == null) {
            throw new NameNotFoundException("No entry found for the phone name:" + name);
        }

        Optional<MobilePhone> mobilePhone = mobilePhonesByName.stream().filter(MobilePhone::isAvailable).findFirst();
        mobilePhone.ifPresent(mp -> {
            mp.setBookedBy(tester);
            mp.setBookedDate(Instant.now());
            mp.setAvailable(false);
        });
        return mobilePhone.orElse(null);
    }

    @Transactional
    @Synchronized
    public MobilePhone returnPhone(String name, Tester tester) throws NameNotFoundException {
        List<MobilePhone> mobilePhonesByName = mobilePhones.get(name);
        if (mobilePhonesByName == null) {
            throw new NameNotFoundException("No entry found for the phone name:" + name);
        }

        Optional<MobilePhone> mobilePhone = mobilePhonesByName.stream().filter(mp -> !mp.isAvailable() && mp.getBookedBy().equals(tester)).findFirst();
        mobilePhone.ifPresent(mp -> {
            mp.setBookedBy(null);
            mp.setBookedDate(null);
            mp.setAvailable(true);
        });
        return mobilePhone.orElse(null);
    }

}
