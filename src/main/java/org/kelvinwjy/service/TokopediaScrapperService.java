package org.kelvinwjy.service;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.kelvinwjy.exception.BusinessLogicException;
import org.kelvinwjy.entity.Handphone;

public class TokopediaScrapperService {

    private static final String CSV_PREFIX_NAME = "Top 100 Handphone product_";
    private static final String CSV_FORMAT = ".csv";

    private TokopediaScrapper scrapper;

    public TokopediaScrapperService() {
        scrapper = new TokopediaScrapper();
    }

    public String scrapTopHandphoneTokopedia(int size)
            throws BusinessLogicException {
        String filename = CSV_PREFIX_NAME + System.currentTimeMillis() + CSV_FORMAT;
        List<Handphone> handphones = scrapper.getTopHandphone(size);

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.enable(Feature.IGNORE_UNKNOWN);
        csvMapper.addMixIn(Handphone.class, Handphone.HandphoneFormat.class);
        CsvSchema schema = csvMapper.schemaFor(Handphone.class).withHeader();

        try {
            File file = new File(filename);
            csvMapper.writer(schema).writeValue(file, handphones);
            return filename;
        } catch (IOException | RuntimeException e) {
            throw new BusinessLogicException(e.getMessage());
        }
    }
}
