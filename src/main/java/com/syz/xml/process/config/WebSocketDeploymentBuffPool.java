package com.syz.xml.process.config;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: mybatis-plus
 * @Package: com.czkj.mybatisplus.config
 * @ClassName: WebSocketDeploymentBuffPool
 * @Author: Administrator
 * @Description:
 * @Date 2019/11/19/01917:52
 */
@Component
public class WebSocketDeploymentBuffPool implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo info = new WebSocketDeploymentInfo();
            info.setBuffers(new DefaultByteBufferPool(false,1024));
            deploymentInfo.addServletContextAttribute(
                    "io.undertow.websockets.jsr.WebSocketDeploymentInfo",info);
        });
    }
}
