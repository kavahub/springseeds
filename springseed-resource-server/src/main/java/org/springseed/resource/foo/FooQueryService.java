package org.springseed.resource.foo;

import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * 查询服务
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
public class FooQueryService {
    private final FooRepository fooRepository;

    public FooQueryService(FooRepository fooRepository) {
        this.fooRepository = fooRepository;
    }

    public Optional<Foo> findById(final String id) {
        final Foo foo = fooRepository.findById(id).orElseThrow(() -> new FooNotFoundExcepiton(String.valueOf(id)));
        return Optional.of(foo);
    }
}
