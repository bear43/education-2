package service.tracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;
import model.Order;
import model.Status;
import model.Task;
import service.generator.IdGenerator;

public class SimpleTaskTracker implements TaskTracker {

    private final List<Task> tasks;
    private final IdGenerator idGenerator;

    public SimpleTaskTracker(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        this.tasks = new ArrayList<>();
    }

    @Override
    public Task create(Task task) {
        Objects.requireNonNull(task, "task can not be null");
        if (task.id() != null) {
            throw new IllegalArgumentException("id should be null for new task");
        }
        Task persistentTask = task.withId(idGenerator.generate());
        tasks.add(persistentTask);
        return persistentTask;
    }

    @Override
    public Task get(int id) {
        return tasks.stream()
                .filter(task -> task.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such task with id " + id));
    }

    @Override
    public Collection<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    @Override
    public void delete(int id) {
        Task task = get(id);
        tasks.remove(task);
    }

    @Override
    public Collection<Task> getTodayTasksSorted(Order order) {
        return tasks.stream()
                .filter(task -> task.deadlineDate() == null ||
                                task.deadlineDate().isEqual(LocalDate.now()))
                .sorted(Comparator.comparing(Task::priority, order.getComparator()))
                .toList();
    }

    @Override
    public Collection<Task> getTasksByStatusSorted(Status status, Order order) {
        return tasks.stream()
                .filter(task -> task.status().equals(status))
                .sorted(Comparator.comparing(Task::priority, order.getComparator()))
                .toList();
    }

    @Override
    public Map<Status, Collection<Task>> getTasksPerStatusSorted(Order order) {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::status,
                        HashMap::new,
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::priority)
                        ))));
    }

    @Override
    public Task update(Task task) {
        Task target = get(task.id());
        tasks.remove(target);
        tasks.add(task);
        return task;
    }
}
