package net.bitnine.exception.test;

import net.bitnine.util.messages.ErrorCodes;

public class CustomException1 extends BaseException {

    public CustomException1() {
        super(ErrorCodes.CUSTOM_EXCEPTION_1);
    }

}
