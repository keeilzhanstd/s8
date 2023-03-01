package com.s8.keeilzhanstd.challenge.services;

import lombok.NoArgsConstructor;
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

    //@Value("${fx.secret}")
    // remove secret from code at prod
    private String SECRET = "778d2472420ef861e2c96556";
    // Build url string to send request
    private final String url_str = "https://v6.exchangerate-api.com/v6/" + SECRET + "/latest/";

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
