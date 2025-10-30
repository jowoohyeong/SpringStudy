package hello.login.web.argumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)      // 파라미터에만 적용
@Retention(RetentionPolicy.RUNTIME) // 실제 동작할때까지 어노테이션이 남아있도록 하는 어노테이션
public @interface Login {
}
