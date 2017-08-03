package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component : 스프링의 빈으로 인식되기 위해
//@Aspect : AOP 기능을 하는 클래스의 선언
//@Component
//@Aspect
public class SampleAdvice {

	private static final Logger logger =
			LoggerFactory.getLogger(SampleAdvice.class);
	
	// execution : Pointcut을 지정하는 문법, AspectJ 언어의 문법
	// 리턴타입이 무관하고, MessageService로 시작하는 모든 클래스의
	// 인자값이 0개이상인 모든 메서드
	
	//@Pointcut("execution(* org.zerock.service.MessageService*.*(..))")
	public void myPointcut(){}
	
	//@Before("myPointcut()")
	public void startLog(JoinPoint jp){
		
		logger.info("---------------------------------");
		logger.info("---------------------------------");
	}
	
	//@Around("myPointcut()")
	public Object timeLog(ProceedingJoinPoint pjp) throws Throwable{
		
		long startTime = System.currentTimeMillis();
		logger.info(Arrays.toString(pjp.getArgs()));
		
		Object result = pjp.proceed();
		
		long endTime = System.currentTimeMillis();
		logger.info(pjp.getSignature().getName()+" : "+(endTime-startTime));
		logger.info("=============================================");
		
		return result;
	}
}
