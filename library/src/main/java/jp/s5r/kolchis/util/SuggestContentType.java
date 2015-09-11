package jp.s5r.kolchis.util;

import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public final class SuggestContentType {

    private SuggestContentType() {
    }

    public static ContentType fromFileExtension(final Uri uri) {
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

            case "mpd":
                return ContentType.MPEG_DASH;

            case "ts":
                return ContentType.TS;

            default:
                return ContentType.UNKNOWN;
        }
    }

    public static ContentType fromHttpRequest(final Uri uri, final OkHttpClient client) throws IOException {
        final Request request = new Request.Builder()
                .url(uri.toString())
                .head()
                .build();

        final Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            final String contentType = response.header("Content-Type", "unknown").toLowerCase();
            if (contentType.contains("x-mpegurl")) {
                return ContentType.HLS;
            } else if (contentType.contains("mpd") || contentType.contains("dash")) {
                return ContentType.MPEG_DASH;
            } else if (contentType.equals("application/vnd.ms-sstr+xml")) {
                return ContentType.SMOOTH_STREAMING;
            } else if (contentType.contains("mp2t")) {
                return ContentType.TS;
            } else if (contentType.contains("mp4")) {
                return ContentType.MP4;
            }
        }

        return ContentType.UNKNOWN;
    }
}
