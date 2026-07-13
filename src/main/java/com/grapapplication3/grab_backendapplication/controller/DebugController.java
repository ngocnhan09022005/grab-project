package com.grapapplication3.grab_backendapplication.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/debug/db")
@RequiredArgsConstructor
public class DebugController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/khach-hang/describe")
    public List<Map<String, Object>> describe() {
        return jdbcTemplate.queryForList("DESCRIBE KHACH_HANG");
    }

    @GetMapping("/khach-hang/rows")
    public List<Map<String, Object>> rows() {
        return jdbcTemplate.queryForList("SELECT * FROM KHACH_HANG LIMIT 100");
    }

    @GetMapping("/xe/describe")
    public List<Map<String, Object>> describeXe() {
        return jdbcTemplate.queryForList("DESCRIBE XE");
    }

    @GetMapping("/xe/rows")
    public List<Map<String, Object>> rowsXe() {
        return jdbcTemplate.queryForList("SELECT * FROM XE LIMIT 100");
    }

    @GetMapping("/chuyen-di/describe")
    public List<Map<String, Object>> describeChuyenDi() {
        return jdbcTemplate.queryForList("DESCRIBE CHUYEN_DI");
    }

    @GetMapping("/chuyen-di/rows")
    public List<Map<String, Object>> rowsChuyenDi() {
        return jdbcTemplate.queryForList("SELECT * FROM CHUYEN_DI LIMIT 100");
    }

    @GetMapping("/khach-hang/fix-columns")
    public String fixColumns() {
        try {
            // Drop duplicate snake_case columns if they exist
            jdbcTemplate.execute("ALTER TABLE KHACH_HANG DROP COLUMN IF EXISTS ho_ten");
            jdbcTemplate.execute("ALTER TABLE KHACH_HANG DROP COLUMN IF EXISTS so_dien_thoai");
            jdbcTemplate.execute("ALTER TABLE KHACH_HANG DROP COLUMN IF EXISTS ngay_dang_ky");
            return "ok: dropped duplicate columns (if they existed)";
        } catch (Exception ex) {
            return "error: " + ex.getMessage();
        }
    }
}
