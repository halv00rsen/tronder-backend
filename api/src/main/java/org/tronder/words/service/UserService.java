package org.tronder.words.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tronder.words.dataAccessObject.UserProviderDTO;
import org.tronder.words.errors.UserNotFoundException;
import org.tronder.words.utils.urlconnector.UrlConnector;
import org.tronder.words.utils.urlconnector.UrlConnectorException;

import java.io.IOException;
import java.util.Map;

@Service
public class UserService {

    @Value("${user.provider.url}")
    private String userProviderUrl;

    public UserProviderDTO getUserInfo(String sub) throws UrlConnectorException {
        UrlConnector<UserProviderDTO> connector = new UrlConnector<>(userProviderUrl);
        try {
            connector.setJsonData(Map.of("sub", sub));
            return connector.getDataFromBody(UserProviderDTO.class);
        } catch (IOException|UrlConnectorException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

}
