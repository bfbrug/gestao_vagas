package com.bfbrug.gestao_vagas.modules.company.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bfbrug.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import com.bfbrug.gestao_vagas.modules.company.dto.AuthCompanyTDO;
import com.bfbrug.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthCompanyUseCase {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyTDO authCompanyTDO) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyTDO.getUsername()).orElseThrow(
                () ->{
                    throw new UsernameNotFoundException("Username/password incorrect");
                });

        var passwordMatches = this.passwordEncoder.matches(authCompanyTDO.getPassword(), company.getPassword());

        if(!passwordMatches){
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiredIn = Instant.now().plus(Duration.ofMinutes(10));

        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiredIn)
                .withSubject(company.getId().toString())
                .withClaim("roles", Arrays.asList("COMPANY"))
                .sign(algorithm);

        var authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
                .access_token(token)
                .expires_in(expiredIn.toEpochMilli())
                .build();

        return authCompanyResponseDTO;
    }
}
