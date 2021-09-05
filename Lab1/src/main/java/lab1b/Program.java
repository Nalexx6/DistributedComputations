package lab1b;

import javax.swing.*;
import java.awt.*;

public class Program {
    static Thread thDec;
    static Thread thInc;
    static int semaphore = 0;

    public static void main(String[] args) {
        JFrame win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(500, 400);

        JPanel panel = getPanel();

        win.setContentPane(panel);
        win.setVisible(true);

    }

    private static JPanel getPanel() {
        JPanel panel = new JPanel();
        JButton btnStart1 = new JButton("Start1");
        JButton btnStart2 = new JButton("Start2");
        JButton btnStop1 = new JButton("Stop1");
        JButton btnStop2 = new JButton("Stop2");
        JTextField status = new JTextField(20);
        status.setText("Free");
        status.setBackground(Color.GREEN);



        JSlider slider = new JSlider();
        btnStart1.addActionListener(e -> {
            if(semaphore == 0) {
                thInc = new Thread(
                        () -> {
                            System.out.println(Thread.currentThread().getName() + " : Slider set to value 90");
                            slider.setValue(90);

                            while (!Thread.interrupted()){
                                //wait
                            }
                            semaphore--;

                            System.out.println(Thread.currentThread().getName() + " is shutting down");

                        });

                thInc.setPriority(Thread.MIN_PRIORITY);

                thInc.start();
                semaphore = 1;
                status.setText("Occupied by first thread");
                status.setBackground(Color.RED);

                btnStop2.setEnabled(false);
            }

        });

        btnStart2.addActionListener(e -> {
            if(semaphore == 0) {
                thDec = new Thread(
                        () -> {
                            System.out.println(Thread.currentThread().getName() + " : Slider set to value 10");
                            slider.setValue(10);

                            while (!Thread.interrupted()){
                                //wait
                            }
                            semaphore--;

                            System.out.println(Thread.currentThread().getName() + " is shutting down");
                        });

                thDec.setPriority(Thread.MAX_PRIORITY);

                thDec.start();
                semaphore = 1;
                status.setText("Occupied by second thread");
                status.setBackground(Color.RED);

                btnStop1.setEnabled(false);
            }

        });

        btnStop1.addActionListener(e -> {
            if(semaphore == 1) {
                thInc.interrupt();
                btnStop2.setEnabled(true);
                semaphore = 0;

                status.setText("Free");
                status.setBackground(Color.GREEN);
            }
        });

        btnStop2.addActionListener(e -> {
            if(semaphore == 1) {
                thDec.interrupt();

                btnStop1.setEnabled(true);
                semaphore = 0;

                status.setText("Free");
                status.setBackground(Color.GREEN);
            }
        });

        panel.add(btnStart1);
        panel.add(btnStart2);
        panel.add(btnStop1);
        panel.add(btnStop2);
        panel.add(slider);
        panel.add(status);
        return panel;
    }
}
