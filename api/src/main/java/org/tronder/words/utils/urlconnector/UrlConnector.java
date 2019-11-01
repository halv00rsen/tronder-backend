package org.tronder.words.utils.urlconnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UrlConnector<T> {

    private static final int STATUS_OK = 200;

    private HttpURLConnection connection;


    public UrlConnector(String url) throws UrlConnectorException {
        try {
            URL urlInstance = new URL(url);
            connection = (HttpURLConnection) urlInstance.openConnection();
            connection.setRequestMethod("GET");
        } catch (IOException e) {
            throw new UrlConnectorException("Could not create url connector instance");
        }
    }


    public void setJsonData(Object object) throws IOException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        writeDataToBody(serializeJson(object));
    }


    private String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }


    private void writeDataToBody(String data) throws IOException {
        OutputStream outputStream = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        writer.write(data);
        writer.flush();
        writer.close();
        outputStream.close();
    }


    public T getDataFromBody(TypeReference<T> typeReference) throws IOException {
        checkValidResponse();
        T data = getSuccessData(typeReference);
        connection.disconnect();
        return data;
    }


    public T getDataFromBody(Class<T> className) throws IOException {
        checkValidResponse();
        T data = getSuccessData(className);
        connection.disconnect();
        return data;
    }


    private void checkValidResponse() throws IOException {
        int responseCode = connectAndGetResponseCode();
        if (!isResponseOk(responseCode)) {
            Map<String, String> errors = getErrors();
            connection.disconnect();
            throw new UrlConnectorException(errors);
        }
    }


    private T getSuccessData(TypeReference<T> typeReference) throws IOException {
        InputStream inputStream = connection.getInputStream();
        return new ObjectMapper().readValue(inputStream, typeReference);
    }


    private T getSuccessData(Class<T> className) throws IOException {
        InputStream inputStream = connection.getInputStream();
        return new ObjectMapper().readValue(inputStream, className);
    }


    private Map<String, String> getErrors() throws IOException {
        InputStream errorStream = connection.getErrorStream();
        return new ObjectMapper().readValue(errorStream, new TypeReference<Map<String, String>>() {});
    }


    private boolean isResponseOk(int responseCode) {
        return responseCode == STATUS_OK;
    }


    private int connectAndGetResponseCode() throws IOException {
        connection.connect();
        return connection.getResponseCode();
    }

}
