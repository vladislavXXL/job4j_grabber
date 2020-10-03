package quartz;

import converter.DateConverter;
import models.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import propsloader.PropertyLoader;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Class SqlRuPars.
 * @author v.ivanov
 * @version 1
 * @since 03.09.2020
 */
public class SqlRuParse implements Parse {
    /** Logger instance field.*/
    private static final Logger LOG = LoggerFactory.getLogger(SqlRuParse.class.getName());

    /**
     * Method gets all post list.
     * @param link value
     * @return resulting Post instances list
     */
    public List<Post> list(String link) {
        List<Post> result = new ArrayList<>();
        Properties props = PropertyLoader.getProps("rabbit.properties");
        int pages = Integer.parseInt(props.getProperty("parse.pages"));
        boolean isReady = false;
        int numPage = 1;
        while (!isReady) {
            if (pages < 1 || numPage == pages) {
                isReady = true;
            }
            Document doc = null;
            try {
                //doc = Jsoup.connect("http://www.sql.ru/forum/job-offers/" + numPage++).get();
                doc = Jsoup.connect(link + numPage++).get();
            } catch (IOException ioe) {
                LOG.error(ioe.getMessage(), ioe);
            }
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String url = href.attr("href");
                LOG.info(url);
                result.add(detail(url));
            }
        }
        return result;
    }

    /**
     * Method loads details of particular post.
     * @param link value
     * @return Post instance
     */
    @Override
    public Post detail(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }
        String title = doc.select(".messageHeader").get(0).text();
        String author = doc.select(".msgBody a").get(0).text();
        String description = doc.select(".msgBody").get(1).text();
        String date = doc.select(".msgFooter").get(0).text().split("\\[")[0].trim();
        Date convDate = DateConverter.convertStringToDate(date);
        return new Post(link, title, author, description, new Timestamp(convDate.getTime()));
    }
}
