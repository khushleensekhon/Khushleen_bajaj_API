package com.chitkara.bfhl.service;

import com.chitkara.bfhl.dto.BfhlRequest;
import com.chitkara.bfhl.dto.BfhlResponse;

public interface BfhlService {
    BfhlResponse processData(BfhlRequest request);
}
