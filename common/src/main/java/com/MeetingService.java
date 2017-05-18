package com;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

@SuppressWarnings("all")
public interface MeetingService extends Remote {

    Collection<InsertedMeeting> getMeetings() throws SQLException, RemoteException;

    void createMeeting(Meeting meeting) throws SQLException, RemoteException;

    void deleteMeeting(InsertedMeeting meeting) throws SQLException, RemoteException;

    void updateMeeting(InsertedMeeting meeting) throws SQLException, RemoteException;

}
