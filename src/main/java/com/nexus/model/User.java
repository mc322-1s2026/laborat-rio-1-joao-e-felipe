package com.nexus.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.stream.Stream;

import com.nexus.service.Workspace;

public class User {
    private final String username;
    private final String email;
    private static final String EMAIL_REQUIREMENTS = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REQUIREMENTS);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public User(String username, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username não pode ser vazio.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.username = username;
        this.email = email;
    }

    public String consultEmail() {
        return email;
    }

    public String consultUsername() {
        return username;
    }

    // public static User findUser(Workspace workspace, String username) {
    // List<User> users = workspace.getUsers();
    // return users.stream()
    // .filter(user -> user.consultUsername().equals(username))
    // .findFirst()
    // .orElse(null);
    // }

    private Stream<Task> getUserTasksStream(Workspace workspace) {
        return workspace.getTasks().stream()
                .filter(task -> task.getOwner().equals(this))
                .filter(task -> task.getStatus().equals(TaskStatus.IN_PROGRESS));
    }

    public long calculateWorkload(Workspace workspace) {
        return getUserTasksStream(workspace).count();
    }

    @Override
    public String toString() {
        return username;
    }
}
