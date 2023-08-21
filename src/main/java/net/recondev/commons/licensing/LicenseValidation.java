package net.recondev.commons.licensing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
public class LicenseValidation {
    public static boolean validateLicense(String licenseKey, String product) {
        String apiUrl = "http://103.195.102.32:49304/api/client";
        String apiKey = "G1gje4OBpsAr5gpkqvEgAiRHdumxIGUo";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", apiKey);
            connection.setDoOutput(true);

            String jsonInputString = String.format("{\"licensekey\": \"%s\", \"product\": \"%s\"}", licenseKey, product);

            try(OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }


                if (response.toString().contains("\"status_id\":\"SUCCESS\"")) {
                    return true;
                } else {
                    return false;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
/*
String key = getConfig().getString("license-key");
String product = "ReconDevCommons";
getLogger().info("Validating license...");
if (LicenseValidation.validateLicense(key, product)) {
    getLogger().info("License is valid!");
} else {
    getLogger().log(Level.SEVERE, "License is invalid!");
    getServer().getPluginManager().disablePlugin(this);
}

This is a basic implemetation of the license system
 */