package br.ufrgs.artic.model;

import br.ufrgs.artic.utils.DynamicProgramming;
import br.ufrgs.artic.validation.PrecisionCalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArticVenue {

    public String name;
    public String publisher;
    public String date;
    public String year;
    public String location;
    public String page;
    public String volume;
    public String number;
    public String isbn;
    public String issn;
    public String doi;

    public int getEditDistance(ArticVenue targetVenue) {
        int totalDistance = 0;
        if (name != null && targetVenue.name != null) {
            int distance = DynamicProgramming.distance(name.replaceAll("[^A-Za-z0-9]", ""), targetVenue.name.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (name != null || targetVenue.name != null) {
            totalDistance += 5;
        }

        if (publisher != null && targetVenue.publisher != null) {
            int distance = DynamicProgramming.distance(publisher.replaceAll("[^A-Za-z0-9]", ""), targetVenue.publisher.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (publisher != null || targetVenue.publisher != null) {
            totalDistance += 5;
        }

        if (date != null && targetVenue.date != null) {
            int distance = DynamicProgramming.distance(date.replaceAll("[^A-Za-z0-9]", ""), targetVenue.date.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (date != null || targetVenue.date != null) {
            totalDistance += 5;
        }

        if (year != null && targetVenue.year != null) {
            int distance = DynamicProgramming.distance(year.replaceAll("[^A-Za-z0-9]", ""), targetVenue.year.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (year != null || targetVenue.year != null) {
            totalDistance += 5;
        }

        if (location != null && targetVenue.location != null) {
            int distance = DynamicProgramming.distance(location.replaceAll("[^A-Za-z0-9]", ""), targetVenue.location.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (location != null || targetVenue.location != null) {
            totalDistance += 5;
        }

        if (page != null && targetVenue.page != null) {
            int distance = DynamicProgramming.distance(page.replaceAll("[^A-Za-z0-9]", ""), targetVenue.page.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (page != null || targetVenue.page != null) {
            totalDistance += 5;
        }

        if (volume != null && targetVenue.volume != null) {
            int distance = DynamicProgramming.distance(volume.replaceAll("[^A-Za-z0-9]", ""), targetVenue.volume.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (volume != null || targetVenue.volume != null) {
            totalDistance += 5;
        }

        if (number != null && targetVenue.number != null) {
            int distance = DynamicProgramming.distance(number.replaceAll("[^A-Za-z0-9]", ""), targetVenue.number.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (number != null || targetVenue.number != null) {
            totalDistance += 5;
        }

        if (isbn != null && targetVenue.isbn != null) {
            int distance = DynamicProgramming.distance(isbn.replaceAll("[^A-Za-z0-9]", ""), targetVenue.isbn.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (isbn != null || targetVenue.isbn != null) {
            totalDistance += 5;
        }

        if (issn != null && targetVenue.issn != null) {
            int distance = DynamicProgramming.distance(issn.replaceAll("[^A-Za-z0-9]", ""), targetVenue.issn.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (issn != null || targetVenue.issn != null) {
            totalDistance += 5;
        }

        if (doi != null && targetVenue.doi != null) {
            int distance = DynamicProgramming.distance(doi.replaceAll("[^A-Za-z0-9]", ""), targetVenue.doi.replaceAll("[^A-Za-z0-9]", ""));
            totalDistance += distance > 10 ? 10 : distance;
        } else if (doi != null || targetVenue.doi != null) {
            totalDistance += 5;
        }

        return totalDistance;
    }

    public Map<String, Double> getPrecision(ArticVenue targetVenue) {
        double totalPrecision = 0.0;
        int count = 0;

        Map<String, Double> validationMap = new HashMap<String, Double>();

        if (targetVenue.name != null) {
            double namePrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.name.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (name != null) ? Arrays.asList(name.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += namePrecision;
            validationMap.put("NAME", namePrecision);
            count++;
        } else if (name != null) {
            count++;
        }

        if (targetVenue.publisher != null) {
            double publisherPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.publisher.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (publisher != null) ? Arrays.asList(publisher.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += publisherPrecision;
            validationMap.put("PUBLISHER", publisherPrecision);
            count++;
        } else if (publisher != null) {
            count++;
        }

        if (targetVenue.date != null) {
            double datePrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.date.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (date != null) ? Arrays.asList(date.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += datePrecision;
            validationMap.put("DATE", datePrecision);
            count++;
        } else if (date != null) {
            count++;
        }

        if (targetVenue.year != null) {
            double yearPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.year.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (year != null) ? Arrays.asList(year.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += yearPrecision;
            validationMap.put("YEAR", yearPrecision);
            count++;
        } else if (year != null) {
            count++;
        }

        if (targetVenue.location != null) {
            double locationPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.location.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (location != null) ? Arrays.asList(location.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += locationPrecision;
            validationMap.put("LOCATION", locationPrecision);
            count++;
        } else if (location != null) {
            count++;
        }

        if (targetVenue.page != null) {
            double pagePrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.page.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (page != null) ? Arrays.asList(page.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += pagePrecision;
            validationMap.put("PAGE", pagePrecision);
            count++;
        } else if (page != null) {
            count++;
        }

        if (targetVenue.volume != null) {
            double volumePrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.volume.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (volume != null) ? Arrays.asList(volume.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += volumePrecision;
            validationMap.put("VOLUME", volumePrecision);
            count++;
        } else if (volume != null) {
            count++;
        }

        if (targetVenue.number != null) {
            double numberPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.number.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (number != null) ? Arrays.asList(number.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += numberPrecision;
            validationMap.put("NUMBER", numberPrecision);
            count++;
        } else if (number != null) {
            count++;
        }

        if (targetVenue.isbn != null) {
            double isbnPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.isbn.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (isbn != null) ? Arrays.asList(isbn.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += isbnPrecision;
            validationMap.put("ISBN", isbnPrecision);
            count++;
        } else if (isbn != null) {
            count++;
        }

        if (targetVenue.issn != null) {
            double issnPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.issn.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (issn != null) ? Arrays.asList(issn.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += issnPrecision;
            validationMap.put("ISSN", issnPrecision);
            count++;
        } else if (issn != null) {
            count++;
        }

        if (targetVenue.doi != null) {
            double doiPrecision = PrecisionCalculator.getPrecision(Arrays.asList(targetVenue.doi.replaceAll("[^A-Za-z0-9]", "").split(" ")),
                    (doi != null) ? Arrays.asList(doi.replaceAll("[^A-Za-z0-9]", "").split(" ")) : null);
            totalPrecision += doiPrecision;
            validationMap.put("DOI", doiPrecision);
            count++;
        } else if (doi != null) {
            count++;
        }

        validationMap.put("TOTAL", totalPrecision / count);

        return validationMap;
    }
}
