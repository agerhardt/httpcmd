package de.age.httpcmd;

import com.sun.jersey.api.client.WebResource.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author tumbleweed
 */
public abstract class Authentication {
    
    public abstract void applyTo(Builder builder);
    
    public static Authentication none() {
        return NONE;
    }
    
    public static Authentication basic(String user, String password) {
        return new BasicAuthentication(user, password);
    }
    
    private static final Authentication NONE = new Authentication() {

        @Override
        public void applyTo(Builder builder) {
        }
    };

    
    private static class BasicAuthentication extends Authentication {
        private final String user;
        private final String password;
        
        public BasicAuthentication(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        public void applyTo(Builder builder) {
            String authString = user + ":" + password;
            String base64 = DatatypeConverter.printBase64Binary(authString.getBytes());
            builder.header(HttpHeaders.AUTHORIZATION, base64);
        }
    }
}
