package com.nexus.model;

import java.time.LocalDate;
import java.util.Objects;
import com.nexus.exception.NexusValidationException;

public class Task {
    // Métricas Globais (Alunos implementam a lógica de incremento/decremento)
    private int totalTasksCreated = 0;
    private int totalValidationErrors = 0;
    private int activeWorkload = 0;

    private static int nextId = 1;

    private int id;
    private LocalDate deadline; // Imutável após o nascimento
    private String title;
    private TaskStatus status;
    private User owner;

    public Task(String title, LocalDate deadline) {
        this.id = nextId++;
        this.deadline = deadline;
        this.title = title;
        this.status = TaskStatus.TO_DO;
        
        // Ação do Aluno:
        totalTasksCreated++; 
    }

    /**
     * Move a tarefa para IN_PROGRESS.
     * Regra: Só é possível se houver um owner atribuído e não estiver BLOCKED.
     */
    public void moveToInProgress(User user) {
        // TODO: Implementar lógica de proteção e atualizar activeWorkload
        // IN PROGRESS
        // Se falhar, incrementar totalValidationErrors e lançar NexusValidationException

        // Validação 
        if(Objects.isNull(user)){
            throw new NexusValidationException("Usuário não existe");
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
        // TODO: Implementar lógica de proteção e atualizar activeWorkload (decrementar)
        // IN PROGRESS
        if(this.getStatus() == TaskStatus.BLOCKED){
            return;
        }
        this.status = TaskStatus.DONE;
        activeWorkload--;
    }

    public void setBlocked(boolean blocked) {
        if (blocked) {
            this.status = TaskStatus.BLOCKED;
        } else {
            this.status = TaskStatus.TO_DO; // Simplificação para o Lab
        }
    }

    // Getters
    public int getTotalTasksCreated() { return totalTasksCreated; }
    public int getTotalValidationErrors() { return totalValidationErrors; }
    public int getActiveWorkload() { return activeWorkload; }
    public int getId() { return id; }
    public TaskStatus getStatus() { return status; }
    public String getTitle() { return title; }
    public LocalDate getDeadline() { return deadline; }
    public User getOwner() { return owner; }
}