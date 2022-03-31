package org.springseed.oauth2.operator;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



/**
 * 
 * 操作员数据访问接口
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@CacheConfig(cacheNames = {"entitycache-operators"})
public interface OperatorRepository extends JpaRepository<Operator, String>, JpaSpecificationExecutor<Operator> {
    @Cacheable(unless = "#result == null")
    Operator findByUsernameIgnoreCase(String username);

    @CacheEvict
    void deleteByUsernameIgnoreCase(String username);
    
}
