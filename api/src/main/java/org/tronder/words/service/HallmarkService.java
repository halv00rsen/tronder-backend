package org.tronder.words.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tronder.words.model.Hallmark;
import org.tronder.words.repository.HallmarkRepository;

import java.util.List;

@Service
public class HallmarkService {

    private final HallmarkRepository hallmarkRepository;

    @Autowired
    public HallmarkService(HallmarkRepository hallmarkRepository) {
        this.hallmarkRepository = hallmarkRepository;
    }

    public Iterable<Hallmark> saveHallmarks(Iterable<Hallmark> saveHallmarks) {
        return hallmarkRepository.saveAll(saveHallmarks);
    }

    public List<Hallmark> getHallmarks() {
        return hallmarkRepository.findAll();
    }

}
