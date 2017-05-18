package com;

import java.rmi.Remote;
import java.rmi.RemoteException;

@SuppressWarnings("all")
public interface Server extends Remote {

    MeetingService unlockMeetingService(Key key) throws RemoteException;

    NotificationService unlockNotificationService(Key key) throws RemoteException;

    PersonService unlockPersonService(Key key) throws RemoteException;

    LabelService unlockLabelService(Key key) throws RemoteException;

    Key login(String username, String password) throws RemoteException;

}