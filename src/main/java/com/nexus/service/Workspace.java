package com.nexus.service;

import com.nexus.model.Project;
import com.nexus.model.Task;
import com.nexus.model.TaskStatus;
import com.nexus.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;

public class Workspace {

    // Users
    private final List<User> users = new ArrayList<>();
    private final List<Project> projects = new ArrayList<>();

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User findUser(String username) {
        List<User> users = getUsers();
        return users.stream()
                .filter(user -> user.consultUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // Tasks
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task findTask(int taskID) {
        List<Task> userTasks = getTasks();
        return userTasks.stream()
                .filter(task -> task.getId() == taskID)
                .findFirst()
                .orElse(null);
    }

    private List<User> topPerformers() {
        List<Task> tasksDone = this.getTasksFiltered(TaskStatus.DONE);

        Map<User, Long> counts = tasksDone.stream()
                .collect(Collectors.groupingBy(
                        Task::getOwner,
                        Collectors.counting()));

        return getUsers().stream()
                .sorted(Comparator.comparingLong((User user) -> counts.getOrDefault(user, 0L)).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksFiltered(TaskStatus taskStatus) {
        List<Task> tasksDone = this.getTasks();
        return tasksDone.stream()
                .filter(task -> task.getStatus().equals(taskStatus))
                .collect(Collectors.toList());
    }

    private List<User> overloadedUsers() {
        List<Task> tasksInProgress = this.getTasksFiltered(TaskStatus.IN_PROGRESS);

        Map<User, Long> counts = tasksInProgress.stream()
                .collect(Collectors.groupingBy(
                        Task::getOwner,
                        Collectors.counting()));

        return getUsers().stream()
                .filter(user -> counts.getOrDefault(user, 0L) > 10)
                .collect(Collectors.toList());
    }

    private void projectHealth() {
        projects.stream()
                .forEach(p -> System.out.printf("Projeto %s: %.2f%%\n", p.consultName(), p.projectHealth()));
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public TaskStatus bottleneck() {
        List<Task> tasks = this.getTasks();
        Map<TaskStatus, Long> statusCount = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
        return statusCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void reportStatus() {
        System.out.println("REPORT STATUS BEGINNING -----------");
        System.out.println("Top Performers:");
        System.out.println(this.topPerformers());
        System.out.println("Overloaded Users:");
        System.out.println(this.overloadedUsers());
        System.out.println("Project Health:");
        this.projectHealth();
        System.out.println("Bottleneck:");
        System.out.println(this.bottleneck());
        System.out.println("REPORT STATUS END -----------");
    }
}