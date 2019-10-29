package org.tronder.words.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tronder.words.mockData.HallmarkMock;
import org.tronder.words.model.Hallmark;
import org.tronder.words.repository.HallmarkRepository;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class HallmarkServiceTest {

    @MockBean
    private HallmarkRepository hallmarkRepository;

    private HallmarkService hallmarkService;
    private List<Hallmark> hallmarks;

    @Before
    public void init() {
        hallmarks = HallmarkMock.getHallmarks();
        hallmarkService = new HallmarkService(hallmarkRepository);
    }

    @Test
    public void testSaveHallmarks() {
        Mockito.when(hallmarkRepository.saveAll(hallmarks)).thenReturn(hallmarks);
        Iterator<Hallmark> savedEntities = hallmarkService.saveHallmarks(hallmarks).iterator();
        int counter = 0;
        while (savedEntities.hasNext()) {
            counter += 1;
            savedEntities.next();
        }
        assertThat(counter, is(2));
    }

    @Test
    public void testGetHallmarks() {
        Mockito.when(hallmarkRepository.findAll()).thenReturn(hallmarks);
        List<Hallmark> hallmarks = hallmarkService.getHallmarks();
        assertThat(hallmarks.size(), is(2));
    }

}
