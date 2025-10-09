package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label nusnetid;
    @FXML
    private Label email;
    @FXML
    private Label telegram;
    @FXML
    private Label slot;
    @FXML
    private VBox homeworkContainer;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        nusnetid.setText(person.getNusnetid().value);
        email.setText(person.getEmail().value);
        telegram.setText(person.getTelegram().value);
        slot.setText(person.getSlot().value);
        showHomework();
    }

    private void showHomework() {
        if (person.getHomeworkTracker() == null) {
            return;
        }

        // If no homework exists yet
        if (person.getHomeworkTracker().asMap().isEmpty()) {
            Label placeholder = new Label("No homework yet");
            placeholder.setStyle("-fx-font-size: 11px; -fx-font-style: italic; -fx-text-fill: #666;");
            homeworkContainer.getChildren().add(placeholder);
            return;
        }

        person.getHomeworkTracker().asMap().forEach((assignmentId, status) -> {
            Label hwLabel = new Label("HW" + assignmentId + ": " + status);
            hwLabel.setStyle("-fx-font-size: 11px;"); // optional styling
            homeworkContainer.getChildren().add(hwLabel);
        });
    }
}
