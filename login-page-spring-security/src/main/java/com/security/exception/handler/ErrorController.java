package com.security.exception.handler;


/**
 * Мы также можем настроить страницу ошибок на основе Thymeleaf.
 * В этом случае Spring Security вообще не задействована,
 * мы должны просто добавить обработчик исключений в нашу конфигурацию Spring
 */
//@ControllerAdvice
//@Slf4j
public class ErrorController {

 /*   @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {

        log.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = "Unknown error";
        if(throwable != null){
            errorMessage = throwable.getMessage();
        }

        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }*/
}
