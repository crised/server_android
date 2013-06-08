package com.heron.provider;

import java.io.IOException;
import java.io.InputStream;

public interface ResponseHandler {
    void handleResponse(InputStream in)
            throws IOException;
}
