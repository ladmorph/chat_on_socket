package model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "time")
    private String date;

    @Column(name = "messages")
    private int messages;

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getDate() {
        return date;
    }

    public User setDate(String date) {
        this.date = date;
        return this;
    }

    public int getMessages() {
        return messages;
    }

    public User setMessages(int messages) {
        this.messages = messages;
        return this;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                        ", username='" + username + '\'' +
                        ", date='" + date + '\'' +
                        ", messages=" + messages;
    }
}