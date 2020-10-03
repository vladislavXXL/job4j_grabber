package storage;

import models.Post;

import java.util.List;

/**
 * Interface Store.
 * @author v.ivanov
 * @version 1
 * @since 28.09.2020
 */
public interface Store {
    /**
     * Method to save Post instance into store.
     * @param post instance
     */
    void save(Post post);

    /**
     * Method to get all saved posts from store.
     * @return result list of posts
     */
    List<Post> getAll();

    /**
     * Method to find post by id.
     * @param id value
     * @return result Post instance
     */
    Post findById(String id);
}
