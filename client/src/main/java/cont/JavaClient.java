package cont;

import com.InsertedMeeting;
import com.Meeting;
import com.RemoteClient;
import model.Cache;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * A client which communicates with the server.
 */
@SuppressWarnings("all")
public class JavaClient extends UnicastRemoteObject implements RemoteClient, Serializable {

    private Cache cache;

    public JavaClient(Cache cache) throws RemoteException {
        super(0);
        this.cache = cache;
    }

    @Override
    public void onMeetingUpdate(InsertedMeeting meeting) throws RemoteException {
        this.cache.updateMeeting(meeting);
    }

    @Override
    public void onMeetingDelete(InsertedMeeting meeting) throws RemoteException {
        this.cache.removeMeeting(meeting);
    }

    @Override
    public void onMeetingCreate(InsertedMeeting meeting) throws RemoteException {
        this.cache.addMeeting(meeting);
    }

    @Override
    public void onMeetingLabelUpdate(InsertedMeeting.Label label) throws RemoteException {
        this.cache.updateMeetingLabel(label);
    }

    @Override
    public void onMeetingLabelDelete(InsertedMeeting.Label label) throws RemoteException {
        this.cache.removeMeetingLabel(label);
    }

    @Override
    public void onMeetingLabelCreate(InsertedMeeting.Label label) throws RemoteException {
       this.cache.addMeetingLabel(label);
    }

}



