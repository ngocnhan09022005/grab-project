package com.grapapplication3.grab_backendapplication.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "KHACH_HANG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKH")
    private Integer maKH;
    
    @Column(name = "HoTen")
    private String hoTen;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "ho_ten")
    private String ho_ten;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "so_dien_thoai")
    private String so_dien_thoai;

    @Column(name = "Email")
    private String email;

    @Column(name = "NgayDangKy")
    private LocalDate ngayDangKy;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "ngay_dang_ky")
    private LocalDate ngay_dang_ky;

    public String getHoTen() {
        return hoTen != null ? hoTen : ho_ten;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
        this.ho_ten = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai != null ? soDienThoai : so_dien_thoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
        this.so_dien_thoai = soDienThoai;
    }

    public LocalDate getNgayDangKy() {
        return ngayDangKy != null ? ngayDangKy : ngay_dang_ky;
    }

    public void setNgayDangKy(LocalDate ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
        this.ngay_dang_ky = ngayDangKy;
    }
}