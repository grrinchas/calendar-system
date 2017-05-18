package service;

import com.NotificationService;
import com.RemoteClient;
import server.Server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class ClientUpdateService extends UnicastRemoteObject implements NotificationService, Serializable {

    private static Logger LOGGER = Logger.getLogger(Server.class.getSimpleName());

    private Collection<RemoteClient> clients;

    public ClientUpdateService() throws RemoteException {
        super(0);
        this.clients = new HashSet<>();
    }

    @Override
    public void registerClient(RemoteClient client) throws RemoteException {
        this.clients.add(client);
        LOGGER.log(Level.INFO, "ClientUpdateService: client has been registered");
    }

    @Override
    public void deregisterClient(RemoteClient client) throws RemoteException {
        this.clients.remove(client);
        LOGGER.log(Level.INFO, "ClientUpdateService: client has been deregistered");
    }

    @Override
    public boolean isRegistered(RemoteClient client) throws RemoteException {
        return this.clients.contains(client);
    }

    @Override
    public Collection<RemoteClient> getClients() {
        return this.clients;
    }
}
