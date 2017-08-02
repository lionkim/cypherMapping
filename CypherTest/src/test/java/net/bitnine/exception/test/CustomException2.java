package net.bitnine.exception.test;

import net.bitnine.util.messages.ErrorCodes;

public class CustomException2 extends BaseException {

    public CustomException2() {
        super(ErrorCodes.CUSTOM_EXCEPTION_2);
    }

}
