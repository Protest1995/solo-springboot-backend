package com.solo.portfolio.config;

import com.solo.portfolio.model.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局驗證異常處理器
 * 將系統中的資料驗證異常統一處理，轉換為前端友好的回應格式
 * 支援兩種主要的驗證異常：
 * 1. 方法參數驗證異常（@Valid註解產生）
 * 2. 約束違反異常（其他Bean Validation異常）
 */
@RestControllerAdvice
public class ValidationExceptionHandler {

    /**
     * 處理方法參數驗證異常
     * 通常發生在使用@Valid註解驗證請求體時
     *
     * @param ex 方法參數驗證異常
     * @return 包含錯誤訊息的統一回應格式
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // 將所有驗證錯誤收集並格式化為單一錯誤訊息
        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> err instanceof FieldError fe
                        ? (fe.getField() + ": " + fe.getDefaultMessage())  // 欄位錯誤包含欄位名稱
                        : err.getDefaultMessage())                         // 一般錯誤只包含訊息
                .collect(Collectors.joining("; "));
        
        // 回傳400錯誤狀態碼和格式化的錯誤訊息
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(false, message, null, null, null));
    }

    /**
     * 處理約束違反異常
     * 通常發生在使用@Validated註解進行方法級別驗證時
     *
     * @param ex 約束違反異常
     * @return 包含錯誤訊息的統一回應格式
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AuthResponse> handleConstraintViolation(ConstraintViolationException ex) {
        // 將所有約束違反錯誤收集為單一錯誤訊息
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        
        // 回傳400錯誤狀態碼和格式化的錯誤訊息
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(false, message, null, null, null));
    }
}


