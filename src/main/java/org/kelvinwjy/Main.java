package org.kelvinwjy;

import java.util.logging.Level;
import org.kelvinwjy.exception.BusinessLogicException;
import org.kelvinwjy.service.TokopediaScrapperService;

public class Main {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.openqa").setLevel(Level.OFF);

        int size = Integer.parseInt(args[0]);

        System.out.println("Scrapping top " + size + " handphones in tokopedia");
        TokopediaScrapperService service = new TokopediaScrapperService();
        try {
            String csvName = service.scrapTopHandphoneTokopedia(size);
            System.out.println("Scrapping Success saved to " + csvName);
        } catch (BusinessLogicException e) {
            System.err.println("Failed Scrapping. Please try again.");
        }
    }
}
