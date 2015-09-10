package jp.s5r.kolchis.util;

import android.net.Uri;

public final class SuggestContentType {
    public enum ContentType {
        UNKNOWN,
        MP4,
        HLS,
        MPEG_DASH,
        SMOOTH_STREAMING,
        TS
    }

    private SuggestContentType() {
    }

    public static ContentType fromExtension(Uri uri) {
        final String lastPassSegment = uri.getLastPathSegment();
        final int position = lastPassSegment.lastIndexOf('.');
        final String extension = lastPassSegment.substring(position, lastPassSegment.length());
        switch (extension) {
            case "mp4":
            case "m4v":
                return ContentType.MP4;

            case "m3u8":
                return ContentType.HLS;

            case "ism":
                return ContentType.SMOOTH_STREAMING;

            case "ts":
                return ContentType.TS;

            default:
                return ContentType.UNKNOWN;
        }
    }

    public static ContentType fromMimeType(Uri uri) {
        return ContentType.UNKNOWN;
    }
}
