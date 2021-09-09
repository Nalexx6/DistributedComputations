package lab1a;

import javax.swing.*;

public class Program {
    private static Thread thDec;
    private static Thread thInc;
    private static boolean STOP;

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
                        while (!STOP && slider.getValue() > 10 && slider.getValue() < 90) {
                            synchronized (slider) {
                                slider.setValue(slider.getValue() + 1);
                            }
                            System.out.println(Thread.currentThread().getName() +
                                    " : Slider set to value to" + slider.getValue());
                            try {
                                Thread.sleep(3);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    });

            thDec = new Thread(
                    () -> {
                        while (!STOP && slider.getValue() > 10 && slider.getValue() < 90 ) {
                            synchronized (slider) {
                                slider.setValue(slider.getValue() - 1);
                            }
                            System.out.println(Thread.currentThread().getName() +
                                    " : Slider set to value to" + slider.getValue());
                            try {
                                Thread.sleep(3);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    });

            thInc.setPriority(1);
            thDec.setPriority(1);

            STOP = false;
            slider.setValue(50);

            thInc.setDaemon(true);
            thDec.setDaemon(true);

            thInc.start();
            thDec.start();

            btn.setEnabled(false);
        });

        JButton bPlus = new JButton("+");
        bPlus.addActionListener(e -> thInc.setPriority(thInc.getPriority() < 10 ? thInc.getPriority() + 1 : 10));
        JButton bMinus = new JButton("-");
        bMinus.addActionListener(e -> thDec.setPriority(thDec.getPriority() < 10 ? thDec.getPriority() + 1 : 10));
        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> {
            slider.setValue(50);

            STOP = true;

            btn.setEnabled(true);
        });
        panel.add(bMinus);
        panel.add(bPlus);
        panel.add(btn);
        panel.add(stop);
        panel.add(slider);
        return panel;
    }
}