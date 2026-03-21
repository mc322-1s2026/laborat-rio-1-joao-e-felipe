package com.nexus.service;

import com.nexus.Main;
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
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        // Retorna uma visão não modificável para garantir encapsulamento
        return Collections.unmodifiableList(tasks);
    }

    public void reportStatus() {

    }

    private List<User> topPerformers() {
        List<Task> tasksDone = this.getTasksFiltered(TaskStatus.DONE);

        Map<User, Long> counts = tasksDone.stream()
                .collect(Collectors.groupingBy(
                        Task::getOwner,
                        Collectors.counting()));

        return Main.getUsers().stream()
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

        return Main.getUsers().stream()
                .filter(user -> counts.getOrDefault(user, 0L) > 10)
                .collect(Collectors.toList());
    }

    private double projectHealth() {
        return 0;
    }
}