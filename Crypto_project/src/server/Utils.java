package server;
/**
 * @author sala
 */
public class Utils {
    public static String writeInput(int[] plainText) {
    	StringBuilder sb = new StringBuilder();
        for(int i = 0; i <= 3; i++) {
            for(byte b  : TwoFish.asBytes(plainText[i])) {
            	sb.append(String.format("%02X", b));
            }
        }
        return sb.toString();
    }

    static void printInternal(int[] whitened) {
        for(int whitenedEntry : whitened) {
            System.out.print(String.format("%02X ", whitenedEntry));
        }
        System.out.println();
    }
}
