package com.grapapplication3.grab_backendapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grapapplication3.grab_backendapplication.service.DeadlockService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/deadlock")
@RequiredArgsConstructor
public class DeadlockController {

    private final DeadlockService deadlockService;

    @GetMapping
    public String page() {
        return "deadlock";
    }

    @PostMapping("/start-tx1")
    public String startTx1(@RequestParam(defaultValue = "1") int chuyenDiId,
            @RequestParam(defaultValue = "1") int taiXeId,
            Model model) {
        deadlockService.startTransaction1Async(chuyenDiId, taiXeId);
        model.addAttribute("msg", "Started Transaction 1 (locks CHUYEN_DI then TAI_XE) - check logs");
        return "deadlock";
    }

    @PostMapping("/start-tx2")
    public String startTx2(@RequestParam(defaultValue = "1") int chuyenDiId,
            @RequestParam(defaultValue = "1") int taiXeId,
            Model model) {
        deadlockService.startTransaction2Async(chuyenDiId, taiXeId);
        model.addAttribute("msg", "Started Transaction 2 (locks TAI_XE then CHUYEN_DI) - check logs");
        return "deadlock";
    }
}
