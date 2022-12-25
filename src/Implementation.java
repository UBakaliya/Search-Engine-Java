package src;

import java.util.*;

public class Implementation implements Interface {
    // store the file data (bodyText.splitWord --> urls.set)
    private Map<String, Set<String>> database;
    private Set<String> res;

    public Implementation() {
        this.res = new HashSet<>();
        this.database = new HashMap<>();
    }

    public int size() {
        return this.database.size();
    }

    @Override
    public String cleanToken(String str) {
        // Check if the string is empty or null
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Initialize variables to store the start and end indices of the string
        int start = 0;
        int end = str.length() - 1;

        // Iterate through the string from the front and back, until we find a
        // non-punctuation character
        while (start <= end && !Character.isLetterOrDigit(str.charAt(start))) {
            start++;
        }
        while (end >= start && !Character.isLetterOrDigit(str.charAt(end))) {
            end--;
        }

        // Return the substring from the start index to the end index
        return str.substring(start, end + 1).toLowerCase();
    }

    @Override
    public Set<String> gatherToken(String bodyText) {
        // split the string by spaces and insert the split string into set
        Set<String> GToken = new HashSet<String>(Arrays.asList(this.cleanToken(bodyText).split(" ")));
        return GToken;
    }

    @Override
    public void buildDB(String bodyText, String url) {
        Set<String> text = this.gatherToken(bodyText);
        for (final String i : text) {
            if (!i.equals("") && !i.equals(" ")) {
                this.database.computeIfAbsent(i, V -> new HashSet<>()).add(url);
            }
        }
    }

    private Set<String> findQuery(String query) {
        if (!this.database.containsKey(query))
            return new HashSet<>();
        return this.database.get(query);
    }

    @Override
    public Set<String> search(String query) {
        // split the string by spaces
        ArrayList<String> splitQuery = new ArrayList<>(Arrays.asList(query.split(" ")));
        // if the search term is only one then:
        if (splitQuery.size() <= 1) {
            this.cleanToken(query);
            this.res = this.findQuery(query);
            return this.res;
        }
        // if the search term more then one term then:
        for (final String i : splitQuery) {
            if (i.charAt(0) == '+')
                this.res.removeAll(this.findQuery(this.cleanToken(i)));
            else if (i.charAt(0) == '-')
                this.res.retainAll(this.findQuery(this.cleanToken(i)));
            else
                this.res.addAll(this.findQuery(this.cleanToken(i)));

        }
        return this.res;
    }

}
