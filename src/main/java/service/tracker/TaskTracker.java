package service.tracker;

import java.util.Collection;
import java.util.Map;
import model.Order;
import model.Status;
import model.Task;

public interface TaskTracker {
    Task create(Task task);
    Task get(int id);
    Collection<Task> getAll();
    void delete(int id);
    Collection<Task> getTodayTasksSorted(Order order);
    Collection<Task> getTasksByStatusSorted(Status status, Order order);
    Map<Status, Collection<Task>> getTasksPerStatusSorted(Order order);
    Task update(Task task);
}
