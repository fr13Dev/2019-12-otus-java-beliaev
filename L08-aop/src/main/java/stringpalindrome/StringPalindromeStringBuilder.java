package stringpalindrome;

public class StringPalindromeStringBuilder implements StringPalindrome {

    @Override
    public boolean isPalindrome(String src) {
        final StringBuilder builder = new StringBuilder(src);
        return builder.reverse().toString().equals(src);
    }
}
