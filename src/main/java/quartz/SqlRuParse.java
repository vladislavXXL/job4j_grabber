package quartz;

import converter.DateConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import propsloader.PropertyLoader;

/**
 * Class SqlRuPars.
 * @author v.ivanov
 * @version 1
 * @since 03.09.2020
 */
public class SqlRuParse {
    /**
     * Entry point.
     * @param args args
     * @throws Exception thrown exception
     */
    public static void main(String[] args) throws Exception {
        int pages = Integer.parseInt(PropertyLoader.getProps().getProperty("parse.pages"));
        boolean isReady = false;
        int numPage = 1;
        while (!isReady) {
            if (pages < 1 || numPage == pages) {
                isReady = true;
            }
            Document doc = Jsoup.connect("http://www.sql.ru/forum/job-offers/" + numPage++).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String url = href.attr("href");
                System.out.println(url);
                System.out.println(href.text());
                System.out.println(DateConverter.convertStringToDate(td.lastElementSibling().text()));
                getPostDetails(url);
            }
        }
    }

    /**
     * Method to get post details.
     * @param url value
     * @throws Exception thrown exception
     */
    private static void getPostDetails(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        String author = doc.select(".msgBody a").get(0).text();
        String description = doc.select(".msgBody").get(1).text();
        System.out.printf("Author: %s\nDescription: %s\n", author, description);
    }
}
