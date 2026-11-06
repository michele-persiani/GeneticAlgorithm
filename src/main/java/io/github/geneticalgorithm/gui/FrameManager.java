package io.github.geneticalgorithm.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FrameManager
{
    private static FrameManager instance;
    private final JFrame frame;

    private final java.util.List<Component> components = new ArrayList<>();

    private FrameManager()
    {
        frame = new JFrame("Genetic Algorithm");
        frame.setLayout(new FlowLayout());
    }

    public void repaint()
    {
        SwingUtilities.invokeLater(() -> {
            frame.repaint();
            components.forEach(Component::repaint);
        });
    }

    public void show()
    {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public void addModule(Component ...components)
    {
        SwingUtilities.invokeLater(() -> {
            Panel panel = new Panel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            Arrays.stream(components).forEach(c -> {
                panel.add(c);
                FrameManager.this.components.add(c);
            });
            frame.add(panel);
            frame.pack();
        });
    }

    public void runSynchronously(Runnable runnable)
    {
        SwingUtilities.invokeLater(runnable);
    }

    public static FrameManager getInstance()
    {
        if(instance == null)
            instance = new FrameManager();
        return instance;
    }
}
