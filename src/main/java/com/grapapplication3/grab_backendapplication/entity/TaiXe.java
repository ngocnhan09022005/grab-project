package com.grapapplication3.grab_backendapplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TAI_XE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiXe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTX")
    private Integer maTX;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "CCCD")
    private String cccd;

    @Column(name = "TrangThai")
    private String trangThai;
}