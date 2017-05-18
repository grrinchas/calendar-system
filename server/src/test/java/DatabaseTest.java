import com.InsertedMeeting;
import com.Meeting;
import db.Database;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.time.Instant;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings("all")
public class DatabaseTest {

    private Database database;

    @Before
    public void setUp() throws Exception {
        this.database = new Database("\\src\\main\\resources\\sqlite.db");
    }

    @Test
    public void getMeetingsTest() throws Exception {
        assertThat(this.database.getMeetings(1)).isNotEmpty();
    }

    @Test
    public void createMeetingTest() throws Exception {

        int userID = 1;

        // first lets create a meeting
        Meeting meeting = new Meeting("Test", Instant.now(), Instant.now(), "Description", "Location", new HashSet<>());

        // and assert that database does not contain it
        assertThat(this.database.getMeetings(userID).stream().map(InsertedMeeting::getMeeting)).doesNotContain(meeting);

        // then insert a meeting into database
        InsertedMeeting insertedMeeting = this.database.createMeeting(meeting, userID);

        // and assert that it contains it
        assertThat(this.database.getMeetings(userID)).containsOnlyOnce(insertedMeeting);

        // clean after test
        this.database.deleteMeeting(insertedMeeting, userID);
    }

    @Test
    public void deleteMeetingTest() throws Exception {

        int userID = 1;

        // first lets prepare database for delete test
        Meeting meeting = new Meeting("Test", Instant.now(), Instant.now(), "Description", "Location", new HashSet<>());
        InsertedMeeting insertedMeeting = this.database.createMeeting(meeting, userID);

        // make sure that database contains a meeting
        assertThat(this.database.getMeetings(userID)).containsOnlyOnce(insertedMeeting);

        // then delete inserted meeting from database
        InsertedMeeting deletedMeeting = this.database.deleteMeeting(insertedMeeting, userID);

        // assert that inserted and deleted meetings are the same
        assertThat(insertedMeeting).isEqualTo(deletedMeeting);

        // and that database does not contain deleted meeting
        assertThat(this.database.getMeetings(userID)).doesNotContain(deletedMeeting);
    }

    @Test
    public void updateMeetingTest() throws Exception {

        int userID = 1;

        // first lets prepare database for the test
        Meeting meeting = new Meeting("Test", Instant.now(), Instant.now(), "Description", "Location", new HashSet<>());
        InsertedMeeting insertedMeeting = this.database.createMeeting(meeting, userID);
        assertThat(this.database.getMeetings(userID)).containsOnlyOnce(insertedMeeting);

        // create new properties of the meeting
        String newTitle = "New Title";
        String newDescription = "New Description";
        String newLocation = "New Location";
        Instant newStart = Instant.now().plusSeconds(100);
        Instant newEnd = Instant.now().plusSeconds(100);
        InsertedMeeting.Label newLabel = new InsertedMeeting.Label(new Meeting.Label("Label", Color.YELLOW, true), 1);

        // assert that all the properties are indeed new
        assertThat(insertedMeeting.getTitle()).isNotEqualTo(newTitle);
        assertThat(insertedMeeting.getDescription()).isNotEqualTo(newDescription);
        assertThat(insertedMeeting.getLocation()).isNotEqualTo(newLocation);
        assertThat(insertedMeeting.getStart()).isNotEqualTo(newStart);
        assertThat(insertedMeeting.getEnd()).isNotEqualTo(newEnd);
        assertThat(insertedMeeting.getLabels()).doesNotContain(newLabel);

        // update inserted meeting with new properties
        insertedMeeting.setTitle(newTitle);
        insertedMeeting.setDescription(newDescription);
        insertedMeeting.setLocation(newLocation);
        insertedMeeting.setStart(newStart);
        insertedMeeting.setEnd(newEnd);
        insertedMeeting.getLabels().add(newLabel);
        InsertedMeeting updatedMeeting = this.database.updateMeeting(insertedMeeting, userID);

        // first assert that updated meeting is the same as inserted one
        assertThat(insertedMeeting).isEqualTo(updatedMeeting);

        // and assert that properties have been indeed updated
        assertThat(updatedMeeting.getTitle()).isEqualTo(newTitle);
        assertThat(updatedMeeting.getDescription()).isEqualTo(newDescription);
        assertThat(updatedMeeting.getLocation()).isEqualTo(newLocation);
        assertThat(updatedMeeting.getStart()).isEqualTo(newStart);
        assertThat(updatedMeeting.getEnd()).isEqualTo(newEnd);
        assertThat(updatedMeeting.getLabels()).containsOnlyOnce(newLabel);

        // clean after test
        this.database.deleteMeeting(insertedMeeting, userID);
    }

    @Test
    public void getMeetingLabels() throws Exception {
        assertThat(this.database.getLabels(1)).isNotEmpty();
    }

    @Test
    public void createLabelTest() throws Exception {

        int userID = 1;

        // first lets create a label
        Meeting.Label label = new Meeting.Label("Label", Color.YELLOW, true);

        // and assert that database does not contain it
        assertThat(this.database.getLabels(userID).stream().map(InsertedMeeting.Label::getLabel)).doesNotContain(label);

        // then insert a label into database
        InsertedMeeting.Label insertedLabel = this.database.createMeetingLabel(label, userID);

        // and assert that it contains it
        assertThat(this.database.getLabels(userID)).containsOnlyOnce(insertedLabel);

        // clean after test
        this.database.deleteMeetingLabel(insertedLabel, userID);
    }

    @Test
    public void deleteLabelTest() throws Exception {

        int userID = 1;

        // first lets prepare database for delete test
        Meeting.Label label = new Meeting.Label("Label", Color.YELLOW, true);
        InsertedMeeting.Label insertedLabel = this.database.createMeetingLabel(label, userID);

        // make sure that database contains a label
        assertThat(this.database.getLabels(userID)).containsOnlyOnce(insertedLabel);

        // then delete inserted label from database
        InsertedMeeting.Label deletedLabel = this.database.deleteMeetingLabel(insertedLabel, userID);

        // assert that inserted and deleted labels are the same
        assertThat(insertedLabel).isEqualTo(deletedLabel);

        // and that database does not contain deleted label
        assertThat(this.database.getLabels(userID)).doesNotContain(deletedLabel);
    }
    @Test
    public void updateLabelTest() throws Exception {

        int userID = 1;

        // first lets prepare database for the test
        Meeting.Label label = new Meeting.Label("Label", Color.YELLOW, true);
        InsertedMeeting.Label insertedLabel = this.database.createMeetingLabel(label, userID);
        assertThat(this.database.getLabels(userID)).containsOnlyOnce(insertedLabel);

        // create new properties of the label
        String newName = "New Name";
        Color newColor = Color.GREEN;
        boolean newActive = false;

        // assert that all the properties are indeed new
        assertThat(insertedLabel.getName()).isNotEqualTo(newName);
        assertThat(insertedLabel.getColor()).isNotEqualTo(newColor);
        assertThat(insertedLabel.isActive()).isNotEqualTo(newActive);

        // update inserted label with new properties
        insertedLabel.setName(newName);
        insertedLabel.setColor(newColor);
        insertedLabel.setActive(newActive);
        InsertedMeeting.Label updatedLabel = this.database.updateMeetingLabel(insertedLabel, userID);

        // first assert that updated label is the same as inserted one
        assertThat(insertedLabel).isEqualTo(updatedLabel);

        // and assert that properties have been indeed updated
        assertThat(updatedLabel.getName()).isEqualTo(newName);
        assertThat(updatedLabel.getColor()).isEqualTo(newColor);
        assertThat(updatedLabel.isActive()).isEqualTo(newActive);

        // clean after test
        this.database.deleteMeetingLabel(insertedLabel, userID);
    }

    @Test
    public void findPersons() throws Exception {
        assertThat(this.database.findPersons(1, "man")).isNotEmpty();
    }


}