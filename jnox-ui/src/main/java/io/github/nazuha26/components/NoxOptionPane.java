package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;
import io.github.nazuha26.utils.IconManager;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * <table class="borderless">
 * <caption>Common dialog</caption>
 * <tr>
 *  <td style="background-color:#414245FF" rowspan=2>icon</td>
 *  <td style="background-color:#414245FF">message</td>
 * </tr>
 * <tr>
 *  <td style="background-color:#414245FF">input value</td>
 * </tr>
 * <tr>
 *   <td style="background-color:#414245FF" colspan=2>option buttons</td>
 * </tr>
 * </table>
 */
@Slf4j
public class NoxOptionPane {

    private static final int ICON_SIZE = 48;

    public enum MessageType {
        ERROR, INFORMATION, WARNING, QUESTION, PLAIN
    }

    public enum OptionType {
        DEFAULT, YES_NO, YES_NO_CANCEL, OK_CANCEL
    }

    public enum OptionResult {
        YES, NO, CANCEL, OK, CLOSED
    }

    /**
     * Shows a simple message dialog with a default title based on the message type.
     * @param parentComponent The parent component determining the frame in which the dialog is displayed. Can be null.
     * @param message         The string text to display in the dialog.
     * @param messageType     The type of message (determines the default title and icon).
     */
    public static void showMessageDialog(Component parentComponent, String message, MessageType messageType) {
        showMessageDialog(parentComponent, message, getDefaultTitle(messageType), messageType);
    }

    /**
     * Shows a simple message dialog with a specific title.
     * @param parentComponent The parent component determining the frame in which the dialog is displayed. Can be null.
     * @param message         The string text to display in the dialog.
     * @param title           The string to display in the dialog's title bar.
     * @param messageType     The type of message (determines the icon).
     */
    public static void showMessageDialog(Component parentComponent, String message, String title, MessageType messageType) {
        DialogRunner runner = new DialogRunner(parentComponent, message, title, OptionType.DEFAULT, messageType);
        runner.show();
    }

    /**
     * Shows a confirmation dialog with a default title, asking the user to select an option.
     * @param parentComponent The parent component determining the frame in which the dialog is displayed. Can be null.
     * @param message         The string text (question/prompt) to display.
     * @param optionType      An enum designating the set of option buttons to display (e.g., YES_NO, OK_CANCEL).
     * @param messageType     The type of message (determines the default title and icon).
     * @return An OptionResult indicating the option selected by the user (e.g., YES, NO, CANCEL, CLOSED).
     */
    public static OptionResult showConfirmDialog(Component parentComponent, String message, OptionType optionType, MessageType messageType) {
        return showConfirmDialog(parentComponent, message, getDefaultTitle(messageType), optionType, messageType);
    }

    /**
     * Shows a confirmation dialog with a specific title and option buttons.
     * @param parentComponent The parent component determining the frame in which the dialog is displayed. Can be null.
     * @param message         The string text (question/prompt) to display.
     * @param title           The string to display in the dialog's title bar.
     * @param optionType      An enum designating the set of option buttons to display (e.g., YES_NO_CANCEL).
     * @param messageType     The type of message (determines the icon).
     * @return An OptionResult indicating the option selected by the user (e.g., YES, NO, CANCEL, CLOSED).
     */
    public static OptionResult showConfirmDialog(Component parentComponent, String message, String title, OptionType optionType, MessageType messageType) {
        DialogRunner runner = new DialogRunner(parentComponent, message, title, optionType, messageType);
        return runner.show();
    }


    /**
     * An internal helper class responsible for building and displaying the dialog.
     * This class encapsulates the UI creation and event handling logic.
     */
    private static class DialogRunner {
        private OptionResult result = OptionResult.CLOSED;
        private final NoxNativeDialog dialog;

        public DialogRunner(Component parentComponent, String message, String title, OptionType optionType, MessageType messageType) {
            Window window = parentComponent == null ? JOptionPane.getRootFrame() : SwingUtilities.getWindowAncestor(parentComponent);
            Frame owner = window instanceof Frame ? (Frame) window : null;

            dialog = new NoxNativeDialog(owner, title, true);
            dialog.setResizable(false);

            JPanel body = dialog.getBody();
            body.setLayout(new BorderLayout(15, 15));
            body.setBorder(new EmptyBorder(15, 15, 15, 15));

            // Icon
            Icon icon = getIconForType(messageType);
            if (icon != null) {
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setVerticalAlignment(SwingConstants.TOP);
                body.add(iconLabel, BorderLayout.WEST);
            }

            // Text message
            JTextArea messageArea = new JTextArea(message);
            messageArea.setColumns(30);
            messageArea.setFont(NoxTheme.FONT_PLAIN);
            messageArea.setForeground(NoxTheme.TEXT_PRIMARY);
            messageArea.setBackground(NoxTheme.BG_PRIMARY);
            messageArea.setWrapStyleWord(true);
            messageArea.setLineWrap(true);
            messageArea.setEditable(false);
            messageArea.setFocusable(false);
            messageArea.setOpaque(true);

            // Wrap in JScrollPane
            NoxScrollPane scrollPane = new NoxScrollPane(messageArea);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            body.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);

            switch (optionType) {
                case DEFAULT -> {
                    NoxButton okButton = createButton("OK", OptionResult.OK);
                    buttonPanel.add(okButton);
                    dialog.getRootPane().setDefaultButton(okButton);
                }
                case YES_NO -> {
                    NoxButton yesButton = createButton("Yes", OptionResult.YES);
                    NoxButton noButton = createButton("No", OptionResult.NO);
                    buttonPanel.add(yesButton);
                    buttonPanel.add(noButton);
                    dialog.getRootPane().setDefaultButton(yesButton);
                }
                case YES_NO_CANCEL -> {
                    NoxButton yesButton = createButton("Yes", OptionResult.YES);
                    NoxButton noButton = createButton("No", OptionResult.NO);
                    NoxButton cancelButton = createButton("Cancel", OptionResult.CANCEL);
                    buttonPanel.add(yesButton);
                    buttonPanel.add(noButton);
                    buttonPanel.add(cancelButton);
                    dialog.getRootPane().setDefaultButton(yesButton);
                }
                case OK_CANCEL -> {
                    NoxButton okButton = createButton("OK", OptionResult.OK);
                    NoxButton cancelButton = createButton("Cancel", OptionResult.CANCEL);
                    buttonPanel.add(okButton);
                    buttonPanel.add(cancelButton);
                    dialog.getRootPane().setDefaultButton(okButton);
                }
            }

            setupArrowKeyNavigation(buttonPanel);

            body.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setMinimumSize(new Dimension(400, 200));
            dialog.pack();
            dialog.setLocationRelativeTo(window);
        }

        public OptionResult show() {
            dialog.setVisible(true);
            return this.result;
        }

        private NoxButton createButton(String text, OptionResult actionResult) {
            NoxButton btn = new NoxButton(text);
            btn.setPreferredSize(new Dimension(100, 32));
            btn.addActionListener(e -> {
                this.result = actionResult;
                dialog.dispose();
            });
            return btn;
        }

        /**
         * Set focus to the next component using the left and right arrow keys.
         *
         * @apiNote You need to press 'Space' to trigger a button action,
         * because by pressing 'Enter' swing triggers default button.
         */
        private void setupArrowKeyNavigation(JPanel panel) {
            InputMap inputMap = panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            ActionMap actionMap = panel.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "focusNext");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "focusPrevious");

            actionMap.put("focusNext", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                }
            });

            actionMap.put("focusPrevious", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                }
            });
        }
    }



    private static String getDefaultTitle(MessageType messageType) {
        return switch (messageType) {
            case ERROR -> "Error";
            case INFORMATION -> "Information";
            case WARNING -> "Warning";
            case QUESTION -> "Question";
            case PLAIN -> "Message";
        };
    }

    private static Icon getIconForType(MessageType messageType) {
        if (messageType == MessageType.PLAIN) {
            return null;
        }

        String path = switch (messageType) {
            case ERROR -> "/icons/svg/notifications/error.svg";
            case INFORMATION -> "/icons/svg/notifications/info.svg";
            case WARNING -> "/icons/svg/notifications/warning.svg";
            case QUESTION -> "/icons/svg/notifications/question.svg";
            default -> throw new IllegalStateException("Unexpected value: " + messageType);
        };

        Icon swingFallback = UIManager.getIcon("OptionPane." + getFallbackString(messageType) + "Icon");
        return IconManager.getSvgIcon(path, ICON_SIZE, ICON_SIZE, swingFallback);
    }

    private static String getFallbackString(MessageType messageType) {
        return switch (messageType) {
            case ERROR -> "error";
            case INFORMATION -> "information";
            case WARNING -> "warning";
            case QUESTION -> "question";
            default -> "";
        };
    }
}