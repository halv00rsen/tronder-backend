package org.tronder.words.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.tronder.words.mockData.HallmarkMock;
import org.tronder.words.model.Hallmark;
import org.tronder.words.service.HallmarkService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HallmarkController.class)
public class HallmarkControllerTest extends ControllerTest {

    @MockBean
    private HallmarkService hallmarkService;

    private List<Hallmark> hallmarks;

    @Before
    public void init() {
        hallmarks = HallmarkMock.getHallmarks();
    }

    @Test
    public void testGetHallmarksNoAuth() throws Exception {
        validateGetRequest(getRequest(false));
    }

    @Test
    public void testGetHallmarksWithAuth() throws Exception {
        validateGetRequest(getRequest(true));
    }

    private void validateGetRequest(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", hasItem(hallmarks.get(0).getHallmark())))
                .andExpect(jsonPath("$", hasItem(hallmarks.get(1).getHallmark())));
    }

    private ResultActions getRequest(boolean useSession) throws Exception {
        Mockito.when(hallmarkService.getHallmarks()).thenReturn(hallmarks);
        MockHttpServletRequestBuilder getRequest;
        if (useSession) {
            getRequest = getGetRequestWithSession();
        } else {
            getRequest = getGetRequest();
        }
        return mockMvc.perform(getRequest);
    }

    private MockHttpServletRequestBuilder getGetRequestWithSession() {
        return getGetRequest().session(generateAuthSession());
    }

    private MockHttpServletRequestBuilder getGetRequest() {
        return get("/hallmark");
    }

}
