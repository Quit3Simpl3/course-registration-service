package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.UUID;
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
        Client client = new Client(UUID.randomUUID().toString());
        this.clients.put(client.getId(), client);
        return client;
    }

    /**
     * Return the Client object matching the given uuid.
     * @param clientId - The client's UUID.
     * @return - Client object matching the UUID.
     */
    public Client get(String clientId) {
        return this.clients.get(clientId);
    }

    /**
     * Associates a user object with a client object.
     */
    public void setUser(String clientId, String username) {
        Client client = this.get(clientId);
        User user = Database.getInstance().getUser(username);
        client.setUser(user);
    }
}
