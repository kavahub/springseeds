package org.springseed.oauth2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springseed.oauth2.operator.Operator;
import org.springseed.oauth2.operator.OperatorRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 生成演示数据
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Component
@Slf4j
public class TestDataRunner implements CommandLineRunner  {
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<Operator> operators = operatorRepository.findAll();
        if (operators.isEmpty()) {
            log.info("生成演示数据...");
            operators.add(new Operator("zhangs", "张三", "zhangs@qq.com", "17000000001"));
            operators.add(new Operator("lis", "李四", "lis@qq.com", "17000000002"));
            operators.add(new Operator("wangw", "王五", "wangw@qq.com", "17000000003"));

            operators.forEach(e -> e.setPassword(passwordEncoder.encode("123")));
            operatorRepository.saveAll(operators);
        }
    }
    
}
