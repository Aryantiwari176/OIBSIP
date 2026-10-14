package com.oibsip.reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class TestController {

    private final JdbcTemplate jdbcTemplate;

    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/test")
    public String test() {
        return "Online Reservation System Backend is Running!";
    }

    @PostMapping("/api/journey/search")
    public Map<String, String> searchJourney(
            @RequestBody Map<String, String> details) {

        String from = details.get("from");
        String to = details.get("to");
        String date = details.get("date");
        String travelClass = details.get("travelClass");

        Map<String, String> result = new HashMap<>();

        if (from == null || from.isBlank()
                || to == null || to.isBlank()
                || date == null || date.isBlank()) {
            result.put("message", "Please enter all journey details.");
            return result;
        }

        if (from.trim().equalsIgnoreCase(to.trim())) {
            result.put("message",
                    "Source and destination must be different.");
            return result;
        }

        String pnr = UUID.randomUUID()
                .toString().replace("-", "")
                .substring(0, 10).toUpperCase();

        jdbcTemplate.update(
                "INSERT INTO reservations " +
                "(passenger_name, train_number, train_name, " +
                "source_station, destination_station, journey_date, " +
                "travel_class, pnr, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "Guest",
                "TBD",
                "Train not selected",
                from.trim(),
                to.trim(),
                java.sql.Date.valueOf(date),
                travelClass == null ? "Sleeper" : travelClass,
                pnr,
                "SEARCHED"
        );

        result.put("message", "Journey saved successfully!");
        result.put("from", from.trim());
        result.put("to", to.trim());
        result.put("date", date);
        result.put("travelClass",
                travelClass == null ? "Sleeper" : travelClass);
        result.put("pnr", pnr);

        return result;
    }
}