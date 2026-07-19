package com.grapapplication3.grab_backendapplication.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grapapplication3.grab_backendapplication.entity.ChuyenDi;
import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.entity.TaiXe;
import com.grapapplication3.grab_backendapplication.repository.ChuyenDiRepository;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;
import com.grapapplication3.grab_backendapplication.repository.TaiXeRepository;
import com.grapapplication3.grab_backendapplication.service.BookingScenario;
import com.grapapplication3.grab_backendapplication.service.BookingTransactionService;
import com.grapapplication3.grab_backendapplication.service.exception.CustomerNotFoundException;
import com.grapapplication3.grab_backendapplication.service.exception.DeadlockException;
import com.grapapplication3.grab_backendapplication.service.exception.DriverBusyException;
import com.grapapplication3.grab_backendapplication.service.exception.DriverNotFoundException;
import com.grapapplication3.grab_backendapplication.service.exception.PaymentFailedException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final KhachHangRepository khachHangRepository;
    private final ChuyenDiRepository chuyenDiRepository;
    private final TaiXeRepository taiXeRepository;
    private final BookingTransactionService bookingTransactionService;

    @GetMapping("/user/dashboard")
    public String dashboard(HttpSession session, Model model) {
        KhachHang kh = null;
        Object uid = session.getAttribute("userId");
        if (uid != null) {
            Integer userId = (Integer) uid;
            kh = khachHangRepository.findById(userId).orElse(null);
        }
        if (kh == null) {
            kh = new KhachHang();
            kh.setHoTen("Khách");
        }
        model.addAttribute("user", kh);
        model.addAttribute("history", chuyenDiRepository.findAll());
        model.addAttribute("availableDrivers", taiXeRepository.findAll().stream()
                .filter(this::isDriverAvailable)
                .toList());
        return "user-dashboard";
    }

    @GetMapping("/user")
    public String userRoot() {
        return "redirect:/user/dashboard";
    }

    @PostMapping(value = "/user/dat-xe")
    public String datXe(@RequestParam String diemDon,
                        @RequestParam String diemDen,
                        @RequestParam Integer soKm,
                        @RequestParam(required = false, defaultValue = "1") Integer maTX,
                        @RequestParam(required = false) String serviceType,
                        @RequestParam(required = false) String transactionScenario,
                        HttpSession session,
                        Model model) {
        KhachHang kh = null;
        Object uid = session.getAttribute("userId");
        if (uid != null) {
            kh = khachHangRepository.findById((Integer) uid).orElse(null);
        }
        if (kh == null) {
            kh = new KhachHang();
            kh.setHoTen("Khách");
            kh.setSoDienThoai("0000000000");
            kh.setEmail("guest@example.com");
            kh.setPassword("guest");
            kh.setNgayDangKy(LocalDate.now());
            kh = khachHangRepository.save(kh);
        }

        BigDecimal giaTien = BigDecimal.valueOf(soKm).multiply(BigDecimal.valueOf(4500));
        model.addAttribute("calculatedPrice", giaTien);
        model.addAttribute("distanceKm", soKm);
        model.addAttribute("diemDon", diemDon);
        model.addAttribute("diemDen", diemDen);
        model.addAttribute("soKm", soKm);
        model.addAttribute("selectedDriverId", maTX);
        model.addAttribute("serviceType", serviceType != null ? serviceType : "TAXI");

        List<TaiXe> availableDrivers = new java.util.ArrayList<>(
                taiXeRepository.findAll().stream()
                        .filter(this::isDriverAvailable)
                        .toList());

        ChuyenDi createdTrip = null;
        try {
            BookingScenario scenario = resolveScenario(transactionScenario, maTX, giaTien, diemDon, diemDen);
            createdTrip = bookingTransactionService.processBooking(kh.getMaKH(), maTX, diemDon, diemDen, giaTien, scenario);
            model.addAttribute("success", "Đặt chuyến thành công.");
        } catch (CustomerNotFoundException | DriverNotFoundException | DeadlockException | PaymentFailedException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (DriverBusyException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("driverBusy", true);
            model.addAttribute("busyDriverMessage", ex.getMessage());
            if (maTX != null) {
                taiXeRepository.findById(maTX).ifPresent(selectedDriver -> {
                    if (availableDrivers.stream().noneMatch(driver -> driver.getMaTX().equals(selectedDriver.getMaTX()))) {
                        availableDrivers.add(selectedDriver);
                    }
                });
            }
        }

        model.addAttribute("user", kh);
        model.addAttribute("history", chuyenDiRepository.findAll());
        model.addAttribute("availableDrivers", availableDrivers);
        model.addAttribute("selectedScenario", transactionScenario);
        if (createdTrip != null) {
            return "redirect:/user/chuyen-di/" + createdTrip.getMaChuyen();
        }
        return "user-dashboard";
    }

    @GetMapping("/user/chuyen-di/{id}")
    public String tripDetail(@PathVariable Integer id, HttpSession session, Model model) {
        ChuyenDi trip = chuyenDiRepository.findById(id).orElse(null);
        if (trip == null) {
            return "redirect:/user/dashboard";
        }

        KhachHang kh = null;
        Object uid = session.getAttribute("userId");
        if (uid != null) {
            kh = khachHangRepository.findById((Integer) uid).orElse(null);
        }
        if (kh == null && trip.getKhachHang() != null) {
            kh = trip.getKhachHang();
        }
        if (kh == null) {
            kh = new KhachHang();
            kh.setHoTen("Khách");
        }

        model.addAttribute("user", kh);
        model.addAttribute("trip", trip);
        return "trip-detail";
    }

    @PostMapping("/user/chuyen-di/{id}/hoan-thanh")
    public String completeTrip(@PathVariable Integer id,
                               @RequestParam String phuongThucThanhToan,
                               HttpSession session,
                               Model model) {
        try {
            ChuyenDi trip = chuyenDiRepository.findById(id).orElse(null);
            if (trip != null) {
                Object uid = session.getAttribute("userId");
                if (uid != null) {
                    KhachHang kh = khachHangRepository.findById((Integer) uid).orElse(null);
                    if (kh != null) {
                        trip.setKhachHang(kh);
                        chuyenDiRepository.save(trip);
                    }
                }
            }
            bookingTransactionService.completeTrip(id, phuongThucThanhToan);
            model.addAttribute("success", "Chuyến đi đã hoàn tất. Tài xế đã trở về trạng thái rảnh.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "redirect:/user/dashboard?completed=true";
    }

    @PostMapping("/user/chuyen-di/{id}/huy")
    public String cancelTrip(@PathVariable Integer id, HttpSession session, Model model) {
        try {
            bookingTransactionService.cancelTrip(id);
            model.addAttribute("success", "Chuyến đi đã được hủy.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "redirect:/user/dashboard?cancelled=true";
    }

    private boolean isDriverAvailable(TaiXe driver) {
        if (driver == null || driver.getTrangThai() == null) {
            return false;
        }
        String status = driver.getTrangThai().trim().toLowerCase(Locale.ROOT);
        return status.contains("online") || status.contains("san sang") || status.contains("sẵn sàng") || status.contains("ready");
    }

    private BookingScenario resolveScenario(String transactionScenario, Integer driverId, BigDecimal amount, String pickup, String destination) {
        if (transactionScenario != null && !transactionScenario.isBlank()) {
            return BookingScenario.fromString(transactionScenario);
        }

        String combined = String.join(" ",
                pickup == null ? "" : pickup,
                destination == null ? "" : destination).toLowerCase(Locale.ROOT);

        if (combined.contains("deadlock")) {
            return BookingScenario.DEADLOCK;
        }
        if (driverId != null && driverId == 999) {
            return BookingScenario.DRIVER_NOT_FOUND;
        }
        if (amount != null && amount.compareTo(new BigDecimal("100000000")) >= 0) {
            return BookingScenario.PAYMENT_FAILURE;
        }
        if (combined.contains("customer")) {
            return BookingScenario.CUSTOMER_NOT_FOUND;
        }
        return BookingScenario.STANDARD;
    }
}
