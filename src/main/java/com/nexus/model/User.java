package com.nexus.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            throw new IllegalArgumentException("Email invalido");
        }
        //this.workspace = workspace;
        this.username = username;
        this.email = email;
    }

    public String consultEmail() {
        return email;
    }

    public String consultUsername() {
        return username;
    }

    // TODO: 
    //public long calculateWorkload() {
    //    List<Task> userTasks = workspace.getTasks();
    //    return userTasks.stream()
    //            .filter(task -> task.getOwner() != null && task.getOwner().consultUsername().equals(username))
    //            .filter(task -> task.getStatus() != null && task.getStatus().equals(TaskStatus.IN_PROGRESS))
    //            .count();
    //}
}
