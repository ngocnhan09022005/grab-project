package com.grapapplication3.grab_backendapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.grapapplication3.grab_backendapplication.repository.ChuyenDiRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ChuyenDiRepository chuyenDiRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("chuyenDis", chuyenDiRepository.findAll());
        return "admin-dashboard";
    }
}
