package com.internvision.internvision_restful_api_development.repository;

import com.internvision.internvision_restful_api_development.model.document.RevokedToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevokedTokenRepository extends MongoRepository<RevokedToken, String> {
    Optional<RevokedToken> findByToken(String token);

}
