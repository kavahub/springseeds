package org.springseed.oauth2.operator;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * 操作员接口
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/operators")
public class OperatorController {
    private final OperatorQueryHandler operatorQueryHandler;
    private final OperatorSaveHandler operatorSaveHandler;
    private final OperatorRepository operatorRepository;
    private final ModelMapper modelMapper;

    public OperatorController(OperatorQueryHandler operatorQueryHandler, OperatorRepository operatorRepository,
            OperatorSaveHandler operatorSaveHandler, ModelMapper modelMapper) {
        this.operatorQueryHandler = operatorQueryHandler;
        this.operatorRepository = operatorRepository;
        this.operatorSaveHandler = operatorSaveHandler;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<OperatorVO> save(@Validated @RequestBody OperatorVO vo) {
        Operator operator = operatorSaveHandler.save(modelMapper.map(vo, Operator.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(operator, OperatorVO.class));
    }

    @GetMapping("/current")
    public ResponseEntity<OperatorVO> findCurrent(final Authentication authentication) {
        final String username = authentication.getName();

        return operatorQueryHandler.findByUsername(username)
                .map((e) -> modelMapper.map(e, OperatorVO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OperatorVO> findById(@PathVariable String id) {
        return operatorRepository.findById(id)
                .map((e) -> modelMapper.map(e, OperatorVO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/username/{username}")
    public ResponseEntity<OperatorVO> findByUsername(@PathVariable String username) {
        return operatorQueryHandler.findByUsername(username)
                .map((e) -> modelMapper.map(e, OperatorVO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/page")
    public Page<OperatorVO> getPage(@RequestParam(value = "nickname", required = false) String nickname,
            Pageable pageable) {
        Page<Operator> page = this.operatorQueryHandler.findByPage(nickname, pageable);
        return page.map(e -> modelMapper.map(e, OperatorVO.class));
    }
}
