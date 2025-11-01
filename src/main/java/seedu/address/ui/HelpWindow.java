package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-w11-1.github.io/tp/UserGuide.html";
    public static final String COMMAND_SYNTAX = "here is the list of all command syntax:\n"
            + "- add_student: add_student n/NAME i/NUSNETID t/TELEGRAM g/GROUPID [p/PHONE] [e/EMAIL]\n"
            + "- edit_student: edit_student INDEX [n/NAME] [i/NUSNETID] [t/TELEGRAM] [g/GROUPID] [p/PHONE] [e/EMAIL]\n"
            + "- delete: delete INDEX\n"
            + "- clear: clear\n"
            + "- find: find KEYWORD \n"
            + "- list: list\n"
            + "- help: help\n"
            + "- exit: exit\n"
            + "- add_hw: add_hw i/NETID a/ASSIGNMENT_ID or add_hw i/all a/ASSIGNMENT_ID\n"
            + "- mark_hw: mark_hw i/NUSNETID a/ASSIGNMENT_ID status/complete|incomplete|late\n"
            + "- delete_hw: delete_hw i/NUSNETID a/ASSIGNMENT_ID or delete_hw i/all a/ASSIGNMENT_ID\n"
            + "- mark_attendance: mark_attendance i/NUSNETID w/WEEK status/ATTENDANCE_STATUS(present|absent|excused)\n"
            + "- mark_all_attendance: mark_all_attendance g/GROUP w/WEEK "
            + "status/ATTENDANCE_STATUS(present|absent|excused)\n"
            + "- add_consult: add_consult i/NUSNETID from/START_TIME to/END_TIME\n"
            + "- delete_consult: delete_consult i/NUSNETID\n"
            + "- list_consult: list_consult\n"
            + "- create_group: create_group g/GROUPID\n"
            + "- add_to_group: add_to_group i/NUSNETID g/GROUPID\n"
            + "-find_group: find_group g/GROUPID\n";

    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL + "\n" + COMMAND_SYNTAX;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
