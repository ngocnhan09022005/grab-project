package com.grapapplication3.grab_backendapplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KhachHangRepository khachHangRepository;

    @Test
    void registerShouldRejectBlankFields() throws Exception {
        mockMvc.perform(post("/register")
                .param("hoTen", "")
                .param("soDienThoai", "")
                .param("email", "")
                .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void registerShouldCreateCustomerAndRedirectToDashboard() throws Exception {
        KhachHang saved = new KhachHang();
        saved.setMaKH(7);
        when(khachHangRepository.save(any(KhachHang.class))).thenReturn(saved);

        mockMvc.perform(post("/register")
                .param("hoTen", "Nguyen Van A")
                .param("soDienThoai", "0909123456")
                .param("email", "nguyen@example.com")
                .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"));

        verify(khachHangRepository).save(any(KhachHang.class));
    }
}
