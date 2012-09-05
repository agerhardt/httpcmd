package de.age.httpcmd.commands;

import de.age.httpcmd.Authentication;
import de.age.httpcmd.Command;
import de.age.httpcmd.CommandFactory;
import de.age.httpcmd.Context;

/**
 *
 * @author tumbleweed
 */
public class AuthCommand implements Command {
    public static final CommandFactory FACTORY = new AuthCommandFactory();

    private static class AuthCommandFactory implements CommandFactory {
        
        public Command createCommand(Context context, String... args) {
            if (args == null || args.length <= 1) {
                return new ErrorCommand("missing authentication method");
            }
            if ("none".equalsIgnoreCase(args[1])) {
                return new AuthCommand(context, Authentication.none());
            } else if ("basic".equalsIgnoreCase(args[1])) {
                if (args.length != 3 || !args[2].contains(":")) {
                    return new ErrorCommand("missing user:password");
                }
                String auth[] = args[2].split(":");
                if (auth.length != 2) {
                    return new ErrorCommand("user:password string has wrong format");
                }
                return new AuthCommand(context, Authentication.basic(auth[0], auth[1]));
            } else {
                return new ErrorCommand("unknown authentication method [" + args[1] + "]");
            }
        }
        
    }
    
    private final Context context;
    private final Authentication authentication;
    
    private AuthCommand(Context context, Authentication authentication) {
        this.context = context;
        this.authentication = authentication;
    }

    public void doCommand() {
        context.setAuthentication(authentication);
    }
    
}
