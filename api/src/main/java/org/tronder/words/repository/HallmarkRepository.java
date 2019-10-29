package org.tronder.words.repository;

import org.springframework.data.repository.CrudRepository;
import org.tronder.words.model.Hallmark;

import java.util.List;

public interface HallmarkRepository extends CrudRepository<Hallmark, String> {

    List<Hallmark> findAll();
}
