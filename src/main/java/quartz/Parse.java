package quartz;

import models.Post;

import java.util.List;

/**
 * Interface Parse.
 * @author v.ivanov
 * @version 1
 * @since 27.09.2020
 */
public interface Parse {
    /**
     * Method gets all posts list.
     * @param link value
     * @return resulting Post instances list
     */
    List<Post> list(String link);

    /**
     * Method loads details of particular post.
     * @param link value
     * @return Post instance
     */
    Post detail(String link);
}
