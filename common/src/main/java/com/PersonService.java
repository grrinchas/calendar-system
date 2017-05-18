package com;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

@SuppressWarnings("all")
public interface PersonService extends Remote {

    Collection<Person> findPerson(String name) throws SQLException, RemoteException;
}
