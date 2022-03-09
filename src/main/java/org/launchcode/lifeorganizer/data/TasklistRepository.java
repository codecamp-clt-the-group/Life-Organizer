package org.launchcode.lifeorganizer.data;

import org.launchcode.lifeorganizer.models.Tasklist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasklistRepository  extends CrudRepository<Tasklist, Integer> {
}
