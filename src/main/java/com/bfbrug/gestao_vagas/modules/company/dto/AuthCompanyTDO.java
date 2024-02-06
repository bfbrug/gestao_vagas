package com.bfbrug.gestao_vagas.modules.company.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyTDO {

    private String password;
    private String username;
}
