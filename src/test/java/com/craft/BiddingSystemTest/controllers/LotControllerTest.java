package com.craft.BiddingSystemTest.controllers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class LotControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void showLots_ActiveIsNull_ShouldReturnLots() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lot/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("lots"));
    }

    @Test
    public void showLots_ActiveIsTrue_ShouldReturnLots() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lot/all/true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("lots"));
    }

    @Test
    public void showLots_ActiveIsFalse_ShouldReturnLots() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lot/all/false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("lots"));
    }

    @Test
    public void showLot_NotAuthUser_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lot/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void showLotCreationForm_NotAuthUser_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lot/create"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void deleteLot_NotAuthUser_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lot/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void showLot_AuthUser_ShouldReturn200Or404() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/lot/1"))
                .andDo(print())
                .andReturn();

        int resultStatus = mvcResult.getResponse().getStatus();
        Assertions.assertTrue(resultStatus == 200 || resultStatus == 404);
    }

    @Test
    @WithMockUser(roles = {"USER", "AUCTIONEER"})
    public void showLotCreationForm_AuthUser_ShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lot/create"))
                .andDo(print())
                .andExpect(status().isOk())
        .andExpect(view().name("lotCreateForm"));
    }
}
