package service;

import com.InsertedMeeting;
import com.LabelService;
import com.Meeting;
import com.RemoteClient;
import db.Database;
import server.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class DBLabelService extends UnicastRemoteObject implements LabelService, Serializable {


    private static Logger LOGGER = Logger.getLogger(DBLabelService.class.getSimpleName());

    private Database database;
    private User user;

    public DBLabelService(Database database, User user) throws RemoteException {
        super(0);
        this.database = database;
        this.user = user;
    }

    @Override
    public Collection<InsertedMeeting.Label> getLabels() throws SQLException, RemoteException {
        return this.database.getLabels(user.getID());
    }

    @Override
    public void createLabel(Meeting.Label label) throws SQLException, RemoteException {
        InsertedMeeting.Label lbl = this.database.createMeetingLabel(label, user.getID());
        for(RemoteClient client: this.user.getNotificationService().getClients())
            client.onMeetingLabelCreate(lbl);
    }

    @Override
    public void deleteLabel(InsertedMeeting.Label label) throws SQLException, RemoteException {
        InsertedMeeting.Label lbl = this.database.deleteMeetingLabel(label, user.getID());
        for(RemoteClient client: this.user.getNotificationService().getClients())
            client.onMeetingLabelDelete(lbl);
    }

    @Override
    public void updateLabel(InsertedMeeting.Label label) throws SQLException, RemoteException {
        InsertedMeeting.Label lbl = this.database.updateMeetingLabel(label, user.getID());
        for(RemoteClient client: this.user.getNotificationService().getClients())
            client.onMeetingLabelUpdate(lbl);
    }
}
