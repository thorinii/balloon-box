/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.level.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class Animation {

    private final List<Step> steps;
    private final float totalTime;

    Animation(List<Step> steps) {
        this.steps = new ArrayList<>(steps);

        float total = 0;
        for (Step step : steps)
            total += step.length;
        totalTime = total;
    }

    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public float getTotalTime() {
        return totalTime;
    }

    public boolean isInfinite() {
        return totalTime == 0;
    }

    public Step getStep(float selectedTime) {
        if (isInfinite()) {
            return steps.get(0);
        } else {
            float timeSoFar = 0;

            for (Step step : steps) {
                if (timeSoFar <= selectedTime && timeSoFar + step.length > selectedTime)
                    return step;

                timeSoFar += step.length;
            }

            throw new IllegalArgumentException("selectedTime out of range for this " + totalTime + " second animation;"
                                               + " could not find step at " + selectedTime + " seconds");
        }
    }

    @Override
    public String toString() {
        return "Animation{" + "steps=" + steps + ", totalTime=" + totalTime + '}';
    }

    public static class Step {

        public final float length;
        public final Image[] images;

        Step(Image[] images, float length) {
            this.images = images;
            this.length = length;
        }

        @Override
        public String toString() {
            return "Step{length=" + length + ", images=" + Arrays.toString(images) + '}';
        }
    }

    public static class Image {

        public final String name;

        Image(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Img(" + name + ")";
        }
    }
}
