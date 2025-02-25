package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String title;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Pattern(regexp = "^(в ожидании|в процессе|завершено)$", message = "Недопустимое значение статуса")
    private String status;

    @Pattern(regexp = "^(высокий|средний|низкий)$", message = "Недопустимое значение приоритета")
    private String priority;

    @Min(value = 0, message = "Исполнитель должен быть больше нуля")
    private Integer performer;


    private Integer author;


    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPerformer() {
        return this.performer != null ? this.performer.intValue() : null;
    }


    public void setPerformer(int performer) {
        this.performer = performer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
