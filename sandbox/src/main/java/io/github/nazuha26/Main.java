package io.github.nazuha26;

import io.github.nazuha26.components.Nox;
import io.github.nazuha26.components.NoxButton;
import io.github.nazuha26.components.NoxNativeDialog;
import io.github.nazuha26.components.NoxNativeFrame;
import io.github.nazuha26.components.NoxOptionPane;
import io.github.nazuha26.components.NoxScrollPane;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

@Slf4j
public class Main {
    private static final Dimension BUTTON_SIZE = new Dimension(220, 36);

    private static NoxNativeFrame mainFrame;
    private static JLabel statusLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Nox.install();

            mainFrame = Nox.frame()
                    .title("Nox UI Sandbox")
                    .size(940, 700)
                    .minimumSize(720, 460)
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
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
        body.setBackground(NoxTheme.BG_PRIMARY);

        body.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createFrameBuilderSection(frame));
        content.add(verticalSpace());

        content.add(createDialogBuilderSection(frame));
        content.add(verticalSpace());

        content.add(createButtonBuilderSection(frame));
        content.add(verticalSpace());

        content.add(createOptionPaneSection(frame));
        content.add(verticalSpace());

        content.add(createScrollPaneBuilderSection());
        content.add(verticalSpace());

        content.add(createThemeSection());

        NoxScrollPane scrollPane = Nox.scrollPane()
                .view(content)
                .build();

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        body.add(scrollPane, BorderLayout.CENTER);

        statusLabel = createLabel("Ready", NoxTheme.FONT_PLAIN_SMALL);
        statusLabel.setBorder(new EmptyBorder(0, 4, 0, 4));
        body.add(statusLabel, BorderLayout.SOUTH);
    }

    private static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 6));
        header.setOpaque(false);

        JLabel title = createLabel("Nox UI Sandbox", NoxTheme.FONT_BOLD);
        JLabel subtitle = createLabel(
                "<html>Builder-based test window for NoxFrameBuilder, NoxDialogBuilder, " +
                        "NoxButtonBuilder, NoxScrollPaneBuilder, NoxOptionPane and NoxTheme.</html>",
                NoxTheme.FONT_PLAIN
        );

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.CENTER);

        return header;
    }

    private static JPanel createFrameBuilderSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.frame().title().size().minimumSize().resizable().maximizable().minimizable().closable().build()."
        ));

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

        frameTests.add(createButton("Default frame", e ->
                openFrameVariant("Default frame", true, true, true, true)
        ));

        frameTests.add(createButton("Fixed frame", e ->
                openFrameVariant("Fixed frame", false, true, true, true)
        ));

        frameTests.add(createButton("No maximize", e ->
                openFrameVariant("No maximize", true, false, true, true)
        ));

        frameTests.add(createButton("No minimize", e ->
                openFrameVariant("No minimize", true, true, false, true)
        ));

        frameTests.add(createButton("No close button", e ->
                openFrameVariant("No close button", true, true, true, false)
        ));

        frameTests.add(createButton("All disabled", e ->
                openFrameVariant("All disabled", false, false, false, false)
        ));

        panel.add(frameTests);

        return createSection("NoxFrameBuilder", panel);
    }

    private static JPanel createDialogBuilderSection(NoxNativeFrame owner) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.dialog().owner().title().modal().size().minimumSize().resizable().locationRelativeTo().build()."
        ));

        JPanel row = createFlowPanel();

        row.add(createButton("Modal resizable dialog", e ->
                openDialogVariant(owner, "Modal resizable dialog", true, true, false)
        ));

        row.add(createButton("Modeless fixed dialog", e ->
                openDialogVariant(owner, "Modeless fixed dialog", false, false, false)
        ));

        row.add(createButton("Scrollable dialog", e ->
                openDialogVariant(owner, "Scrollable dialog", true, true, true)
        ));

        panel.add(row);

        return createSection("NoxDialogBuilder", panel);
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
                .onClick(e -> updateStatus("Default button clicked"))
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

        JTextField field = new JTextField("Focus this field and press Enter to test default button.");
        field.setFont(NoxTheme.FONT_PLAIN);
        field.setForeground(NoxTheme.TEXT_PRIMARY);
        field.setCaretColor(NoxTheme.TEXT_PRIMARY);
        field.setBackground(NoxTheme.BG_SURFACE);
        field.setBorder(new EmptyBorder(8, 10, 8, 10));

        panel.add(field);

        return createSection("NoxButtonBuilder", panel);
    }

    private static JPanel createOptionPaneSection(NoxNativeFrame frame) {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks public NoxOptionPane API with all message types and option types."
        ));

        JPanel messageTypes = createFlowPanel();

        messageTypes.add(createButton("ERROR", e ->
                NoxOptionPane.showMessageDialog(
                        frame,
                        randomLongMessage(),
                        "Error Test",
                        NoxOptionPane.MessageType.ERROR
                )
        ));

        messageTypes.add(createButton("INFORMATION", e ->
                NoxOptionPane.showMessageDialog(
                        frame,
                        randomLongMessage(),
                        "Information Test",
                        NoxOptionPane.MessageType.INFORMATION
                )
        ));

        messageTypes.add(createButton("WARNING", e ->
                NoxOptionPane.showMessageDialog(
                        frame,
                        randomLongMessage(),
                        "Warning Test",
                        NoxOptionPane.MessageType.WARNING
                )
        ));

        messageTypes.add(createButton("QUESTION", e ->
                NoxOptionPane.showMessageDialog(
                        frame,
                        randomLongMessage(),
                        "Question Test",
                        NoxOptionPane.MessageType.QUESTION
                )
        ));

        messageTypes.add(createButton("PLAIN", e ->
                NoxOptionPane.showMessageDialog(
                        frame,
                        randomLongMessage(),
                        "Plain Test",
                        NoxOptionPane.MessageType.PLAIN
                )
        ));

        panel.add(messageTypes);

        JPanel optionTypes = createFlowPanel();

        optionTypes.add(createButton("DEFAULT", e ->
                showConfirm(frame, NoxOptionPane.OptionType.DEFAULT, NoxOptionPane.MessageType.PLAIN)
        ));

        optionTypes.add(createButton("YES_NO", e ->
                showConfirm(frame, NoxOptionPane.OptionType.YES_NO, NoxOptionPane.MessageType.QUESTION)
        ));

        optionTypes.add(createButton("YES_NO_CANCEL", e ->
                showConfirm(frame, NoxOptionPane.OptionType.YES_NO_CANCEL, NoxOptionPane.MessageType.WARNING)
        ));

        optionTypes.add(createButton("OK_CANCEL", e ->
                showConfirm(frame, NoxOptionPane.OptionType.OK_CANCEL, NoxOptionPane.MessageType.INFORMATION)
        ));

        panel.add(optionTypes);

        return createSection("NoxOptionPane", panel);
    }

    private static JPanel createScrollPaneBuilderSection() {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel(
                "Checks Nox.scrollPane().view().preferredSize().build() and custom scrollbars."
        ));

        JTextArea textArea = new JTextArea(createLongScrollText());
        textArea.setFont(NoxTheme.FONT_PLAIN);
        textArea.setForeground(NoxTheme.TEXT_PRIMARY);
        textArea.setCaretColor(NoxTheme.TEXT_PRIMARY);
        textArea.setBackground(NoxTheme.BG_SURFACE);
        textArea.setEditable(false);
        textArea.setLineWrap(false);

        NoxScrollPane scrollPane = Nox.scrollPane()
                .view(textArea)
                .preferredSize(760, 180)
                .build();

        panel.add(scrollPane);

        return createSection("NoxScrollPaneBuilder", panel);
    }

    private static JPanel createThemeSection() {
        JPanel panel = createRowsPanel();

        panel.add(createInfoLabel("Checks main colors and fonts from NoxTheme."));

        JPanel row = createFlowPanel();
        row.add(createColorBox("BG_PRIMARY", NoxTheme.BG_PRIMARY));
        row.add(createColorBox("BG_SURFACE", NoxTheme.BG_SURFACE));
        row.add(createColorBox("ACCENT", NoxTheme.ACCENT_PRIMARY));
        row.add(createColorBox("OUTLINE", NoxTheme.OUTLINE));
        row.add(createColorBox("ERROR", NoxTheme.ERROR));
        row.add(createColorBox("WARNING", NoxTheme.WARNING));
        row.add(createColorBox("SUCCESS", NoxTheme.SUCCESS));

        panel.add(row);

        return createSection("NoxTheme", panel);
    }

    private static void openFrameVariant(
            String title,
            boolean resizable,
            boolean maximizable,
            boolean minimizable,
            boolean closable
    ) {
        NoxNativeFrame frame = Nox.frame()
                .title(title)
                .size(480, 270)
                .minimumSize(360, 190)
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
                        "This frame was created with NoxFrameBuilder.</html>",
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
    }

    private static void openDialogVariant(
            NoxNativeFrame owner,
            String title,
            boolean modal,
            boolean resizable,
            boolean scrollable
    ) {
        NoxNativeDialog dialog = Nox.dialog(owner)
                .title(title)
                .modal(modal)
                .size(scrollable ? 540 : 440, scrollable ? 360 : 230)
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
    }

    private static void showConfirm(
            Component parent,
            NoxOptionPane.OptionType optionType,
            NoxOptionPane.MessageType messageType
    ) {
        NoxOptionPane.OptionResult result = NoxOptionPane.showConfirmDialog(
                parent,
                "Select one option to test result mapping.",
                "Option Test: " + optionType,
                optionType,
                messageType
        );

        updateStatus("Option result = " + result);
        log.info("OptionPane result for {}: {}", optionType, result);
    }

    private static NoxButton createButton(String text, java.awt.event.ActionListener actionListener) {
        return Nox.button(text)
                .preferredSize(BUTTON_SIZE.width, BUTTON_SIZE.height)
                .onClick(actionListener)
                .build();
    }

    private static JPanel createSection(String title, Component content) {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setOpaque(true);
        section.setBackground(NoxTheme.BG_SURFACE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NoxTheme.OUTLINE),
                new EmptyBorder(14, 14, 14, 14)
        ));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(createLabel(title, NoxTheme.FONT_BOLD), BorderLayout.NORTH);
        section.add(content, BorderLayout.CENTER);

        return section;
    }

    private static JPanel createRowsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
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

    private static JCheckBox createCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setFont(NoxTheme.FONT_PLAIN);
        checkBox.setForeground(NoxTheme.TEXT_PRIMARY);
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        return checkBox;
    }

    private static JPanel createColorBox(String name, Color color) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(120, 62));

        JPanel swatch = new JPanel();
        swatch.setBackground(color);
        swatch.setBorder(BorderFactory.createLineBorder(NoxTheme.OUTLINE));

        JLabel label = createLabel(name, NoxTheme.FONT_PLAIN_SMALL);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(swatch, BorderLayout.CENTER);
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

    private static String randomLongMessage() {
        String[] messages = {
                "A critical error occurred while loading data. Please try again later.",
                "The operation completed successfully. All changes have been saved.",
                "You are using an outdated version of the program. An update is recommended.",
                "Are you sure you want to permanently delete these files?",
                "This is a long test message. It checks wrapping, scrolling, icon rendering, modal behavior, " +
                        "default buttons and keyboard navigation inside NoxOptionPane."
        };

        return messages[new Random().nextInt(messages.length)];
    }

    private static String createLongScrollText() {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= 60; i++) {
            sb.append(i)
                    .append(". NoxScrollPane test line. ")
                    .append("This line is long enough to test horizontal scrolling: ")
                    .append("ABCDEFGHIJKLMNOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyz-0123456789")
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}