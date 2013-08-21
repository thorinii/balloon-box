package me.lachlanap.balloonbox.core.story;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import static com.google.common.base.Preconditions.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A helper class for loading a {@link Story} from a text file.
 * Format is:
 * <p/>
 * <pre>
 * (blank lines allowed)
 * # this is a comment
 * &lt;scene-type&gt; &lt;name&gt;
 * ...
 * </pre>
 * <p/>
 * @author Lachlan
 */
public class StoryLoader {

    public static Story load(File rawFile) throws IOException {
        List<String> lines = Files.readLines(rawFile, Charsets.UTF_8);
        return load(lines);
    }

    public static Story load(String raw) {
        return load(raw.split("\n"));
    }

    public static Story load(String[] raw) {
        return load(Arrays.asList(raw));
    }

    public static Story load(Iterable<String> raw) {
        raw = Iterables.transform(raw, new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input.trim();
            }
        });
        raw = Iterables.filter(raw, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                // filter blank lines and comments
                return !input.isEmpty() && !input.startsWith("#");
            }
        });

        checkArgument(Iterables.size(raw) > 0, "Cannot have empty definition");
        return loadSafe(raw);
    }

    private static Story loadSafe(Iterable<String> raw) {
        List<Scene> scenes = new ArrayList<>();

        for (String line : raw)
            scenes.add(process(line));

        return new Story(scenes.toArray(new Scene[scenes.size()]));
    }

    private static Scene process(String line) {
        int firstSpace = line.indexOf(' ');
        checkElementIndex(firstSpace, line.length());

        String sceneType = line.substring(0, firstSpace);

        switch (sceneType) {
            case "level":
                return new LevelScene(line.substring(firstSpace + 1).trim());
            case "cinematic":
                return new CinematicScene(line.substring(firstSpace + 1).trim());
            default:
                throw new IllegalArgumentException("Invalid scene type: " + sceneType);
        }
    }
}
