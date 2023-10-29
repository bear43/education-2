package service.handler;

public interface CommandHandler {
    String getCommand();
    void handle(String[] args);
}
