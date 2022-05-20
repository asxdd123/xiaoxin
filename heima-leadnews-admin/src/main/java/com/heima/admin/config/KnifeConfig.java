package com.heima.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * 开启knife4j接口文档
 * 在浏览器输入地址：http://当前端口/doc.html
 */
@Configuration
@ComponentScan("com.heima.common.knife4j")
public class KnifeConfig {
}
