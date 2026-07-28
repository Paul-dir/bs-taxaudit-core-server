package com.act.taxaudit.integration;

import com.act.taxaudit.application.usecase.StartDeskAuditUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class DeskAuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StartDeskAuditUseCase startDeskAuditUseCase;

    @Test
    public void testStartDeskAudit() throws Exception {
        UUID auditCaseId = UUID.randomUUID();
        String tin = "1234567890";

        com.act.taxaudit.domain.aggregate.DeskAudit deskAudit = org.mockito.Mockito.mock(com.act.taxaudit.domain.aggregate.DeskAudit.class);
        when(startDeskAuditUseCase.execute(any(UUID.class), any(String.class))).thenReturn(deskAudit);

        mockMvc.perform(post("/desk-audits")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"auditCaseId\":\"" + auditCaseId + "\",\"tin\":\"" + tin + "\"}"))
            .andExpect(status().isCreated());
    }
}
