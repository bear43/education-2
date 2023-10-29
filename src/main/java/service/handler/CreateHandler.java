package service.handler;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import model.Task;
import service.tracker.TaskTracker;

public class CreateHandler implements CommandHandler {

    private final TaskTracker taskTracker;

    public CreateHandler(TaskTracker taskTracker) {
        this.taskTracker = taskTracker;
    }

    @Override
    public String getCommand() {
        return "create";
    }

    @Override
    public void handle(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("create command demands such arguments as: summary, priority, deadlineDate");
        }
        String summary = args[0];
        String priority = args[1];
        String deadlineDate = args[2];
        Task task = createTask(summary, priority, deadlineDate);
        Task createdTask = taskTracker.create(task);
        System.out.println("Task successfully created. Id: " + createdTask.id());
    }

    private Task createTask(String summary, String priority, String deadlineDate) {
        try {
            int priorityValue = Integer.parseInt(priority);
            if (priorityValue < 1) {
                throw new NumberFormatException("priority should be greater or equal 1");
            }
            LocalDate deadlineDateValue = LocalDate.parse(deadlineDate);
            if (deadlineDateValue.isBefore(LocalDate.now())) {
                throw new DateTimeParseException("deadlineDate is before current date", deadlineDate, -1);
            }
            return new Task(summary, priorityValue, deadlineDateValue);
        } catch (DateTimeParseException | NumberFormatException exception) {
            throw new IllegalArgumentException("priority or deadlineDate in wrong format: " + exception.getMessage());
        }
    }
}
