package perc.firebase.models;

import java.util.UUID;

public class User {

    public String name;

    public String pass;

    public String uuid;

    public User(String name, String pass, String uuid) {
        this.name = name;
        this.pass = pass;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
