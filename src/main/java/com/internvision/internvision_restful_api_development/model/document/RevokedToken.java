package com.internvision.internvision_restful_api_development.model.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@Document(collection = "revoked_tokens")
public class RevokedToken {
    @Id
    private String id;
    private String token;
    private Instant revokedAt;
    private Date expiresAt;

}