package service;

import com.InsertedMeeting;
import com.Meeting;
import com.MeetingService;
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
public class DBMeetingService extends UnicastRemoteObject implements MeetingService, Serializable {

    private static Logger LOGGER = Logger.getLogger(DBMeetingService.class.getSimpleName());

    private Database database;
    private User user;

    public DBMeetingService(Database database, User user) throws RemoteException {
        super(0);
        this.database = database;
        this.user = user;
    }

    @Override
    public Collection<InsertedMeeting> getMeetings() throws SQLException, RemoteException {
        return this.database.getMeetings(this.user.getID());
    }

    @Override
    public void createMeeting(Meeting meeting) throws SQLException, RemoteException {
        InsertedMeeting m  = this.database.createMeeting(meeting, this.user.getID());
        for(RemoteClient client: this.user.getNotificationService().getClients())
            client.onMeetingCreate(m);
    }

    @Override
    public void deleteMeeting(InsertedMeeting meeting) throws SQLException, RemoteException {
        InsertedMeeting m = this.database.deleteMeeting(meeting, this.user.getID());
        for(RemoteClient client: this.user.getNotificationService().getClients())
            client.onMeetingDelete(m);
    }

    @Override
    public void updateMeeting(InsertedMeeting meeting) throws SQLException, RemoteException {
        InsertedMeeting m = this.database.updateMeeting(meeting, this.user.getID());
        for(RemoteClient client: this.user.getNotificationService().getClients())
            client.onMeetingUpdate(m);
    }



}
