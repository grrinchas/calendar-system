import server.Server;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("all")
public class Main {

    public static void main(String[] args) throws Exception {
        new Server(1099, "Server").start();
    }
}
