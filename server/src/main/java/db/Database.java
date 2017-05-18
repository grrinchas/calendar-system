package db;

import com.InsertedMeeting;
import com.Meeting;
import com.Person;

import java.awt.*;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for accessing database.
 */
@SuppressWarnings("all")
public final class Database {

    private static Logger LOGGER = Logger.getLogger(Database.class.getSimpleName());

    // database driver
    private static String DEFAULT_JDBC_DRIVER = "org.sqlite.JDBC";

    // absolute path of the project
    private static String DEFAULT_DIRECTORY = System.getProperty("user.dir");

    // default relative path of the database (this should be appened to the DEFAULT_DIRECTORY)
    private static String DEFAULT_JDBC_URL = "\\server\\src\\main\\resources\\sqlite.db";

    // database URL
    private String url = "jdbc:sqlite:" + DEFAULT_DIRECTORY;

    public Database(String url) {
        this.url = this.url + url;
    }

    public Database() {
        this(DEFAULT_JDBC_URL);
    }

    public Collection<InsertedMeeting> getMeetings(int userID) throws SQLException {

        Collection<InsertedMeeting> meetings = new HashSet<>();
        Connection connection = DriverManager.getConnection(this.url);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                "SELECT MEETING.* FROM MEETING INNER JOIN userMeeting ON MEETING.MEETING_ID = userMeeting" + "" + ""
                    + ".MEETING_ID INNER JOIN USER ON userMeeting.USER_ID = USER.USER_ID WHERE (ISDELETED = 0 AND" +
                    " " + "" + "" + "" + "USER.USER_ID = ? )");

            statement.setInt(1, userID);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int meetingID = result.getInt("MEETING_ID");
                Instant start = result.getTimestamp("START_TIME").toInstant();
                Instant end = result.getTimestamp("FINISH_TIME").toInstant();
                String title = result.getString("TITLE");
                String description = result.getString("DESCRIPTION");
                String location = result.getString("LOCATION");
                meetings.add(new InsertedMeeting(
                    new Meeting(title, start, end, description, location, new HashSet<InsertedMeeting.Label>()), meetingID));
            }

            for (InsertedMeeting m : meetings) {
                statement = connection.prepareStatement(
                    "SELECT label.* FROM label INNER JOIN meetingLabel ON label.LABEL_ID = meetingLabel.LABEL_ID " +
                        "WHERE meetingLabel.MEETING_ID = ? AND label.USER_ID = ?");
                statement.setInt(1, m.getID());
                statement.setInt(2, userID);

                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    int labelID = set.getInt("LABEL_ID");
                    String name = set.getString("NAME");
                    Color colour = Color.decode(set.getString("COLOUR"));
                    boolean active = set.getInt("ACTIVE") == 1 ? true : false;
                    InsertedMeeting.Label label = new InsertedMeeting.Label(new Meeting.Label(name, colour, active), labelID);
                    m.getLabels().add(label);
                }
            }


        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [SELECT * FROM MEETING]");

        return meetings;
    }

    public List<Person> findPersons(int userID, String criteria) throws SQLException {

        List<Person> persons = new ArrayList<>();
        Connection connection = DriverManager.getConnection(this.url);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT * FROM USER WHERE USERNAME LIKE ?");
            statement.setString(1, "%" + criteria + "%");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                if (result.getInt("USER_ID") != userID) {
                    persons.add(new Person(result.getString("USERNAME")));
                }
            }

        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [SELECT * FROM USER]");

        return persons;
    }


    public int getUserID(String username, String password) throws SQLException {
        int userID = -1;

        Connection connection = DriverManager.getConnection(this.url);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT USER_ID FROM USER WHERE USERNAME =? AND PASSWORD = ?");
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            while (result.next())
                userID = result.getInt("USER_ID");

        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [SELECT USER_ID]");

        return userID;
    }


    /**
     * Helper method which closes resources. Should be called in finally clause.
     *
     * @param stmt to be closed
     * @param con  connection to be closed
     * @throws SQLException
     */
    private void closeResources(Statement stmt, Connection con) throws SQLException {

        if (stmt != null)
            stmt.close();
        if (con != null)
            con.close();
    }


    public InsertedMeeting createMeeting(Meeting meeting, int userID) throws SQLException {

        Connection connection = DriverManager.getConnection(this.url);

        PreparedStatement statement = null;
        InsertedMeeting insertedMeeting = null;

        try {
            statement = connection.prepareStatement(
                "INSERT INTO meeting (START_TIME, FINISH_TIME, TITLE, DESCRIPTION, LOCATION, ISDELETED) VALUES " + ""
                    + "(?, ?, ?, ?, ?, 0)");

            statement.setTimestamp(1, Timestamp.from(meeting.getStart()));
            statement.setTimestamp(2, Timestamp.from(meeting.getEnd()));
            statement.setString(3, meeting.getTitle());
            statement.setString(4, meeting.getDescription());
            statement.setString(5, meeting.getLocation());
            statement.executeUpdate();
            int meetingID = statement.getGeneratedKeys().getInt(1);

            statement = connection.prepareStatement("INSERT INTO userMeeting ( MEETING_ID,  USER_ID ) VALUES(?, ?)");
            statement.setInt(1, meetingID);
            statement.setInt(2, userID);
            statement.execute();

            statement = connection.prepareStatement("INSERT INTO meetingLabel (MEETING_ID, LABEL_ID) VALUES(?, ?)");
            for (InsertedMeeting.Label label : meeting.getLabels()) {
                statement.setInt(1, meetingID);
                statement.setInt(2, label.getID());
                statement.execute();
            }

            insertedMeeting = new InsertedMeeting(meeting, meetingID);

        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [INSERT INTO MEETING] " + meeting.getStart());
        return insertedMeeting;
    }

    public InsertedMeeting deleteMeeting(InsertedMeeting meeting, int UserID) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url);

        PreparedStatement statement = null;
        InsertedMeeting deletedMeeting = null;

        try {
            statement = connection.prepareStatement("DELETE FROM userMeeting WHERE MEETING_ID = ?");
            statement.setInt(1, meeting.getID());
            statement.execute();

            statement = connection.prepareStatement("DELETE FROM meetingLabel WHERE MEETING_ID = ?");
            statement.setInt(1, meeting.getID());
            statement.execute();

            statement = connection.prepareStatement("UPDATE MEETING SET ISDELETED = 1 WHERE MEETING_ID = ? ");
            statement.setInt(1, meeting.getID());
            statement.execute();

            deletedMeeting = meeting;

        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [DELETE MEETING WHERE ID=ID]");

        return deletedMeeting;

    }

    public InsertedMeeting updateMeeting(InsertedMeeting meeting, int userID) throws SQLException {

        Connection connection = DriverManager.getConnection(this.url);

        PreparedStatement statement = null;
        InsertedMeeting updatedMeeting = null;

        try {
            statement = connection.prepareStatement(
                "UPDATE MEETING SET START_TIME = ?, FINISH_TIME = ?, TITLE = ?, DESCRIPTION = ?, LOCATION = ? " +
                    "WHERE MEETING_ID = ? ");
            statement.setTimestamp(1, Timestamp.from(meeting.getStart()));
            statement.setTimestamp(2, Timestamp.from(meeting.getEnd()));
            statement.setString(3, meeting.getTitle());
            statement.setString(4, meeting.getDescription());
            statement.setString(5, meeting.getLocation());
            statement.setInt(6, meeting.getID());
            statement.execute();

            statement = connection.prepareStatement("DELETE FROM meetingLabel WHERE MEETING_ID = ?");
            statement.setInt(1, meeting.getID());
            statement.execute();

            statement = connection.prepareStatement("INSERT INTO meetingLabel (MEETING_ID, LABEL_ID ) VALUES (?, ?)");
            for (InsertedMeeting.Label l : meeting.getLabels()) {
                statement.setInt(1, meeting.getID());
                statement.setInt(2, l.getID());
                statement.execute();
            }

            updatedMeeting = meeting;

        } finally {
            this.closeResources(statement, connection);

            LOGGER.log(Level.INFO, "Database: query was successful [UPDATE MEETING WHERE ID=ID]");
        }
        return updatedMeeting;
    }

    public Collection<InsertedMeeting.Label> getLabels(int userID) throws SQLException {

        Collection<InsertedMeeting.Label> labels = new ArrayList<>();
        Connection connection = DriverManager.getConnection(this.url);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT * FROM label WHERE USER_ID = ?");
            statement.setInt(1, userID);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("LABEL_ID");
                String name = result.getString("NAME");
                Color colour = Color.decode(result.getString("COLOUR"));
                boolean active = result.getInt("ACTIVE") == 1 ? true : false;
                InsertedMeeting.Label label = new InsertedMeeting.Label(new Meeting.Label(name, colour, active), id);
                labels.add(label);
            }

        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [SELECT * FROM LABEL]");

        return labels;
    }


    public InsertedMeeting.Label createMeetingLabel(Meeting.Label label, int userID) throws SQLException {

        InsertedMeeting.Label insertedLabel = null;
        Connection connection = DriverManager.getConnection(this.url);
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                "INSERT INTO label (NAME, COLOUR, USER_ID, ACTIVE) VALUES (?, ?, ?, 1)");
            statement.setString(1, label.getName());
            statement.setString(2, "#" + Integer.toHexString(label.getColor().getRGB()).substring(2));
            statement.setInt(3, userID);
            statement.executeUpdate();
            insertedLabel = new InsertedMeeting.Label(label, statement.getGeneratedKeys().getInt(1));

        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [INSERT INTO label] ");
        return insertedLabel;
    }

    public InsertedMeeting.Label deleteMeetingLabel(InsertedMeeting.Label label, int UserID) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url);
        PreparedStatement statement = null;
        InsertedMeeting.Label deletedLabel = null;

        try {

            statement = connection.prepareStatement("DELETE FROM meetingLabel WHERE LABEL_ID = ?");
            statement.setInt(1, label.getID());
            statement.execute();

            statement = connection.prepareStatement("DELETE FROM label WHERE LABEL_ID = ? ");
            statement.setInt(1, label.getID());
            statement.execute();

            deletedLabel = label;

        } finally {
            this.closeResources(statement, connection);
        }

        LOGGER.log(Level.INFO, "Database: query was successful [DELETE label WHERE ID= " + label.getID() + "]");

       return deletedLabel;
    }

    public InsertedMeeting.Label updateMeetingLabel(InsertedMeeting.Label label, int userID) throws SQLException {

        Connection connection = DriverManager.getConnection(this.url);
        PreparedStatement statement = null;
        InsertedMeeting.Label updatedLabel = null;

        try {
            statement = connection.prepareStatement(
                "UPDATE label SET NAME = ?, COLOUR = ?, ACTIVE = ? WHERE LABEL_ID = ? ");

            statement.setString(1, label.getName());
            statement.setString(2, "#" + Integer.toHexString(label.getColor().getRGB()).substring(2));
            statement.setInt(3, label.isActive() ? 1 : 0);
            statement.setInt(4, label.getID());
            statement.execute();
            updatedLabel = label;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResources(statement, connection);
        }
        LOGGER.log(Level.INFO, "Database: query was successful [UPDATE label WHERE ID= " + label.getID() + "]");
        return updatedLabel;
    }
}
