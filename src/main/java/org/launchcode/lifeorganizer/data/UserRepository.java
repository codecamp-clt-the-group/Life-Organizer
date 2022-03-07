package org.launchcode.lifeorganizer.data;


import org.launchcode.lifeorganizer.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {

}
