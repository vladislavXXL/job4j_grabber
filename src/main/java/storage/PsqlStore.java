package storage;

import models.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import propsloader.PropertyLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Class PsqlStore.
 *
 * @author v.ivanov
 * @version 1
 * @since 02.10.2020
 */
public class PsqlStore implements Store {
    /** Field logger.*/
    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    /** Field connection.*/
    private Connection cnn;

    /**
     * PsqlStore constructor.
     * @param propertyFile property file name
     */
    public PsqlStore(String propertyFile) {
        Properties props = PropertyLoader.getProps(propertyFile);
        try {
            Class.forName(props.getProperty("jdbc.driver"));
            this.cnn = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Method to save Post instance into store.
     * @param post instance
     */
    @Override
    public void save(Post post) {
        try (PreparedStatement pst = this.cnn.prepareStatement(
                "insert into post(name, text, link, created, author) values(?, ?, ?, ?, ?)")) {
            pst.setString(1, post.getTitle());
            pst.setString(2, post.getDescription());
            pst.setString(3, post.getLink());
            pst.setTimestamp(4, post.getCreated());
            pst.setString(5, post.getAuthor());
            pst.executeUpdate();
        } catch (SQLException sqle) {
            LOG.error(sqle.getMessage(), sqle);
        }
    }

    /**
     * Method to get all saved posts from store.
     * @return result list of posts
     */
    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        try (Statement pst = this.cnn.createStatement()) {
            try (ResultSet rst = pst.executeQuery("select * from post")) {
                while (rst.next()) {
                    result.add(new Post(
                            rst.getString("link"),
                            rst.getString("name"),
                            rst.getString("author"),
                            rst.getString("text"),
                            rst.getTimestamp("created")
                    ));
                }
            }
        } catch (SQLException sqle) {
            LOG.error(sqle.getMessage(), sqle);
        }
        return result;
    }

    /**
     * Method to find post by id.
     * @param id value
     * @return result Post instance
     */
    @Override
    public Post findById(String id) {
        Post result = null;
        try (PreparedStatement checkPst = this.cnn.prepareStatement("select count(*) as count from post where id=?")) {
            checkPst.setInt(1, Integer.parseInt(id));
            try (ResultSet checkRst = checkPst.executeQuery()) {
                while (checkRst.next()) {
                    int count = checkRst.getInt("count");
                    if (count != 1) {
                        throw new NoSuchElementException();
                    }
                }
            }
            try (PreparedStatement pst = this.cnn.prepareStatement("select * from post where id=?")) {
                pst.setInt(1, Integer.parseInt(id));
                try (ResultSet rst = pst.executeQuery()) {
                    while (rst.next()) {
                        result = new Post(
                                rst.getString("link"),
                                rst.getString("name"),
                                rst.getString("author"),
                                rst.getString("text"),
                                rst.getTimestamp("created")
                        );
                    }
                }
            }
        } catch (NoSuchElementException | SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Entry point.
     * @param args arguments
     */
    public static void main(String[] args) {
        PsqlStore store = new PsqlStore("db.properties");
        Post p = new Post(
                "https://sql.ru",
                "JavaScript middle developer",
                "author.sql",
                "Some good job for javascript middle developer",
                new Timestamp(System.currentTimeMillis()));
        store.save(p);
        store.getAll().forEach(System.out::println);
    }
}
