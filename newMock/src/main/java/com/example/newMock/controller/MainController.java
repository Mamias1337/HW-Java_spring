package com.example.newMock.controller;

import com.example.newMock.model.RequestDTO;
import com.example.newMock.model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.random.RandomGenerator;

@RestController
public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            name = "maxLimit",
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO){
        try{
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String rqUID = requestDTO.getRqUID();
            String Currency;


            if (firstDigit == '8'){
                maxLimit = new BigDecimal(2000);
                Currency = new String("US");
            } else if (firstDigit == '9'){
                maxLimit = new BigDecimal(1000);
                Currency = new String("EU");
            } else {
                maxLimit = new BigDecimal(10000);
                Currency = new String("RUB");
            }

            BigDecimal Balance = new BigDecimal(RandomGenerator.getDefault().nextDouble())
                    .multiply(maxLimit)
                    .setScale(2, RoundingMode.HALF_UP);

            ResponseDTO responseDTO = new ResponseDTO();

            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(Currency);
            responseDTO.setBalance(Balance);
            responseDTO.setMaxLimit(maxLimit);

            log.info("^_^ RequestDTO ^_^" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.info("^_^ ResponseDTO ^_^" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
