package quartz;

import converter.DateConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        Document doc = Jsoup.connect("http://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td: row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            System.out.println(DateConverter.convertStringToDate(td.lastElementSibling().text()));
        }
    }
}
