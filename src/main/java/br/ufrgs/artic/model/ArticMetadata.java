package br.ufrgs.artic.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * This model represents the metadata for Artic project.
 */
public class ArticMetadata {

    public String title;
    public List<ArticAuthor> authors;
    public List<ArticVenue> venues;
    public String affiliation;
    public String links;
    public List<String> emails;

    private static final Gson parser = new GsonBuilder().setPrettyPrinting().create();

    public String toJson() {
        return parser.toJson(this);
    }

    public static ArticMetadata getInstance(String jsonMetadata) {
        if (jsonMetadata == null) {
            return null;
        }
        return parser.fromJson(jsonMetadata, ArticMetadata.class);
    }

    public void addAffiliation(String affiliation) {
        if (this.affiliation == null) {
            this.affiliation = affiliation;
        } else {
            this.affiliation += " " + affiliation;
        }
    }
}
