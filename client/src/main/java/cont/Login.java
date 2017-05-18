package cont;

import com.Key;
import com.Server;
import view.dialog.LoginDialog;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for logging into the server and aquiring a key.
 */
@SuppressWarnings("all")
public class Login {

    private Logger LOGGER = Logger.getLogger(Login.class.getSimpleName());

    // listeners which will be notified on successfull login
    private Collection<LoginListener> listeners;

    // server to log into
    private Server server;

    // dialog to read username and password from
    private LoginDialog dialog;

    /**
     * Constructs Login with default server.
     *
     * @param server to log into
     */
    public Login(Server server) {
        this.server = server;
        this.dialog = new LoginDialog();
        this.listeners = new HashSet<>();

        // register this login with the dialog
        this.dialog.addLoginController(this);
    }

    /**
     * Login into the server with provided username and password.
     *
     * @param username to use for log in
     * @param password to use for log in
     */
    public void login(String username, String password) {

        try {
            Key key = server.login(username, password);
            if (key == null) {

                // if login was not successfull display an error
                this.dialog.setLoginMessage("No such username or password");
                LOGGER.log(Level.INFO, "Login: no such username or password");
            } else {

                // lets close dialog
                this.dialog.dispose();

                // and notify all the listeners
                this.listeners.forEach(l -> l.onLogin(key));

                LOGGER.log(Level.INFO, "Login: Login was successful");
            }

        } catch (RemoteException e) {
            LOGGER.log(Level.INFO, "Login: Can't login to the server", e);
        }
    }

    /**
     * Displays login dialog
     */
    public void displayLogin() {
        this.dialog.setVisible(true);
    }

    /**
     * Add login listener. All listeners which will be notifed on successful log in
     *
     * @param listener to be notified
     */
    public void addLoginListener(LoginListener listener) {
        this.listeners.add(listener);
    }
}
