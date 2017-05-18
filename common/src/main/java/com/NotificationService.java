package com;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

@SuppressWarnings("all")
public interface NotificationService extends Remote {

    void registerClient(RemoteClient client) throws RemoteException;

    void deregisterClient(RemoteClient client) throws RemoteException;

    boolean isRegistered(RemoteClient client) throws RemoteException;

    Collection<RemoteClient> getClients() throws RemoteException;
}
