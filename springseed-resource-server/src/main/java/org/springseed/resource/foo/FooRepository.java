package org.springseed.resource.foo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 数据访问接口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public interface FooRepository extends JpaRepository<Foo, String> {
    
}
