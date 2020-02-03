import ru.otus.aop.proxy.IoC;
import stringpalindrome.StringPalindrome;
import stringpalindrome.StringPalindromeNativeChecking;
import stringpalindrome.StringPalindromeStringBuilder;

public class Demo {

    public static void main(String[] args) {
        final StringPalindrome instance1 = IoC.getInstance(new StringPalindromeNativeChecking());
        System.out.println(StringPalindromeNativeChecking.class.getName());
        System.out.println(instance1.isPalindrome("tac cat"));
        System.out.println(instance1.generate());
        System.out.println("--------------------");
        final StringPalindrome instance2 = IoC.getInstance(new StringPalindromeStringBuilder());
        System.out.println(StringPalindromeStringBuilder.class.getName());
        System.out.println(instance2.isPalindrome("gl&hv"));
        System.out.println(instance2.generate());
    }
}
