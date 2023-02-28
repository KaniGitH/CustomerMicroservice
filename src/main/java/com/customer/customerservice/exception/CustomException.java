package com.customer.customerservice.exception;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class CustomException {
    public static void validateDOB(String dob) throws InvalidAgeException {

            int age = 0;
            LocalDate dateOfBirth = LocalDate.parse(dob);
            LocalDate curDate = LocalDate.now();
            if ((dateOfBirth != null) && (curDate != null)) {
                age = Period.between(dateOfBirth, curDate).getYears();
            }
            if (age < 18) {
                // throw an object of user defined exception
                throw new InvalidAgeException("age is not valid to open an account");
            }

    }
}
