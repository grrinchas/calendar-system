package cont;

import com.*;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class responsible for unlocking services from the server
 */
@SuppressWarnings("all")
public class ServiceProvider {

    private Logger LOGGER = Logger.getLogger(Login.class.getSimpleName());

    // server which has services
    private Server server;

    /**
     * Constructs unlocker with default server
     *
     * @param server to unlock services from
     */
    public ServiceProvider(Server server) {
        this.server = server;
    }

    public MeetingService unlockMeetingService(Key key) {
        MeetingService service = null;
        try {
            service = this.server.unlockMeetingService(key);
            if (service == null)
                LOGGER.log(Level.SEVERE, "ServiceProvider: key failed to unlock meeting service");
            else {
                LOGGER.log(Level.INFO, "ServiceProvider: unlocked meeting service ");
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "ServiceProvider: can't unlock meeting service", e);
            System.exit(-1);
        }
        return service;
    }

    public PersonService unlockPersonService(Key key) {
        PersonService service = null;
        try {
            service = this.server.unlockPersonService(key);
            if (service == null)
                LOGGER.log(Level.SEVERE, "ServiceProvider: key failed to unlock person service");
            else {
                LOGGER.log(Level.INFO, "ServiceProvider: unlocked person service ");
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "ServiceProvider: can't unlock person service", e);
            System.exit(-1);
        }
        return service;
    }
    public LabelService unlockLabelService(Key key) {
        LabelService service = null;
        try {
            service = this.server.unlockLabelService(key);
            if (service == null)
                LOGGER.log(Level.SEVERE, "ServiceProvider: key failed to unlock label service");
            else {
                LOGGER.log(Level.INFO, "ServiceProvider: unlocked label service ");
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "ServiceProvider: can't unlock label service", e);
            System.exit(-1);
        }
        return service;
    }


    public NotificationService unlockNotificationService(Key key) {

        NotificationService service = null;

        try {
            service = this.server.unlockNotificationService(key);

            if (service == null)
                LOGGER.log(Level.SEVERE, "ServiceProvider: key failed to unlock notification service");
            else {
                LOGGER.log(Level.INFO, "ServiceProvider: unlocked notification service ");
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "ServiceProvider: can't unlock notification service", e);
            System.exit(-1);
        }

        return service;
    }

}
