package com.nexus.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import com.nexus.exception.NexusValidationException;
import com.nexus.service.Workspace;

public class Task {
    // Métricas Globais (Alunos implementam a lógica de incremento/decremento)
    private static int totalTasksCreated = 0;
    private static int activeWorkload = 0;

    private static int nextId = 1;

    private int id;
    private LocalDate deadline; // Imutável após o nascimento
    private String title;
    private TaskStatus status;
    private User owner;
    private int estimatedEffort;

    public Task(String title, LocalDate deadline, int estimatedEffort) {
        this.id = nextId++;
        this.deadline = deadline;
        this.title = title;
        this.status = TaskStatus.TO_DO;
        this.estimatedEffort = estimatedEffort;

        totalTasksCreated++;
    }

    /**
     * Move a tarefa para IN_PROGRESS.
     * Regra: Só é possível se houver um owner atribuído e não estiver BLOCKED.
     */
    public void moveToInProgress() {
        if (this.owner == null) {
            throw new NexusValidationException("Task não está atribuída a nenhum usuário");
        }
        this.status = TaskStatus.IN_PROGRESS;
        activeWorkload++;
        return;
    }

    /**
     * Finaliza a tarefa.
     * Regra: Só pode ser movida para DONE se não estiver BLOCKED.
     */
    public void markAsDone() {
        if (this.getStatus() == TaskStatus.BLOCKED) {
            return;
        }
        this.status = TaskStatus.DONE;
        activeWorkload--;
    }

    private void setBlocked(boolean blocked) {
        if (blocked) {
            this.status = TaskStatus.BLOCKED;
        } else {
            this.status = TaskStatus.TO_DO; // Simplificação para o Lab
        }
    }

    public void assignUser(String username, Workspace workspace){
        User user = workspace.findUser(username);
        if(user == null){
            throw new NexusValidationException("Usuário não existe");
        }
        this.owner = user;
    }

    public static void assignUser(int taskId, String username, Workspace workspace) {
        Task task = workspace.findTask(taskId);
        if(task == null){
            throw new NexusValidationException("Tarefa não existe");
        }
        task.assignUser(username, workspace);
    }

    private void changeStatus(String status_code){
        switch(status_code){
            case "BLOCKED":
                this.setBlocked(true);
                break;
            case "IN_PROGRESS": 
                this.moveToInProgress();
                break;
            case "TODO":
                this.setBlocked(false);
                break;
            case "DONE":
                this.markAsDone();
                break;
            default:
                throw new NexusValidationException("Estado de tarefa '" + status_code + "' não existe");
        }
    }

    public static void changeStatus(int taskId, String status, Workspace workspace) {
        Task task = workspace.findTask(taskId);
        if(task == null){
            throw new NexusValidationException("Tarefa não existe");
        }
        task.changeStatus(status);
    }

    // Getters
    public static int getTotalTasksCreated() {
        return totalTasksCreated;
    }

    public static int getActiveWorkload() {
        return activeWorkload;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public User getOwner() {
        return owner;
    }

    public int consultEstimatedEffort() {
        return estimatedEffort;
    }

}