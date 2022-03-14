package org.launchcode.lifeorganizer.data;

import org.launchcode.lifeorganizer.models.DefaultTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultTaskRepository extends CrudRepository<DefaultTask, Integer> {
}
