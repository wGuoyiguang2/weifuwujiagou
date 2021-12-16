package com.order.service;

import com.github.rholder.retry.RetryException;

import java.util.concurrent.ExecutionException;

public interface ContractService {

    Boolean sendContractRetry(String url, String orderNo) throws ExecutionException, RetryException;
}
