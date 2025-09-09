package com.back.global.aspect;

import com.back.global.rsData.RsData;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseAspect {
    private final HttpServletResponse response;

    public ResponseAspect(HttpServletResponse response) {
        this.response = response;
    }

    @Around("""
            execution(public com.back.global.rsData.RsData *(..)) &&
                            (
                                within(@org.springframework.stereotype.Controller *) ||
                                within(@org.springframework.web.bind.annotation.RestController *)
                            ) &&
                            (
                                @annotation(org.springframework.web.bind.annotation.GetMapping) ||
                                @annotation(org.springframework.web.bind.annotation.PostMapping) ||
                                @annotation(org.springframework.web.bind.annotation.PutMapping) ||
                                @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
                                @annotation(org.springframework.web.bind.annotation.RequestMapping)
                            )
            """)
    public Object handleResponse(ProceedingJoinPoint joinPoint) throws Throwable {

        //원래 대상 메서드 실행
        Object proceed = joinPoint.proceed();

        //반환 객체가 RsData type일 경우
        RsData rsData = (RsData<?>) proceed;
        response.setStatus(rsData.statusCode());

        return proceed;
    }
}
