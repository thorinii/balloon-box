package me.lachlanap.balloonbox.core.story;

/**
 *
 * @author lachlan
 */
public abstract class Scene {

    private final String name;

    public Scene(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + " name=" + getName() + "]";
    }
}
