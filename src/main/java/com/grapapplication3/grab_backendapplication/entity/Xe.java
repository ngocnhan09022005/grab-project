package com.grapapplication3.grab_backendapplication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "XE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Xe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaXe")
    private Integer maXe;

    @ManyToOne
    @JoinColumn(name = "MaTX")
    private TaiXe taiXe;

    @Column(name = "BienSoXe")
    private String bienSoXe;

    @Column(name = "LoaiXe")
    private String loaiXe;

    @Column(name = "MauXe")
    private String mauXe;
}