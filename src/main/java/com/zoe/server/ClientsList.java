package com.zoe.server;

import com.google.common.collect.Lists;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zy
 */
public class ClientsList {
    private static final List<Socket> CLIENTS = Collections.synchronizedList(Lists.newLinkedList());

    private ClientsList() {
    }

    protected static List<Socket> getClients() {
        return CLIENTS;
    }
}
