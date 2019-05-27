package com.demo.cloudsleuth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PaymentInfo {
    long id;
    Date date;
}
