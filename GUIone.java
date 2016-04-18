import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class GUIone extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new GUIone();
	}

	JPanel mainPanel;
	JButton select;
	static JScrollPane scroll;
	JRadioButton zon, zoff;
	static JButton count;
	static JTextArea countprint;
	JFileChooser fc = new JFileChooser();
	BufferedImage colonyArray;
	double oneHundred = -1;
	public static TitledBorder readyBorder;
	Calibrate cPanel;
	preferences pPanel;
	JFrame popUp1, popUp2;
	File file;
	boolean zeroc = false;

	public GUIone() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
		this.setTitle("Cell Colony Counter");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainPanel = new JPanel(new GridBagLayout());
		this.add(mainPanel);
		this.setSize(360, 250);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		select = new JButton("Select File");
		select.addActionListener(this);
		mainPanel.add(select, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		count = new JButton("count");
		count.setEnabled(false);
		count.addActionListener(this);
		mainPanel.add(count, constraints);

		JPanel zPanel = new JPanel();
		Border zBorder = BorderFactory.createTitledBorder("Zero Correction");
		zPanel.setBorder(zBorder);
		ButtonGroup zGroup = new ButtonGroup();
		zon = new JRadioButton("on");
		zoff = new JRadioButton("off");
		zon.addActionListener(this);
		zoff.addActionListener(this);
		zGroup.add(zon);
		zGroup.add(zoff);
		constraints.gridx = 0;
		constraints.gridy = 0;
		zPanel.add(zon, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		zPanel.add(zoff, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		mainPanel.add(zPanel, constraints);
		zoff.setSelected(true);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 3;
		constraints.gridwidth = 3;
		countprint = new JTextArea(10, 15);
		countprint.setEditable(false);
		scroll = new JScrollPane(countprint);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mainPanel.add(scroll, constraints);

		readyBorder = BorderFactory.createTitledBorder("Select File");
		readyBorder.setTitleColor(Color.red);

		scroll.setBorder(readyBorder);

		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == select) {
			countprint.append("\n");
			oneHundred = -1;
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				try {
					colonyArray = (BufferedImage) ImageIO.read(file);
					System.out.println(colonyArray.getWidth());
					System.out.println(colonyArray.getHeight());
					if (colonyArray == null)
						JOptionPane.showMessageDialog(this,
								"File is not an Image!", "Error",
								JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(this, "Error Reading Image",
							"Error", JOptionPane.ERROR_MESSAGE);
				}

				//
				// colonyCounter counter = new colonyCounter(colonyArray);
				// System.out.println(counter.purpleCount(counter.ipixels));
			}

			if (popUp1 == null)
				popUp1 = new JFrame();
			else {
				popUp1.dispose();
				popUp1 = new JFrame();
			}
			if (popUp2 == null)
				popUp2 = new JFrame();
			else {
				popUp2.dispose();
				popUp2 = new JFrame();
			}
			configure(e);
			System.out.println(this.zeroc);
		} else if (e.getSource() == count) {
			countprint.append(file.getName().substring(0,
					file.getName().length() - 4)
					+ "\n");
			for (int i = 0; i < 6; i++) {
				BufferedImage tempi = null;
				if (i == 0)
					tempi = cPanel.i1;
				else if (i == 1)
					tempi = cPanel.i2;
				else if (i == 2)
					tempi = cPanel.i3;
				else if (i == 3)
					tempi = cPanel.i4;
				else if (i == 4)
					tempi = cPanel.i5;
				else if (i == 5)
					tempi = cPanel.i6;
				colonyCounter counter = new colonyCounter(tempi, this.zeroc);
				double pcount = counter.purpleCount();
				if (this.oneHundred == -1) {
					this.oneHundred = pcount;
				}

				float percent = (float) (pcount / (double) oneHundred * 100);
				if (percent > 100)
					percent = 100;
				countprint.append(pcount + "\t" + percent + "\n");

				// + "\t"
				// + counter.purpleCount(counter.ipixels) * 4
				cPanel.countedImage(counter.getData(), i + 1);
			}
		} else if (e.getSource() == zoff) {
			this.zeroc = false;
			System.out.println(this.zeroc);
		} else if (e.getSource() == zon) {
			this.zeroc = true;
			System.out.println(this.zeroc);
		}
	}

	public void configure(ActionEvent e) {
		popUp1.setTitle("Calibrate " + file.getName());
		cPanel = new Calibrate(colonyArray);
		popUp1.add(cPanel);
		popUp1.setSize(640, 830);
		popUp1.setVisible(true);

		popUp2.setTitle("Colony Size");
		pPanel = new preferences();
		popUp2.add(pPanel);
		popUp2.pack();
		popUp2.setVisible(true);

	}

	public static void changeBorder(int i) {
		if (i == 1) {
			TitledBorder readyBorder1 = BorderFactory
					.createTitledBorder("Calibrate!");
			readyBorder1.setTitleColor(Color.red);
			scroll.setBorder(readyBorder1);
		} else {
			Border readyBorder2 = BorderFactory.createLineBorder(Color.green);
			scroll.setBorder(readyBorder2);
			count.setEnabled(true);
		}
	}

}

class Calibrate extends JPanel implements MouseListener {

	private Image img;
	JLabel pic;
	Point reference;
	Point c1, c2, L6T, L5T, L4T, L4B, M63T, M52T, M41T, M41B, R3T, R2T, R1T,
			R1B;
	BufferedImage i1, i2, i3, i4, i5, i6;

	public BufferedImage geti1() {
		return i1;
	}

	public BufferedImage geti2() {
		return i2;
	}

	public BufferedImage geti3() {
		return i3;
	}

	public BufferedImage geti4() {
		return i4;
	}

	public BufferedImage geti5() {
		return i5;
	}

	public BufferedImage geti6() {
		return i6;
	}

	public Calibrate(BufferedImage a) {
		super();
		// pic = new JLabel(new ImageIcon(a.getScaledInstance(638, 825,
		// Image.SCALE_DEFAULT)));
		this.img = (Image) a.getScaledInstance(638, 825, Image.SCALE_DEFAULT);
		// this.add(pic);
		this.addMouseListener(this);
		this.setSize(640, 830);
		GUIone.changeBorder(1);
	}

	public void countedImage(int[] data, int i) {
		BufferedImage bimg = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bbGr = bimg.createGraphics();
		bbGr.drawImage(img, 0, 0, null);
		bbGr.dispose();
		if (i == 1) {
			bimg.setRGB(M41T.x, M41T.y, 110, 110, data, 0, 110);
		} else if (i == 2) {
			bimg.setRGB(M52T.x, M52T.y, 110, 110, data, 0, 110);
		} else if (i == 3) {
			bimg.setRGB(M63T.x, M63T.y, 110, 110, data, 0, 110);
		} else if (i == 4) {
			bimg.setRGB(L4T.x, L4T.y, 110, 110, data, 0, 110);
		} else if (i == 5) {
			bimg.setRGB(L5T.x, L5T.y, 110, 110, data, 0, 110);
		} else if (i == 6) {
			bimg.setRGB(L6T.x, L6T.y, 110, 110, data, 0, 110);
		}
		this.img = bimg;
		this.removeAll();
		this.repaint();
	}

	protected void paintComponent(Graphics g) {
		g.drawImage(this.img, 0, 0, this);
		if (c1 != null) {

			g.setColor(Color.red);
			g.fillOval(c1.x - 5, c1.y - 7, 10, 10);
		}
		if (c2 != null) {

			g.setColor(Color.red);
			g.fillOval(c2.x - 5, c2.y - 7, 10, 10);
		}
		if (c2 != null) {
			g.setColor(Color.red);
			g.drawLine(c1.x, c1.y - 60, c2.x, c2.y + 55);

			M63T = new Point(c1.x, c1.y - 60);
			g.drawLine(M63T.x - 110, M63T.y, M63T.x, M63T.y);
			L6T = new Point(M63T.x - 110, M63T.y);
			g.drawLine(M63T.x + 110, M63T.y, M63T.x, M63T.y);
			R3T = new Point(M63T.x + 110, M63T.y);

			M52T = new Point(c1.x, c1.y + 55);
			g.drawLine(M52T.x - 110, M52T.y, M52T.x, M52T.y);
			L5T = new Point(M52T.x - 110, M52T.y);
			g.drawLine(M52T.x + 110, M52T.y, M52T.x, M52T.y);
			R2T = new Point(M52T.x + 110, M52T.y);

			M41T = new Point(M52T.x, M52T.y + 115);
			g.drawLine(M41T.x - 110, M41T.y, M41T.x, M41T.y);
			L4T = new Point(M41T.x - 110, M41T.y);
			g.drawLine(M41T.x + 110, M41T.y, M41T.x, M41T.y);
			R1T = new Point(M41T.x + 110, M41T.y);

			M41B = new Point(c2.x, c2.y + 55);
			g.drawLine(M41B.x - 110, M41B.y, M41B.x, M41B.y);
			L4B = new Point(M41B.x - 110, M41B.y);
			g.drawLine(M41B.x + 110, M41B.y, M41B.x, M41B.y);
			R1B = new Point(M41B.x + 110, M41B.y);

			g.drawLine(L6T.x, L6T.y, L4B.x, L4B.y);
			g.drawLine(R3T.x, R3T.y, R1B.x, R1B.y);
			crop();
			GUIone.changeBorder(2);
		}
	}

	public void mouseClicked(MouseEvent e) {
		reference = e.getPoint();
		// SwingUtilities.convertPointFromScreen(e.getPoint(), pic);
		System.out.println(reference);
		if (c1 == null)
			c1 = new Point(reference.x, reference.y);
		else if (c2 == null)
			c2 = new Point(reference.x, reference.y);
		this.removeAll();
		this.repaint();
		System.out.println("c1: " + c1);
		System.out.println("M63T: " + M63T);
		System.out.println("M52T: " + M52T);
		System.out.println("M41T: " + M41T);
		System.out.println("M41B: " + M41B);
		System.out.println("L6T: " + L6T);
		System.out.println("L5T: " + L5T);
		System.out.println("L4T: " + L4T);
		System.out.println("L4B: " + L4B);
		System.out.println("R3T: " + R3T);
		System.out.println("R2T: " + R2T);
		System.out.println("R1T: " + R1T);
		System.out.println("R1B: " + R1B);
	}

	public void crop() {

		BufferedImage bimg = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bbGr = bimg.createGraphics();
		bbGr.drawImage(img, 0, 0, null);
		bbGr.dispose();

		i6 = bimg.getSubimage(L6T.x, L6T.y, 110, 110);
		/*
		 * Image si6 = i6.getScaledInstance(93, 93, Image.SCALE_DEFAULT);
		 * BufferedImage s6bimg = new BufferedImage(si6.getWidth(null),
		 * si6.getHeight(null), BufferedImage.TYPE_INT_ARGB); Graphics2D s6bbGr
		 * = s6bimg.createGraphics(); s6bbGr.drawImage(si6, 0, 0, null);
		 * s6bbGr.dispose(); i6 = s6bimg;
		 */

		i5 = bimg.getSubimage(L5T.x, L5T.y, 110, 110);
		i4 = bimg.getSubimage(L4T.x, L4T.y, 110, 110);
		i3 = bimg.getSubimage(M63T.x, M63T.y, 110, 110);
		i2 = bimg.getSubimage(M52T.x, M52T.y, 110, 110);
		i1 = bimg.getSubimage(M41T.x, M41T.y, 110, 110);
		/*
		 * Image si1 = i1.getScaledInstance(93, 93, Image.SCALE_DEFAULT);
		 * BufferedImage s1bimg = new BufferedImage(si1.getWidth(null),
		 * si1.getHeight(null), BufferedImage.TYPE_INT_ARGB); Graphics2D s1bbGr
		 * = s1bimg.createGraphics(); s1bbGr.drawImage(si1, 0, 0, null);
		 * s1bbGr.dispose(); i1 = s1bimg;
		 */
		GUIone.readyBorder.setTitle("Ready");
		GUIone.readyBorder.setTitleColor(Color.green);
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

class preferences extends JPanel implements ActionListener {

	JRadioButton avg1, avg2, avg3, avg4, avg5;
	JRadioButton min1, min2, min3, min4, min5;
	JButton apply;

	public preferences() {
		super(new GridBagLayout());
		GridBagConstraints pc = new GridBagConstraints();

		JPanel aPanel = new JPanel(new GridBagLayout());
		aPanel.setBorder(BorderFactory
				.createTitledBorder("Average Colony Size"));
		ButtonGroup avgGroup = new ButtonGroup();
		avg1 = new JRadioButton("1");
		avg1.addActionListener(this);

		avgGroup.add(avg1);
		avg2 = new JRadioButton("2");
		avg2.addActionListener(this);

		avgGroup.add(avg2);
		avg3 = new JRadioButton("3");
		avg3.addActionListener(this);

		avgGroup.add(avg3);
		avg4 = new JRadioButton("4");
		avg4.addActionListener(this);

		avgGroup.add(avg4);
		avg5 = new JRadioButton("5");
		avg5.addActionListener(this);

		avgGroup.add(avg5);

		pc.gridx = 0;
		pc.gridy = 1;
		aPanel.add(avg1, pc);
		pc.gridx = 1;
		pc.gridy = 1;
		aPanel.add(avg2, pc);
		pc.gridx = 2;
		pc.gridy = 1;
		aPanel.add(avg3, pc);
		pc.gridx = 3;
		pc.gridy = 1;
		aPanel.add(avg4, pc);
		pc.gridx = 4;
		pc.gridy = 1;
		aPanel.add(avg5, pc);
		pc.gridx = 0;
		pc.gridy = 0;
		this.add(aPanel, pc);

		JPanel mPanel = new JPanel(new GridBagLayout());
		mPanel.setBorder(BorderFactory
				.createTitledBorder("Minimum Colony Size"));
		ButtonGroup minGroup = new ButtonGroup();
		min1 = new JRadioButton("1");
		min1.addActionListener(this);

		minGroup.add(min1);
		min2 = new JRadioButton("2");
		min2.addActionListener(this);

		minGroup.add(min2);
		min3 = new JRadioButton("3");
		min3.addActionListener(this);

		minGroup.add(min3);
		min4 = new JRadioButton("4");
		min4.addActionListener(this);

		minGroup.add(min4);
		min5 = new JRadioButton("5");
		min5.addActionListener(this);

		minGroup.add(min5);

		pc.gridx = 0;
		pc.gridy = 1;
		mPanel.add(min1, pc);
		pc.gridx = 1;
		pc.gridy = 1;
		mPanel.add(min2, pc);
		pc.gridx = 2;
		pc.gridy = 1;
		mPanel.add(min3, pc);
		pc.gridx = 3;
		pc.gridy = 1;
		mPanel.add(min4, pc);
		pc.gridx = 4;
		pc.gridy = 1;
		mPanel.add(min5, pc);
		pc.gridx = 0;
		pc.gridy = 1;
		this.add(mPanel, pc);

		apply = new JButton("Apply");
		apply.addActionListener(this);
		pc.gridx = 1;
		pc.gridy = 2;
		this.add(apply, pc);

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
