package com.grapapplication3.grab_backendapplication.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grapapplication3.grab_backendapplication.entity.ChuyenDi;
import com.grapapplication3.grab_backendapplication.entity.KhachHang;
import com.grapapplication3.grab_backendapplication.entity.TaiXe;
import com.grapapplication3.grab_backendapplication.repository.ChuyenDiRepository;
import com.grapapplication3.grab_backendapplication.repository.KhachHangRepository;
import com.grapapplication3.grab_backendapplication.repository.TaiXeRepository;
import com.grapapplication3.grab_backendapplication.service.exception.CustomerNotFoundException;
import com.grapapplication3.grab_backendapplication.service.exception.DeadlockException;
import com.grapapplication3.grab_backendapplication.service.exception.DriverBusyException;
import com.grapapplication3.grab_backendapplication.service.exception.DriverNotFoundException;
import com.grapapplication3.grab_backendapplication.service.exception.PaymentFailedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingTransactionService {

    private final KhachHangRepository khachHangRepository;
    private final TaiXeRepository taiXeRepository;
    private final ChuyenDiRepository chuyenDiRepository;

    private boolean isDriverAvailable(TaiXe driver) {
        if (driver == null || driver.getTrangThai() == null) {
            return false;
        }
        String status = driver.getTrangThai().trim().toLowerCase(Locale.ROOT);
        return status.contains("online") || status.contains("san sang") || status.contains("sẵn sàng") || status.contains("ready");
    }

    @Transactional
    public ChuyenDi processBooking(Integer customerId, Integer driverId, String pickup, String destination, BigDecimal amount, BookingScenario scenario) {
        if (customerId == null) {
            throw new CustomerNotFoundException("Khách hàng không tồn tại");
        }

        KhachHang customer = khachHangRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Khách hàng không tồn tại"));

        TaiXe driver = taiXeRepository.findByIdForUpdate(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Tài xế không tồn tại"));

        if (!isDriverAvailable(driver)) {
            throw new DriverBusyException("Tài xế " + driver.getHoTen() + " đang có chuyến. Vui lòng chờ đến khi chuyến hiện tại hoàn tất.");
        }

        if (scenario == BookingScenario.DEADLOCK) {
            throw new DeadlockException("Hai giao dịch gây deadlock");
        }

        if (scenario == BookingScenario.PAYMENT_FAILURE) {
            throw new PaymentFailedException("Thanh toán thất bại dẫn đến rollback");
        }

        driver.setTrangThai("Dang lam viec");
        taiXeRepository.save(driver);

        ChuyenDi trip = new ChuyenDi();
        trip.setKhachHang(customer);
        trip.setTaiXe(driver);
        trip.setDiemDon(pickup);
        trip.setDiemDen(destination);
        trip.setGiaTien(amount);
        trip.setThoiGianDat(LocalDateTime.now());
        trip.setTrangThai("Dang xu ly");
        trip.setDaThanhToan(false);
        return chuyenDiRepository.save(trip);
    }

    @Transactional
    public ChuyenDi completeTrip(Integer tripId, String paymentMethod) {
        ChuyenDi trip = chuyenDiRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyến đi"));

        trip.setTrangThai("Hoan thanh");
        trip.setThoiGianHoanThanh(LocalDateTime.now());
        trip.setPhuongThucThanhToan(paymentMethod);
        trip.setDaThanhToan(true);

        if (trip.getTaiXe() != null) {
            TaiXe driver = trip.getTaiXe();
            driver.setTrangThai("Online");
            taiXeRepository.save(driver);
        }

        return chuyenDiRepository.save(trip);
    }

    @Transactional
    public ChuyenDi cancelTrip(Integer tripId) {
        ChuyenDi trip = chuyenDiRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyến đi"));

        trip.setTrangThai("Da huy");
        trip.setThoiGianHoanThanh(LocalDateTime.now());
        trip.setDaThanhToan(false);

        if (trip.getTaiXe() != null) {
            TaiXe driver = trip.getTaiXe();
            driver.setTrangThai("Online");
            taiXeRepository.save(driver);
        }

        return chuyenDiRepository.save(trip);
    }
}
