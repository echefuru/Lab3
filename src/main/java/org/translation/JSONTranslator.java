package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    public static final String ALPHA3 = "alpha3";
    private final JSONArray jsonArray1;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            this.jsonArray1 = jsonArray;

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray1.length(); i++) {
            if (jsonArray1.getJSONObject(i).getString(ALPHA3).equals(country)) {
                Iterator<String> keys = jsonArray1.getJSONObject(i).keys();
                keys.next();
                keys.next();
                keys.next();
                while (keys.hasNext()) {
                    String key = keys.next();
                    result.add(key);
                }
                break;
            }
        }
        return result;
    }

    @Override
    public List<String> getCountries() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray1.length(); i++) {
            result.add(new String(jsonArray1.getJSONObject(i).getString(ALPHA3)));
        }
        return result;
    }

    @Override
    public String translate(String country, String language) {
        ArrayList<String> countries = (ArrayList<String>) this.getCountries();
        if (countries.contains(country)) {
            ArrayList<String> languages = (ArrayList<String>) this.getCountryLanguages(country);
            if (languages.contains(language)) {
                for (int conNum = 0; conNum < jsonArray1.length(); conNum++) {
                    if (jsonArray1.getJSONObject(conNum).getString(ALPHA3).equals(country)) {
                        return jsonArray1.getJSONObject(conNum).getString(language);
                    }
                }
            }
        }
        return "Country not found";
    }
}
