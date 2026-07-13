package com.grapapplication3.grab_backendapplication.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.grapapplication3.grab_backendapplication.dto.DatXeRequest;
import com.grapapplication3.grab_backendapplication.entity.ChuyenDi;
import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.repository.ChuyenDiRepository;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final KhachHangRepository khachHangRepository;
    private final ChuyenDiRepository chuyenDiRepository;

    @GetMapping("/user/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object uid = session.getAttribute("userId");
        if (uid == null) return "redirect:/login";
        Integer userId = (Integer) uid;
        KhachHang kh = khachHangRepository.findById(userId).orElse(null);
        model.addAttribute("user", kh);
        model.addAttribute("history", chuyenDiRepository.findAll());
        return "user-dashboard";
    }

    @PostMapping(value = "/user/dat-xe")
    public String datXe(DatXeRequest request, HttpSession session) {
        Object uid = session.getAttribute("userId");
        if (uid == null) return "redirect:/login";

        KhachHang kh = khachHangRepository.findById((Integer) uid).orElse(null);
        if (kh == null) return "redirect:/login";

        ChuyenDi chuyenDi = new ChuyenDi();
        chuyenDi.setKhachHang(kh);
        chuyenDi.setDiemDon(request.getDiemDon());
        chuyenDi.setDiemDen(request.getDiemDen());
        chuyenDi.setGiaTien(request.getGiaTien());
        chuyenDi.setThoiGianDat(LocalDateTime.now());
        chuyenDi.setTrangThai("Dang xu ly");
        chuyenDiRepository.save(chuyenDi);

        return "redirect:/user/dashboard";
    }
}
