package org.launchcode.lifeorganizer.data;

import org.launchcode.lifeorganizer.models.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {
}
