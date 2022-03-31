package org.springseed.oauth2.operator;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.experimental.UtilityClass;

/**
 * 
 * 操作员查询处理
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
public class OperatorQueryHandler {
    private final OperatorRepository operatorRepository;

    public OperatorQueryHandler(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    public Operator findById(final String id) {
        return operatorRepository.findById(id).orElseThrow(() -> new OperatorNotFoundExcepiton(id));
    }

    public Optional<Operator> findByUsername(final String username) {
        return Optional.ofNullable(operatorRepository.findByUsernameIgnoreCase(username));
    }

    public Page<Operator> findByPage(String nickname, Pageable pageable) {
        Specification<Operator> specification = OperatorSpecification.empty();
        if (StringUtils.hasText(nickname)) {
            specification = specification.and(OperatorSpecification.nicknameLike(nickname));
        }
        return operatorRepository.findAll(specification, pageable);
    }

    @UtilityClass
    private static class OperatorSpecification {
        public Specification<Operator> empty() {
            return (Root<Operator> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                return null;
            };
        }
    
        public Specification<Operator> nicknameLike(final String nickname) {
            return (Root<Operator> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                return cb.like(root.get("nickname"), "%" + nickname + "%");
            };
        }
    }


}
