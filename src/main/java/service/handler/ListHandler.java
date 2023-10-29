package service.handler;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import model.Order;
import model.Status;
import model.Task;
import service.tracker.TaskTracker;

public class ListHandler implements CommandHandler {

    private static final String ALL = "all";
    private static final String TODAY = "today";
    private static final String STATUS = "status";
    private static final String LIST = "list";
    private final TaskTracker taskTracker;

    public ListHandler(TaskTracker taskTracker) {
        this.taskTracker = taskTracker;
    }

    @Override
    public String getCommand() {
        return LIST;
    }

    @Override
    public void handle(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No arguments given for list. Should give at least 1 arg: all, today, status");
        }
        String mode = args[0];
        String[] commandArgs = null;
        if (args.length > 1) {
            commandArgs = new String[args.length - 1];
            System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);
        }
        switch (mode) {
            case ALL -> handleAll(commandArgs);
            case TODAY -> handleToday();
            case STATUS -> handleStatus(commandArgs);
            default -> System.err.printf("Unknown list mode %s. Known modes are all, today, status%n", mode);
        }
    }

    private void handleAll(String[] commandArgs) {
        String output;
        if (commandArgs == null) {//no grouping by status
            System.out.println("id | priority | summary | dateCreated | deadlineDate | status");
            output = mapToString(taskTracker.getAll());
        } else if (commandArgs.length == 1 && commandArgs[0].equals("grouped_by_status")) {
            output = taskTracker.getTasksPerStatusSorted(Order.ASC)
                    .entrySet()
                    .stream()
                    .map(entry -> "----[%s]----%n%s".formatted(entry.getKey().name(),
                            mapToString(entry.getValue())))
                    .collect(Collectors.joining("\n"));
        } else {
            throw new IllegalArgumentException("all can either contain grouped_by_status modifier or not. Other is prohibited");
        }
        System.out.println(output);
    }

    private void handleToday() {
        String tasks = mapToString(taskTracker.getTodayTasksSorted(Order.ASC));
        System.out.println(tasks);
    }

    private void handleStatus(String[] commandArgs) {
        if (commandArgs == null) {
            throw new IllegalArgumentException("list by status should contain target status");
        } else if (commandArgs.length == 1) {
            String statusInput = commandArgs[0];
            try {
                Status status = Status.valueOf(statusInput);
                String tasks = mapToString(taskTracker.getTasksByStatusSorted(status, Order.ASC));
                System.out.println(tasks);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("Unknown status %s. Known: TODO/IN_PROGRESS/DONE".formatted(
                        statusInput));
            }
        } else {
            throw new IllegalArgumentException("list by status should contain only one argument - target status TODO/IN_PROGRESS/DONE");
        }
    }

    private String mapToString(Collection<Task> tasks) {
        return tasks.stream()
                .map(this::mapToString)
                .collect(Collectors.joining("\n"));
    }

    private String mapToString(Task task) {
        return "%s %d %s %s %s %s".formatted(task.id(), task.priority(),
                task.summary(), task.dateCreated(), task.deadlineDate(), task.status());
    }
}
