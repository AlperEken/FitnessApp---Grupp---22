package org.FitnessApp1.view;

import java.util.Random;

public enum MotivationalQuotes {
    QUOTE1("Believe in yourself and all that you are."),
    QUOTE2("Push yourself, because no one else is going to do it for you."),
    QUOTE3("Don’t make excuses, make progress."),
    QUOTE4("Stay strong. Stand up. Have a voice."),
    QUOTE5("Strive for progress, not perfection."),
    QUOTE6("Do it for the ‘after’ selfie."),
    QUOTE7("Don’t wait for opportunity. Create it."),
    QUOTE8("Small steps every day lead to big results."),
    QUOTE9("You are capable of amazing things."),
    QUOTE10("One more rep. One more step. One more day.");

    private final String quote;
    private static final Random RANDOM = new Random();


    // 🔧 Korrekt konstruktor med parameter
    MotivationalQuotes(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }

    // 🔁 Statisk metod som returnerar ett slumpmässigt citat
    public static String getRandomQuote() {
        MotivationalQuotes[] quotes = values();

        int index = RANDOM.nextInt(quotes.length);
        return quotes[index].getQuote();
    }
}
