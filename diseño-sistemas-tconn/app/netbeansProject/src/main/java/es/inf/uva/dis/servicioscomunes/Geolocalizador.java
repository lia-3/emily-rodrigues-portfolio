package es.inf.uva.dis.servicioscomunes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

public class Geolocalizador {

    /** Obtiene las coordenadas del dispositivo a través de la IP */
    public double[] obtenerCoordsActuales() throws IOException {
        URL url = new URL("http://ip-api.com/json/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
        }

        JsonReaderFactory factory = Json.createReaderFactory(null);
        try (JsonReader reader = factory.createReader(new StringReader(result.toString()))) {
            JsonObject coords = reader.readObject();
            double lat = coords.getJsonNumber("lat").doubleValue();
            double lon = coords.getJsonNumber("lon").doubleValue();
            return new double[]{lat, lon};
        }
    }

    /** Dadas unas coordenadas devuelve la dirección aproximada */
    public String obtenerDireccion(double lat, double lon) throws IOException {
        String urlStr = "https://nominatim.openstreetmap.org/reverse?lat="
                + lat + "&lon=" + lon + "&format=json";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "JavaApp");
        conn.setRequestMethod("GET");

        try (JsonReader reader = Json.createReader(conn.getInputStream())) {
            return reader.readObject().getString("display_name");
        }
    }

    /** Distancia en metros entre dos puntos GPS (fórmula Haversine) */
    public int calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double radioTierra = 6371e3;
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);

        return (int) (radioTierra * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}