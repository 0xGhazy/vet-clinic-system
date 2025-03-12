package com.vetclinic.api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IDGenerator {

    public static String trxId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return LocalDateTime.now().format(formatter);
    }
}
