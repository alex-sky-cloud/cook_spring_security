package com.example.web;

import com.example.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WebController {

    private final AsyncService asyncService;

    @GetMapping(value = "/async")
    public void executeWithInternalThread() throws Exception {

        log.info("In executeWithInternalThread - before call: "
                + SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

        asyncService.checkIfPrincipalPropagated();

        log.info("In executeWithInternalThread - after call: "
                + SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @GetMapping(value = "/asyncRunnable")
    public void executeWithExternalThread_Runnable() {

        log.info("In executeWithExternalThread_Runnable: "
                + SecurityContextHolder.getContext().getAuthentication().getName());

        asyncService.checkIfPrincipalPropagatedToNewRunnable();
    }
}
