package com.anindya.dummysplitwise.controllers;

import com.anindya.dummysplitwise.controllers.requests.CreateExpenseRequest;
import com.anindya.dummysplitwise.controllers.responses.BaseResponse;
import com.anindya.dummysplitwise.entities.Expense;
import com.anindya.dummysplitwise.repository.IExpenseRepository;
import com.anindya.dummysplitwise.service.IExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

@RestController
@RequestMapping("/expense")
@Slf4j
public class ExpenseController {
    @Autowired private IExpenseService expenseService;

    @PostMapping("")
    public ResponseEntity<BaseResponse> createExpense(@RequestBody CreateExpenseRequest request) {
        log.info("Inside createExpense");

        try {
            return new ResponseEntity<>(
                new BaseResponse(expenseService.createExpense(request), null),
                HttpStatus.OK);
        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(
                new BaseResponse(null, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse> fetchExpenses(HttpServletRequest httpServletRequest,
        @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        log.info("Inside fetchExpenses");

        BigInteger userId = new BigInteger(httpServletRequest.getHeader("userId"));

        try {
            return new ResponseEntity<>(
                new BaseResponse(expenseService.fetchExpenses(userId, pageNo, pageSize), null),
                HttpStatus.OK);
        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(
                new BaseResponse(null, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{expenseId}/details")
    public ResponseEntity<BaseResponse> fetchExpenseDetails(HttpServletRequest httpServletRequest,
        @PathVariable BigInteger expenseId) {
        log.info("Inside fetchExpenseDetails");

        BigInteger userId = new BigInteger(httpServletRequest.getHeader("userId"));

        try {
            return new ResponseEntity<>(
                new BaseResponse(expenseService.fetchExpenseDetails(expenseId, userId), null),
                HttpStatus.OK);
        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(
                new BaseResponse(null, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
