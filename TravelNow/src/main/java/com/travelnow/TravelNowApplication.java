package com.travelnow;

import com.travelnow.core.dbacces.models.security.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class TravelNowApplication {

public static void main(String[] args) {
	SpringApplication.run(TravelNowApplication.class, args);
}

}
