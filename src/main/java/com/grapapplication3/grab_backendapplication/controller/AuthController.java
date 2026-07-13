package com.grapapplication3.grab_backendapplication.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final KhachHangRepository khachHangRepository;

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String hoTen,
                           @RequestParam String soDienThoai,
                           @RequestParam String email,
                           HttpSession session) {

        KhachHang existing = khachHangRepository.findBySoDienThoaiAndEmail(soDienThoai, email);
        if (existing == null) {
            KhachHang kh = new KhachHang();
            kh.setHoTen(hoTen);
            kh.setSoDienThoai(soDienThoai);
            kh.setEmail(email);
            kh.setNgayDangKy(LocalDate.now());
            kh = khachHangRepository.save(kh);
            session.setAttribute("userId", kh.getMaKH());
        } else {
            session.setAttribute("userId", existing.getMaKH());
        }
        return "redirect:/user/dashboard";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String soDienThoai,
                        @RequestParam String email,
                        HttpSession session,
                        Model model) {

        KhachHang kh = khachHangRepository.findBySoDienThoaiAndEmail(soDienThoai, email);
        if (kh == null) {
            model.addAttribute("error", "Không tìm thấy tài khoản. Vui lòng đăng ký.");
            return "login";
        }
        session.setAttribute("userId", kh.getMaKH());
        return "redirect:/user/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
