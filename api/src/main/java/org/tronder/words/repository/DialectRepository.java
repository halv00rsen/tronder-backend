package org.tronder.words.repository;

import org.springframework.data.repository.CrudRepository;
import org.tronder.words.model.Dialect;

import java.util.List;

public interface DialectRepository extends CrudRepository<Dialect, Integer> {

    List<Dialect> findAllByPublicDialectIsTrue();
    List<Dialect> findAllByPublicDialectIsTrueOrCreatedByEquals(String createdBy);
}
