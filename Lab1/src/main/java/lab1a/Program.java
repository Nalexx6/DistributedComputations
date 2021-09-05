package lab1a;

import javax.swing.*;

public class Program {
    static Thread thDec;
    static Thread thInc;

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
        JButton btn = new JButton("Start");
        JSlider slider = new JSlider();
        btn.addActionListener(e -> {
            thInc = new Thread(
                    () -> {
                        for (int i = 0; i < 200; i++) {
                            System.out.println(Thread.currentThread().getName() + " : Slider set to value 90");
                            slider.setValue(90);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    });

            thDec = new Thread(
                    () -> {
                        for (int i = 0; i < 200; i++) {
                            System.out.println(Thread.currentThread().getName() + " : Slider set to value 10");
                            slider.setValue(10);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    });

            thInc.setPriority(1);
            thDec.setPriority(1);

            thInc.start();
            thDec.start();
        });

        JButton bPlus = new JButton("+");
        bPlus.addActionListener(e -> thInc.setPriority(thInc.getPriority() < 10 ? thInc.getPriority() + 1 : 10));
        JButton bMinus = new JButton("-");
        bMinus.addActionListener(e -> thDec.setPriority(thDec.getPriority() < 10 ? thDec.getPriority() + 1 : 10));
        panel.add(bMinus);
        panel.add(bPlus);
        panel.add(btn);
        panel.add(slider);
        return panel;
    }
}