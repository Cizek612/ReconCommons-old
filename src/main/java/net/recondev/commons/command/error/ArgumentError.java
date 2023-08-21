package net.recondev.commons.command.error;

public class ArgumentError
        extends Error {
    private final Throwable back;
    private final String prettyMessage;

    public ArgumentError(final String cause, final String prettyMessage, final Throwable back) {
        super(cause, null, false, false);
        this.prettyMessage = prettyMessage;
        this.back = back;
    }

    public Throwable getBack() {
        return this.back;
    }

    public String getPrettyMessage() {
        return this.prettyMessage;
    }
}