package com.demo.cloudsleuth.controller;

import com.demo.cloudsleuth.model.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
@Slf4j
public class Controller {
    private Random random = new Random();

    @RequestMapping("/paymentInfo/accounts/{id}")
    public PaymentInfo getPaymentInfoByAccountId(@PathVariable long id) {
        log.info("Handling getPaymentInfoByAccountId: id={}", id);

        // use delay to pretend processing is happening
        randomDelay();

        return new PaymentInfo(id, new Date());
    }

    private void randomDelay() {
        try {
            // sleep between 1 to 3 secs
            Thread.sleep((1 + random.nextInt(3))*1000);
        } catch (Exception e) {
            // ignore
        }
    }
}
