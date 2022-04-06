package org.springseed.oauth2.operator;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.hutool.core.lang.Validator;

/**
 * 
 * 操作员保存处理
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
public class OperatorSaveHandler {
    private final OperatorRepository operatorRepository;

    public OperatorSaveHandler(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    public Operator save(final Operator needToSave) {
        final String phoneNumber = needToSave.getPhoneNumber();
        if (StringUtils.hasText(phoneNumber) && !Validator.isMobile(phoneNumber)) {
            throw new PhoneNumberValidateExcepiton(phoneNumber);
        }

        final String email = needToSave.getEmail();
        if (StringUtils.hasText(email) && !Validator.isEmail(email)) {
            throw new EmailValidateExcepiton(email);
        }

        return operatorRepository.save(needToSave);
    }
}
