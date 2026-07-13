package com.grapapplication3.grab_backendapplication.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("ALTER TABLE KHACH_HANG DROP COLUMN IF EXISTS ho_ten");
            jdbcTemplate.execute("ALTER TABLE KHACH_HANG DROP COLUMN IF EXISTS so_dien_thoai");
            jdbcTemplate.execute("ALTER TABLE KHACH_HANG DROP COLUMN IF EXISTS ngay_dang_ky");
            System.out.println("DatabaseMigration: duplicate KHACH_HANG columns dropped if present.");
        } catch (Exception ex) {
            System.err.println("DatabaseMigration failed: " + ex.getMessage());
        }
    }
}
