package io.github.nazuha26;

import io.github.nazuha26.components.Nox;
import io.github.nazuha26.components.NoxBorders;
import io.github.nazuha26.components.NoxButton;
import io.github.nazuha26.components.NoxLogPane;
import io.github.nazuha26.components.NoxLogSegment;
import io.github.nazuha26.components.NoxNativeDialog;
import io.github.nazuha26.components.NoxNativeFrame;
import io.github.nazuha26.components.NoxOptionPane;
import io.github.nazuha26.components.NoxPasswordField;
import io.github.nazuha26.components.NoxScrollPane;
import io.github.nazuha26.components.NoxTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final Dimension BUTTON_SIZE = new Dimension(220, 36);
    private static final Dimension SMALL_BUTTON_SIZE = new Dimension(150, 34);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static NoxNativeFrame mainFrame;
    private static JLabel statusLabel;
    private static NoxLogPane logPane;
    private static int logCounter;
    private static boolean logLineWrap = true;
    private static boolean logAutoScroll = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Nox.install();

            mainFrame = Nox.frame()
                    .title("Nox UI Sandbox Overview")
                    .size(1080, 820)
                    .minimumSize(820, 560)
                    .resizable(true)
                    .maximizable(true)
                    .minimizable(true)
                    .closable(true)
                    .build();

            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        content.add(createFrameBuilderSection(frame));
        content.add(verticalSpace());
        content.add(createDialogBuilderSection(frame));
        content.add(verticalSpace());
        content.add(createButtonBuilderSection(frame));
        content.add(verticalSpace());
        content.add(createInputFieldsSection(frame));
        content.add(verticalSpace());
        content.add(createOptionPaneSection(frame));
        content.add(verticalSpace());
        content.add(createLogPaneSection());
        content.add(verticalSpace());
        content.add(createScrollPaneBuilderSection());
        content.add(verticalSpace());
        content.add(createBordersAndThemeSection());

        NoxScrollPane scrollPane = Nox.scrollPane()
                .view(content)
                .build();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
                "<html>One window that demonstrates the public builders, native window wrappers, inputs, log pane, borders, scrollbars and option dialogs.</html>",
                NoxTheme.FONT_PLAIN
        );

        JPanel text = new JPanel(new BorderLayout(0, 4));
        text.setOpaque(false);
        text.add(title, BorderLayout.NORTH);
        text.add(subtitle, BorderLayout.CENTER);

        NoxButton resetTitle = Nox.button("Reset title")
                .preferredSize(140, 34)
                .onClick(e -> {
                    frame.setTitle("Nox UI Sandbox Overview");
                    updateStatus("Main frame title was reset");
                })
                .build();

        header.add(text, BorderLayout.CENTER);
        header.add(resetTitle, BorderLayout.EAST);
        return header;
    }

    private static JPanel createOverviewSection() {
        JPanel panel = createRowsPanel();
        panel.add(createInfoLabel("Coverage map for the sandbox window. Green labels mean the component has an interactive example below."));

        JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(createCoverageItem("Nox.frame / NoxNativeFrame", "window flags, title, variants", true));
        grid.add(createCoverageItem("Nox.dialog / NoxNativeDialog", "modal, modeless, custom body", true));
        grid.add(createCoverageItem("Nox.button", "text, size, default button, click", true));
        grid.add(createCoverageItem("Nox.textField", "placeholder, columns, action, disabled", true));
        grid.add(createCoverageItem("Nox.passwordField", "placeholder, echo char, action", true));
        grid.add(createCoverageItem("Nox.optionPane", "messages and confirm results", true));
        grid.add(createCoverageItem("Nox.logPane", "segments, styles, wrapping, trimming", true));
        grid.add(createCoverageItem("Nox.scrollPane", "custom scrollbars and preferred size", true));
        grid.add(createCoverageItem("NoxBorders / NoxTheme", "surface, titled, accent, palette", true));

        panel.add(grid);
        return createSection("Overview", panel);
    }

    private static JPanel createFrameBuilderSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.frame().title().size().minimumSize().resizable().maximizable().minimizable().closable().build()."
        ));

        JPanel titleRow = createFlowPanel();
        NoxTextField titleField = Nox.textField(frame.getTitle())
                .placeholder("Window title")
                .preferredSize(330, 36)
                .onAction(e -> applyMainFrameTitle(frame, (NoxTextField) e.getSource()))
                .build();
        titleRow.add(createMutedLabel("Main frame title:"));
        titleRow.add(titleField);
        titleRow.add(createSmallButton("Apply", e -> applyMainFrameTitle(frame, titleField)));
        panel.add(titleRow);

        JCheckBox resizableBox = createCheckBox("resizable", frame.isResizable());
        JCheckBox maximizableBox = createCheckBox("maximizable", frame.isMaximizable());
        JCheckBox minimizableBox = createCheckBox("minimizable", frame.isMinimizable());
        JCheckBox closableBox = createCheckBox("closable", frame.isClosable());

        resizableBox.addActionListener(e -> {
            frame.setResizable(resizableBox.isSelected());
            updateStatus("Main frame resizable = " + frame.isResizable());
        });
        maximizableBox.addActionListener(e -> {
            frame.setMaximizable(maximizableBox.isSelected());
            updateStatus("Main frame maximizable = " + frame.isMaximizable());
        });
        minimizableBox.addActionListener(e -> {
            frame.setMinimizable(minimizableBox.isSelected());
            updateStatus("Main frame minimizable = " + frame.isMinimizable());
        });
        closableBox.addActionListener(e -> {
            frame.setClosable(closableBox.isSelected());
            updateStatus("Main frame closable = " + frame.isClosable());
        });

        JPanel toggles = createFlowPanel();
        toggles.add(resizableBox);
        toggles.add(maximizableBox);
        toggles.add(minimizableBox);
        toggles.add(closableBox);
        panel.add(toggles);

        JPanel frameTests = createFlowPanel();
        frameTests.add(createButton("Default frame", e -> openFrameVariant("Default frame", true, true, true, true)));
        frameTests.add(createButton("Fixed frame", e -> openFrameVariant("Fixed frame", false, true, true, true)));
        frameTests.add(createButton("No maximize", e -> openFrameVariant("No maximize", true, false, true, true)));
        frameTests.add(createButton("No minimize", e -> openFrameVariant("No minimize", true, true, false, true)));
        frameTests.add(createButton("No close button", e -> openFrameVariant("No close button", true, true, true, false)));
        frameTests.add(createButton("All disabled", e -> openFrameVariant("All disabled", false, false, false, false)));
        panel.add(frameTests);

        return createSection("NoxFrameBuilder / NoxNativeFrame", panel);
    }

    private static JPanel createDialogBuilderSection(NoxNativeFrame owner) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.dialog(), Nox.dialog(owner), Nox.dialog(owner, title), owner(), title(), modal(), size(), minimumSize(), resizable(), locationRelativeTo() and defaultCloseOperation()."
        ));

        JPanel row = createFlowPanel();
        row.add(createButton("Modal resizable dialog", e -> openDialogVariant(owner, "Modal resizable dialog", true, true, false)));
        row.add(createButton("Modeless fixed dialog", e -> openDialogVariant(owner, "Modeless fixed dialog", false, false, false)));
        row.add(createButton("Scrollable dialog", e -> openDialogVariant(owner, "Scrollable dialog", true, true, true)));
        row.add(createButton("Form dialog", e -> openFormDialog(owner)));
        panel.add(row);

        return createSection("NoxDialogBuilder / NoxNativeDialog", panel);
    }

    private static JPanel createButtonBuilderSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.button(), Nox.button(text), text(), preferredSize(), onClick(), defaultButton() and build()."
        ));

        NoxButton simpleButton = Nox.button("Simple button")
                .preferredSize(BUTTON_SIZE.width, BUTTON_SIZE.height)
                .onClick(e -> updateStatus("Simple button clicked"))
                .build();

        NoxButton defaultButton = Nox.button()
                .text("Default button")
                .preferredSize(BUTTON_SIZE.width, BUTTON_SIZE.height)
                .defaultButton(frame.getRootPane())
                .onClick(e -> updateStatus("Default button clicked from Enter or mouse"))
                .build();

        NoxButton dialogButton = Nox.button("Open test dialog")
                .preferredSize(BUTTON_SIZE.width, BUTTON_SIZE.height)
                .onClick(e -> openDialogVariant(frame, "Opened by NoxButtonBuilder", true, true, false))
                .build();

        JPanel row = createFlowPanel();
        row.add(simpleButton);
        row.add(defaultButton);
        row.add(dialogButton);
        panel.add(row);

        NoxTextField focusField = Nox.textField()
                .placeholder("Focus here and press Enter to trigger the default button")
                .preferredSize(520, 36)
                .build();
        JPanel hintRow = createFlowPanel();
        hintRow.add(focusField);
        panel.add(hintRow);

        return createSection("NoxButtonBuilder", panel);
    }

    private static JPanel createInputFieldsSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.textField() and Nox.passwordField(): text(), placeholder(), columns(), preferredSize(), editable(), enabled(), echoChar(), onAction() and build()."
        ));

        NoxTextField nameField = Nox.textField()
                .placeholder("Name or search value")
                .columns(24)
                .preferredSize(280, 36)
                .onAction(e -> updateStatus("Text field action: " + ((NoxTextField) e.getSource()).getText()))
                .build();

        NoxTextField readOnlyField = Nox.textField("Read-only value")
                .preferredSize(220, 36)
                .editable(false)
                .build();

        NoxTextField disabledField = Nox.textField("Disabled value")
                .preferredSize(220, 36)
                .enabled(false)
                .build();

        JPanel textFields = createFlowPanel();
        textFields.add(labeled("Editable", nameField));
        textFields.add(labeled("Read-only", readOnlyField));
        textFields.add(labeled("Disabled", disabledField));
        panel.add(textFields);

        NoxPasswordField passwordField = Nox.passwordField()
                .placeholder("Password")
                .columns(20)
                .preferredSize(280, 36)
                .echoChar('•')
                .onAction(e -> updatePasswordStatus((NoxPasswordField) e.getSource()))
                .build();

        NoxPasswordField disabledPassword = Nox.passwordField("secret")
                .preferredSize(220, 36)
                .echoChar('•')
                .enabled(false)
                .build();

        JCheckBox showPassword = createCheckBox("show password", false);
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
            updateStatus("Password echoChar visible = " + showPassword.isSelected());
        });

        JPanel passwordRow = createFlowPanel();
        passwordRow.add(labeled("Password", passwordField));
        passwordRow.add(labeled("Disabled password", disabledPassword));
        passwordRow.add(showPassword);
        passwordRow.add(createSmallButton("Check length", e -> updatePasswordStatus(passwordField)));
        passwordRow.add(createSmallButton("Open form", e -> openFormDialog(frame)));
        panel.add(passwordRow);

        return createSection("NoxTextFieldBuilder / NoxPasswordFieldBuilder", panel);
    }

    private static JPanel createOptionPaneSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel("Checks public NoxOptionPane API with all message types and option types."));

        JPanel messageTypes = createFlowPanel();
        messageTypes.add(createSmallButton("ERROR", e -> showMessage(frame, NoxOptionPane.MessageType.ERROR)));
        messageTypes.add(createSmallButton("INFORMATION", e -> showMessage(frame, NoxOptionPane.MessageType.INFORMATION)));
        messageTypes.add(createSmallButton("WARNING", e -> showMessage(frame, NoxOptionPane.MessageType.WARNING)));
        messageTypes.add(createSmallButton("QUESTION", e -> showMessage(frame, NoxOptionPane.MessageType.QUESTION)));
        messageTypes.add(createSmallButton("PLAIN", e -> showMessage(frame, NoxOptionPane.MessageType.PLAIN)));
        panel.add(messageTypes);

        JPanel optionTypes = createFlowPanel();
        optionTypes.add(createSmallButton("DEFAULT", e -> showConfirm(frame, NoxOptionPane.OptionType.DEFAULT, NoxOptionPane.MessageType.PLAIN)));
        optionTypes.add(createSmallButton("YES_NO", e -> showConfirm(frame, NoxOptionPane.OptionType.YES_NO, NoxOptionPane.MessageType.QUESTION)));
        optionTypes.add(createSmallButton("YES_NO_CANCEL", e -> showConfirm(frame, NoxOptionPane.OptionType.YES_NO_CANCEL, NoxOptionPane.MessageType.WARNING)));
        optionTypes.add(createSmallButton("OK_CANCEL", e -> showConfirm(frame, NoxOptionPane.OptionType.OK_CANCEL, NoxOptionPane.MessageType.INFORMATION)));
        panel.add(optionTypes);

        return createSection("NoxOptionPane", panel);
    }

    private static JPanel createLogPaneSection() {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.logPane(), NoxLogPaneBuilder, append(), appendLine(), clear(), maxLines, autoScroll, lineWrap, NoxLogSegment and NoxLogStyle."
        ));

        logPane = Nox.logPane()
                .preferredSize(900, 230)
                .autoScroll(logAutoScroll)
                .lineWrap(logLineWrap)
                .maxLines(80)
                .initialLine(
                        NoxLogSegment.accent("NoxLogPane ready "),
                        NoxLogSegment.muted("maxLines=80, autoScroll=true, lineWrap=true")
                )
                .initialLine(
                        NoxLogSegment.success("success "),
                        NoxLogSegment.warning("warning "),
                        NoxLogSegment.error("error "),
                        NoxLogSegment.accent("accent "),
                        NoxLogSegment.muted("muted")
                )
                .build();

        JPanel controls = createFlowPanel();
        controls.add(createSmallButton("Add normal", e -> appendNormalLogLine()));
        controls.add(createSmallButton("Add styled", e -> appendStyledLogLine()));
        controls.add(createSmallButton("Add 100 lines", e -> appendManyLogLines()));
        controls.add(createSmallButton("Toggle wrap", e -> toggleLogWrap()));
        controls.add(createSmallButton("Toggle scroll", e -> toggleLogAutoScroll()));
        controls.add(createSmallButton("Clear", e -> {
            logPane.clear();
            updateStatus("Log pane cleared");
        }));

        panel.add(controls);
        panel.add(logPane);
        return createSection("NoxLogPane / NoxLogSegment / NoxLogStyle", panel);
    }

    private static JPanel createScrollPaneBuilderSection() {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel("Checks Nox.scrollPane().view().preferredSize().build() and custom horizontal/vertical scrollbars."));

        JTextArea textArea = new JTextArea(createLongScrollText());
        textArea.setFont(NoxTheme.FONT_PLAIN);
        textArea.setForeground(NoxTheme.TEXT_PRIMARY);
        textArea.setCaretColor(NoxTheme.TEXT_PRIMARY);
        textArea.setBackground(NoxTheme.BG_SURFACE);
        textArea.setEditable(false);
        textArea.setLineWrap(false);

        NoxScrollPane scrollPane = Nox.scrollPane()
                .view(textArea)
                .preferredSize(900, 180)
                .build();

        panel.add(scrollPane);
        return createSection("NoxScrollPaneBuilder / NoxScrollBarUI", panel);
    }

    private static JPanel createBordersAndThemeSection() {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel("Checks NoxBorders.surface(), NoxBorders.titled(), NoxBorders.titledAccent() and visible NoxTheme palette/font constants."));

        JPanel borderSamples = createFlowPanel();
        borderSamples.add(createBorderSample("surface()", NoxBorders.surface()));
        borderSamples.add(createBorderSample("titled()", NoxBorders.titled("Default title")));
        borderSamples.add(createBorderSample("titledAccent()", NoxBorders.titledAccent("Accent title")));
        panel.add(borderSamples);

        JPanel colors = createFlowPanel();
        colors.add(createColorBox("BG_PRIMARY", NoxTheme.BG_PRIMARY));
        colors.add(createColorBox("BG_SURFACE", NoxTheme.BG_SURFACE));
        colors.add(createColorBox("ACCENT", NoxTheme.ACCENT_PRIMARY));
        colors.add(createColorBox("OUTLINE", NoxTheme.OUTLINE));
        colors.add(createColorBox("ERROR", NoxTheme.ERROR));
        colors.add(createColorBox("WARNING", NoxTheme.WARNING));
        colors.add(createColorBox("SUCCESS", NoxTheme.SUCCESS));
        panel.add(colors);

        JPanel fonts = createFlowPanel();
        fonts.add(createFontSample("FONT_PLAIN", NoxTheme.FONT_PLAIN));
        fonts.add(createFontSample("FONT_BOLD", NoxTheme.FONT_BOLD));
        fonts.add(createFontSample("FONT_SMALL", NoxTheme.FONT_PLAIN_SMALL));
        fonts.add(createFontSample("FONT_TINY", NoxTheme.FONT_PLAIN_TINY));
        panel.add(fonts);

        return createSection("NoxBorders / NoxTheme", panel);
    }

    private static void openFrameVariant(String title, boolean resizable, boolean maximizable, boolean minimizable, boolean closable) {
        NoxNativeFrame frame = Nox.frame()
                .title(title)
                .size(500, 300)
                .minimumSize(370, 210)
                .resizable(resizable)
                .maximizable(maximizable)
                .minimizable(minimizable)
                .closable(closable)
                .build();

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(mainFrame);

        JPanel body = frame.getBody();
        body.setLayout(new BorderLayout(0, 12));
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
        body.setBackground(NoxTheme.BG_PRIMARY);

        JLabel info = createLabel(
                "<html><b>" + title + "</b><br><br>" +
                        "resizable = " + resizable + "<br>" +
                        "maximizable = " + maximizable + "<br>" +
                        "minimizable = " + minimizable + "<br>" +
                        "closable = " + closable + "<br><br>" +
                        "This window was created with NoxFrameBuilder.</html>",
                NoxTheme.FONT_PLAIN
        );

        NoxButton closeButton = Nox.button("Dispose this frame")
                .preferredSize(BUTTON_SIZE.width, BUTTON_SIZE.height)
                .defaultButton(frame.getRootPane())
                .onClick(e -> frame.dispose())
                .build();

        body.add(info, BorderLayout.CENTER);
        body.add(wrapCenter(closeButton), BorderLayout.SOUTH);
        frame.setVisible(true);
        updateStatus("Opened frame variant: " + title);
    }

    private static void openDialogVariant(NoxNativeFrame owner, String title, boolean modal, boolean resizable, boolean scrollable) {
        NoxNativeDialog dialog = Nox.dialog(owner)
                .title(title)
                .modal(modal)
                .size(scrollable ? 560 : 460, scrollable ? 380 : 250)
                .minimumSize(360, 190)
                .resizable(resizable)
                .defaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
                .locationRelativeTo(owner)
                .build();

        JPanel body = dialog.getBody();
        body.setLayout(new BorderLayout(0, 12));
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
        body.setBackground(NoxTheme.BG_PRIMARY);

        if (scrollable) {
            JTextArea textArea = new JTextArea(createLongScrollText());
            textArea.setFont(NoxTheme.FONT_PLAIN);
            textArea.setForeground(NoxTheme.TEXT_PRIMARY);
            textArea.setCaretColor(NoxTheme.TEXT_PRIMARY);
            textArea.setBackground(NoxTheme.BG_SURFACE);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            NoxScrollPane scrollPane = Nox.scrollPane()
                    .view(textArea)
                    .build();
            body.add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel message = createLabel(
                    "<html><b>" + title + "</b><br><br>" +
                            "modal = " + modal + "<br>" +
                            "resizable = " + resizable + "<br><br>" +
                            "This dialog was created with NoxDialogBuilder.</html>",
                    NoxTheme.FONT_PLAIN
            );
            body.add(message, BorderLayout.CENTER);
        }

        NoxButton closeButton = Nox.button("Close dialog")
                .preferredSize(BUTTON_SIZE.width, BUTTON_SIZE.height)
                .defaultButton(dialog.getRootPane())
                .onClick(e -> dialog.dispose())
                .build();

        body.add(wrapCenter(closeButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
        updateStatus("Opened dialog variant: " + title);
    }

    private static void openFormDialog(NoxNativeFrame owner) {
        NoxNativeDialog dialog = Nox.dialog(owner, "Input components dialog")
                .modal(true)
                .size(520, 300)
                .minimumSize(430, 260)
                .resizable(true)
                .defaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
                .locationRelativeTo(owner)
                .build();

        JPanel body = dialog.getBody();
        body.setLayout(new BorderLayout(0, 14));
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
        body.setBackground(NoxTheme.BG_PRIMARY);

        JPanel form = createRowsPanel();
        NoxTextField login = Nox.textField()
                .placeholder("Login")
                .preferredSize(320, 36)
                .build();
        NoxPasswordField password = Nox.passwordField()
                .placeholder("Password")
                .preferredSize(320, 36)
                .echoChar('•')
                .build();
        form.add(labeled("Login", login));
        form.add(Box.createVerticalStrut(8));
        form.add(labeled("Password", password));

        JPanel actions = createFlowPanel();
        actions.add(Nox.button("Submit")
                .preferredSize(150, 34)
                .defaultButton(dialog.getRootPane())
                .onClick(e -> {
                    updateStatus("Form submitted: login=" + login.getText() + ", passwordLength=" + password.getPassword().length);
                    Arrays.fill(password.getPassword(), '\0');
                    dialog.dispose();
                })
                .build());
        actions.add(createSmallButton("Cancel", e -> dialog.dispose()));

        body.add(createInfoLabel("This custom modal dialog uses NoxTextField, NoxPasswordField and NoxButton inside NoxNativeDialog."), BorderLayout.NORTH);
        body.add(form, BorderLayout.CENTER);
        body.add(actions, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static void showMessage(Component parent, NoxOptionPane.MessageType messageType) {
        NoxOptionPane.showMessageDialog(
                parent,
                randomLongMessage(),
                "Message Test: " + messageType,
                messageType
        );
        updateStatus("Shown message dialog: " + messageType);
    }

    private static void showConfirm(Component parent, NoxOptionPane.OptionType optionType, NoxOptionPane.MessageType messageType) {
        NoxOptionPane.OptionResult result = NoxOptionPane.showConfirmDialog(
                parent,
                "Select one option to test result mapping.",
                "Option Test: " + optionType,
                optionType,
                messageType
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
                NoxLogSegment.muted(" styled #" + logCounter)
        );
        updateStatus("Added styled log line #" + logCounter);
    }

    private static void appendManyLogLines() {
        for (int i = 0; i < 100; i++) {
            logCounter++;
            logPane.appendLine(
                    NoxLogSegment.muted("[" + now() + "] "),
                    NoxLogSegment.of("bulk line #" + logCounter + " "),
                    i % 3 == 0 ? NoxLogSegment.success("SUCCESS") : NoxLogSegment.accent("INFO")
            );
        }
        updateStatus("Added 100 lines. maxLines should keep only the latest 80 lines.");
    }

    private static void toggleLogWrap() {
        logLineWrap = !logLineWrap;
        logPane.setLineWrap(logLineWrap);
        updateStatus("Log lineWrap = " + logLineWrap);
    }

    private static void toggleLogAutoScroll() {
        logAutoScroll = !logAutoScroll;
        logPane.setAutoScroll(logAutoScroll);
        updateStatus("Log autoScroll = " + logAutoScroll);
    }

    private static NoxButton createButton(String text, java.awt.event.ActionListener actionListener) {
        return Nox.button(text)
                .preferredSize(BUTTON_SIZE.width, BUTTON_SIZE.height)
                .onClick(actionListener)
                .build();
    }

    private static NoxButton createSmallButton(String text, java.awt.event.ActionListener actionListener) {
        return Nox.button(text)
                .preferredSize(SMALL_BUTTON_SIZE.width, SMALL_BUTTON_SIZE.height)
                .onClick(actionListener)
                .build();
    }

    private static JPanel createSection(String title, Component content) {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setOpaque(true);
        section.setBackground(NoxTheme.BG_SURFACE);
        section.setBorder(NoxBorders.titledAccent(title));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(content, BorderLayout.CENTER);
        return section;
    }

    private static JPanel createRowsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private static JPanel createFlowPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private static JPanel wrapCenter(Component component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(component);
        return panel;
    }

    private static JPanel labeled(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = createMutedLabel(title);
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(NoxTheme.TEXT_PRIMARY);
        return label;
    }

    private static JLabel createInfoLabel(String text) {
        JLabel label = createLabel("<html>" + text + "</html>", NoxTheme.FONT_PLAIN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static JLabel createMutedLabel(String text) {
        JLabel label = createLabel(text, NoxTheme.FONT_PLAIN_SMALL);
        label.setForeground(new Color(
                NoxTheme.TEXT_PRIMARY.getRed(),
                NoxTheme.TEXT_PRIMARY.getGreen(),
                NoxTheme.TEXT_PRIMARY.getBlue(),
                160
        ));
        return label;
    }

    private static JCheckBox createCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setFont(NoxTheme.FONT_PLAIN);
        checkBox.setForeground(NoxTheme.TEXT_PRIMARY);
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        return checkBox;
    }

    private static JPanel createCoverageItem(String title, String description, boolean covered) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(true);
        panel.setBackground(NoxTheme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(covered ? NoxTheme.SUCCESS : NoxTheme.WARNING),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel titleLabel = createLabel(title, NoxTheme.FONT_BOLD);
        JLabel descriptionLabel = createMutedLabel(description);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descriptionLabel, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createBorderSample(String text, javax.swing.border.Border border) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(NoxTheme.BG_PRIMARY);
        panel.setBorder(border);
        panel.setPreferredSize(new Dimension(180, 72));
        panel.add(createMutedLabel(text), BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createColorBox(String name, Color color) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(120, 64));

        JPanel swatch = new JPanel();
        swatch.setBackground(color);
        swatch.setBorder(BorderFactory.createLineBorder(NoxTheme.OUTLINE));

        JLabel label = createLabel(name, NoxTheme.FONT_PLAIN_SMALL);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(swatch, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);
        return panel;
    }

    private static JPanel createFontSample(String name, Font font) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(true);
        panel.setBackground(NoxTheme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NoxTheme.OUTLINE),
                new EmptyBorder(8, 10, 8, 10)
        ));
        panel.setPreferredSize(new Dimension(160, 58));

        JLabel sample = createLabel("Aa 123", font);
        JLabel label = createMutedLabel(name);
        panel.add(sample, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);
        return panel;
    }

    private static Component verticalSpace() {
        return Box.createVerticalStrut(12);
    }

    private static void updateStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }

        log.info(text);
    }

    private static String now() {
        return LocalTime.now().format(TIME_FORMAT);
    }

    private static String randomLongMessage() {
        String[] messages = {
                "A critical error occurred while loading data. Please try again later.",
                "The operation completed successfully. All changes have been saved.",
                "You are using an outdated version of the program. An update is recommended.",
                "Are you sure you want to permanently delete these files?",
                "This is a long test message. It checks wrapping, scrolling, icon rendering, modal behavior, default buttons and keyboard navigation inside NoxOptionPane."
        };

        return messages[new Random().nextInt(messages.length)];
    }

    private static String createLongScrollText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 70; i++) {
            sb.append(i)
                    .append(". NoxScrollPane test line. ")
                    .append("This line is long enough to test horizontal scrolling: ")
                    .append("ABCDEFGHIJKLMNOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyz-0123456789")
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
