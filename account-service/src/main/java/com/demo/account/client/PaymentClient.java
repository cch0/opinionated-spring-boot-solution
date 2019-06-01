package com.demo.account.client;

import com.demo.account.client.model.PaymentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "payment", url = "${client.payment.url}")
public interface PaymentClient {
    @RequestMapping(method = RequestMethod.GET, value = "/paymentInfo/accounts/{accountId}", consumes = "application/json")
    PaymentInfo getPaymentInfo(@PathVariable("accountId") long accountId);
}
