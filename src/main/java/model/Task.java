package model;

import java.time.LocalDate;
import java.util.Objects;

public record Task(Integer id, String summary, int priority, LocalDate dateCreated, LocalDate deadlineDate, Status status) {

    public Task {
        Objects.requireNonNull(summary, "summary can not be null");
        Objects.requireNonNull(dateCreated, "dateCreated can not be null");
    }

    public Task(String summary, int priority, LocalDate deadlineDate) {
        this(null, summary, priority, LocalDate.now(), deadlineDate, Status.TODO);
    }

    public Task withId(int id) {
        return new Task(id, summary, priority, dateCreated, deadlineDate, status);
    }

    public Task withStatus(Status status) {
        return new Task(id, summary, priority, dateCreated, deadlineDate, status);
    }
}
