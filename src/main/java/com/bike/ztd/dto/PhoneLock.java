package com.bike.ztd.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PhoneLock {
    private String phone;
    private Date date;

    public PhoneLock() {
    }

    public PhoneLock(String phone, Date date) {
        this.phone = phone;
        this.date = date;
    }
}
