package com.grapapplication3.grab_backendapplication.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.grapapplication3.grab_backendapplication.entity.ChuyenDi;
import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.entity.TaiXe;
import com.grapapplication3.grab_backendapplication.entity.Xe;
import com.grapapplication3.grab_backendapplication.repository.ChuyenDiRepository;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;
import com.grapapplication3.grab_backendapplication.repository.TaiXeRepository;
import com.grapapplication3.grab_backendapplication.repository.XeRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final KhachHangRepository khachHangRepository;
    private final TaiXeRepository taiXeRepository;
    private final XeRepository xeRepository;
    private final ChuyenDiRepository chuyenDiRepository;

    public DataLoader(KhachHangRepository khachHangRepository,
                      TaiXeRepository taiXeRepository,
                      XeRepository xeRepository,
                      ChuyenDiRepository chuyenDiRepository) {
        this.khachHangRepository = khachHangRepository;
        this.taiXeRepository = taiXeRepository;
        this.xeRepository = xeRepository;
        this.chuyenDiRepository = chuyenDiRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (khachHangRepository.count() == 0) {
            KhachHang k1 = new KhachHang();
            k1.setHoTen("Nguyen Van A");
            k1.setSoDienThoai("0123456789");
            k1.setEmail("a@example.com");
            k1.setNgayDangKy(LocalDate.now());
            k1 = khachHangRepository.save(k1);

            KhachHang k2 = new KhachHang();
            k2.setHoTen("Le Thi B");
            k2.setSoDienThoai("0987654321");
            k2.setEmail("b@example.com");
            k2.setNgayDangKy(LocalDate.now());
            k2 = khachHangRepository.save(k2);

            TaiXe t1 = new TaiXe();
            t1.setHoTen("Tran Van C");
            t1.setSoDienThoai("0912345678");
            t1.setCccd("123456789");
            t1.setTrangThai("San sang");
            t1 = taiXeRepository.save(t1);

            Xe x1 = new Xe();
            x1.setTaiXe(t1);
            x1.setBienSoXe("30A-12345");
            x1.setLoaiXe("Bike");
            x1.setMauXe("Red");
            x1 = xeRepository.save(x1);

            ChuyenDi c1 = new ChuyenDi();
            c1.setKhachHang(k1);
            c1.setTaiXe(t1);
            c1.setDiemDon("123 Nguyen Trai");
            c1.setDiemDen("456 Le Loi");
            c1.setGiaTien(new BigDecimal("50000"));
            c1.setThoiGianDat(LocalDateTime.now().minusDays(1));
            c1.setThoiGianHoanThanh(LocalDateTime.now().minusDays(1).plusHours(1));
            c1.setTrangThai("Hoan thanh");
            chuyenDiRepository.save(c1);
        }
    }
}
