package service;

import com.*;
import db.Database;
import server.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class DBPersonService extends UnicastRemoteObject implements PersonService, Serializable {

    private static Logger LOGGER = Logger.getLogger(DBPersonService.class.getSimpleName());

    private Database database;
    private User user;

    public DBPersonService(Database database, User user) throws RemoteException {
        super(0);
        this.database = database;
        this.user = user;
    }

    @Override
    public Collection<Person> findPerson(String name) throws SQLException {
        return this.database.findPersons(user.getID(), name);
    }

}
