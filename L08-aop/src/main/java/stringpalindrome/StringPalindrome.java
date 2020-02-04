package stringpalindrome;

public interface StringPalindrome {

    boolean isPalindrome(String src);

    default String generate() {
        return "abc cba";
    }

    default String generate(boolean toUpperCase) {
        return toUpperCase ? generate().toUpperCase() : generate();
    }
}
