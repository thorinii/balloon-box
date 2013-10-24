package me.lachlanap.balloonbox.core.story;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * A helper class for loading a {@link Story} from a text file.
 * Format is:
 * <br />
 * <pre>
 * (blank lines allowed)
 * # this is a comment
 * &lt;scene-type&gt; &lt;name&gt;
 * ...
 * </pre>
 * <br />
 * <p/>
 * @author Lachlan
 */
public class StoryLoader {

    public static Story load(FileHandle handle) {
        if (handle == null)
            throw new StoryLoadingException("Could not find file referenced by handle");
        return load(handle.read());
    }

    public static Story load(InputStream stream) {
        List<String> lines = new ArrayList<>();

        try (
                InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line);
        } catch (IOException ioe) {
            throw new StoryLoadingException("Error reading story file", ioe);
        }

        return load(lines);
    }

    public static Story load(String[] raw) {
        return load(Arrays.asList(raw));
    }

    public static Story load(Iterable<String> raw) {
        raw = Iterables.transform(raw, new Function<String, String>() {
            @Override
            public String apply(String input) {
                if (input == null)
                    return null;
                return input.trim();
            }
        });
        raw = Iterables.filter(raw, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                // filter blank lines and comments
                if (input == null)
                    return false;
                return !input.isEmpty() && !input.startsWith("#");
            }
        });

        checkArgument(Iterables.size(raw) > 0, "Cannot have empty definition");
        return _load(raw);
    }

    private static Story _load(Iterable<String> raw) {
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
                throw new StoryLoadingException("Invalid scene type: " + sceneType);
        }
    }

    private StoryLoader() {
    }
}
