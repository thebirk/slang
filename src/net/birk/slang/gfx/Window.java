package net.birk.slang.gfx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class Window {

	private int width;
	private int height;
	private JFrame f;
	private Canvas canvas;
	private boolean shouldClose = false;
	private Graphics g;

	public Window(int width, int height) {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.setResizable(false);
		this.width = width;
		this.height = height;

		canvas = new Canvas();
		Dimension size = new Dimension(800, 600);
		canvas.setPreferredSize(size);
		canvas.setMinimumSize(size);
		canvas.setMaximumSize(size);
		f.add(canvas);
		f.pack();

		f.setLocationRelativeTo(null);
		f.setVisible(true);

		// Add keyhandler when we want to support those
		//f.addKeyListener(this);

		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				shouldClose = true;
			}
		});

		canvas.createBufferStrategy(2);
		g = canvas.getBufferStrategy().getDrawGraphics();
	}

	public void clear(int color) {
		g.setColor(new Color(color));
		g.fillRect(0, 0, width, height);
	}

	public void fillRect(int x, int y, int width, int height, int r, int g, int b) {
		//TODO: Clamp color
		this.g.setColor(new Color(r, g, b));
		this.g.fillRect(x, y, width, height);
	}

	public void drawTexture(Texture texture, int x, int y) {
		if(texture.getImage() != null) {
			g.drawImage(texture.getImage(), x, y, null);
		}
	}

	public void present() {
		BufferStrategy bs = canvas.getBufferStrategy();
		g.dispose();
		bs.show();
		g = canvas.getBufferStrategy().getDrawGraphics();
	}

	public boolean shouldClose() {
		return shouldClose;
	}

}
