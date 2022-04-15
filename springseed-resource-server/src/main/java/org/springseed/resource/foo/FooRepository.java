package org.springseed.resource.foo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public interface FooRepository extends JpaRepository<Foo, String> {
    
}
