package jp.s5r.kolchis.mediaplayer;

public class MediaPlayerException extends Exception {

    public static final int UNKNOWN_ERROR = 0;

    private int errorCode = UNKNOWN_ERROR;

    public MediaPlayerException() {
    }

    public MediaPlayerException(String detailMessage) {
        super(detailMessage);
    }

    public MediaPlayerException(int errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public MediaPlayerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MediaPlayerException(Throwable throwable) {
        super(throwable);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
