import java.io.*;  
import java.net.*;  
import java.util.Scanner;
import java.util.Date;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ED {  
  public static void main(String[] args) {  
    try{
      Socket s=new Socket("localhost",9501); 
      s.setTcpNoDelay(true);
      final DataOutputStream dout=new DataOutputStream(s.getOutputStream());  

      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("config.txt")));
      String config = null;
      boolean burst = true;
      while ((config = br.readLine()) != null) {

        Scanner sc = new Scanner(config);
        double c1 = Double.parseDouble(sc.next());
        double c2 = Double.parseDouble(sc.next());
        double c3 = Double.parseDouble(sc.next());
        double d1 = Double.parseDouble(sc.next());
        double d2 = Double.parseDouble(sc.next());
        double lambda = Double.parseDouble(sc.next());
        double y = Double.parseDouble(sc.next());

        double factor = Double.parseDouble(sc.next());
        c1 /= factor;
        c2 /= factor;
        c3 /= factor;
        d1 /= factor;
        d2 /= factor;

        double a1 = (c1 >= lambda) ? 1 : c1 / lambda;
        if ((a1 / c1) > ((y * a1 + 1 - a1) / d1))
          a1 = c1 / (d1 + c1 - y * c1);
        double a2 = ((1 - a1) >= a1 / c1 * c2) ? a1 / c1 * c2 : (1 - a1);
        if ((a1 / c1) < (y * (a1 + a2) + (1 - a1 - a2)) / d2) {
          if ((1 - a1) >= a1 / c1 * c2) {
            a1 = c1 / (d2 + (1 - y) * (c1 + c2));
            a2 = c2 * a1 / c1;
          } else {
            a1 = c1 / d2;
            a2 = 1 - a1;
          }
        }
        if (a2 >= 0.01) {
          a2 = a2 / (1 - a1);
        }

        dout.writeUTF(String.format("%f %f %f %f %f %f %f %f %f", c1, c2, c3, d1, d2, lambda, y, a1, a2));
        dout.flush();

        for (int i = 1; i <= 3; i += 1) {

          switch (i) {
            case 1:break;
            case 2:a1 = 0;a2 = 0;break;
            case 3:a1 = 1;a2 = 0;break;
          }

          int cnt = 0;
          double acc = 0;
          int curr_c = -1;
          int curr_d = -1;

          final BlockingQueue<Pair> cQueue = new LinkedBlockingQueue<>();
          final BlockingQueue<Pair> dQueue = new LinkedBlockingQueue<>();
          final double cc = c1;
          final double dd = d1;
          final double yy = y;
          

          Thread computing = new Thread(new Runnable() {
            public void run() {
              try {
                while (true) {
                  Pair p = cQueue.take();
                  Thread.sleep((int)(100.0 / cc));
                  p.D = 2;
                  dQueue.put(p);
                }
              } catch(Exception e) {
                System.out.println("Computing Exception");
              }  
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
              } catch(Exception e) {
                System.out.println("Delivering Exception");
              }  
            }
          });
          computing.start();
          delivering.start();

          boolean lasts = true;
          while(lasts) {
            cnt = cnt + 1;
            acc = acc + a1;
            if (burst && cnt >= 20 && cnt < 40) {
              Thread.sleep((int)(60.0 / lambda));
            } else if (burst && cnt >= 100 && cnt < 150) {
              Thread.sleep((int)(50.0 / lambda));
            } else {
              Thread.sleep((int)(100.0 / lambda));
            }
            long g_time = new Date().getTime();
            if (acc >= 1) {
              Pair p = new Pair(g_time, 1);
              acc = acc - 1;
              cQueue.put(p);
            }
            else {
              Pair p = new Pair(g_time, 2);
              dQueue.put(p);
            }

            if (cnt >= 200) {
              lasts = false;
              Pair p = new Pair(0,0);
              dout.writeUTF(p.toString());
              dout.flush();
            }
          }
          computing.interrupt();
          delivering.interrupt();
        }
      }
      dout.writeUTF("0 0 0 0 0 0 0 0 0");
      dout.flush();
      dout.close();  
      s.close();  

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
