package de.age.httpcmd;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.age.httpcmd.commands.AuthCommand;
import de.age.httpcmd.commands.BaseCommand;
import de.age.httpcmd.commands.ErrorCommand;
import de.age.httpcmd.commands.ExitCommand;
import de.age.httpcmd.commands.HttpCommandFactory;
import de.age.httpcmd.util.EnhancedStringTokenizer;

public class HttpCommandLineClient {

    private static final String DEFAULT_SERVER = "http://localhost:8080";
    private static final String DEFAULT_PATH = "";
    private static final String PROMPT_FORMAT = "[%s]> ";
    
    private final Map<String, CommandFactory> commands;
    private final Context context;
    
    public HttpCommandLineClient() {
        final Client c = new Client();
        final WebResource resource = c.resource(DEFAULT_SERVER).path(DEFAULT_PATH);
        context = new Context(resource, MediaType.APPLICATION_JSON_TYPE);
        commands = Collections.unmodifiableMap(createCommands());
    }
    
    private Map<String, CommandFactory> createCommands() {
        Map<String, CommandFactory> result = new HashMap<String, CommandFactory>();
        HttpCommandFactory httpCommand = new HttpCommandFactory();
        result.put("get", httpCommand);
        result.put("put", httpCommand);
        result.put("post", httpCommand);
        result.put("delete", httpCommand);
        result.put("head", httpCommand);
        result.put("exit", ExitCommand.FACTORY);
        result.put("base", BaseCommand.FACTORY);
        result.put("auth", AuthCommand.FACTORY);
        result.put("", new CommandFactory() {
            @Override
            public Command createCommand(Context context, String... args) {
                return Command.NONE;
            }
        });
        return result;
    }
    
    public String getPrompt() {
        return String.format(PROMPT_FORMAT, context.getResource().getURI().toString());
    }

    private Command parseCommand(String nextLine) {
        EnhancedStringTokenizer tok = new EnhancedStringTokenizer(nextLine, " ", "\"");
        List<String> tokens = tok.getAllTokens();
        if (tokens.size() == 0) {
        	return ErrorCommand.UNKNOWNCOMMAND_FACTORY.createCommand(context, "");
        }
        CommandFactory factory = commands.get(tokens.get(0));
        if (factory == null) {
            factory = ErrorCommand.UNKNOWNCOMMAND_FACTORY;
        }
        return factory.createCommand(context, tokens.toArray(new String[tokens.size()]));
    }
    
    public static void main(String[] args) {
        HttpCommandLineClient client = new HttpCommandLineClient();
        Command nextCommand = Command.NONE;
        Scanner scanner = new Scanner(System.in);
        do {
            performCommand(nextCommand);
            final String nextLine = scanner.nextLine();
            nextCommand = client.parseCommand(nextLine);
        } while (true);
    }

    private static void performCommand(Command nextCommand) {
        try {
            nextCommand.doCommand();
        } catch (Exception exc) {
            System.err.println("Error during execution");
            exc.printStackTrace();
        }
    }
}
