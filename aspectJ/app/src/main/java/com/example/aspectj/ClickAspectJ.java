package com.example.aspectj;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ClickAspectJ {



    // 先找到切入点 click
    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void methodClickEnter() {

    }

    // 在方法前插入代码
    @Around("methodClickEnter()")
    public Object  handleClickMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        if (!Utils.isFastClick()) {
             result = joinPoint.proceed();
        }
        return result ;

    }

    @Pointcut("execution(@com.example.aspectj.ToastAnnotation * *(..))")
    public void methodToastEnter() {

    }

    @After("methodToastEnter()")
    public void handleToastMethod(JoinPoint joinPoint) {
        View view = (View)joinPoint.getArgs()[0];

        ToastAnnotation toastAnnotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ToastAnnotation.class);
        String value = toastAnnotation.value();
        Log.i("nole", "nole "+value);
//        Toast.makeText(view.getContext(),"00000",Toast.LENGTH_SHORT).show();
    }



//    @Around("methodToastEnter()")
//    public Object handleToastMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        View view = (View)joinPoint.getArgs()[0];
//        Toast.makeText(view.getContext(),"00000",Toast.LENGTH_SHORT).show();
//
//        Object result = null;
//        result = joinPoint.proceed();
//
//        return result ;
//
//    }

}
