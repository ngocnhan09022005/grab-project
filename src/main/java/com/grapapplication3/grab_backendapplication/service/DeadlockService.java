package com.grapapplication3.grab_backendapplication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeadlockService {

    private static final Logger log = LoggerFactory.getLogger(DeadlockService.class);

    private final JdbcTemplate jdbcTemplate;

    public void startTransaction1Async(int chuyenDiId, int taiXeId) {
        new Thread(() -> {
            try {
                transaction1(chuyenDiId, taiXeId);
            } catch (Exception ex) {
                log.error("transaction1 error", ex);
            }
        }, "tx1-thread").start();
    }

    public void startTransaction2Async(int chuyenDiId, int taiXeId) {
        new Thread(() -> {
            try {
                transaction2(chuyenDiId, taiXeId);
            } catch (Exception ex) {
                log.error("transaction2 error", ex);
            }
        }, "tx2-thread").start();
    }

    @Transactional
    public void transaction1(int chuyenDiId, int taiXeId) {
        log.info("Transaction1 START - locking CHUYEN_DI id={}", chuyenDiId);
        jdbcTemplate.queryForList("SELECT * FROM CHUYEN_DI WHERE id = ? FOR UPDATE", chuyenDiId);
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        log.info("Transaction1 trying to lock TAI_XE id={}", taiXeId);
        jdbcTemplate.queryForList("SELECT * FROM TAI_XE WHERE id = ? FOR UPDATE", taiXeId);
        jdbcTemplate.update("UPDATE CHUYEN_DI SET status = status WHERE id = ?", chuyenDiId);
        log.info("Transaction1 COMMIT - finished");
    }

    @Transactional
    public void transaction2(int chuyenDiId, int taiXeId) {
        log.info("Transaction2 START - locking TAI_XE id={}", taiXeId);
        jdbcTemplate.queryForList("SELECT * FROM TAI_XE WHERE id = ? FOR UPDATE", taiXeId);
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        log.info("Transaction2 trying to lock CHUYEN_DI id={}", chuyenDiId);
        jdbcTemplate.queryForList("SELECT * FROM CHUYEN_DI WHERE id = ? FOR UPDATE", chuyenDiId);
        jdbcTemplate.update("UPDATE TAI_XE SET status = status WHERE id = ?", taiXeId);
        log.info("Transaction2 COMMIT - finished");
    }
}
