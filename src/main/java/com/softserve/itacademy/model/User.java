package com.softserve.itacademy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"password", "myTodos"})
@EqualsAndHashCode(of = "email")
public class User {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
    private List<ToDo> myTodos = new ArrayList<>();

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        return normalized.isEmpty() ? null : normalized;
    }

    public User(String firstName, String lastName, String email, String password, List<ToDo> myTodos) {
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        this.password = password;
        this.myTodos = (myTodos == null) ? new ArrayList<>() : myTodos;
    }

    public void setEmail(String email) {
        this.email = normalizeEmail(email);
    }

    public void setMyTodos(List<ToDo> myTodos) {
        this.myTodos = (myTodos == null) ? new ArrayList<>() : myTodos;
    }
}
