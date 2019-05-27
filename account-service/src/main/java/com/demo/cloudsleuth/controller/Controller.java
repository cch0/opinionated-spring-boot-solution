package com.demo.cloudsleuth.controller;

import com.demo.cloudsleuth.client.PaymentClient;
import com.demo.cloudsleuth.client.model.PaymentInfo;
import com.demo.cloudsleuth.model.AccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @Autowired
    PaymentClient paymentClient;

    @RequestMapping("/accounts/{id}")
    public AccountInfo getAccountInfo(@PathVariable long id) {
        log.info("Handling getAccountInfo, id={}", id);

        PaymentInfo paymentInfo = paymentClient.getPaymentInfo(id);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setPaymentInfo(paymentInfo);

        return accountInfo;
    }
}
