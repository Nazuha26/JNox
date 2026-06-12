package io.github.nazuha26.components;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class NoxLogPaneBuilder {
    NoxLogPaneBuilder() { }

    private Dimension preferredSize;
    private boolean autoScroll = true;
    private boolean lineWrap = true;
    private int maxLines = 1000;

    private final List<NoxLogSegment[]> initialLines = new ArrayList<>();

    public NoxLogPaneBuilder preferredSize(int width, int height) {
        this.preferredSize = new Dimension(width, height);
        return this;
    }

    public NoxLogPaneBuilder autoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
        return this;
    }

    public NoxLogPaneBuilder lineWrap(boolean lineWrap) {
        this.lineWrap = lineWrap;
        return this;
    }

    /**
     * Sets maximum line count.
     * Use 0 or negative value to disable line limit.
     *
     * @param maxLines maximum lines to keep
     * @return current builder instance
     */
    public NoxLogPaneBuilder maxLines(int maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    public NoxLogPaneBuilder initialLine(String text) {
        this.initialLines.add(new NoxLogSegment[] {
                NoxLogSegment.of(text)
        });
        return this;
    }

    public NoxLogPaneBuilder initialLine(NoxLogSegment... segments) {
        this.initialLines.add(segments);
        return this;
    }

    /**
     * Builds and returns a configured component.
     *
     * @return configured component
     */
    public NoxLogPane build() {
        NoxLogPane logArea = new NoxLogPane();

        logArea.setAutoScroll(autoScroll);
        logArea.setLineWrap(lineWrap);
        logArea.setMaxLines(maxLines);

        if (preferredSize != null) {
            logArea.setPreferredSize(preferredSize);
        }

        for (NoxLogSegment[] line : initialLines) {
            logArea.appendLine(line);
        }

        return logArea;
    }
}