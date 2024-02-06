package com.bfbrug.gestao_vagas.modules.candidate.useCases;

import com.bfbrug.gestao_vagas.exceptions.UserNotFoundException;
import com.bfbrug.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import com.bfbrug.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    public ProfileCandidateResponseDTO execute(UUID idCandidate){
        var candidate = this.candidateRepository.findById(idCandidate)
                .orElseThrow(() -> {
                    throw new UserNotFoundException();
                });

        var candidateDTO = ProfileCandidateResponseDTO.builder()
                .name(candidate.getName())
                .username(candidate.getUsername())
                .email(candidate.getEmail())
                .description(candidate.getDescription())
                .id(candidate.getId())
                .build();

        return candidateDTO;
    }
}
