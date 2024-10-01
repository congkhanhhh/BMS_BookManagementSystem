package com.bookstore.project.aspect;

import com.bookstore.project.service.PermissionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class HasPermissionAspect {

    @Autowired
    private PermissionService permissionService;

    @Around("@annotation(com.bookstore.project.aspect.HasPermission)")
    public Object hasPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        HasPermission hasPermission = method.getAnnotation(HasPermission.class);


        permissionService.hasPermission(hasPermission.value());


        return joinPoint.proceed();
    }
}

