package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.entity.PaymentMethod;
import com.example.project_reactnative.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public ResponseEntity<APIResponse<List<PaymentMethod>>> getPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods();
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách phương thức thanh toán thành công!", paymentMethods), HttpStatus.OK);
    }
}
