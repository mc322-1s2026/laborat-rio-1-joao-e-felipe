package com.nexus.model;

import java.util.ArrayList;
import java.util.List;

import com.nexus.exception.NexusValidationException;

public class Project {
    private String name;
    private List<Task> tasks = new ArrayList<>();
    private int totalBudget;

    public Project(String name, int totalBudget) {
        this.name = name;
        this.totalBudget = totalBudget;
    }

    public void addTask(Task t) {
        if (t.consultEstimatedEffort() > totalBudget) {
            throw new NexusValidationException("Total budget excedido");
        } else {
            tasks.add(t);
        }
    }

    public String consultName() {
        return name;
    }

    public List<Task> getProjectTasks() {
        return tasks;
    }

    public double projectHealth() {
        // In Percentage
        double doneCount = tasks.stream()
                .filter(task -> task.getStatus().equals(TaskStatus.DONE))
                .count();
        if(tasks.size() != 0){
            return (doneCount / tasks.size()) * 100;
        } else{
            return 100;
        }
    }
}
