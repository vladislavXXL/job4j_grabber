package propsloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class PropertyLoader.
 * @author v.ivanov
 * @version 1.0
 * @since 10.09.2020
 */
public class PropertyLoader {
    /**
     * Method to get properties to read parameters.
     * @return Properties instance
     */
    public static Properties getProps() {
        Properties result = new Properties();
        try (InputStream in = PropertyLoader.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            result.load(in);
            return result;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
