package hotsixturtles.tupli.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * SimpleAsyncTaskExecutor 보다 조금 자세한 설정이 가능해짐
 * Flask 비동기 신청
 */
@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);  // 실행대기 Thread  // Flask는 한번에 하나밖에 실행 못하니 할당 많이해도 의미가 없다 오히려 소켓 에러남
        executor.setMaxPoolSize(10);  // 동시 동작 최대 Thread
        executor.setQueueCapacity(500);  // Max 초과시 Queue에 저장되는 업무내역
        executor.setThreadNamePrefix("flask-async-");  // 접두사 설정
       executor.initialize();
        return executor;
    }
}