package com.grapapplication3.grab_backendapplication.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CHUYEN_DI")
public class ChuyenDi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChuyen")
    private Integer maChuyen;

    @ManyToOne
    @JoinColumn(name = "MaKH")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "MaTX")
    private TaiXe taiXe;

    @Column(name = "DiemDon")
    private String diemDon;

    @Column(name = "DiemDen")
    private String diemDen;

    @Column(name = "ThoiGianDat")
    private LocalDateTime thoiGianDat;

    @Column(name = "ThoiGianHoanThanh")
    private LocalDateTime thoiGianHoanThanh;

    @Column(name = "GiaTien")
    private BigDecimal giaTien;

    @Column(name = "TrangThai")
    private String trangThai;
}