package com.s8.keeilzhanstd.challenge.services;

import com.s8.keeilzhanstd.challenge.utility.PropertiesReader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class FxRatesService {

    // Build url string to send request
    private final String url_str = "https://v6.exchangerate-api.com/v6/" + PropertiesReader.getProperty("FX_RATE_API_KEY") + "/latest/";

    public String getLatestRates(String currency) throws IOException {

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

            return response.toString();
        }

        // Error during request ( RC: !200 )
        return null;
    }

}
