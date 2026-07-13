package com.grapapplication3.grab_backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grapapplication3.grab_backendapplication.entity.Xe;

public interface XeRepository extends JpaRepository<Xe, Integer> {
}