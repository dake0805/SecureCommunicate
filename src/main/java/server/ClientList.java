package server;

import client.Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientList {
    private static List<Socket> clients = new ArrayList<>();

    private ClientList() {
    }

    protected static List<Socket> getClients() {
        return clients;
    }
}
