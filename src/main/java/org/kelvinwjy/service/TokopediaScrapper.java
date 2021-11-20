package org.kelvinwjy.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.kelvinwjy.exception.BusinessLogicException;
import org.kelvinwjy.entity.Handphone;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TokopediaScrapper {
    private static final String URL_TA = "https://ta.tokopedia.com/promo";
    private static final String URL_TOP_HANDPHONE = "https://www.tokopedia.com/p/handphone-tablet/handphone?ob=5&page=";

    private static final String XPATH_PRODUCT_LIST = "//div[@data-testid='lstCL2ProductList']/div";
    private static final String XPATH_PRODUCT_LINK = "a[@data-testid='lnkProductContainer']";
    private static final String XPATH_PRODUCT_NAME = "//h1[@data-testid='lblPDPDetailProductName']";
    private static final String XPATH_PRODUCT_DESCRIPTION = "//*[@data-testid='lblPDPDescriptionProduk']";
    private static final String XPATH_PRODUCT_IMG_URL = "//*[@data-testid='PDPImageMain']//img";
    private static final String XPATH_PRODUCT_PRICE = "//*[@data-testid='lblPDPDetailProductPrice']";
    private static final String XPATH_PRODUCT_RATING = "//*[@data-testid='lblPDPDetailProductRatingNumber']";
    private static final String XPATH_MERCHANT_NAME = "//*[@data-testid='llbPDPFooterShopName']//h2";

    private static final String HREF = "href";
    private static final String SRC = "src";
    private static final String EMPTY = "";
    private static final String DOT = ".";
    private static final String CURRENCY_IDR = "Rp";

    public List<Handphone> getTopHandphone(int size)
            throws BusinessLogicException {

        final WebDriverService webDriver = new WebDriverService();
        final List<Handphone> handphones = new ArrayList<>(size);

        try {
            List<String> tabs = webDriver.prepareTwoTabs();
            for (int page = 1; handphones.size() < size; page++) {
                String url = URL_TOP_HANDPHONE + page;
                final List<WebElement> items = webDriver.getElementListByScrollingDown(url,
                        XPATH_PRODUCT_LIST, tabs.get(0)); // switch to main tab

                for (WebElement item : items) {
                    String path = item.findElement(By.xpath(XPATH_PRODUCT_LINK)).getAttribute(HREF);
                    if (isTopAdsLink(path)) {
                        continue;
                    }

                    webDriver.getWebpage(path, tabs.get(1)); //switch to new tab

                    // trigger lazy load
                    webDriver.scrollDownSmall();
                    webDriver.waitOnElement(XPATH_MERCHANT_NAME);

                    handphones.add(extractProduct(webDriver, path));
                    System.out.println("Product list now : " + handphones.size());

                    if (handphones.size() == size) {
                        break;
                    }
                    webDriver.switchTab(tabs.get(0)); //switches to main tab
                }

            }
        } catch (Exception e) {
            System.err.println("Failed to scrapping " + e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        } finally {
            webDriver.quit();
        }

        return handphones;
    }

    private Handphone extractProduct(WebDriverService webDriver, String path) {
        String name = webDriver.getText(XPATH_PRODUCT_NAME);
        String description = webDriver.getText(XPATH_PRODUCT_DESCRIPTION);
        String imageUrl = webDriver.getText(XPATH_PRODUCT_IMG_URL, SRC);
        String price = webDriver.getText(XPATH_PRODUCT_PRICE)
                .split(CURRENCY_IDR)[1].replace(DOT, EMPTY);
        String merchantName = webDriver.getText(XPATH_MERCHANT_NAME);
        String rating = webDriver.getText(XPATH_PRODUCT_RATING);

        return Handphone.builder()
                .name(name)
                .description(getDescription(description))
                .imageLink(imageUrl)
                .merchantName(merchantName)
                .price(Double.parseDouble(price))
                .rating(getRating(rating))
                .link(path)
                .build();
    }

    private String getDescription(String description) {
        return description.replace("\n", "  ");
    }

    private double getRating(String rating) {
        if(StringUtils.isEmpty(rating)){
            return 0.0;
        }

        return Double.parseDouble(rating);
    }

    private boolean isTopAdsLink(String path) {
        return path.contains(URL_TA);
    }
}
