package server;

import com.*;
import db.Database;
import service.ClientUpdateService;
import service.DBLabelService;
import service.DBMeetingService;
import service.DBPersonService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("all")
public final class Server extends UnicastRemoteObject implements com.Server {

    private static Logger LOGGER = Logger.getLogger(Server.class.getSimpleName());

    // list of users
    private Collection<User> users;

    // register which holds reference to this server
    private Registry registry;

    // port by which clients cand find server
    private int PORT;

    // server name by which clients can identify it in the server
    private String name;

    // database to get data from
    private Database database;

    /**
     * Constructs Server with port and a name
     *
     * @param port
     * @param name
     * @throws RemoteException
     */
    public Server(int port, String name) throws RemoteException {
        this.PORT = port;
        this.name = name;
        this.database = new Database();
        this.users = new HashSet<>();
    }

    /**
     * Starts the Server. Clients which want to connect remotely to this server, has to look up
     * it in the registry, by provided name and the port.
     */
    public void start() {

        try {
            registry = LocateRegistry.createRegistry(this.PORT);
            registry.rebind(this.name, new Server(this.PORT, this.name));
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.log(Level.INFO, "Server [" + this.name + "]: started on port [" + this.PORT + "]");
    }

    @Override
    public MeetingService unlockMeetingService(Key key) throws RemoteException {
        MeetingService meetingService = null;

        Optional<User> user = this.users.stream().filter(u -> u.getKey().equals(key)).findFirst();
        if (user.isPresent()) {
            if (user.get().getMeetingService() == null) {
                meetingService = new DBMeetingService(this.database, user.get());
                user.get().setMeetingService(meetingService);
            } else {
                meetingService = user.get().getMeetingService();
            }
            LOGGER.log(Level.INFO, "Server: user [" + user.get().getID() + "] unlocked meeting service");
        } else
            LOGGER.log(Level.SEVERE, "Server: user [" + user.get().getID() + "] key failed to unlock meeting service");
        return meetingService;
    }

    @Override
    public NotificationService unlockNotificationService(Key key) throws RemoteException {
        NotificationService notificationService = null;

        Optional<User> user = this.users.stream().filter(u -> u.getKey().equals(key)).findFirst();
        if (user.isPresent()) {
            if (user.get().getNotificationService() != null) {
                notificationService = user.get().getNotificationService();
            } else if (user.get().getMeetingService() != null) {
                notificationService = new ClientUpdateService();
                user.get().setNotificationService(notificationService);
            }
            LOGGER.log(Level.INFO, "Server: user [" + user.get().getID() + "] unlocked meeting service");
        } else
            LOGGER.log(Level.SEVERE, "Server: user [" + user.get().getID() + "] key failed to unlock meeting service");
        return notificationService;
    }

    @Override
    public PersonService unlockPersonService(Key key) throws RemoteException {
        PersonService personService = null;

        Optional<User> user = this.users.stream().filter(u -> u.getKey().equals(key)).findFirst();
        if (user.isPresent()) {
            if (user.get().getPersonService() == null) {
                personService = new DBPersonService(this.database, user.get());
                user.get().setPersonService(personService);
            } else {
                personService = user.get().getPersonService();
            }
            LOGGER.log(Level.INFO, "Server: user [" + user.get().getID() + "] unlocked person service");
        } else
            LOGGER.log(Level.SEVERE, "Server: user [" + user.get().getID() + "] key failed to unlock person service");
        return personService;
    }

    @Override
    public LabelService unlockLabelService(Key key) throws RemoteException {
        LabelService labelService = null;

        Optional<User> user = this.users.stream().filter(u -> u.getKey().equals(key)).findFirst();
        if (user.isPresent()) {
            if (user.get().getLabelService() == null) {
                labelService = new DBLabelService(this.database, user.get());
                user.get().setLabelService(labelService);
            } else {
                labelService = user.get().getLabelService();
            }
            LOGGER.log(Level.INFO, "Server: user [" + user.get().getID() + "] unlocked label service");
        } else
            LOGGER.log(Level.SEVERE, "Server: user [" + user.get().getID() + "] key failed to unlock label service");
        return labelService;
    }

    @Override
    public Key login(String username, String password) throws RemoteException {
        Key key = null;

        try {
            int userID = this.database.getUserID(username, password);
            if (userID != -1) {
                for (User u : this.users)
                    if (userID == u.getID())
                        return u.getKey();

                key = new Key(username, password);
                User user = new User(username, userID, key);
                this.users.add(user);
                LOGGER.log(Level.INFO, "Server: user login successfully ");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Server: failed to select user id", e);
        }
        return key;
    }
}
