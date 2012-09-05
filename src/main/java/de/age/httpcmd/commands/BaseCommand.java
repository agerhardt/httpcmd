package de.age.httpcmd.commands;

import de.age.httpcmd.Command;
import de.age.httpcmd.CommandFactory;
import de.age.httpcmd.Context;
import java.util.Map;

public class BaseCommand implements Command {

    public static final CommandFactory FACTORY = new CommandFactory() {

        @Override
        public Command createCommand(Context context, String... args) {
            return new BaseCommand(context, args[1]);
        }
    };
    
    private final Context context;
    private final String base;

    public BaseCommand(Context context, String base) {
        this.context = context;
        this.base = base;
    }
    
    @Override
    public void doCommand() {
        System.out.println("Setting base to [" + base + "]");
        context.setBase(base);
    }

}
