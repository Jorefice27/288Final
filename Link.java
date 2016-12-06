/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgk28gui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author John
 */
public class Link {
    
    private Socket link;
    private OutputStreamWriter out;
    private InputStreamReader isr;
    BufferedReader in;
    private final String SERVER_NAME = "192.168.1.1";
    private final int PORT_NUM = 42880;
    
    public Link() throws IOException
    {
        link = new Socket(SERVER_NAME, PORT_NUM);
        out = new OutputStreamWriter(new BufferedOutputStream(link.getOutputStream()));
        isr = new InputStreamReader(new BufferedInputStream(link.getInputStream()));
        in = new BufferedReader(isr);
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
    
    public String recieveMessage() throws IOException
    {
//        return in.read(new char[50]);
        return in.readLine();
    }
    
    private class CommReceive implements Runnable
    {
        ArrayList<String> messages;
        
        public CommReceive()
        {
            messages = new ArrayList<String>();
        }

        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
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
