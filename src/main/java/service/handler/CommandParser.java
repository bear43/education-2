package service.handler;

import model.Command;

public class CommandParser {
    public static Command parse(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length == 0) {
            throw new IllegalArgumentException("Empty input");
        }
        if (tokens.length == 1) {
            return new Command(tokens[0], new String[0]);
        }
        String[] args = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, args, 0, args.length);
        return new Command(tokens[0], args);
    }
}
