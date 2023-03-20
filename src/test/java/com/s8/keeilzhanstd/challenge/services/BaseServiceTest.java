package com.s8.keeilzhanstd.challenge.services;

import com.s8.keeilzhanstd.challenge.S8Application;
import com.s8.keeilzhanstd.challenge.config.JwTokenService;
import com.s8.keeilzhanstd.challenge.models.user.UserRepository;
import com.s8.keeilzhanstd.challenge.testcontainers.config.ContainersEnvironment;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = S8Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseServiceTest extends ContainersEnvironment {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    protected JwTokenService jwTokenService;
    @Autowired
    protected FxRatesService fxRatesService;
    @Autowired
    protected ValidationService validationService;

//    @Autowired
//    protected KafkaTemplate<String, String> kafkaTemplate;
//    @Autowired
//    protected Consumer<String, String> consumer;


}
