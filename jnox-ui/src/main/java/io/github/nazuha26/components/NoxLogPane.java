package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

public class NoxLogPane extends NoxScrollPane {
    private static final int DEFAULT_MAX_LINES = 1000;

    private final NoxLogTextPane textPane;
    private final StyledDocument document;

    @Getter @Setter private boolean autoScroll = true;
    @Getter private int maxLines = DEFAULT_MAX_LINES;

    NoxLogPane() {
        this(new NoxLogTextPane());
    }

    private NoxLogPane(NoxLogTextPane textPane) {
        super(textPane);

        this.textPane = textPane;
        this.document = textPane.getStyledDocument();

        setBorder(NoxBorders.surface());
        setViewportBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        textPane.setEditable(false);
        textPane.setFocusable(true);
        textPane.setOpaque(false);

        textPane.setFont(NoxTheme.FONT_PLAIN);
        textPane.setForeground(NoxTheme.TEXT_PRIMARY);
        textPane.setBackground(NoxTheme.BG_PRIMARY);
        textPane.setCaretColor(NoxTheme.TEXT_PRIMARY);
        textPane.setSelectionColor(NoxTheme.ACCENT_PRIMARY);
        textPane.setSelectedTextColor(NoxTheme.TEXT_PRIMARY);

        textPane.setBorder(BorderFactory.createEmptyBorder());
    }

    public void append(String text) {
        append(NoxLogSegment.of(text));
    }

    public void append(NoxLogSegment segment) {
        if (segment == null) {
            return;
        }

        runOnEventDispatchThread(() -> {
            insertSegment(segment);
            trimLinesIfNeeded();
            scrollToBottomIfNeeded();
        });
    }

    public void append(NoxLogSegment... segments) {
        if (segments == null || segments.length == 0) {
            return;
        }

        runOnEventDispatchThread(() -> {
            for (NoxLogSegment segment : segments) {
                if (segment != null) {
                    insertSegment(segment);
                }
            }

            trimLinesIfNeeded();
            scrollToBottomIfNeeded();
        });
    }

    public void appendLine(String text) {
        appendLine(NoxLogSegment.of(text));
    }

    public void appendLine(NoxLogSegment... segments) {
        runOnEventDispatchThread(() -> {
            if (segments != null) {
                for (NoxLogSegment segment : segments) {
                    if (segment != null) {
                        insertSegment(segment);
                    }
                }
            }

            insertSegment(NoxLogSegment.of(System.lineSeparator()));
            trimLinesIfNeeded();
            scrollToBottomIfNeeded();
        });
    }

    public void clear() {
        runOnEventDispatchThread(() -> textPane.setText(""));
    }

    /**
     * Sets maximum line count.
     * Use 0 or negative value to disable line limit.
     *
     * @param maxLines maximum lines to keep
     */
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        runOnEventDispatchThread(this::trimLinesIfNeeded);
    }

    public boolean isLineWrap() {
        return textPane.isLineWrap();
    }

    public void setLineWrap(boolean lineWrap) {
        textPane.setLineWrap(lineWrap);
        textPane.revalidate();
        textPane.repaint();
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public int getTextLength() {
        return document.getLength();
    }

    private void insertSegment(NoxLogSegment segment) {
        try {
            document.insertString(
                    document.getLength(),
                    segment.text(),
                    segment.style().toAttributes()
            );
        } catch (BadLocationException e) {
            throw new IllegalStateException("Failed to append log segment.", e);
        }
    }

    private void trimLinesIfNeeded() {
        if (maxLines <= 0) {
            return;
        }

        Element root = document.getDefaultRootElement();
        int lineCount = root.getElementCount();

        if (lineCount <= maxLines) {
            return;
        }

        int linesToRemove = lineCount - maxLines;
        int removeEndOffset = root.getElement(linesToRemove).getStartOffset();

        try {
            document.remove(0, removeEndOffset);
        } catch (BadLocationException e) {
            throw new IllegalStateException("Failed to trim log lines.", e);
        }
    }

    private void scrollToBottomIfNeeded() {
        if (autoScroll) {
            textPane.setCaretPosition(document.getLength());
        }
    }

    private void runOnEventDispatchThread(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) {
            action.run();
        } else {
            SwingUtilities.invokeLater(action);
        }
    }

    private static final class NoxLogTextPane extends JTextPane {
        @Getter @Setter private boolean lineWrap = true;

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return lineWrap;
        }
    }
}