package com.anindya.dummysplitwise.controllers.responses;

import com.anindya.dummysplitwise.entities.UserLedger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FetchUserLedgersResponse {
    private Long count;
    private List<UserLedger> userLedgers;

    public FetchUserLedgersResponse(Long count, List<UserLedger> userLedgers) {
        this.count = count;
        this.userLedgers = userLedgers;
    }
}
