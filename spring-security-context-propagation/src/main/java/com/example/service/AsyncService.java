package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AsyncService {

    @Async
    public void checkIfPrincipalPropagated() {

        Thread thread = Thread.currentThread();
        long threadId = thread.getId();
        ThreadGroup threadGroup = thread.getThreadGroup();
        String threadGroupName = threadGroup.getName();

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        log.info("threadGroupName : " + threadGroupName);
        log.info("ThreadId : " + threadId);
        log.info("Метод -  checkIfPrincipalPropagated (async): " + username);
    }

    public void checkIfPrincipalPropagatedToNewRunnable() {

        Runnable task = () -> {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            log.info(" ++++++++++ In checkIfPrincipalPropagatedToNewRunnable inside Runnable: "
                    + username);
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            var contextTask = new DelegatingSecurityContextRunnable(task);
            executorService.submit(contextTask);
        } finally {
            executorService.shutdown();
        }
    }

    public String checkIfPrincipalPropagatedToNewCallable()
            throws ExecutionException, InterruptedException {

        Callable<String> task = () -> {

            String username = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();

            log.info("In checkIfPrincipalPropagatedToNewCallable inside Callable: "
                    + username);

            return username;
        };

        ExecutorService e = Executors.newCachedThreadPool();
        try {
            var contextTask = new DelegatingSecurityContextCallable<>(task);
            return "Ciao, " + e.submit(contextTask).get() + "!";
        } finally {
            e.shutdown();
        }
    }

    public void checkIfPrincipalPropagatedToExecutorService() {

        Runnable task = () -> {
            String username = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();

            log.info("In checkIfPrincipalPropagatedToExecutorService inside " +
                    "Runnable: "
                    + username);
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService = new DelegatingSecurityContextExecutorService(executorService);

        try {
            executorService.submit(task);
        } finally {
            executorService.shutdown();
        }
    }
}
