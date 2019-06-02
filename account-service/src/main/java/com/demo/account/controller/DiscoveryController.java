package com.demo.account.controller;

import com.demo.account.model.DiscoveryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.util.Optional;

@RestController
@Slf4j
/**
 * An example controller to show how service discovery can be accomplished using Consul.
 *
 * Payment Service assumes to provide /ping endpoint and the host/port information can be discovered
 * by using DiscoveryClient.
 *
 * Account Service provides a /discovery client which in turns uses Consul to discover the network
 * location of Payment Service and calls its /ping endpoint.
 */
public class DiscoveryController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<URI> paymentServiceUrl() {
        return discoveryClient.getInstances("Payment")
                .stream()
                .map(si -> si.getUri())
                .findFirst();
    }

    @GetMapping(value = "/discovery", produces = "application/json")
    public DiscoveryInfo discoveryPing() throws RestClientException, ServiceUnavailableException {
        URI service = paymentServiceUrl()
                .map(s -> s.resolve("/ping"))
                .map(uri -> { log.info("host={}", uri.getHost()); return uri; })
                .orElseThrow(ServiceUnavailableException::new);

        String paymentInfo = restTemplate.getForEntity(service, String.class).getBody();

        DiscoveryInfo discoveryInfo = new DiscoveryInfo();
        discoveryInfo.getInfo().put("payment", paymentInfo);

        return discoveryInfo;
    }
}
