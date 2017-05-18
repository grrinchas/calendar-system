package com;

import java.rmi.Remote;
import java.rmi.RemoteException;

@SuppressWarnings("all")
public interface RemoteClient extends Remote {

    void onMeetingUpdate(InsertedMeeting meeting) throws RemoteException;

    void onMeetingDelete(InsertedMeeting meeting) throws RemoteException;

    void onMeetingCreate(InsertedMeeting meeting) throws RemoteException;

    void onMeetingLabelUpdate(InsertedMeeting.Label label) throws RemoteException;

    void onMeetingLabelDelete(InsertedMeeting.Label label) throws RemoteException;

    void onMeetingLabelCreate(InsertedMeeting.Label label) throws RemoteException;
}
