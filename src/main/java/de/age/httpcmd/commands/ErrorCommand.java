package de.age.httpcmd.commands;

import de.age.httpcmd.Command;
import de.age.httpcmd.CommandFactory;
import de.age.httpcmd.Context;

public class ErrorCommand implements Command {

    public static final CommandFactory UNKNOWNCOMMAND_FACTORY = new CommandFactory() {

        @Override
        public Command createCommand(Context context, String... args) {
            return new ErrorCommand("unknown command [" + args[0] + "]");
        }
    };
    
    private final String message;

    public ErrorCommand(String message) {
        this.message = message;
    }
    
    @Override
    public void doCommand() {
        System.err.println("Error, " + message);
    }

}
