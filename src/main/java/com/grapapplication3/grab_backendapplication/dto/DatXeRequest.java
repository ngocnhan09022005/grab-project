package com.grapapplication3.grab_backendapplication.dto;

import java.math.BigDecimal;

public class DatXeRequest {

    private Integer maKH;
    private Integer maTX;
    private String diemDon;
    private String diemDen;
    private BigDecimal giaTien;

    public Integer getMaKH() {
        return maKH;
    }

    public void setMaKH(Integer maKH) {
        this.maKH = maKH;
    }

    public Integer getMaTX() {
        return maTX;
    }

    public void setMaTX(Integer maTX) {
        this.maTX = maTX;
    }

    public String getDiemDon() {
        return diemDon;
    }

    public void setDiemDon(String diemDon) {
        this.diemDon = diemDon;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    public BigDecimal getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(BigDecimal giaTien) {
        this.giaTien = giaTien;
    }
}