package me.lachlanap.balloonbox.core.story;

/**
 *
 * @author lachlan
 */
public class StoryLoadingException extends RuntimeException {

    public StoryLoadingException(String message) {
        super(message);
    }

    public StoryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
