package com.s8.keeilzhanstd.challenge.services;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FxRatesServiceTest {

    @Test
    void getLatestRates() {
        FxRatesService fxRatesService = new FxRatesService();
        String rates = "";
        try{
            rates = fxRatesService.getLatestRates("USD");
        } catch (Exception e) {
            fail();
        }
        var status = new JSONObject(rates).get("result").toString();
        assertEquals("success", status);
    }
}