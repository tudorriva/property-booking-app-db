package org.code.Entities;

import javax.persistence.*;

@MappedSuperclass
public abstract class User implements HasId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int userID;

    @Column(name = "name")
    protected String name;

    @Column(name = "email")
    protected String email;

    @Column(name = "phone")
    protected String phone;

    public User() {
        // Default constructor required by Hibernate
    }

    public User(int userID, String name, String email, String phone) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}