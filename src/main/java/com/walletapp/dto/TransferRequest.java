package com.walletapp.dto;

import com.walletapp.model.User;
import lombok.Data;

@Data
public class TransferRequest extends BaseTransactionRequest{

    private Long receiverId;
}
