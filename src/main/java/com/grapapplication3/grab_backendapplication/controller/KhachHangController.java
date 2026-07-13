package com.grapapplication3.grab_backendapplication.controller;

import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {

    private final KhachHangRepository repository;

    @GetMapping
    public List<KhachHang> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public KhachHang create(@RequestBody KhachHang khachHang) {
        return repository.save(khachHang);
    }
}