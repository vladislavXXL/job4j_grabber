package models;

import java.sql.Timestamp;

/**
 * Class Post.
 * @author v.ivanov
 * @version 1
 * @since 15.09.2020
 */
public class Post {

    /**
     * Default constructor.
     */
    public Post() {

    }

    /**
     * Constructor.
     * @param link value
     * @param title value
     * @param author value
     * @param description value
     * @param created value
     */
    public Post(String link, String title, String author, String description, Timestamp created) {
        this.link = link;
        this.title = title;
        this.author = author;
        this.description = description;
        this.created = created;
    }

    /** Link.*/
    private String link;

    /** Title.*/
    private String title;

    /** Author.*/
    private String author;

    /** Description.*/
    private String description;

    /** Created.*/
    private Timestamp created;

    /**
     * Link field getter.
     * @return link value
     */
    public String getLink() {
        return link;
    }

    /**
     * Link field setter.
     * @param link value
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Description field getter.
     * @return description value
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description field setter.
     * @param description value
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Created field getter.
     * @return created value
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     * Created field setter.
     * @param created value
     */
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    /**
     * Title field getter.
     * @return title value
     */
    public String getTitle() {
        return title;
    }
    /**
     * Title field setter.
     * @param title value
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Author field getter.
     * @return author value
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Author field setter.
     * @param author value
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
