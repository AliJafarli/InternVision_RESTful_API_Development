package com.internvision.internvision_restful_api_development.repository;

import com.internvision.internvision_restful_api_development.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
