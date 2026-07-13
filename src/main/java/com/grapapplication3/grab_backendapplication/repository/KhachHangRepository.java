package com.grapapplication3.grab_backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grapapplication3.grab_backendapplication.entity.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
	KhachHang findBySoDienThoai(String soDienThoai);
	KhachHang findByEmail(String email);
	KhachHang findBySoDienThoaiAndEmail(String soDienThoai, String email);
}