package com.covid19tracker.web.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtils {

    public static String getOtp(){
        String otp = new Random().ints(6, 0, 9).
                mapToObj(operand -> operand + "").collect(Collectors.joining());
      log.debug("OTP generated {}" ,otp);
      return otp;
    }

    public static Calendar getCalendar(int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field,amount);
        return calendar;
    }

}
