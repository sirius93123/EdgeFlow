import java.io.*;  
import java.net.*;  
import java.util.Scanner;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AP {  
  public static void main(String[] args) {  
    try{      
      Socket in=new Socket("localhost",9007); 
      DataInputStream din=new DataInputStream(in.getInputStream());

      Socket out=new Socket("localhost",9501);
      out.setTcpNoDelay(true);
      final DataOutputStream dout=new DataOutputStream(out.getOutputStream());

      String config = null;
      while (true) {
        config = (String)din.readUTF();
        dout.writeUTF(config);
        dout.flush();
        System.out.println(config);

        Scanner sc = new Scanner(config);
        double c1 = Double.parseDouble(sc.next());
        double c2 = Double.parseDouble(sc.next());
        double c3 = Double.parseDouble(sc.next());
        double d1 = Double.parseDouble(sc.next());
        double d2 = Double.parseDouble(sc.next());
        double lambda = Double.parseDouble(sc.next());
        double y = Double.parseDouble(sc.next());
        double a1 = Double.parseDouble(sc.next());
        double a2 = Double.parseDouble(sc.next());
        if (lambda < 0.01) break;

        for (int i = 1; i <= 3; i += 1) {

          switch (i) {
            case 1:break;
            case 2:a1 = 0;a2 = 0;break;
            case 3:a1 = 1;a2 = 0;break;
          }

          int curr_c = -1;
          int curr_d = -1;
          double acc = 0;

          final BlockingQueue<Pair> cQueue = new LinkedBlockingQueue<>();
          final BlockingQueue<Pair> dQueue = new LinkedBlockingQueue<>();
          final double cc = c2;
          final double dd = d2;
          final double yy = y;

          Thread computing = new Thread(new Runnable() {
            public void run() {
              try {
                while (true) {
                  Pair p = cQueue.take();
                  Thread.sleep((int)(100.0 / cc));
                  p.D = 1;
                  dQueue.put(p);
                }
              } catch(Exception e){System.out.println(e);}
            }
          });
          Thread delivering = new Thread(new Runnable() {
            public void run() {
              try {
                while (true) {
                  Pair p = dQueue.take();
                  switch(p.D) {
                    case 1:
                      Thread.sleep((int)(100.0 / dd * yy));
                      break;
                    case 2:
                      Thread.sleep((int)(100.0 / dd));
                      break;
                  }
                  dout.writeUTF(p.toString());
                  dout.flush();
                }
              } catch(Exception e){System.out.println(e);}
            }
          });
          computing.start();
          delivering.start();
    
          boolean lasts = true;
          while (lasts) {
            acc = acc + a2;
            String str = (String)din.readUTF();
            Pair p = new Pair(str);
    
            switch(p.D) {
              case 0:
    	        dout.writeUTF(str);
                dout.flush();
                lasts = false;
                break;
              case 1:
                dQueue.put(p);
                break;
              case 2:
                if (acc >= 1) {
                  acc = acc - 1;
                  cQueue.add(p);
                }
                else {
                  dQueue.add(p);
                }
                break;
            }
          }
          computing.interrupt();
          delivering.interrupt();
        }
      }
      //s.close();  
    }catch(Exception e){System.out.println(e);}  
  }
}  

class Pair {
  public long A;
  public int D;

  public Pair(long a, int d) {
    this.A = a;
    this.D = d;
  }
  public Pair(String str) {
    Scanner s = new Scanner(str);
    this.A = Long.parseLong(s.next());
    this.D = Integer.parseInt(s.next());
  }

  public String toString() {
    return Long.toString(A) + "\t" + Integer.toString(D);
  }
}
