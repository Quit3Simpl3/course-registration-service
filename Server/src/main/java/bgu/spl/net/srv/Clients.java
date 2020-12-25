package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Clients {
    // Private fields:
    ConcurrentHashMap<String, Client> clients; // clients<uuid, client>

    private final static class SingletonHolder {
        private final static Clients instance = new Clients();
    }

    public static Clients getInstance() {
        return Clients.SingletonHolder.instance;
    }

    private Clients() {
        this.clients = new ConcurrentHashMap<>();
    }

    /**
     * Creates and returns a new Client object.
     * @return - Client object matching the UUID.
     */
    public Client assign() {
        Client client = new Client();
        return this.clients.put(client.getId(), client);
    }

    /**
     * Return the Client object matching the given uuid.
     * @param clientId - The client's UUID.
     * @return - Client object matching the UUID.
     */
    public Client get(String clientId) {
        return this.clients.get(clientId);
    }
}
