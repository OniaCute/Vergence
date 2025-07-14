package cc.vergence.util.other;

public class Console {
    public void logAuth(String message) {
        logAuth(message, false);
    }

    /**
     * Input a distinctive text with an ID into the log, so that it can be used for troubleshooting and repair when problems occur.This function is not allowed to have the "nameless" option because it is necessary to ensure that the Flag comes from the Vergence Client.
     * @param id a special number to correct where is the error happen.
     * @param text a piece of helpful text to help fix errors.
     */
    public void logFlag(int id, String text) {
        System.out.println("[Vergence] " + "[FLAG] [" + id + "] " + text);
    }

    public void logAuth(String message, boolean nameless) {
        System.out.println(nameless ? "" : "[Vergence] " + "[AUTH] " + message);
    }

    public void logInfo(String message) {
        logInfo(message, false);
    }

    public void logInfo(String message, boolean nameless) {
        System.out.println(nameless ? "" : "[Vergence] " + "[INFO] " + message);
    }

    public void logWarn(String message) {
        logWarn(message, false);
    }

    public void logWarn(String message, boolean nameless) {
        System.out.println(nameless ? "" : "[Vergence] " + "[WARN] " + message);
    }

    public void logError(String message) {
        logError(message, false);
    }

    public void logError(String message, boolean nameless) {
        System.out.println(nameless ? "" : "[Vergence] " + "[ERROR] " + message);
    }
}
