package net.bitnine.exception;

import net.bitnine.exception.BaseException;
import net.bitnine.exception.CustomException1;
import net.bitnine.exception.CustomException2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ekansh
 * @since 19/2/16
 */
@RestController
@RequestMapping({ "", "/" })
public class HomeController {

    @RequestMapping("/ex1")
    public String ex1() {
        // 전역 처리자 메소드 handleBaseException에 잡힐 것이다.  
        throw new BaseException("Base Exception");
    }

    @RequestMapping("/ex2")
    public String ex2() {
        //전역 처리자 메소드 handleBaseException에 잡힐 것이다.    
        throw new CustomException1();
    }

    @RequestMapping("/ex3")
    public String ex3() {
        // 전역 처리자 메소드 handleBaseException에 잡힐 것이다.    
        throw new CustomException2();
    }

    @RequestMapping("/ex4")
    public String ex4() {
        //전역 처리자 메소드 handleBaseException에 잡힐 것이다.    
        throw new NullPointerException("null pointer exception");
    }

    @RequestMapping("/ex5")
    public String ex5() {
        // 컨트롤러 예외 처리자 메소드 nfeHandler에 잡힐 것이다.
        throw new NumberFormatException("number format exception");
    }

    /**
     * 이 컨트롤러 내에서 발생하는 모든 Number Format 예외를 처리한다 *
     */
    @ExceptionHandler(value = NumberFormatException.class)
    public String nfeHandler(NumberFormatException e) {
        return e.getMessage();
    }

}
