package de.age.httpcmd.commands;

import de.age.httpcmd.Command;
import de.age.httpcmd.CommandFactory;
import de.age.httpcmd.Context;

public class ExitCommand implements Command {

    public static final CommandFactory FACTORY = new CommandFactory() {

        @Override
        public Command createCommand(Context context, String... args) {
            return new ExitCommand();
        }
    };
    
    @Override
    public void doCommand() {
        System.out.println("Exiting ...");
        System.exit(0);
    }

}
