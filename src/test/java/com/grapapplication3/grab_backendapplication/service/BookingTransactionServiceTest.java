package com.grapapplication3.grab_backendapplication.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

class BookingTransactionServiceTest {

    private KhachHangRepository khachHangRepository;
    private TaiXeRepository taiXeRepository;
    private ChuyenDiRepository chuyenDiRepository;
    private BookingTransactionService bookingTransactionService;

    @BeforeEach
    void setUp() {
        khachHangRepository = mock(KhachHangRepository.class);
        taiXeRepository = mock(TaiXeRepository.class);
        chuyenDiRepository = mock(ChuyenDiRepository.class);
        bookingTransactionService = new BookingTransactionService(khachHangRepository, taiXeRepository, chuyenDiRepository);
    }

    @Test
    void shouldThrowWhenCustomerDoesNotExist() {
        when(khachHangRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                bookingTransactionService.processBooking(999, 1, "A", "B", new BigDecimal("50000"), BookingScenario.STANDARD)
        );
    }

    @Test
    void shouldThrowWhenCustomerIdIsNull() {
        assertThrows(CustomerNotFoundException.class, () ->
                bookingTransactionService.processBooking(null, 1, "A", "B", new BigDecimal("50000"), BookingScenario.STANDARD)
        );
    }

    @Test
    void shouldThrowWhenDriverDoesNotExist() {
        KhachHang customer = new KhachHang();
        customer.setMaKH(1);
        when(khachHangRepository.findById(1)).thenReturn(Optional.of(customer));
        when(taiXeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () ->
                bookingTransactionService.processBooking(1, 99, "A", "B", new BigDecimal("50000"), BookingScenario.STANDARD)
        );
    }

    @Test
    void shouldThrowWhenDeadlockScenarioIsSelected() {
        KhachHang customer = new KhachHang();
        customer.setMaKH(1);
        TaiXe driver = new TaiXe();
        driver.setMaTX(2);
        driver.setTrangThai("Online");
        when(khachHangRepository.findById(1)).thenReturn(Optional.of(customer));
        when(taiXeRepository.findByIdForUpdate(2)).thenReturn(Optional.of(driver));

        assertThrows(DeadlockException.class, () ->
                bookingTransactionService.processBooking(1, 2, "A", "B", new BigDecimal("50000"), BookingScenario.DEADLOCK)
        );
    }

    @Test
    void shouldThrowWhenPaymentFails() {
        KhachHang customer = new KhachHang();
        customer.setMaKH(1);
        TaiXe driver = new TaiXe();
        driver.setMaTX(2);
        driver.setTrangThai("Online");
        when(khachHangRepository.findById(1)).thenReturn(Optional.of(customer));
        when(taiXeRepository.findByIdForUpdate(2)).thenReturn(Optional.of(driver));

        assertThrows(PaymentFailedException.class, () ->
                bookingTransactionService.processBooking(1, 2, "A", "B", new BigDecimal("50000"), BookingScenario.PAYMENT_FAILURE)
        );
    }

    @Test
    void shouldThrowWhenDriverIsBusy() {
        KhachHang customer = new KhachHang();
        customer.setMaKH(1);
        TaiXe driver = new TaiXe();
        driver.setMaTX(2);
        driver.setTrangThai("Dang di");
        when(khachHangRepository.findById(1)).thenReturn(Optional.of(customer));
        when(taiXeRepository.findByIdForUpdate(2)).thenReturn(Optional.of(driver));

        assertThrows(DriverBusyException.class, () ->
                bookingTransactionService.processBooking(1, 2, "A", "B", new BigDecimal("50000"), BookingScenario.STANDARD)
        );
    }

    @Test
    void shouldCompleteTripAndFreeDriver() {
        TaiXe driver = new TaiXe();
        driver.setMaTX(2);
        driver.setTrangThai("Dang lam viec");

        ChuyenDi trip = new ChuyenDi();
        trip.setMaChuyen(10);
        trip.setTaiXe(driver);
        trip.setTrangThai("Dang xu ly");
        trip.setThoiGianDat(LocalDateTime.now());

        when(chuyenDiRepository.findById(10)).thenReturn(Optional.of(trip));
        when(chuyenDiRepository.save(trip)).thenReturn(trip);

        bookingTransactionService.completeTrip(10, "Tien mat");

        assertEquals("Hoan thanh", trip.getTrangThai());
        assertEquals("Tien mat", trip.getPhuongThucThanhToan());
        assertEquals("Online", driver.getTrangThai());
        verify(taiXeRepository).save(driver);
        verify(chuyenDiRepository).save(trip);
    }

    @Test
    void shouldCancelTripAndFreeDriver() {
        TaiXe driver = new TaiXe();
        driver.setMaTX(2);
        driver.setTrangThai("Dang lam viec");

        ChuyenDi trip = new ChuyenDi();
        trip.setMaChuyen(11);
        trip.setTaiXe(driver);
        trip.setTrangThai("Dang xu ly");
        trip.setThoiGianDat(LocalDateTime.now());

        when(chuyenDiRepository.findById(11)).thenReturn(Optional.of(trip));
        when(chuyenDiRepository.save(trip)).thenReturn(trip);

        bookingTransactionService.cancelTrip(11);

        assertEquals("Da huy", trip.getTrangThai());
        assertEquals(Boolean.FALSE, trip.getDaThanhToan());
        assertEquals("Online", driver.getTrangThai());
        verify(taiXeRepository).save(driver);
        verify(chuyenDiRepository).save(trip);
    }
}
