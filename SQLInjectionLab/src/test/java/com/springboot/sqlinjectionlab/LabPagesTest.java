package com.springboot.sqlinjectionlab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LabPagesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicLabPagesRender() throws Exception {
        mockMvc.perform(get("/lab"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Offensive Web Lab")));

        mockMvc.perform(get("/lab/union").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("payload-list")));

        mockMvc.perform(get("/lab/sql/error").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Error Based")));

        mockMvc.perform(get("/lab/sql/boolean").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Boolean Blind")));

        mockMvc.perform(get("/lab/sql/time").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Time Blind")));

        mockMvc.perform(get("/lab/xss/reflected").param("q", "safe-keyword"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("safe-keyword")));

        mockMvc.perform(get("/lab/xss/stored"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("stored-author")));

        mockMvc.perform(get("/lab/discovery"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Directory Brute Force")));

        mockMvc.perform(get("/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User-agent")));

        mockMvc.perform(get("/admin-panel/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Legacy Admin")));

        mockMvc.perform(get("/private/flag.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("FLAG{directory_brute_force_complete}")));
    }

    @Test
    void vulnerableParametersRespondToTrainingPayloads() throws Exception {
        mockMvc.perform(get("/lab/union").param("q", "%' UNION SELECT 999,'lab-u','lab-u@test','user','p' -- "))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("lab-u")));

        mockMvc.perform(get("/lab/sql/error")
                        .param("id", "1 AND updatexml(1,concat(0x7e,'lab-echo',0x7e),1)"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("lab-echo")));

        mockMvc.perform(get("/lab/sql/boolean")
                        .param("id", "1 AND substring(database(),1,1)='s'"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("notice")));
    }
}
