package org.tronder.words.utils.urlconnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.tronder.words.dataAccessObject.UserProviderDTO;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UrlConnectorTest {

    private final String url = "http://some.local.url";

    @Mock
    private HttpURLConnection connection;

    @InjectMocks
    private UrlConnector<Map<String, String>> connector = new UrlConnector<>(url);

    @InjectMocks
    private UrlConnector<UserProviderDTO> connectorUser = new UrlConnector<>(url);


    @Test(expected = UrlConnectorException.class)
    public void testInvalidUrl() {
        new UrlConnector<>("localhost");
    }

    @Test(expected = UrlConnectorException.class)
    public void testInvalidResponseCode() throws IOException {
        Mockito.when(connection.getResponseCode()).thenReturn(400);
        Map<String, String> data = new HashMap<>();
        data.put("error", "some weird error");
        Mockito.when(connection.getErrorStream())
                .thenReturn(generateInputStreamFromData(data));
        connector.getDataFromBody(getTypeReference());
    }

    @Test
    public void testValidResponse() throws IOException {
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        Map<String, String> data = new HashMap<>();
        data.put("sub", "some-sub");
        Mockito.when(connection.getInputStream())
                .thenReturn(generateInputStreamFromData(data));
        Map<String, String> dataParsed = connector.getDataFromBody(getTypeReference());
        assertThat(data.get("sub"), is(dataParsed.get("sub")));
        verify(connection, times(1)).connect();
        verify(connection, times(1)).disconnect();
    }

    @Test
    public void testSetJsonData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(connection.getOutputStream()).thenReturn(outputStream);
        Map<String, String> data = new HashMap<>();
        data.put("sub", "some-sub");
        connector.setJsonData(data);
        assertThat(getJson(data), is(outputStream.toString()));
        verify(connection, times(0)).connect();
    }

    @Test
    public void testGetUserDTO() throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("email", "some@email.no");
        data.put("name", "some name");
        data.put("sub", "some-sub");
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        Mockito.when(connection.getInputStream())
                .thenReturn(generateInputStreamFromData(data));
        UserProviderDTO userProviderDTO = connectorUser.getDataFromBody(UserProviderDTO.class);
        assertThat(data.get("email"), is(userProviderDTO.getEmail()));
        assertThat(data.get("name"), is(userProviderDTO.getName()));
        assertThat(data.get("sub"), is(userProviderDTO.getSub()));
    }


    private InputStream generateInputStreamFromData(Object object) throws UnsupportedEncodingException, JsonProcessingException {
        String json = getJson(object);
        return new ByteArrayInputStream(json.getBytes("UTF-8"));
    }


    private String getJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private TypeReference<Map<String, String>> getTypeReference() {
        return new TypeReference<Map<String, String>>() {};
    }
}
