package io.github.nazuha26;

import io.github.nazuha26.jnox.Nox;
import io.github.nazuha26.jnox.border.NoxBorders;
import io.github.nazuha26.jnox.button.NoxButton;
import io.github.nazuha26.jnox.dialog.NoxOptionPane;
import io.github.nazuha26.jnox.input.NoxPasswordField;
import io.github.nazuha26.jnox.input.NoxTextField;
import io.github.nazuha26.jnox.log.NoxLogPane;
import io.github.nazuha26.jnox.log.NoxLogSegment;
import io.github.nazuha26.jnox.scroll.NoxScrollPane;
import io.github.nazuha26.jnox.theme.NoxTheme;
import io.github.nazuha26.jnox.window.NoxNativeDialog;
import io.github.nazuha26.jnox.window.NoxNativeFrame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Main {
    private static final Dimension BUTTON_SIZE = new Dimension(220, 36);
    private static final Dimension SMALL_BUTTON_SIZE = new Dimension(160, 34);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static NoxNativeFrame mainFrame;
    private static JLabel statusLabel;
    private static NoxLogPane logPane;
    private static int logCounter;
    private static boolean logAutoScroll = true;
    private static boolean logLineWrap = true;
    private static NoxButton logAutoScrollButton;
    private static NoxButton logWrapButton;
    private static JLabel logStateLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Nox.install();

            mainFrame = new NoxNativeFrame("Nox UI Sandbox Overview");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(1080, 820);
            mainFrame.setMinimumSize(new Dimension(820, 560));
            mainFrame.setResizable(true);
            mainFrame.setMaximizable(true);
            mainFrame.setMinimizable(true);
            mainFrame.setClosable(true);
            mainFrame.setLocationRelativeTo(null);

            buildWindow(mainFrame);
            mainFrame.setVisible(true);
        });
    }

    private static void buildWindow(NoxNativeFrame frame) {
        JPanel body = frame.getBody();
        body.setLayout(new BorderLayout(0, 14));
        body.setBorder(new EmptyBorder(18, 18, 18, 18));
        body.setBackground(NoxTheme.BG_PRIMARY);

        body.add(createHeader(frame), BorderLayout.NORTH);

        JPanel content = createRowsPanel();
        content.add(createOverviewSection());
        content.add(verticalSpace());
        content.add(createFrameSection(frame));
        content.add(verticalSpace());
        content.add(createDialogSection(frame));
        content.add(verticalSpace());
        content.add(createButtonSection(frame));
        content.add(verticalSpace());
        content.add(createInputSection(frame));
        content.add(verticalSpace());
        content.add(createOptionPaneSection(frame));
        content.add(verticalSpace());
        content.add(createLogPaneSection());
        content.add(verticalSpace());
        content.add(createScrollPaneSection());
        content.add(verticalSpace());
        content.add(createBordersAndThemeSection());

        NoxScrollPane scrollPane = new NoxScrollPane(content);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(22);

        body.add(scrollPane, BorderLayout.CENTER);

        statusLabel = createLabel("Ready", NoxTheme.FONT_PLAIN_SMALL);
        statusLabel.setBorder(new EmptyBorder(0, 4, 0, 4));
        body.add(statusLabel, BorderLayout.SOUTH);
    }

    private static JPanel createHeader(NoxNativeFrame frame) {
        JPanel header = new JPanel(new BorderLayout(12, 8));
        header.setOpaque(false);

        JLabel title = createLabel("Nox UI Sandbox Overview", NoxTheme.FONT_BOLD.deriveFont(18f));
        JLabel subtitle = createLabel(
                "<html>Sandbox for the direct API: new component() + set...(), without builders and without Nox factory methods.</html>",
                NoxTheme.FONT_PLAIN
        );

        JPanel text = new JPanel(new BorderLayout(0, 4));
        text.setOpaque(false);
        text.add(title, BorderLayout.NORTH);
        text.add(subtitle, BorderLayout.CENTER);

        NoxButton resetTitle = createButton("Reset title", new Dimension(140, 34), e -> {
            frame.setTitle("Nox UI Sandbox Overview");
            updateStatus("Main frame title was reset");
        });

        header.add(text, BorderLayout.CENTER);
        header.add(resetTitle, BorderLayout.EAST);
        return header;
    }

    private static JPanel createOverviewSection() {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("Coverage map. Every item below has a small interactive example."));

        JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(createCoverageItem("NoxNativeFrame", "new + setters"));
        grid.add(createCoverageItem("NoxNativeDialog", "modal/modeless"));
        grid.add(createCoverageItem("NoxButton", "click/default button"));
        grid.add(createCoverageItem("NoxTextField", "placeholder/action"));
        grid.add(createCoverageItem("NoxPasswordField", "echo/show-hide"));
        grid.add(createCoverageItem("NoxOptionPane", "message/confirm"));
        grid.add(createCoverageItem("NoxLogPane", "segments/styles"));
        grid.add(createCoverageItem("NoxScrollPane", "custom scrollbars"));
        grid.add(createCoverageItem("NoxBorders", "surface/titled"));

        panel.add(grid);
        return createSection("Overview", panel);
    }

    private static JPanel createFrameSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("Frame is created directly with new NoxNativeFrame(title), then configured with ordinary setters."));

        NoxTextField titleField = new NoxTextField(frame.getTitle());
        titleField.setPlaceholder("Window title");
        titleField.setPreferredSize(new Dimension(320, 36));
        titleField.addActionListener(e -> applyMainFrameTitle(frame, titleField));

        JPanel titleRow = createFlowPanel();
        titleRow.add(labeled("Title", titleField));
        titleRow.add(createSmallButton("Apply", e -> applyMainFrameTitle(frame, titleField)));

        JPanel flags = createFlowPanel();
        flags.add(createSmallButton("Resizable on/off", e -> {
            frame.setResizable(!frame.isResizable());
            updateStatus("Resizable = " + frame.isResizable());
        }));
        flags.add(createSmallButton("Max button on/off", e -> {
            frame.setMaximizable(!frame.isMaximizable());
            updateStatus("Maximizable = " + frame.isMaximizable());
        }));
        flags.add(createSmallButton("Min button on/off", e -> {
            frame.setMinimizable(!frame.isMinimizable());
            updateStatus("Minimizable = " + frame.isMinimizable());
        }));
        flags.add(createSmallButton("Close button on/off", e -> {
            frame.setClosable(!frame.isClosable());
            updateStatus("Closable = " + frame.isClosable());
        }));

        panel.add(titleRow);
        panel.add(flags);
        return createSection("Frame", panel);
    }

    private static JPanel createDialogSection(NoxNativeFrame owner) {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("Dialogs are created directly with new NoxNativeDialog(owner, title, modal)."));

        JPanel row = createFlowPanel();
        row.add(createButton("Open modal dialog", BUTTON_SIZE, e -> openDemoDialog(owner, true)));
        row.add(createButton("Open modeless dialog", BUTTON_SIZE, e -> openDemoDialog(owner, false)));
        row.add(createButton("Open form dialog", BUTTON_SIZE, e -> openFormDialog(owner)));

        panel.add(row);
        return createSection("Dialog", panel);
    }

    private static JPanel createButtonSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("Buttons are plain NoxButton instances configured with setPreferredSize() and addActionListener()."));

        JPanel row = createFlowPanel();
        row.add(createButton("Simple click", BUTTON_SIZE, e -> updateStatus("Simple button clicked")));

        NoxButton defaultButton = createButton("Default button", BUTTON_SIZE, e -> updateStatus("Default button clicked"));
        frame.getRootPane().setDefaultButton(defaultButton);
        row.add(defaultButton);

        NoxButton disabledButton = createButton("Disabled button", BUTTON_SIZE, e -> updateStatus("This should not run"));
        disabledButton.setEnabled(false);
        row.add(disabledButton);

        panel.add(row);
        return createSection("Button", panel);
    }

    private static JPanel createInputSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("Inputs are created with constructors and configured through setters."));

        NoxTextField textField = new NoxTextField("");
        textField.setPlaceholder("Type title and press Enter");
        textField.setPreferredSize(new Dimension(320, 36));
        textField.addActionListener(e -> applyMainFrameTitle(frame, textField));

        NoxPasswordField passwordField = new NoxPasswordField();
        passwordField.setPlaceholder("Password");
        passwordField.setPreferredSize(new Dimension(320, 36));
        passwordField.addActionListener(e -> updatePasswordStatus(passwordField));

        NoxButton toggleEcho = createSmallButton("Show/hide", e -> {
            passwordField.setEchoChar(passwordField.getEchoChar() == 0 ? '*' : (char) 0);
            updateStatus(passwordField.getEchoChar() == 0 ? "Password visible" : "Password hidden");
        });

        JPanel row1 = createFlowPanel();
        row1.add(labeled("Text", textField));
        row1.add(createSmallButton("Apply title", e -> applyMainFrameTitle(frame, textField)));

        JPanel row2 = createFlowPanel();
        row2.add(labeled("Password", passwordField));
        row2.add(toggleEcho);
        row2.add(createSmallButton("Check length", e -> updatePasswordStatus(passwordField)));

        panel.add(row1);
        panel.add(row2);
        return createSection("Input fields", panel);
    }

    private static JPanel createOptionPaneSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("NoxOptionPane uses the same direct component API internally."));

        JPanel row1 = createFlowPanel();
        row1.add(createButton("Information", BUTTON_SIZE, e -> showMessage(frame, NoxOptionPane.MessageType.INFORMATION)));
        row1.add(createButton("Warning", BUTTON_SIZE, e -> showMessage(frame, NoxOptionPane.MessageType.WARNING)));
        row1.add(createButton("Error", BUTTON_SIZE, e -> showMessage(frame, NoxOptionPane.MessageType.ERROR)));

        JPanel row2 = createFlowPanel();
        row2.add(createButton("YES / NO", BUTTON_SIZE, e -> showConfirm(frame, NoxOptionPane.OptionType.YES_NO)));
        row2.add(createButton("YES / NO / CANCEL", BUTTON_SIZE, e -> showConfirm(frame, NoxOptionPane.OptionType.YES_NO_CANCEL)));
        row2.add(createButton("OK / CANCEL", BUTTON_SIZE, e -> showConfirm(frame, NoxOptionPane.OptionType.OK_CANCEL)));

        panel.add(row1);
        panel.add(row2);
        return createSection("Option pane", panel);
    }

    private static JPanel createLogPaneSection() {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("NoxLogPane is created with new NoxLogPane(), then configured through setters."));

        logPane = new NoxLogPane();
        logPane.setPreferredSize(new Dimension(900, 230));
        logPane.setAutoScroll(logAutoScroll);
        logPane.setLineWrap(logLineWrap);
        logPane.setMaxLines(120);
        logPane.appendLine(
                NoxLogSegment.muted("[" + now() + "] "),
                NoxLogSegment.accent("NoxLogPane ready "),
                NoxLogSegment.success("success "),
                NoxLogSegment.warning("warning "),
                NoxLogSegment.error("error")
        );

        JPanel actions = createFlowPanel();
        actions.add(createSmallButton("Add line", e -> appendNormalLogLine()));
        actions.add(createSmallButton("Add styled", e -> appendStyledLogLine()));
        actions.add(createSmallButton("Add 100", e -> appendManyLogLines()));
        actions.add(createSmallButton("Clear", e -> {
            logPane.clear();
            logCounter = 0;
            updateStatus("Log was cleared");
        }));

        logAutoScrollButton = createSmallButton("Auto scroll: on", e -> toggleLogAutoScroll());
        logWrapButton = createSmallButton("Wrap: on", e -> toggleLogWrap());
        logStateLabel = createLabel("maxLines=120 | length=" + logPane.getTextLength(), NoxTheme.FONT_PLAIN_SMALL);

        JPanel toggles = createFlowPanel();
        toggles.add(logAutoScrollButton);
        toggles.add(logWrapButton);
        toggles.add(logStateLabel);

        panel.add(actions);
        panel.add(toggles);
        panel.add(logPane);
        return createSection("Log pane", panel);
    }

    private static JPanel createScrollPaneSection() {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("NoxScrollPane wraps any Swing component directly through its constructor."));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setFont(NoxTheme.FONT_PLAIN);
        textArea.setForeground(NoxTheme.TEXT_PRIMARY);
        textArea.setBackground(NoxTheme.BG_PRIMARY);
        textArea.setBorder(new EmptyBorder(8, 10, 8, 10));

        for (int i = 1; i <= 40; i++) {
            textArea.append("Scrollable row " + i + " | direct NoxScrollPane(view) API | long text for horizontal overflow check.\n");
        }

        NoxScrollPane scrollPane = new NoxScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(900, 180));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        JPanel actions = createFlowPanel();
        actions.add(createSmallButton("Top", e -> verticalBar.setValue(verticalBar.getMinimum())));
        actions.add(createSmallButton("Middle", e -> verticalBar.setValue(verticalBar.getMaximum() / 2)));
        actions.add(createSmallButton("Bottom", e -> verticalBar.setValue(verticalBar.getMaximum())));

        panel.add(actions);
        panel.add(scrollPane);
        return createSection("Scroll pane", panel);
    }

    private static JPanel createBordersAndThemeSection() {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("Borders and theme constants are still static helpers."));

        JPanel row = createFlowPanel();
        row.add(createThemeCard("BG_PRIMARY", NoxTheme.BG_PRIMARY));
        row.add(createThemeCard("BG_SURFACE", NoxTheme.BG_SURFACE));
        row.add(createThemeCard("ACCENT", NoxTheme.ACCENT_PRIMARY));
        row.add(createThemeCard("SUCCESS", NoxTheme.SUCCESS));
        row.add(createThemeCard("WARNING", NoxTheme.WARNING));
        row.add(createThemeCard("ERROR", NoxTheme.ERROR));

        panel.add(row);
        return createSection("Borders and theme", panel);
    }

    private static void openDemoDialog(NoxNativeFrame owner, boolean modal) {
        String title = modal ? "Modal direct dialog" : "Modeless direct dialog";
        NoxNativeDialog dialog = new NoxNativeDialog(owner, title, modal);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(520, 280);
        dialog.setMinimumSize(new Dimension(430, 240));
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(owner);

        JPanel body = dialog.getBody();
        body.setLayout(new BorderLayout(0, 14));
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
        body.setBackground(NoxTheme.BG_PRIMARY);

        JLabel message = createLabel(
                "<html>This dialog was created through <b>new NoxNativeDialog(owner, title, modal)</b> and configured with ordinary setters.</html>",
                NoxTheme.FONT_PLAIN
        );
        body.add(message, BorderLayout.CENTER);

        NoxButton closeButton = createButton("Close dialog", BUTTON_SIZE, e -> dialog.dispose());
        dialog.getRootPane().setDefaultButton(closeButton);
        body.add(wrapCenter(closeButton), BorderLayout.SOUTH);

        dialog.setVisible(true);
        updateStatus("Opened dialog: " + title);
    }

    private static void openFormDialog(NoxNativeFrame owner) {
        NoxNativeDialog dialog = new NoxNativeDialog(owner, "Input components dialog", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(540, 320);
        dialog.setMinimumSize(new Dimension(430, 260));
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(owner);

        JPanel body = dialog.getBody();
        body.setLayout(new BorderLayout(0, 14));
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
        body.setBackground(NoxTheme.BG_PRIMARY);

        NoxTextField login = new NoxTextField("");
        login.setPlaceholder("Login");
        login.setPreferredSize(new Dimension(320, 36));

        NoxPasswordField password = new NoxPasswordField();
        password.setPlaceholder("Password");
        password.setPreferredSize(new Dimension(320, 36));

        JPanel form = createRowsPanel();
        form.add(labeled("Login", login));
        form.add(Box.createVerticalStrut(8));
        form.add(labeled("Password", password));

        NoxButton submit = createSmallButton("Submit", e -> {
            char[] passwordChars = password.getPassword();
            updateStatus("Form submitted: login=" + login.getText() + ", passwordLength=" + passwordChars.length);
            Arrays.fill(passwordChars, '\0');
            dialog.dispose();
        });
        dialog.getRootPane().setDefaultButton(submit);

        JPanel actions = createFlowPanel();
        actions.add(submit);
        actions.add(createSmallButton("Cancel", e -> dialog.dispose()));

        body.add(createInfoLabel("This custom modal dialog uses NoxTextField, NoxPasswordField and NoxButton."), BorderLayout.NORTH);
        body.add(form, BorderLayout.CENTER);
        body.add(actions, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static void showMessage(Component parent, NoxOptionPane.MessageType messageType) {
        NoxOptionPane.showMessageDialog(
                parent,
                "This message is shown through NoxOptionPane with the direct component implementation.",
                "Message Test: " + messageType,
                messageType
        );
        updateStatus("Shown message dialog: " + messageType);
    }

    private static void showConfirm(Component parent, NoxOptionPane.OptionType optionType) {
        NoxOptionPane.OptionResult result = NoxOptionPane.showConfirmDialog(
                parent,
                "Select one option to test result mapping.",
                "Option Test: " + optionType,
                optionType,
                NoxOptionPane.MessageType.QUESTION
        );
        updateStatus("Option result = " + result);
    }

    private static void applyMainFrameTitle(NoxNativeFrame frame, NoxTextField titleField) {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            title = "Nox UI Sandbox Overview";
            titleField.setText(title);
        }
        frame.setTitle(title);
        updateStatus("Main frame title = " + title);
    }

    private static void updatePasswordStatus(NoxPasswordField passwordField) {
        char[] password = passwordField.getPassword();
        updateStatus("Password length = " + password.length);
        Arrays.fill(password, '\0');
    }

    private static void appendNormalLogLine() {
        logCounter++;
        logPane.appendLine("[" + now() + "] normal log line #" + logCounter + " | textLength=" + logPane.getTextLength());
        updateLogState();
        updateStatus("Added normal log line #" + logCounter);
    }

    private static void appendStyledLogLine() {
        logCounter++;
        logPane.appendLine(
                NoxLogSegment.muted("[" + now() + "] "),
                NoxLogSegment.accent("EVENT "),
                NoxLogSegment.success("success "),
                NoxLogSegment.warning("warning "),
                NoxLogSegment.error("error "),
                NoxLogSegment.muted("styled #" + logCounter)
        );
        updateLogState();
        updateStatus("Added styled log line #" + logCounter);
    }

    private static void appendManyLogLines() {
        for (int i = 0; i < 100; i++) {
            logCounter++;
            logPane.appendLine(
                    NoxLogSegment.muted("[" + now() + "] "),
                    NoxLogSegment.of("bulk line #" + logCounter + " "),
                    i % 3 == 0 ? NoxLogSegment.success("ok") : NoxLogSegment.muted("plain")
            );
        }
        updateLogState();
        updateStatus("Added 100 log lines");
    }

    private static void toggleLogAutoScroll() {
        logAutoScroll = !logAutoScroll;
        logPane.setAutoScroll(logAutoScroll);
        logAutoScrollButton.setText("Auto scroll: " + (logAutoScroll ? "on" : "off"));
        updateLogState();
    }

    private static void toggleLogWrap() {
        logLineWrap = !logLineWrap;
        logPane.setLineWrap(logLineWrap);
        logWrapButton.setText("Wrap: " + (logLineWrap ? "on" : "off"));
        updateLogState();
    }

    private static void updateLogState() {
        if (logStateLabel != null && logPane != null) {
            logStateLabel.setText("maxLines=" + logPane.getMaxLines() + " | length=" + logPane.getTextLength());
        }
    }

    private static NoxButton createButton(String text, Dimension size, ActionListener listener) {
        NoxButton button = new NoxButton(text);
        button.setPreferredSize(size);
        button.addActionListener(listener);
        return button;
    }

    private static NoxButton createSmallButton(String text, ActionListener listener) {
        return createButton(text, SMALL_BUTTON_SIZE, listener);
    }

    private static JPanel createRowsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private static JPanel createFlowPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private static JPanel createSection(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(true);
        panel.setBackground(NoxTheme.BG_SURFACE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new CompoundBorder(
                NoxBorders.titled(title),
                new EmptyBorder(8, 8, 10, 8)
        ));
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(NoxTheme.TEXT_PRIMARY);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private static JLabel createInfoLabel(String text) {
        JLabel label = createLabel("<html>" + text + "</html>", NoxTheme.FONT_PLAIN);
        label.setBorder(new EmptyBorder(2, 4, 8, 4));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static JPanel createCoverageItem(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(true);
        panel.setBackground(NoxTheme.BG_PRIMARY);
        panel.setBorder(new CompoundBorder(NoxBorders.surface(), new EmptyBorder(8, 10, 8, 10)));

        JLabel titleLabel = createLabel(title, NoxTheme.FONT_BOLD);
        JLabel descriptionLabel = createLabel(description, NoxTheme.FONT_PLAIN_SMALL);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descriptionLabel, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createThemeCard(String title, Color color) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(130, 78));
        panel.setBorder(NoxBorders.titled(title));

        JPanel colorBox = new JPanel();
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(90, 26));
        colorBox.setBorder(BorderFactory.createLineBorder(NoxTheme.OUTLINE));

        panel.add(colorBox, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel labeled(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        panel.add(createLabel(labelText, NoxTheme.FONT_PLAIN_SMALL), BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private static Component verticalSpace() {
        return Box.createVerticalStrut(12);
    }

    private static JPanel wrapCenter(Component component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(component);
        return panel;
    }

    private static String now() {
        return LocalTime.now().format(TIME_FORMAT);
    }

    private static void updateStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText("[" + now() + "] " + text);
        }
    }
}