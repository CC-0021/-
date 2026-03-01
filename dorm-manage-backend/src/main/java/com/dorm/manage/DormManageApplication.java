package com.dorm.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 大学生公寓管理系统 - 启动类
 *
 * @author dorm-manage
 */
@SpringBootApplication
@MapperScan("com.dorm.manage.mapper")
public class DormManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormManageApplication.class, args);
    }
}
