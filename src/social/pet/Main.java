package social.pet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Math.*;

class RotatingCube extends JPanel {
    double[][] nodes = {{-1, -1, -1}, {-1, -1, 1}, {-1, 1, -1}, {-1, 1, 1},
            {1, -1, -1}, {1, -1, 1}, {1, 1, -1}, {1, 1, 1}};

    int[][] edges = {{0, 1}, {1, 3}, {3, 2}, {2, 0}, {4, 5}, {5, 7}, {7, 6},
            {6, 4}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};

    public RotatingCube() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.orange);

        scale(100);
        rotateCube(PI / 4, atan(sqrt(2)));
    }


    final void scale(double s) {
        for (double[] node : nodes) {
            node[0] *= s;
            node[1] *= s;
            node[2] *= s;
        }
    }

    final void rotateCube(double angleX, double angleY) {
        double sinX = sin(angleX);
        double cosX = cos(angleX);

        double sinY = sin(angleY);
        double cosY = cos(angleY);

        for (double[] node : nodes) {
            double x = node[0];
            double y = node[1];
            double z = node[2];

            node[0] = x * cosX - z * sinX;
            node[2] = z * cosX + x * sinX;

            z = node[2];

            node[1] = y * cosY - z * sinY;
            node[2] = z * cosY + y * sinY;
        }
    }

    void drawCube(Graphics2D g) {
        g.translate(getWidth() / 2, getHeight() / 2);

        for (int[] edge : edges) {
            double[] xy1 = nodes[edge[0]];
            double[] xy2 = nodes[edge[1]];
            g.drawLine((int) round(xy1[0]), (int) round(xy1[1]),
                    (int) round(xy2[0]), (int) round(xy2[1]));
        }

        for (double[] node : nodes)
            g.fillOval((int) round(node[0]) - 4, (int) round(node[1]) - 4, 8, 8);
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawCube(g);
    }

}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RotatingCube cube = new RotatingCube();
            Timer xTimer = new Timer(17, (ActionEvent e) -> {
                cube.rotateCube(0, PI / 180);
                cube.repaint();
            });
            Timer yTimer = new Timer(17, (ActionEvent e) -> {
                cube.rotateCube(PI / 180, 0);
                cube.repaint();
            });
            Timer zTimer = new Timer(34, (ActionEvent e) -> {
                cube.rotateCube(PI / 90, PI / 90);
                cube.repaint();
            });

            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Rotating Cube");
            f.setResizable(false);
            f.add(cube, BorderLayout.NORTH);

            JPanel btnGroup = new JPanel();
            JButton xBtn = new JButton("X");
            xBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    xTimer.start();

                    yTimer.stop();
                    zTimer.stop();
                }
            });
            JButton yBtn = new JButton("Y");
            yBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    yTimer.start();

                    xTimer.stop();
                    zTimer.stop();
                }
            });
            JButton zBtn = new JButton("Z");
            zBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zTimer.start();

                    xTimer.stop();
                    yTimer.stop();
                }
            });
            btnGroup.add(xBtn);
            btnGroup.add(yBtn);
            btnGroup.add(zBtn);
            f.add(btnGroup, BorderLayout.CENTER);

            JLabel info = new JLabel("4pet.social", SwingConstants.CENTER);
            f.add(info, BorderLayout.SOUTH);

            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}