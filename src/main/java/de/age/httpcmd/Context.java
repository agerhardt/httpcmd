package de.age.httpcmd;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;

public class Context {
    
    private WebResource resource;
    private Authentication authentication;
    private final MediaType defaultMediaType;
    private final Map<String, String> settings;

    public Context(WebResource resource, MediaType defaultMediaType) {
        this.authentication = Authentication.none();
        this.resource = resource;
        this.defaultMediaType = defaultMediaType;
        this.settings = new HashMap<String, String>();
    }

    public MediaType getDefaultMediaType() {
        return defaultMediaType;
    }

    public WebResource getResource() {
        return resource;
    }

    public Map<String, String> getSettings() {
        return settings;
    }
    
    public void setBase(String base) {
        Client c = new Client();
        resource = c.resource(base);
    }
    
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
    
    public Authentication getAuthentication() {
        return this.authentication;
    }
    
}
