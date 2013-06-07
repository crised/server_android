package com.heron.provider;

import java.io.IOException;

import org.apache.http.HttpResponse;

import android.net.Uri;

public interface ResponseHandler {
    void handleResponse(HttpResponse response, Uri uri)
            throws IOException;
}
