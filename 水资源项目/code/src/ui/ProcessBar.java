package ui;


    import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

    import javax.swing.JFrame;
import javax.swing.JProgressBar;

    public class ProcessBar extends JFrame implements Runnable {
        private JProgressBar progress; // �����

        
        public ProcessBar(String str) {
            super(str);
            progress = new JProgressBar(1, 100); // ʵ������

            progress.setStringPainted(true);      // �������

        
            progress.setBackground(Color.PINK); // ���ñ���ɫ
            this.setUndecorated(true);
            this.add(progress);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(500,30);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    		this.setLocation((dim.width - this.getWidth()) / 2,
    				(dim.height - this.getHeight()) / 2);
            this.setVisible(true);
            Thread t = new Thread(this);
            t.start();
        }

        public void run() {

                for(int i=0; i<100; i++) {
                    try {
                        progress.setValue(progress.getValue() + 1); // �����߳̽��У����ӽ����ֵ

                        progress.setString(progress.getValue() + "%");
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
 
               this.dispose();
        
        }
//        
        public static void main(String[] args) {
         new ProcessBar("Test JProcessBar");

        }
    }