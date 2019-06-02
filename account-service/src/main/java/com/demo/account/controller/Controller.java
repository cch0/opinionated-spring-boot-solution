package com.demo.account.controller;

import com.demo.account.client.PaymentClient;
import com.demo.account.client.model.PaymentInfo;
import com.demo.account.config.CustomProperties;
import com.demo.account.model.AccountInfo;
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

    @Autowired
    CustomProperties customProperties;

    @RequestMapping("/accounts/{id}")
    public AccountInfo getAccountInfo(@PathVariable long id) {
        log.info("Handling getAccountInfo, id={}", id);
        log.info("custom properties, description={}", customProperties.getDescription());

        PaymentInfo paymentInfo = paymentClient.getPaymentInfo(id);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setPaymentInfo(paymentInfo);
        accountInfo.setDescription(customProperties.getDescription());

        return accountInfo;
    }
}
