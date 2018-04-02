import java.io.*;  
import java.net.*;  
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CC {  
  public static void main(String[] args) {  
    double c = 36;
    try{      
      Socket s=new Socket("localhost",9007); 
      DataInputStream din=new DataInputStream(s.getInputStream());
      final BufferedWriter result = new BufferedWriter(new FileWriter(new File("result.txt")));

      String config = null;
      while (true) {
        config = (String)din.readUTF();

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
  
          int delta = 0;
          int curr = -1;

          final BlockingQueue<Pair> cQueue = new LinkedBlockingQueue<>();
          final BlockingQueue<Pair> resultQueue = new LinkedBlockingQueue<>();
          final double cc = c3;

          Thread computing = new Thread(new Runnable() {
            public void run() {
              try {
                while (true) {
                  Pair p = cQueue.take();
                  Thread.sleep((int)(100.0 / cc));
                  resultQueue.put(p);
                }
              } catch(Exception e){System.out.println(e);}
            }
          });
          Thread analyzing = new Thread(new Runnable() {
            public void run() {
              try {
                long latency = 0;
                int cnt = 0;
                while (true) {
                  Pair p = resultQueue.take();
                  cnt += 1;
                  long now = new Date().getTime();
                  latency += now - p.A;
                  System.out.printf("%d, %d\n", cnt, (latency / cnt));
                  result.write(Long.toString(now - p.A));
                  result.newLine();
                }
              } catch(Exception e){System.out.println(e);}
            }
          });
          computing.start();
          analyzing.start();
  
          boolean lasts = true;
          while (lasts) {
              String str = (String)din.readUTF();
              Pair p = new Pair(str);
    
              switch(p.D) {
                case 0:
                  lasts = false;
                  result.write("**********\n");
                  System.out.println("*********");
                  break;
                case 1:
                  resultQueue.put(p);
                  break;
                case 2:
                  cQueue.put(p);
                  break;
              }
          }
          computing.interrupt();
          analyzing.interrupt();
        }
        System.out.println();
      }
      result.close();
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
