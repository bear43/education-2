import io.ConsoleReader;
import model.Command;
import service.generator.IdGeneratorImpl;
import service.handler.CommandParser;
import service.handler.CreateHandler;
import service.handler.HandlerManager;
import service.handler.HelpCommand;
import service.handler.ListHandler;
import service.tracker.SimpleTaskTracker;
import service.tracker.TaskTracker;

public class EntryPoint {

    private static final String PROMPT = ">";
    private static final String EXIT_CMD = "exit";

    public static void main(String[] args) {
        TaskTracker taskTracker = new SimpleTaskTracker(new IdGeneratorImpl());
        HandlerManager handlerManager = new HandlerManager();
        handlerManager.add(new HelpCommand(), new CreateHandler(taskTracker),
                new ListHandler(taskTracker));
        do {
            System.out.print(PROMPT);
            String input = ConsoleReader.read().trim();
            if (EXIT_CMD.equals(input)) {
                break;
            }
            try {
                Command command = CommandParser.parse(input);
                handlerManager.invoke(command);
            } catch (IllegalArgumentException exception) {
                System.err.println("Wrong command: " + input);

            }
        } while (true);
    }
}
