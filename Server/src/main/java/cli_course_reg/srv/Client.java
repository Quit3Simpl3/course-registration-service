package cli_course_reg.srv;

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
        if (!Objects.isNull(this.user))
            throw new IllegalStateException("Client is already logged-in with a different user.");

        this.user = user;
    }

    public void removeUser() {
        this.user = null;
    }

    public User getUser() {
//        if (Objects.isNull(user))
//            throw new IllegalStateException("User not set for this client.");
        return this.user;
    }

    public String getId() {
        return this.id;
    }
}
