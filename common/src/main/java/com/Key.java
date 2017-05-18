package com;

import java.io.Serializable;

@SuppressWarnings("all")
public class Key implements Serializable {

    public final String username;
    public final String password;

    public Key(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Key key = (Key) o;

        if (!username.equals(key.username))
            return false;
        if (!password.equals(key.password))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
