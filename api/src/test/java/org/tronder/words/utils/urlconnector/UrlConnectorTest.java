package org.tronder.words.utils.urlconnector;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlConnectorTest {


//    private URL url = Mockito.mock(URL.class);
    private HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);

    @Test(expected = UrlConnectorException.class)
    public void testInvalidUrl() {
        new UrlConnector<>("localhost");
    }

    @Test
    public void testInit() throws IOException {
//        URL url = new URL("http://localhost:5000");
//        URL spyUrl = Mockito.spy(url);
//        Mockito.doReturn(connection).when(spyUrl).openConnection();
        new UrlConnector<>("http://localhost:5000");
    }
}
