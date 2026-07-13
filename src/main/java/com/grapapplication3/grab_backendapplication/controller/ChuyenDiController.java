package com.grapapplication3.grab_backendapplication.controller;

import com.grapapplication3.grab_backendapplication.dto.DatXeRequest;
import com.grapapplication3.grab_backendapplication.entity.ChuyenDi;
import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.entity.TaiXe;
import com.grapapplication3.grab_backendapplication.repository.ChuyenDiRepository;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;
import com.grapapplication3.grab_backendapplication.repository.TaiXeRepository;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chuyen-di")
public class ChuyenDiController {

    private final ChuyenDiRepository chuyenDiRepository;
    private final KhachHangRepository khachHangRepository;
    private final TaiXeRepository taiXeRepository;

    public ChuyenDiController(
            ChuyenDiRepository chuyenDiRepository,
            KhachHangRepository khachHangRepository,
            TaiXeRepository taiXeRepository) {

        this.chuyenDiRepository = chuyenDiRepository;
        this.khachHangRepository = khachHangRepository;
        this.taiXeRepository = taiXeRepository;
    }

    @GetMapping
    public List<ChuyenDi> getAll() {
        return chuyenDiRepository.findAll();
    }

    @PostMapping("/dat-xe")
    public ChuyenDi datXe(@RequestBody DatXeRequest request) {

        KhachHang khachHang = khachHangRepository
                .findById(request.getMaKH())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        TaiXe taiXe = taiXeRepository
                .findById(request.getMaTX())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài xế"));

        ChuyenDi chuyenDi = new ChuyenDi();

        chuyenDi.setKhachHang(khachHang);
        chuyenDi.setTaiXe(taiXe);
        chuyenDi.setDiemDon(request.getDiemDon());
        chuyenDi.setDiemDen(request.getDiemDen());
        chuyenDi.setGiaTien(request.getGiaTien());
        chuyenDi.setThoiGianDat(LocalDateTime.now());
        chuyenDi.setTrangThai("Dang xu ly");

        return chuyenDiRepository.save(chuyenDi);
    }

    @PutMapping("/nhan-cuoc/{id}")
    public ChuyenDi nhanCuoc(@PathVariable Integer id) {

        ChuyenDi chuyenDi = chuyenDiRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        chuyenDi.setTrangThai("Da nhan");

        return chuyenDiRepository.save(chuyenDi);
    }

    @PutMapping("/hoan-thanh/{id}")
    public ChuyenDi hoanThanh(@PathVariable Integer id) {

        ChuyenDi chuyenDi = chuyenDiRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        chuyenDi.setTrangThai("Hoan thanh");
        chuyenDi.setThoiGianHoanThanh(LocalDateTime.now());

        return chuyenDiRepository.save(chuyenDi);
    }
}