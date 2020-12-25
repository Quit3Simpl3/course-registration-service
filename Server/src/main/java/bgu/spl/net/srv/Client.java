package bgu.spl.net.srv;

import java.util.Objects;
import java.util.UUID;

public class Client {
    // Private fields:
    String id;
    User user;

    public Client() {
        new Client(UUID.randomUUID().toString());
    }

    public Client(String id) {
        this.id = id;
        this.user = null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        if (Objects.isNull(user))
            throw new IllegalStateException("User not set for this client.");
        return this.user;
    }

    public String getId() {
        return this.id;
    }
}
