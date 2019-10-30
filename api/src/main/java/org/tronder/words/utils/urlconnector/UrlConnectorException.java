package org.tronder.words.utils.urlconnector;

import java.util.Map;

public class UrlConnectorException extends RuntimeException {

    public UrlConnectorException(String error) {
        super(error);
    }

    public UrlConnectorException(Map<String, String> data) {
        super(data.get("error"));
    }
}
