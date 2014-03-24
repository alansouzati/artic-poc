package br.ufrgs.artic.model;

import br.ufrgs.artic.utils.DynamicProgramming;
import br.ufrgs.artic.validation.PrecisionCalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArticAuthor {

    public String name;
    public String email;
    public String affiliation;

    public int getEditDistance(ArticAuthor targetAuthor) {
        int totalDistance = 0;
        if (name != null && targetAuthor.name != null) {
            int distance = DynamicProgramming.distance(name, targetAuthor.name);
            totalDistance += distance > 10 ? 10 : distance;
        } else if (name != null || targetAuthor.name != null) {
            totalDistance += 2;
        }

        if (email != null && targetAuthor.email != null) {
            int distance = DynamicProgramming.distance(email, targetAuthor.email);
            totalDistance += distance > 10 ? 10 : distance;
        } else if (email != null || targetAuthor.email != null) {
            totalDistance += 2;
        }

        if (affiliation != null && targetAuthor.affiliation != null) {
            int distance = DynamicProgramming.distance(affiliation, targetAuthor.affiliation);
            totalDistance += distance > 10 ? 10 : distance;
        } else if (affiliation != null || targetAuthor.affiliation != null) {
            totalDistance += 2;
        }

        return totalDistance;
    }

    public Map<String, Double> getPrecision(ArticAuthor targetAuthor) {
        double totalPrecision = 0.0;
        int count = 0;

        Map<String, Double> validationMap = new HashMap<String, Double>();

        if (targetAuthor.name != null) {
            double namePrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetAuthor.name.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (name != null) ? Arrays.asList(name.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += namePrecision;
            count++;
            validationMap.put("NAME", namePrecision);
        } else if (name != null) {
            count++;
        }

        if (targetAuthor.email != null) {
            double emailPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetAuthor.email.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (email != null) ? Arrays.asList(email.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += emailPrecision;
            count++;
            validationMap.put("EMAIL", emailPrecision);
        } else if (email != null) {
            count++;
        }

        if (targetAuthor.affiliation != null) {
            double affiliationPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetAuthor.affiliation.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (affiliation != null) ? Arrays.asList(affiliation.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += affiliationPrecision;
            count++;
            validationMap.put("AFFILIATION", affiliationPrecision);
        } else if (affiliation != null) {
            count++;
        }

        validationMap.put("TOTAL", totalPrecision / count);

        return validationMap;
    }

    public static void main(String[] args) {
        String isbn = "978-1-4673-5939-9/13/$31.00";
        System.out.println(isbn.split("\\.\\.\\.")[0].split("/\\$")[0]);
    }
}
