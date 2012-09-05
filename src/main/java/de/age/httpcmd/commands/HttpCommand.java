package de.age.httpcmd.commands;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import de.age.httpcmd.Command;
import de.age.httpcmd.Context;
import java.util.Map;
import javax.ws.rs.core.MediaType;

public class HttpCommand implements Command {

    public static enum Operation {

        GET, PUT, POST, DELETE, HEAD
    }

    public HttpCommand(Context context, Operation operation, String path, MediaType mediaType, Map<String, String> parameters, Map<String, String> headers) {
        this.context = context;
        this.operation = operation;
        this.path = path;
        this.mediaType = mediaType;
        this.parameters = parameters;
        this.headers = headers;
    }
    
    private final Context context;
    private final Operation operation;
    private final String path;
    private final MediaType mediaType;
    private final Map<String, String> parameters;
    private final Map<String, String> headers;

    @Override
    public void doCommand() {
        WebResource resource = context.getResource().path(path);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            resource = resource.queryParam(key, value);
        }
        Builder builder = resource.accept(mediaType);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder = builder.header(key, value);
        }
        context.getAuthentication().applyTo(builder);
        final ClientResponse response = builder.method(operation.name(), ClientResponse.class);
        System.out.println(response.toString());
        System.out.println(response.getEntity(String.class));
    }
}
