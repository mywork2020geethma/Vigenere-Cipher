
/**
 * Write a description of tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class tester {

    public void test(){
    String text = "Tcmp-pxety mj nikhqv htee mrfhtii tyv";
    int[] keys = {17, 14, 12, 4};
    VigenereCipher ex = new VigenereCipher(keys);
    System.out.println(ex.decrypt(text));
    }
    
}
