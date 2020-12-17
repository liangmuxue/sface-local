package com.ss.sdk.server.handler;


import com.ss.sdk.server.ServerRun;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketRun implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ServerRun serverRun = new ServerRun();
        serverRun.start(5300);
    }
}
