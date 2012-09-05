package de.age.httpcmd;

public interface Command {
    
    public static final Command NONE = new Command() {

        @Override
        public void doCommand() {
        }
    };
    
    void doCommand();
    
}
