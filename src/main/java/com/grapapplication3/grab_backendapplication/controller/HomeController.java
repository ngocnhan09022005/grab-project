package com.grapapplication3.grab_backendapplication.controller;

import java.util.Collections;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.grapapplication3.grab_backendapplication.repository.ChuyenDiRepository;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;
import com.grapapplication3.grab_backendapplication.repository.TaiXeRepository;
import com.grapapplication3.grab_backendapplication.repository.XeRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final KhachHangRepository khachHangRepository;
    private final ChuyenDiRepository chuyenDiRepository;
    private final TaiXeRepository taiXeRepository;
    private final XeRepository xeRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        try {
            model.addAttribute("khachHangs", khachHangRepository.findAll());
            model.addAttribute("totalKhachHang", khachHangRepository.count());
        } catch (Exception ex) {
            System.err.println("HomeController: khachHang error: " + ex.getMessage());
            model.addAttribute("khachHangs", Collections.emptyList());
            model.addAttribute("totalKhachHang", 0);
        }

        try {
            model.addAttribute("chuyenDis", chuyenDiRepository.findAll());
            model.addAttribute("totalChuyenDi", chuyenDiRepository.count());
        } catch (Exception ex) {
            System.err.println("HomeController: chuyenDi error: " + ex.getMessage());
            model.addAttribute("chuyenDis", Collections.emptyList());
            model.addAttribute("totalChuyenDi", 0);
        }

        try {
            model.addAttribute("taiXes", taiXeRepository.findAll());
            model.addAttribute("totalTaiXe", taiXeRepository.count());
        } catch (Exception ex) {
            System.err.println("HomeController: taiXe error: " + ex.getMessage());
            model.addAttribute("taiXes", Collections.emptyList());
            model.addAttribute("totalTaiXe", 0);
        }

        try {
            model.addAttribute("xes", xeRepository.findAll());
            model.addAttribute("totalXe", xeRepository.count());
        } catch (Exception ex) {
            System.err.println("HomeController: xe error: " + ex.getMessage());
            model.addAttribute("xes", Collections.emptyList());
            model.addAttribute("totalXe", 0);
        }

        return "index";
    }
}
