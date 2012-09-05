package de.age.httpcmd.commands;

import de.age.httpcmd.Command;
import de.age.httpcmd.CommandFactory;
import de.age.httpcmd.Context;
import de.age.httpcmd.util.EnhancedStringTokenizer;
import java.util.*;
import javax.ws.rs.core.MediaType;

public class HttpCommandFactory implements CommandFactory {

    private static final Map<String, MediaType> MEDIA_TYPES = Collections.unmodifiableMap(createMediaTypes());
    
    private static Map<String, MediaType> createMediaTypes() {
        Map<String, MediaType> result = new HashMap<String, MediaType>();
        result.put("+json", MediaType.APPLICATION_JSON_TYPE);
        result.put("+xml", MediaType.APPLICATION_XML_TYPE);
        result.put("+html", MediaType.TEXT_HTML_TYPE);
        return result;
    }
    
    @Override
    public Command createCommand(Context context, String... args) {
        HttpCommand.Operation operation;
        String path = null;
        MediaType mediaType = null;
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        try {
            operation = HttpCommand.Operation.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException exc) {
            return ErrorCommand.UNKNOWNCOMMAND_FACTORY.createCommand(context, args);
        }
        
        for (int i = 1; i < args.length; i++) {
            String s = args[i];
            if (isPath(s)) {
                path = s;
            } else if (isMediaType(s)) {
                mediaType = MEDIA_TYPES.get(s);
                if (mediaType == null) {
                    return new ErrorCommand("unknown mediatype [" + s + "]");
                }
            } else if (isParameter(s)) {
                EnhancedStringTokenizer tok = new EnhancedStringTokenizer(s, "=", "\"");
                final List<String> tokens = tok.getAllTokens();
                final String key = tokens.get(0);
                final String value = tokens.get(1);
                parameters.put(key, value);
            } else if (isHeader(s)) {
                EnhancedStringTokenizer tok = new EnhancedStringTokenizer(s, ":", "\"");
                final List<String> tokens = tok.getAllTokens();
                final String key = tokens.get(0);
                final String value = tokens.get(1);
                headers.put(key, value);
            } else {
                return new ErrorCommand("unknown argument [" + s + "]");
            }
        }
        if (path == null) {
            return new ErrorCommand("missing path from command " + Arrays.toString(args));
        }
        if (mediaType == null) {
            mediaType = context.getDefaultMediaType();
        }
        return new HttpCommand(context, operation, path, mediaType, parameters, headers);
    }

    private boolean isPath(String s) {
        return s.startsWith("/");
    }

    private boolean isMediaType(String s) {
        return s.startsWith("+");
    }

    private boolean isParameter(String s) {
        return s.contains("=");
    }
    
    private boolean isHeader(String s) {
        return s.contains(":");
    }

}
