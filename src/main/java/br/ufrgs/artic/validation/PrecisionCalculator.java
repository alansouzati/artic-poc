package br.ufrgs.artic.validation;

import br.ufrgs.artic.model.ArticAuthor;
import br.ufrgs.artic.model.ArticMetadata;
import br.ufrgs.artic.model.ArticVenue;
import br.ufrgs.artic.utils.DynamicProgramming;

import java.io.IOException;
import java.util.*;

/**
 * Precision calculator for artic project.
 */
public final class PrecisionCalculator {

    private PrecisionCalculator() {
    }

    public static Map<String, Double> getPrecisionMap(Map<Integer, List<String>> resultMap, String option) throws IOException {

        if ("AUTHOR_INFORMATION".equals(option)) {
            return authorInformationPrecisionCalculator(resultMap);
        } else if ("FIRST_LEVEL".equals(option) || "SECTLABEL_FIRST_LEVEL".equals(option)) {
            return firstLevelPrecisionCalculator(resultMap);
        } else if ("FOOTNOTE".equals(option)) {
            return footnotePrecisionCalculator(resultMap);
        }

        return null;
    }

    private static Map<String, Double> footnotePrecisionCalculator(Map<Integer, List<String>> resultMap) {

        //true positives
        int truePositiveConferenceName = 0;
        int truePositiveConferenceDate = 0;
        int truePositiveConferenceYear = 0;
        int truePositiveConferenceLocation = 0;
        int truePositivePublisher = 0;
        int truePositiveISBN = 0;
        int truePositiveWebsite = 0;
        int truePositiveISSN = 0;
        int truePositiveEmail = 0;
        int truePositiveDOI = 0;
        int truePositiveOther = 0;


        //false positives
        int falsePositiveConferenceName = 0;
        int falsePositiveConferenceDate = 0;
        int falsePositiveConferenceYear = 0;
        int falsePositiveConferenceLocation = 0;
        int falsePositivePublisher = 0;
        int falsePositiveISBN = 0;
        int falsePositiveWebsite = 0;
        int falsePositiveISSN = 0;
        int falsePositiveEmail = 0;
        int falsePositiveDOI = 0;
        int falsePositiveOther = 0;

        //false negatives
        int falseNegativeConferenceName = 0;
        int falseNegativeConferenceDate = 0;
        int falseNegativeConferenceYear = 0;
        int falseNegativeConferenceLocation = 0;
        int falseNegativePublisher = 0;
        int falseNegativeISBN = 0;
        int falseNegativeWebsite = 0;
        int falseNegativeISSN = 0;
        int falseNegativeEmail = 0;
        int falseNegativeDOI = 0;
        int falseNegativeOther = 0;

        //micro F1
        int totalNumberOfLines = 0;
        int totalNumberOfCorrectHits = 0;

        for (Map.Entry<Integer, List<String>> currentEntry : resultMap.entrySet()) {

            totalNumberOfLines++;

            String lastColumn = currentEntry.getValue().get(0);
            String beforeLastColumn = currentEntry.getValue().get(1);

            if (lastColumn.equals(beforeLastColumn)) {

                totalNumberOfCorrectHits++;

                if (beforeLastColumn.equals("CONFERENCE_NAME")) {
                    truePositiveConferenceName++;
                } else if (beforeLastColumn.equals("CONFERENCE_DATE")) {
                    truePositiveConferenceDate++;
                } else if (beforeLastColumn.equals("CONFERENCE_YEAR")) {
                    truePositiveConferenceYear++;
                } else if (beforeLastColumn.equals("CONFERENCE_LOCATION")) {
                    truePositiveConferenceLocation++;
                } else if (beforeLastColumn.equals("PUBLISHER")) {
                    truePositivePublisher++;
                } else if (beforeLastColumn.equals("ISBN")) {
                    truePositiveISBN++;
                } else if (beforeLastColumn.equals("WEBSITE")) {
                    truePositiveWebsite++;
                } else if (beforeLastColumn.equals("ISSN")) {
                    truePositiveISSN++;
                } else if (beforeLastColumn.equals("EMAIL")) {
                    truePositiveEmail++;
                } else if (beforeLastColumn.equals("DOI")) {
                    truePositiveDOI++;
                } else if (beforeLastColumn.equals("OTHER")) {
                    truePositiveOther++;
                }

            } else {
                if (lastColumn.equals("CONFERENCE_NAME")) {
                    falsePositiveConferenceName++;
                } else if (lastColumn.equals("CONFERENCE_DATE")) {
                    falsePositiveConferenceDate++;
                } else if (lastColumn.equals("CONFERENCE_YEAR")) {
                    falsePositiveConferenceYear++;
                } else if (lastColumn.equals("CONFERENCE_LOCATION")) {
                    falsePositiveConferenceLocation++;
                } else if (lastColumn.equals("PUBLISHER")) {
                    falsePositivePublisher++;
                } else if (lastColumn.equals("ISBN")) {
                    falsePositiveISBN++;
                } else if (beforeLastColumn.equals("WEBSITE")) {
                    falsePositiveWebsite++;
                } else if (beforeLastColumn.equals("ISSN")) {
                    falsePositiveISSN++;
                } else if (beforeLastColumn.equals("EMAIL")) {
                    falsePositiveEmail++;
                } else if (beforeLastColumn.equals("DOI")) {
                    falsePositiveDOI++;
                } else if (lastColumn.equals("OTHER")) {
                    falsePositiveOther++;
                }

                if (beforeLastColumn.equals("CONFERENCE_NAME")) {
                    falseNegativeConferenceName++;
                } else if (beforeLastColumn.equals("CONFERENCE_DATE")) {
                    falseNegativeConferenceDate++;
                } else if (beforeLastColumn.equals("CONFERENCE_YEAR")) {
                    falseNegativeConferenceYear++;
                } else if (beforeLastColumn.equals("CONFERENCE_LOCATION")) {
                    falseNegativeConferenceLocation++;
                } else if (beforeLastColumn.equals("PUBLISHER")) {
                    falseNegativePublisher++;
                } else if (beforeLastColumn.equals("ISBN")) {
                    falseNegativeISBN++;
                } else if (beforeLastColumn.equals("WEBSITE")) {
                    falseNegativeWebsite++;
                } else if (beforeLastColumn.equals("ISSN")) {
                    falseNegativeISSN++;
                } else if (beforeLastColumn.equals("EMAIL")) {
                    falseNegativeEmail++;
                } else if (beforeLastColumn.equals("DOI")) {
                    falseNegativeDOI++;
                } else if (beforeLastColumn.equals("OTHER")) {
                    falseNegativeOther++;
                }


            }
        }

        Map<String, Double> precisionMap = new HashMap<String, Double>();

        int precisionCount = 0;
        int f1FeaturesSum = 0;
        if (truePositiveConferenceName > 0) {
            double precisionConferenceName = (double) truePositiveConferenceName * 100 / (truePositiveConferenceName + falsePositiveConferenceName);
            double recallConferenceName = (double) truePositiveConferenceName * 100 / (truePositiveConferenceName + falseNegativeConferenceName);
            double f1ConferenceName = (double) 2 * precisionConferenceName * recallConferenceName / (precisionConferenceName + recallConferenceName);
            precisionMap.put("CONFERENCE_NAME", f1ConferenceName);
            precisionCount++;
            f1FeaturesSum += f1ConferenceName;
        }

        if (truePositiveConferenceDate > 0) {
            double precisionConferenceDate = (double) truePositiveConferenceDate * 100 / (truePositiveConferenceDate + falsePositiveConferenceDate);
            double recallConferenceDate = (double) truePositiveConferenceDate * 100 / (truePositiveConferenceDate + falseNegativeConferenceDate);
            double f1ConferenceDate = (double) 2 * precisionConferenceDate * recallConferenceDate / (precisionConferenceDate + recallConferenceDate);
            precisionMap.put("CONFERENCE_DATE", f1ConferenceDate);
            precisionCount++;
            f1FeaturesSum += f1ConferenceDate;
        }

        if (truePositiveConferenceYear > 0) {
            double precisionConferenceYear = (double) truePositiveConferenceYear * 100 / (truePositiveConferenceYear + falsePositiveConferenceYear);
            double recallConferenceYear = (double) truePositiveConferenceYear * 100 / (truePositiveConferenceYear + falseNegativeConferenceYear);
            double f1ConferenceYear = (double) 2 * precisionConferenceYear * recallConferenceYear / (precisionConferenceYear + recallConferenceYear);
            precisionMap.put("CONFERENCE_YEAR", f1ConferenceYear);
            precisionCount++;
            f1FeaturesSum += f1ConferenceYear;
        }

        if (truePositiveConferenceLocation > 0) {
            double precisionConferenceLocation = (double) truePositiveConferenceLocation * 100 / (truePositiveConferenceLocation + falsePositiveConferenceLocation);
            double recallConferenceLocation = (double) truePositiveConferenceLocation * 100 / (truePositiveConferenceLocation + falseNegativeConferenceLocation);
            double f1ConferenceLocation = (double) 2 * precisionConferenceLocation * recallConferenceLocation / (precisionConferenceLocation + recallConferenceLocation);
            precisionMap.put("CONFERENCE_LOCATION", f1ConferenceLocation);
            precisionCount++;
            f1FeaturesSum += f1ConferenceLocation;
        }

        if (truePositivePublisher > 0) {
            double precisionPublisher = (double) truePositivePublisher * 100 / (truePositivePublisher + falsePositivePublisher);
            double recallPublisher = (double) truePositivePublisher * 100 / (truePositivePublisher + falseNegativePublisher);
            double f1Publisher = (double) 2 * precisionPublisher * recallPublisher / (precisionPublisher + recallPublisher);
            precisionMap.put("PUBLISHER", f1Publisher);
            precisionCount++;
            f1FeaturesSum += f1Publisher;
        }

        if (truePositiveISBN > 0) {
            double precisionISBN = (double) truePositiveISBN * 100 / (truePositiveISBN + falsePositiveISBN);
            double recallISBN = (double) truePositiveISBN * 100 / (truePositiveISBN + falseNegativeISBN);
            double f1ISBN = (double) 2 * precisionISBN * recallISBN / (precisionISBN + recallISBN);
            precisionMap.put("ISBN", f1ISBN);
            precisionCount++;
            f1FeaturesSum += f1ISBN;
        }

        if (truePositiveWebsite > 0) {
            double precisionWebsite = (double) truePositiveWebsite * 100 / (truePositiveWebsite + falsePositiveWebsite);
            double recallWebsite = (double) truePositiveWebsite * 100 / (truePositiveWebsite + falseNegativeWebsite);
            double f1Website = (double) 2 * precisionWebsite * recallWebsite / (precisionWebsite + recallWebsite);
            precisionMap.put("WEBSITE", f1Website);
            precisionCount++;
            f1FeaturesSum += f1Website;
        }

        if (truePositiveISSN > 0) {
            double precisionISSN = (double) truePositiveISSN * 100 / (truePositiveISSN + falsePositiveISSN);
            double recallISSN = (double) truePositiveISSN * 100 / (truePositiveISSN + falseNegativeISSN);
            double f1ISSN = (double) 2 * precisionISSN * recallISSN / (precisionISSN + recallISSN);
            precisionMap.put("ISSN", f1ISSN);
            precisionCount++;
            f1FeaturesSum += f1ISSN;
        }

        if (truePositiveEmail > 0) {
            double precisionEmail = (double) truePositiveEmail * 100 / (truePositiveEmail + falsePositiveEmail);
            double recallEmail = (double) truePositiveEmail * 100 / (truePositiveEmail + falseNegativeEmail);
            double f1Email = (double) 2 * precisionEmail * recallEmail / (precisionEmail + recallEmail);
            precisionMap.put("EMAIL", f1Email);
            precisionCount++;
            f1FeaturesSum += f1Email;
        }

        if (truePositiveDOI > 0) {
            double precisionDOI = (double) truePositiveDOI * 100 / (truePositiveDOI + falsePositiveDOI);
            double recallDOI = (double) truePositiveDOI * 100 / (truePositiveDOI + falseNegativeDOI);
            double f1DOI = (double) 2 * precisionDOI * recallDOI / (precisionDOI + recallDOI);
            precisionMap.put("DOI", f1DOI);
            precisionCount++;
            f1FeaturesSum += f1DOI;
        }

        if (truePositiveOther > 0) {
            double precisionOther = (double) truePositiveOther * 100 / (truePositiveOther + falsePositiveOther);
            double recallOther = (double) truePositiveOther * 100 / (truePositiveOther + falseNegativeOther);
            double f1Other = (double) 2 * precisionOther * recallOther / (precisionOther + recallOther);
            precisionMap.put("OTHER", f1Other);
            precisionCount++;
            f1FeaturesSum += f1Other;
        }

        precisionMap.put("MACRO_F1", (double) f1FeaturesSum / precisionCount);
        precisionMap.put("MICRO_F1", (double) (totalNumberOfCorrectHits * 100) / totalNumberOfLines);

        return precisionMap;
    }

    private static Map<String, Double> authorInformationPrecisionCalculator(Map<Integer, List<String>> resultMap) {
        //true positives
        int truePositiveAuthor = 0;
        int truePositiveAffiliation = 0;
        int truePositiveEmail = 0;
        int truePositiveWebsite = 0;
        int truePositiveOther = 0;


        //false positives
        int falsePositiveAuthor = 0;
        int falsePositiveAffiliation = 0;
        int falsePositiveEmail = 0;
        int falsePositiveWebsite = 0;
        int falsePositiveOther = 0;

        //false negatives
        int falseNegativeAuthor = 0;
        int falseNegativeAffiliation = 0;
        int falseNegativeEmail = 0;
        int falseNegativeWebsite = 0;
        int falseNegativeOther = 0;

        //micro F1
        int totalNumberOfLines = 0;
        int totalNumberOfCorrectHits = 0;

        for (Map.Entry<Integer, List<String>> currentEntry : resultMap.entrySet()) {

            totalNumberOfLines++;

            String lastColumn = currentEntry.getValue().get(0);
            String beforeLastColumn = currentEntry.getValue().get(1);

            if (lastColumn.equals(beforeLastColumn)) {

                totalNumberOfCorrectHits++;

                if (beforeLastColumn.equals("AUTHOR")) {
                    truePositiveAuthor++;
                } else if (beforeLastColumn.equals("AFFILIATION")) {
                    truePositiveAffiliation++;
                } else if (beforeLastColumn.equals("EMAIL")) {
                    truePositiveEmail++;
                } else if (beforeLastColumn.equals("WEBSITE")) {
                    truePositiveWebsite++;
                } else if (beforeLastColumn.equals("OTHER")) {
                    truePositiveOther++;
                }

            } else {
                if (lastColumn.equals("AUTHOR")) {
                    falsePositiveAuthor++;
                } else if (lastColumn.equals("AFFILIATION")) {
                    falsePositiveAffiliation++;
                } else if (lastColumn.equals("EMAIL")) {
                    falsePositiveEmail++;
                } else if (lastColumn.equals("WEBSITE")) {
                    falsePositiveWebsite++;
                } else if (lastColumn.equals("OTHER")) {
                    falsePositiveOther++;
                }

                if (beforeLastColumn.equals("AUTHOR")) {
                    falseNegativeAuthor++;
                } else if (beforeLastColumn.equals("AFFILIATION")) {
                    falseNegativeAffiliation++;
                } else if (beforeLastColumn.equals("EMAIL")) {
                    falseNegativeEmail++;
                } else if (beforeLastColumn.equals("WEBSITE")) {
                    falseNegativeWebsite++;
                } else if (beforeLastColumn.equals("OTHER")) {
                    falseNegativeOther++;
                }

            }

        }

        Map<String, Double> precisionMap = new HashMap<String, Double>();

        int precisionCount = 0;
        int f1FeaturesSum = 0;
        if (truePositiveAuthor > 0) {
            double precisionAuthor = (double) truePositiveAuthor * 100 / (truePositiveAuthor + falsePositiveAuthor);
            double recallAuthor = (double) truePositiveAuthor * 100 / (truePositiveAuthor + falseNegativeAuthor);
            double f1Author = (double) 2 * precisionAuthor * recallAuthor / (precisionAuthor + recallAuthor);
            precisionMap.put("AUTHOR", f1Author);
            precisionCount++;
            f1FeaturesSum += f1Author;
        }

        if (truePositiveAffiliation > 0) {
            double precisionAffiliation = (double) truePositiveAffiliation * 100 / (truePositiveAffiliation + falsePositiveAffiliation);
            double recallAffiliation = (double) truePositiveAffiliation * 100 / (truePositiveAffiliation + falseNegativeAffiliation);
            double f1Affiliation = (double) 2 * precisionAffiliation * recallAffiliation / (precisionAffiliation + recallAffiliation);
            precisionMap.put("AFFILIATION", f1Affiliation);
            precisionCount++;
            f1FeaturesSum += f1Affiliation;
        }

        if (truePositiveEmail > 0) {
            double precisionEmail = (double) truePositiveEmail * 100 / (truePositiveEmail + falsePositiveEmail);
            double recallEmail = (double) truePositiveEmail * 100 / (truePositiveEmail + falseNegativeEmail);
            double f1Email = (double) 2 * precisionEmail * recallEmail / (precisionEmail + recallEmail);
            precisionMap.put("EMAIL", f1Email);
            precisionCount++;
            f1FeaturesSum += f1Email;
        }

        if (truePositiveWebsite > 0) {
            double precisionWebsite = (double) truePositiveWebsite * 100 / (truePositiveWebsite + falsePositiveWebsite);
            double recallWebsite = (double) truePositiveWebsite * 100 / (truePositiveWebsite + falseNegativeWebsite);
            double f1Website = (double) 2 * precisionWebsite * recallWebsite / (precisionWebsite + recallWebsite);
            precisionMap.put("WEBSITE", f1Website);
            precisionCount++;
            f1FeaturesSum += f1Website;
        }

        if (truePositiveOther > 0) {
            double precisionOther = (double) truePositiveOther * 100 / (truePositiveOther + falsePositiveOther);
            double recallOther = (double) truePositiveOther * 100 / (truePositiveOther + falseNegativeOther);
            double f1Other = (double) 2 * precisionOther * recallOther / (precisionOther + recallOther);
            precisionMap.put("OTHER", f1Other);
            precisionCount++;
            f1FeaturesSum += f1Other;
        }

        precisionMap.put("MACRO_F1", (double) f1FeaturesSum / precisionCount);
        precisionMap.put("MICRO_F1", (double) (totalNumberOfCorrectHits * 100) / totalNumberOfLines);

        return precisionMap;
    }

    private static Map<String, Double> firstLevelPrecisionCalculator(Map<Integer, List<String>> resultMap) {
        //true positives
        int truePositiveHeader = 0;
        int truePositiveTitle = 0;
        int truePositiveAuthorInformation = 0;
        int truePositiveBody = 0;
        int truePositiveFootnote = 0;

        //false positives
        int falsePositiveHeader = 0;
        int falsePositiveTitle = 0;
        int falsePositiveAuthorInformation = 0;
        int falsePositiveBody = 0;
        int falsePositiveFootnote = 0;

        //false negatives
        int falseNegativeHeader = 0;
        int falseNegativeTitle = 0;
        int falseNegativeAuthorInformation = 0;
        int falseNegativeBody = 0;
        int falseNegativeFootnote = 0;

        //micro F1
        int totalNumberOfLines = 0;
        int totalNumberOfCorrectHits = 0;

        for (Map.Entry<Integer, List<String>> currentEntry : resultMap.entrySet()) {

            totalNumberOfLines++;

            String lastColumn = currentEntry.getValue().get(0);
            String beforeLastColumn = currentEntry.getValue().get(1);


            if (lastColumn.equals(beforeLastColumn)) {

                totalNumberOfCorrectHits++;

                if (beforeLastColumn.equals("HEADER")) {
                    truePositiveHeader++;

                } else if (beforeLastColumn.equals("TITLE")) {
                    truePositiveTitle++;

                } else if (beforeLastColumn.equals("AUTHOR_INFORMATION")) {
                    truePositiveAuthorInformation++;

                } else if (beforeLastColumn.equals("BODY")) {
                    truePositiveBody++;

                } else if (beforeLastColumn.equals("FOOTNOTE")) {
                    truePositiveFootnote++;

                }
            } else {

                if (lastColumn.equals("HEADER")) {
                    falsePositiveHeader++;

                } else if (lastColumn.equals("TITLE")) {
                    falsePositiveTitle++;

                } else if (lastColumn.equals("AUTHOR_INFORMATION")) {
                    falsePositiveAuthorInformation++;

                } else if (lastColumn.equals("BODY")) {
                    falsePositiveBody++;

                } else if (lastColumn.equals("FOOTNOTE")) {
                    falsePositiveFootnote++;

                }

                if (beforeLastColumn.equals("HEADER")) {
                    falseNegativeHeader++;

                } else if (beforeLastColumn.equals("TITLE")) {
                    falseNegativeTitle++;

                } else if (beforeLastColumn.equals("AUTHOR_INFORMATION")) {
                    falseNegativeAuthorInformation++;

                } else if (beforeLastColumn.equals("BODY")) {
                    falseNegativeBody++;

                } else if (beforeLastColumn.equals("FOOTNOTE")) {
                    falseNegativeFootnote++;

                }

            }
        }

        Map<String, Double> precisionMap = new HashMap<String, Double>();

        int precisionCount = 0;
        int f1FeaturesSum = 0;
        if (truePositiveHeader > 0) {
            double precisionHeader = (double) truePositiveHeader * 100 / (truePositiveHeader + falsePositiveHeader);
            double recallHeader = (double) truePositiveHeader * 100 / (truePositiveHeader + falseNegativeHeader);
            double f1Header = (double) 2 * precisionHeader * recallHeader / (precisionHeader + recallHeader);
            precisionMap.put("HEADER", f1Header);
            precisionCount++;
            f1FeaturesSum += f1Header;
        }

        if (truePositiveTitle > 0) {
            double precisionTitle = (double) truePositiveTitle * 100 / (truePositiveTitle + falsePositiveTitle);
            double recallTitle = (double) truePositiveTitle * 100 / (truePositiveTitle + falseNegativeTitle);
            double f1Title = (double) 2 * precisionTitle * recallTitle / (precisionTitle + recallTitle);
            precisionMap.put("TITLE", f1Title);
            precisionCount++;
            f1FeaturesSum += f1Title;
        }

        if (truePositiveAuthorInformation > 0) {
            double precisionAuthorInformation = (double) truePositiveAuthorInformation * 100 /
                    (truePositiveAuthorInformation + falsePositiveAuthorInformation);
            double recallAuthorInformation = (double) truePositiveAuthorInformation * 100 /
                    (truePositiveAuthorInformation + falseNegativeAuthorInformation);
            double f1AuthorInformation = (double) 2 * precisionAuthorInformation * recallAuthorInformation /
                    (precisionAuthorInformation + recallAuthorInformation);
            precisionMap.put("AUTHOR_INFORMATION", f1AuthorInformation);
            precisionCount++;
            f1FeaturesSum += f1AuthorInformation;
        }

        if (truePositiveBody > 0) {
            double precisionBody = (double) truePositiveBody * 100 / (truePositiveBody + falsePositiveBody);
            double recallBody = (double) truePositiveBody * 100 / (truePositiveBody + falseNegativeBody);
            double f1Body = (double) 2 * precisionBody * recallBody / (precisionBody + recallBody);
            precisionMap.put("BODY", f1Body);
            precisionCount++;
            f1FeaturesSum += f1Body;
        }

        if (truePositiveFootnote > 0) {
            double precisionFootnote = (double) truePositiveFootnote * 100 / (truePositiveFootnote + falsePositiveFootnote);
            double recallFootnote = (double) truePositiveFootnote * 100 / (truePositiveFootnote + falseNegativeFootnote);
            double f1Footnote = (double) 2 * precisionFootnote * recallFootnote / (precisionFootnote + recallFootnote);
            precisionMap.put("FOOTNOTE", f1Footnote);
            precisionCount++;
            f1FeaturesSum += f1Footnote;
        }

        precisionMap.put("MACRO_F1", (double) f1FeaturesSum / precisionCount);
        precisionMap.put("MICRO_F1", (double) (totalNumberOfCorrectHits * 100) / totalNumberOfLines);

        return precisionMap;
    }

    public static Map<String, Double> validateMetadata(ArticMetadata source, ArticMetadata target) {
        Map<String, Double> validationMap = new HashMap<String, Double>();

        calculateF1(validationMap, "TITLE", source.title, target.title);
        calculateF1Authors(validationMap, source.authors, target.authors);
        calculateF1Venues(validationMap, source.venues, target.venues);
        calculateF1Email(validationMap, source.emails, target.emails);
        calculateF1(validationMap, "AFFILIATION", source.affiliation, target.affiliation);
        //calculateF1(validationMap, "LINKS", source.links, target.links);


        double totalF1 = 0.0;
        for (String key : validationMap.keySet()) {
            totalF1 += validationMap.get(key);
        }

        int dividend = (validationMap.containsKey("DANGLING_EMAILS")) ? 1 : 0;
        validationMap.put("MACRO_F1", totalF1 / (validationMap.size() - dividend));
        validationMap.put("MICRO_F1", totalF1 / (validationMap.size() - dividend - 1));

        return validationMap;
    }

    private static void calculateF1Authors(Map<String, Double> validationMap, List<ArticAuthor> source, List<ArticAuthor> target) {
        if (source != null || target != null) {

            Map<String, List<Double>> totalAuthorPrecisionMap = getPrecisionAuthor(source, target);
            Map<String, List<Double>> totalAuthorRecallMap = getPrecisionAuthor(target, source);

            if (totalAuthorPrecisionMap.containsKey("NAME")) {
                double totalAuthorNamePrecision = getTotalPrecision(totalAuthorPrecisionMap, "NAME");
                double totalAuthorNameRecall = getTotalPrecision(totalAuthorRecallMap, "NAME");
                validationMap.put("TOTAL_AUTHORS_NAMES", calculateF1(totalAuthorNamePrecision, totalAuthorNameRecall) * 100);
            }

            if (totalAuthorPrecisionMap.containsKey("EMAIL")) {
                double totalAuthorEmailPrecision = getTotalPrecision(totalAuthorPrecisionMap, "EMAIL");
                double totalAuthorEmailRecall = getTotalPrecision(totalAuthorRecallMap, "EMAIL");
                validationMap.put("TOTAL_AUTHORS_EMAILS", calculateF1(totalAuthorEmailPrecision, totalAuthorEmailRecall) * 100);
            }

            if (totalAuthorPrecisionMap.containsKey("AFFILIATION")) {
                double totalAuthorAffiliationPrecision = getTotalPrecision(totalAuthorPrecisionMap, "AFFILIATION");
                double totalAuthorAffiliationRecall = getTotalPrecision(totalAuthorRecallMap, "AFFILIATION");
                validationMap.put("TOTAL_AUTHORS_AFFILIATION", calculateF1(totalAuthorAffiliationPrecision, totalAuthorAffiliationRecall) * 100);
            }

            double totalAuthorPrecision = getTotalPrecision(totalAuthorPrecisionMap, "TOTAL");
            double totalAuthorRecall = getTotalPrecision(totalAuthorRecallMap, "TOTAL");
            validationMap.put("TOTAL_AUTHORS", calculateF1(totalAuthorPrecision, totalAuthorRecall) * 100);
        }
    }

    private static double getTotalPrecision(Map<String, List<Double>> map, String key) {
        double total = 0.0;

        if (map != null && map.get(key) != null) {
            List<Double> elements = map.get(key);
            for (Double currentValue : map.get(key)) {
                total += currentValue;
            }

            return total / elements.size();
        }

        return 0.0;
    }

    private static void calculateF1Venues(Map<String, Double> validationMap, List<ArticVenue> source, List<ArticVenue> target) {
        if (source != null || target != null) {
            Map<String, List<Double>> totalVenuePrecisionMap = getPrecisionVenue(source, target);
            Map<String, List<Double>> totalVenueRecallMap = getPrecisionVenue(target, source);

            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_NAMES", "NAME");
            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_PUBLISHERS", "PUBLISHER");
            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_DATES", "DATE");
            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_YEARS", "YEAR");
            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_LOCATIONS", "LOCATION");
            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_PAGES", "PAGE");
            //putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_VOLUMES", "VOLUME");
            //putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_NUMBERS", "NUMBER");
            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_ISBNS", "ISBN");
            putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_ISSNS", "ISSN");
            //putF1InMap(validationMap, totalVenuePrecisionMap, totalVenueRecallMap, "TOTAL_VENUES_DOIS", "DOI");

            double totalVenuePrecision = getTotalPrecision(totalVenuePrecisionMap, "TOTAL");
            double totalVenueRecall = getTotalPrecision(totalVenueRecallMap, "TOTAL");
            validationMap.put("TOTAL_VENUES", calculateF1(totalVenuePrecision, totalVenueRecall) * 100);
        }
    }

    private static void putF1InMap(Map<String, Double> validationMap, Map<String, List<Double>> totalVenuePrecisionMap,
                                   Map<String, List<Double>> totalVenueRecallMap, String key, String listKey) {
        if (totalVenuePrecisionMap.containsKey(listKey)) {
            double totalVenuePrecision = getTotalPrecision(totalVenuePrecisionMap, listKey);
            double totalVenueRecall = getTotalPrecision(totalVenueRecallMap, listKey);
            validationMap.put(key, calculateF1(totalVenuePrecision, totalVenueRecall) * 100);
        }
    }

    private static void calculateF1Email(Map<String, Double> validationMap, List<String> source, List<String> target) {
        if (source != null || target != null) {
            double totalEmailPrecision = getPrecisionEmail(source, target);
            double totalEmailRecall = getPrecisionEmail(target, source);

            validationMap.put("DANGLING_EMAILS", calculateF1(totalEmailPrecision, totalEmailRecall) * 100);
        }

    }

    private static double calculateF1(double totalAuthorPrecision, double totalAuthorRecall) {
        double authorF1 = 0.0;
        double sum = (totalAuthorPrecision + totalAuthorRecall);
        if (sum > 0.0) {
            authorF1 = (2 * totalAuthorPrecision * totalAuthorRecall) / sum;
        }
        return authorF1;
    }

    private static Map<String, List<Double>> getPrecisionAuthor(List<ArticAuthor> source, List<ArticAuthor> target) {
        List<ArticAuthor> visitedAuthors = new ArrayList<ArticAuthor>();
        Map<String, List<Double>> totalPrecisionMap = new HashMap<String, List<Double>>();
        if (target != null) {
            for (ArticAuthor currentTargetAuthor : target) {

                ArticAuthor bestAuthorMatch = getBestAuthorMatch(currentTargetAuthor, source, visitedAuthors);

                if (bestAuthorMatch != null) {
                    visitedAuthors.add(bestAuthorMatch);
                    Map<String, Double> precisionMap = currentTargetAuthor.getPrecision(bestAuthorMatch);
                    augmentPrecisionMap(totalPrecisionMap, precisionMap);
                }
            }
        }
        return totalPrecisionMap;
    }

    public static void augmentPrecisionMap(Map<String, List<Double>> totalPrecision, Map<String, Double> currentPrecisionMap) {
        for (Map.Entry<String, Double> currentEntry : currentPrecisionMap.entrySet()) {
            if (totalPrecision.containsKey(currentEntry.getKey())) {
                totalPrecision.get(currentEntry.getKey()).add(currentEntry.getValue());
            } else {
                List<Double> values = new ArrayList<Double>();
                values.add(currentEntry.getValue());
                totalPrecision.put(currentEntry.getKey(), values);
            }
        }
    }

    private static Map<String, List<Double>> getPrecisionVenue(List<ArticVenue> source, List<ArticVenue> target) {
        List<ArticVenue> visitedVenues = new ArrayList<ArticVenue>();
        Map<String, List<Double>> totalPrecisionMap = new HashMap<String, List<Double>>();
        if (target != null) {
            for (ArticVenue currentTargetVenue : target) {

                ArticVenue bestVenueMatch = getBestVenueMatch(currentTargetVenue, source, visitedVenues);

                if (bestVenueMatch != null) {
                    visitedVenues.add(bestVenueMatch);
                    Map<String, Double> precisionMap = currentTargetVenue.getPrecision(bestVenueMatch);
                    augmentPrecisionMap(totalPrecisionMap, precisionMap);
                }
            }
        }
        return totalPrecisionMap;
    }

    private static double getPrecisionEmail(List<String> source, List<String> target) {
        List<String> visitedEmails = new ArrayList<String>();
        double emailPrecision = 0.0;
        double totalEmailPrecision = 0.0;
        if (target != null) {
            for (String currentTargetString : target) {

                String bestEmailMatch = getBestEmailMatch(currentTargetString, source, visitedEmails);

                if (bestEmailMatch != null) {
                    visitedEmails.add(bestEmailMatch);
                    emailPrecision += getPrecision(Arrays.asList(currentTargetString.replaceAll("[^A-Za-z0-9]", "")),
                            Arrays.asList(bestEmailMatch.replaceAll("[^A-Za-z0-9]", "")));
                }
            }

            totalEmailPrecision = emailPrecision / target.size();
        }
        return totalEmailPrecision;
    }

    private static ArticAuthor getBestAuthorMatch(ArticAuthor currentTargetAuthor,
                                                  List<ArticAuthor> authors, List<ArticAuthor> visitedAuthors) {
        int smallestEditDistance = 999999;
        ArticAuthor bestAuthorMatch = null;
        if (authors != null) {
            for (ArticAuthor articAuthor : authors) {
                int editDistance = articAuthor.getEditDistance(currentTargetAuthor);

                if (editDistance < 25 && editDistance < smallestEditDistance && !visitedAuthors.contains(articAuthor)) {
                    smallestEditDistance = editDistance;
                    bestAuthorMatch = articAuthor;
                }
            }
        }

        return bestAuthorMatch;
    }

    private static ArticVenue getBestVenueMatch(ArticVenue currentTargetVenue,
                                                List<ArticVenue> venues, List<ArticVenue> visitedVenues) {
        int smallestEditDistance = 999999;
        ArticVenue bestVenueMatch = null;
        if (venues != null) {
            for (ArticVenue articVenue : venues) {
                int editDistance = articVenue.getEditDistance(currentTargetVenue);

                if (editDistance < 25 && editDistance < smallestEditDistance && !visitedVenues.contains(articVenue)) {
                    smallestEditDistance = editDistance;
                    bestVenueMatch = articVenue;
                }
            }
        }

        return bestVenueMatch;
    }

    private static String getBestEmailMatch(String currentTargetEmail,
                                            List<String> emails, List<String> visitedEmails) {
        int smallestEditDistance = 999999;
        String bestEmailMatch = null;
        if (emails != null) {
            for (String articEmail : emails) {
                int editDistance = DynamicProgramming.distance(currentTargetEmail, articEmail);

                if (editDistance < 5 && editDistance < smallestEditDistance && !visitedEmails.contains(articEmail)) {
                    smallestEditDistance = editDistance;
                    bestEmailMatch = articEmail;
                }
            }
        }

        return bestEmailMatch;
    }

    private static void calculateF1(Map<String, Double> validationMap, String key, String sourceValue, String targetValue) {
        if (sourceValue != null || targetValue != null) {


            List<String> sourceWords = new ArrayList<String>();
            if (sourceValue != null && !sourceValue.isEmpty()) {
                sourceValue = sourceValue.replaceAll("[^A-Za-z0-9]", "");
                sourceWords = Arrays.asList(sourceValue.split(" "));
            }

            List<String> targetWords = new ArrayList<String>();
            if (targetValue != null && !targetValue.isEmpty()) {
                targetValue = targetValue.replaceAll("[^A-Za-z0-9]", "");
                targetWords = Arrays.asList(targetValue.split(" "));
            }

            double titlePrecision = getPrecision(targetWords, sourceWords);
            double titleRecall = getPrecision(sourceWords, targetWords);

            double f1 = calculateF1(titlePrecision, titleRecall);

            validationMap.put(key, f1 * 100);
        }
    }

    public static double getPrecision(List<String> targetWords, List<String> sourceWords) {
        if (sourceWords == null) {
            return 0.0;
        }

        List<String> sourceWordsClone = new ArrayList<String>(sourceWords);
        int precisionHits = 0;
        int length = 0;
        int currentIndex = 0;
        for (String currentWord : targetWords) {

            String bestMatch = getBestWordMatch(currentWord, sourceWords);

            length += currentWord.length();

            if (bestMatch != null) {
                length += bestMatch.length();
                int indexOfHit = sourceWordsClone.indexOf(bestMatch);
                if (indexOfHit >= 0 && indexOfHit >= currentIndex) {
                    currentIndex = indexOfHit;
                    precisionHits += (currentWord.length() + bestMatch.length()) - DynamicProgramming.distance(currentWord, bestMatch);
                    sourceWordsClone.remove(indexOfHit);
                }
            }
        }

        double precision = 0.0;
        if (length > 0) {
            precision = (double) precisionHits / length;

            if(precision < 0) {
                precision = precision * -1;
            }
        }
        return precision;
    }

    private static String getBestWordMatch(String word, List<String> words) {
        int smallestEditDistance = 999999;
        String bestMatch = null;
        if (word != null) {
            for (String currentWord : words) {
                int editDistance = DynamicProgramming.distance(word, currentWord);

                int distanceRelative = (editDistance * 100) / word.length();

                if ((distanceRelative < 75 && editDistance < smallestEditDistance) || currentWord.contains(word)) {
                    smallestEditDistance = editDistance;
                    bestMatch = currentWord;
                }
            }
        }

        return bestMatch;
    }
}
