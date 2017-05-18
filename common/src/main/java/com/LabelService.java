package com;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

@SuppressWarnings("all")
public interface LabelService extends Remote{

    Collection<InsertedMeeting.Label> getLabels() throws SQLException, RemoteException;

    void createLabel(Meeting.Label label) throws SQLException, RemoteException;

    void deleteLabel(InsertedMeeting.Label label) throws SQLException, RemoteException;

    void updateLabel(InsertedMeeting.Label label) throws SQLException, RemoteException;
}
