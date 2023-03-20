package com.s8.keeilzhanstd.challenge.services;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@NoArgsConstructor
public class FxRatesService {
    private static final Logger log = LoggerFactory.getLogger(FxRatesService.class);

    @Value("${fx_apiKey}")
    private String SECRET;


    public String getLatestRates(String currency) throws IOException {

        String url_str = "https://v6.exchangerate-api.com/v6/" + SECRET + "/latest/";
        URL obj = new URL(url_str + currency);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Successful request
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if(log.isDebugEnabled()){
                log.debug("FxRatesService.getLatestRates() response: " + response.toString());
            }

            return response.toString();
        }

        if(log.isDebugEnabled()){
            log.error("FxRatesService.getLatestRates() failed to fetch rates.");
        }

        return null;
    }

}
