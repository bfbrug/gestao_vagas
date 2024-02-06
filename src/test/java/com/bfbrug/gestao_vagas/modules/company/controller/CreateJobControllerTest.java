package com.bfbrug.gestao_vagas.modules.company.controller;

import com.bfbrug.gestao_vagas.exceptions.CompanyNotFoundException;
import com.bfbrug.gestao_vagas.modules.company.dto.CreateJobDTO;
import com.bfbrug.gestao_vagas.modules.company.entities.CompanyEntity;
import com.bfbrug.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.bfbrug.gestao_vagas.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CompanyRepository companyRepository;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void should_be_able_to_create_a_new_job() throws Exception {

        var company = CompanyEntity.builder()
                .description("Company_description")
                .email("email@company.com")
                .password("123456")
                .username("company_username")
                .name("company_name")
                .build();

        company = companyRepository.saveAndFlush(company);

        var createJobDto = CreateJobDTO.builder()
                .benefits("BENEFITS_TEST")
                .description("DESCRIPTION_TEST")
                .level("LEVEL_TEST")
                .build();

        var result = mvc.perform(MockMvcRequestBuilders.post("/company/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objecToJSON(createJobDto))
                        .header("Authorization",
                                TestUtils.genereteToken(company.getId(),
                                        "JAVAGAS_@#23456*@")))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void should_not_be_able_to_create_a_new_job_if_company_not_found() throws Exception {

        var createJobDto = CreateJobDTO.builder()
                .benefits("BENEFITS_TEST")
                .description("DESCRIPTION_TEST")
                .level("LEVEL_TEST")
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/company/job")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objecToJSON(createJobDto))
                .header("Authorization",
                        TestUtils.genereteToken(UUID.randomUUID(),
                                "JAVAGAS_@#23456*@")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
