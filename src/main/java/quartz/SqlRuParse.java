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
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(DateConverter.convertStringToDate(td.lastElementSibling().text()));
            }
        }
    }
}
