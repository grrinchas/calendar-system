package model;

import com.InsertedMeeting;
import com.LabelService;
import com.Meeting;
import com.MeetingService;
import cont.CacheListener;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("all")
public class Cache {

    private Set<CacheListener> listeners;
    private Set<InsertedMeeting> meetings;
    private Set<InsertedMeeting.Label> meetingLabels;
    private MeetingService meetingService;
    private LabelService labelService;

    public Cache(MeetingService meetingService, LabelService labelService) {
        this.meetingService = meetingService;
        this.labelService = labelService;
        this.listeners = new HashSet<>();
        this.meetings = new HashSet<>();
        this.meetingLabels = new HashSet<>();
        this.refreshMeetings();
        this.refreshLabels();
    }

    public void addCacheListener(CacheListener listener) {
        this.listeners.add(listener);
    }

    public Collection<InsertedMeeting> produceMeetings() {
        return this.meetings;
    }

    public Collection<InsertedMeeting.Label> produceLabels() {
        return this.meetingLabels;
    }

    public void updateMeeting(InsertedMeeting meeting) {
        this.refreshMeetings();
        this.listeners.forEach(l -> l.onMeetingChange(this));
    }

    public void removeMeeting(InsertedMeeting meeting) {
        this.refreshMeetings();
        this.listeners.forEach(l -> l.onMeetingChange(this));
    }

    public void addMeeting(InsertedMeeting meeting) {
        this.refreshMeetings();
        this.listeners.forEach(l -> l.onMeetingChange(this));
    }

    public void updateMeetingLabel(InsertedMeeting.Label label) {
        this.refreshLabels();
        this.refreshMeetings();
        this.listeners.forEach(l -> l.onLabelChange(this));
    }

    public void removeMeetingLabel(InsertedMeeting.Label label) {
        this.refreshLabels();
        this.refreshMeetings();
        this.listeners.forEach(l -> l.onLabelChange(this));
    }

    public void addMeetingLabel(InsertedMeeting.Label label) {
        this.refreshLabels();
        this.refreshMeetings();
        this.listeners.forEach(l -> l.onLabelChange(this));
    }

    private void refreshMeetings() {
        this.meetings.clear();

        try {
            this.meetings.addAll(meetingService.getMeetings());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void refreshLabels() {
        this.meetingLabels.clear();

        try {
            this.meetingLabels.addAll(labelService.getLabels());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
