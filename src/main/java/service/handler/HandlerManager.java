package service.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import model.Command;

public class HandlerManager {
    private final Map<String, CommandHandler> handlers;

    public HandlerManager() {
        this.handlers = new HashMap<>();
    }

    public void add(CommandHandler... handlers) {
        Map<String, CommandHandler> handlerMap = Arrays.stream(handlers)
                .collect(Collectors.toMap(CommandHandler::getCommand, Function.identity()));
        this.handlers.putAll(handlerMap);
    }

    public void invoke(Command command) {
        CommandHandler commandHandler = handlers.get(command.command());
        if (commandHandler == null) {
            throw new IllegalArgumentException("Unknown command");
        }
        try {
            commandHandler.handle(command.args());
        } catch (IllegalArgumentException exception) {
            System.err.println("Illegal input exception: " + exception.getMessage());
        }
    }
}
