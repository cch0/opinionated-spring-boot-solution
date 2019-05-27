package com.demo.cloudsleuth.model;

import com.demo.cloudsleuth.client.model.PaymentInfo;
import lombok.Data;

@Data
public class AccountInfo {
    PaymentInfo paymentInfo;
}
