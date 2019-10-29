package org.tronder.words.repository;

import org.springframework.data.repository.CrudRepository;
import org.tronder.words.model.WordEntity;

public interface WordRepository extends CrudRepository<WordEntity, Integer> {

}
