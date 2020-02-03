package stringpalindrome;

import annotation.Log;

public class StringPalindromeNativeChecking implements StringPalindrome {

    @Log
    @Override
    public boolean isPalindrome(String src) {
        int i = 0;
        int j = src.length() - 1;
        while (i < j) {
            if (src.charAt(i) != src.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    @Log
    @Override
    public String generate() {
        return "xyz zyx";
    }
}
