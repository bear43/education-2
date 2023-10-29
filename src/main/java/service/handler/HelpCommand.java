package service.handler;

public class HelpCommand implements CommandHandler {

    private static final String COMMAND = "help";

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public void handle(String[] args) {
        System.out.println("""
                Legend: command mandatoryArg <argWithFreeInputValue> [optionalArg]
                Application has such commands as:
                help,
                exit,
                create <summary> <priority> <deadlineDate|infinity>,
                list all [grouped_by_status]
                list today
                list status <TODO/IN_PROGRESS/DONE>
                update status <TODO/IN_PROGRESS/DONE>
                delete <taskId>""");
    }
}
