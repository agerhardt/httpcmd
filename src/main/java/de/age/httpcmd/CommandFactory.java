package de.age.httpcmd;

public interface CommandFactory {

    Command createCommand(Context context, String... args);
}
