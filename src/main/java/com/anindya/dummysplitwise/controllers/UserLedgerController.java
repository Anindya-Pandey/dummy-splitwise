package com.anindya.dummysplitwise.controllers;

import com.anindya.dummysplitwise.controllers.responses.BaseResponse;
import com.anindya.dummysplitwise.service.IUserLedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

@RestController
@RequestMapping("/ledger")
@Slf4j
public class UserLedgerController {
    @Autowired private IUserLedgerService userLedgerService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> fetchUserLedgers(HttpServletRequest httpServletRequest,
        @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        log.info("Inside fetchUserLedgers");

        BigInteger userId = new BigInteger(httpServletRequest.getHeader("userId"));

        try {
            return new ResponseEntity<>(
                new BaseResponse(userLedgerService.getAllUserLedgersForUserId(userId, pageNo, pageSize), null),
                HttpStatus.OK);
        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(
                new BaseResponse(null, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/overall")
    public ResponseEntity<BaseResponse> fetchTotalOwedAmount(HttpServletRequest httpServletRequest) {
        log.info("Inside fetchTotalOwedAmount");

        BigInteger userId = new BigInteger(httpServletRequest.getHeader("userId"));

        try {
            return new ResponseEntity<>(
                new BaseResponse(userLedgerService.getTotalOwedByUserId(userId), null),
                HttpStatus.OK);
        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(
                new BaseResponse(null, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
