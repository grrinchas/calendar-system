package cont;

import com.*;
import model.*;
import view.MainWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class Controller implements LoginListener {

    private Logger LOGGER = Logger.getLogger(Controller.class.getSimpleName());
    private ServiceProvider serviceProvider;

    public static void main(String[] args) {

        new Controller().start();
    }

    public void start() {
        Server server = this.connect();
        Login login = new Login(server);
        this.serviceProvider = new ServiceProvider(server);

        login.addLoginListener(this);
        login.displayLogin();
    }

    private Server connect() {
        Server server = null;
        try {
            server = (Server) Naming.lookup("//localhost/Server");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Controller: can't connect to the server", e);
            System.exit(-1);
        }
        LOGGER.log(Level.INFO, "Controller: connected to the server");
        return server;
    }


    @Override
    public void onLogin(Key key) {
        MeetingService meetingService = this.serviceProvider.unlockMeetingService(key);
        NotificationService notificationService = this.serviceProvider.unlockNotificationService(key);
        PersonService personService = this.serviceProvider.unlockPersonService(key);
        LabelService labelService = this.serviceProvider.unlockLabelService(key);

        Cache cache = new Cache(meetingService, labelService);

        RemoteClient client = this.createClient(cache);
        this.registerClient(notificationService, client);

        MainWindow mainWindow = new MainWindow(meetingService, cache, labelService);
        mainWindow.setTitle("Calendar system v.1.0 [username: " + key.username + "]");
        mainWindow.addWindowListener(new Disconnect(notificationService, client));

        mainWindow.setVisible(true);


    }

    private void registerClient(NotificationService service, RemoteClient client) {
        try {
            service.registerClient(client);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Controller: client can't be registered", e);
            System.exit(-1);
        }
        LOGGER.log(Level.INFO, "Controller: client has been registered");
    }

    private RemoteClient createClient(Cache cache) {
        JavaClient client = null;
        try {
            client = new JavaClient(cache);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Controller: can't create client", e);
            System.exit(-1);
        }
        LOGGER.log(Level.INFO, "Controller: client has created");
        return client;
    }

    private class Disconnect extends WindowAdapter {

        NotificationService service;
        RemoteClient client;

        Disconnect(NotificationService service, RemoteClient client) {
            this.service = service;
            this.client = client;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            try {
                service.deregisterClient(client);
            } catch (RemoteException e1) {
                LOGGER.log(Level.SEVERE, "Controller: can't remove client from the server", e);

            }
            LOGGER.log(Level.INFO, "Controller: removed client from the server");
        }

    }


}
