package org.springseed.resource.foo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/foo")
public class FooController {
    private final FooRepository fooRepository;
    private final FooQueryService fooQueryService;
    private final ModelMapper modelMapper;

    public FooController(FooRepository fooRepository, FooQueryService fooQueryService, ModelMapper modelMapper) {
        this.fooRepository = fooRepository;
        this.fooQueryService = fooQueryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}")
    public FooDto findById(@PathVariable String id) {
        return fooQueryService.findById(id)
                .map((e) -> modelMapper.map(e, FooDto.class))
                .get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String create(@RequestBody FooDto newFoo) {
        Foo entity = modelMapper.map(newFoo, Foo.class);
        this.fooRepository.save(entity);
        return entity.getId();
    }

    @GetMapping
    public Collection<FooDto> findAll() {
        List<Foo> foos = this.fooRepository.findAll();
        return foos.stream().map(e ->  modelMapper.map(e, FooDto.class))
            .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public void updateFoo(@PathVariable("id") String id, @RequestBody FooDto updatedFoo) {
        fooQueryService.findById(id).ifPresent(entity -> {
            updatedFoo.copyTo(entity);
            fooRepository.save(entity);
        });
    }   
}
