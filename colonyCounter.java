import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class colonyCounter {

	private int h;
	private int w;
	double purplePixels;
	double colonies;
	private int[][] pixels;
	private boolean correction;
	private int hasDark;
	BufferedImage image;
	private int queuecount;
	private int[] data;

	public int[] getData() {
		return this.data;
	}

	public int getCount() {
		return this.queuecount;
	}

	public void setCount(int q) {
		this.queuecount = q;
	}

	public int[][] getPixels() {
		return this.pixels;
	}

	public void setPixels(int[][] p) {
		this.pixels = p;
	}

	public boolean isCorrection() {
		return correction;
	}

	public void setCorrection(boolean correction) {
		this.correction = correction;
	}

	public int isHasDark() {
		return hasDark;
	}

	public void setHasDark(int hasDark) {
		this.hasDark = hasDark;
	}

	public colonyCounter(BufferedImage img, boolean zc) {
		System.out.println("zc: " + zc);
		h = img.getHeight();
		w = img.getWidth();
		this.correction = zc;
		System.out.println("c1: " + this.correction);
		this.hasDark = 0;
		System.out.println("d1: " + this.hasDark);
		setPixels(new int[img.getHeight()][img.getWidth()]);
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				getPixels()[y][x] = img.getRGB(x, y);
			}
		}
		// colonies = purpleCount(pixels);
		//

	}

	public double purpleCount() {
		double purple = 0;
		int counter = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Color tempC = new Color((int) getPixels()[y][x]);
				// System.out.println(x + "," + y + ": " + tempC);
				if (!isClear(tempC)) {
					setCount(0);
					ArrayList<qPixel> queue = new ArrayList<qPixel>();
					queue.add(new qPixel(x, y));

					purple += rippleFill(queue, 0, getPixels());
					setCount(0);
				}
			}
		}

		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.data = new int[w * h];
		ArrayList<Integer> oldData = new ArrayList<Integer>();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				oldData.add(getPixels()[y][x]);
			}
		}
		for (int i = 0; i < data.length; i++) {
			this.data[i] = oldData.get(i);
		}
		image.setRGB(0, 0, w, h, this.data, 0, w);

		System.out.println("c: " + this.correction);
		System.out.println("d: " + this.hasDark);
		if (this.correction == false) {
			System.out.println(purple / 4);
			return purple;
		} else if (this.hasDark > 0) {
			System.out.println(purple / 4);
			return purple;
		} else
			return 0;
	}

	public int cluster(int purples) {
		return 1 + (int) (((double) purples) / 4 + .5);
	}

	public int rippleFill(ArrayList<qPixel> queue, int counter, int[][] tempP) {
		int oy = queue.get(0).getY();
		int ox = queue.get(0).getX();
		tempP[oy][ox] = Color.red.getRGB();
		queue.remove(0);
		setPixels(tempP);
		setCount(getCount() + 1);
		counter++;
		int minX, minY, maxX, maxY;
		if (oy - 1 < 0)
			minY = 0;
		else
			minY = oy - 1;
		if (oy + 1 > h - 1)
			maxY = h - 1;
		else
			maxY = oy + 1;
		if (ox - 1 < 0)
			minX = 0;
		else
			minX = ox - 1;
		if (ox + 1 > w - 1)
			maxX = w - 1;
		else
			maxX = ox + 1;

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				if (!isClear(new Color(getPixels()[y][x]))) {
					queue.add(new qPixel(x, y));
				}
			}
		}

		if (counter < 5 && queue.size() > 0)
			return rippleFill(queue, counter, getPixels());
		else if (counter > 1) {
			for (qPixel a : queue) {
				tempP[a.getY()][a.getX()] = Color.red.getRGB();
				setPixels(tempP);
			}
			return 1;
		} else {
			for (qPixel a : queue) {
				tempP[a.getY()][a.getX()] = Color.red.getRGB();
				setPixels(tempP);
			}
			return 0;
		}

	}

	public static boolean isPurple(Color check) {
		if ((check.getRed() - check.getGreen() > 19)
				& (check.getBlue() - check.getGreen() > 55)) {
			if ((check.getRed() >= 35 & check.getRed() <= 135)
					& (check.getGreen() >= 5 & check.getGreen() <= 125)
					& (check.getBlue() >= 102 & check.getBlue() <= 160)) {
				return true;
			}
		}
		return false;
	}

	public boolean isClear(Color check) {

		double R = check.getRed();
		double G = check.getGreen();
		double B = check.getBlue();

		if (this.correction) {
			if ((check.getRed() - check.getGreen() > 19)
					& (check.getBlue() - check.getGreen() > 55)) {
				if ((check.getRed() >= 35 & check.getRed() <= 135)
						& (check.getGreen() >= 5 & check.getGreen() <= 125)
						& (check.getBlue() >= 102 & check.getBlue() <= 160)) {
					this.hasDark++;
				}
			}
		}

		if (R > 150 || G > 150)
			return true;

		else if (Math.abs(R - G) <= 15 && Math.abs(R - B) <= 25
				&& Math.abs(G - B) <= 25)
			return true;

		else
			return false;

	}
}

class qPixel {
	private int x;
	private int y;

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public qPixel(int ox, int oy) {
		this.x = ox;
		this.y = oy;
	}
}
