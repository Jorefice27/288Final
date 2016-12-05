package pkg228gui;


import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;




/**
 *
 * @author John
 */
public class Link {
    
    private Socket link;
    private OutputStreamWriter out;
    private InputStreamReader in;
    private final String SERVER_NAME = "192.168.1.1";
    private final int PORT_NUM = 42880;
    
    public Link() throws IOException
    {
        link = new Socket(SERVER_NAME, PORT_NUM);
        out = new OutputStreamWriter(new BufferedOutputStream(link.getOutputStream()));
//        out.write('l');
//        out.flush();
        System.out.println("Sent");       
        
    }
    
    public boolean sendByte(char c)
    {
        try {
            out.write(c);    
            out.flush();
            return true;
        } 
        catch (IOException ex) 
        {
            
            return false;
        }
    }
    
    public String recieveMessage()
    {
        return in.getEncoding();
    }
    
   public static void main(String[] args) throws IOException
   {
      Integer x = 0x07030104;
      Integer y = 0xFFFFFFF0;
      Integer z = x + y;
      System.out.println(Integer.toHexString(z));
      System.out.println(z.toString());
   }
}




