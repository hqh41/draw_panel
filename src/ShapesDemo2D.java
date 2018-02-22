import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JApplet;
import javax.swing.JFrame;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - Neither the name of Oracle or the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * This is like the FontDemo applet in volume 1, except that it uses the Java 2D
 * APIs to define and render the graphics and text.
 */

@SuppressWarnings("serial")
public class ShapesDemo2D extends JApplet
{
	protected final static int maxCharHeight = 15;
	protected final static int minFontSize = 6;

	protected final static Color bg = Color.white;
	protected final static Color fg = Color.black;
	protected final static Color red = Color.red;
	protected final static Color white = Color.white;

	protected final static BasicStroke stroke = new BasicStroke(2.0f);
	protected final static BasicStroke wideStroke = new BasicStroke(8.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	protected final static float lastWidth = 20.0f;
	protected final static float dash1[] = { 2*lastWidth };
	protected final static BasicStroke dashed = new BasicStroke(1.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash1, 0.0f);
	protected final static BasicStroke fatDashed = new BasicStroke(lastWidth,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, lastWidth, dash1, 0.0f);
	protected Dimension totalSize;
	protected FontMetrics fontMetrics;

	@Override
	public void init()
	{
		// Initialize drawing colors
		setBackground(bg);
		setForeground(fg);
	}

	FontMetrics pickFont(Graphics2D g2, String longString, int xSpace)
	{
		boolean fontFits = false;
		Font font = g2.getFont();
		FontMetrics fontMetrics = g2.getFontMetrics();
		int size = font.getSize();
		String name = font.getName();
		int style = font.getStyle();

		while (!fontFits)
		{
			if ((fontMetrics.getHeight() <= maxCharHeight)
					&& (fontMetrics.stringWidth(longString) <= xSpace))
			{
				fontFits = true;
			}
			else
			{
				if (size <= minFontSize)
				{
					fontFits = true;
				}
				else
				{
					g2.setFont(font = new Font(name, style, --size));
					fontMetrics = g2.getFontMetrics();
				}
			}
		}

		return fontMetrics;
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension d = getSize();
		int gridWidth = d.width / 6;
		int gridHeight = d.height / 2;

		fontMetrics = pickFont(g2, "Filled and Stroked GeneralPath", gridWidth);

		Color fg3D = Color.lightGray;

		// on commence par effacer le fond
		g2.setColor(getBackground());
		g2.fillRect(0, 0, d.width, d.height);

		g2.setPaint(fg3D);
		g2.draw3DRect(0, 0, d.width - 1, d.height - 1, true);
		g2.draw3DRect(3, 3, d.width - 7, d.height - 7, false);
		g2.setPaint(fg);

		int x = 5;
		int y = 7;
		int rectWidth = gridWidth - (2 * x);
		int stringY = gridHeight - 3 - fontMetrics.getDescent();
		int rectHeight = stringY - fontMetrics.getMaxAscent() - y - 2;

		// draw Line2D.Double
		g2.draw(new Line2D.Double(x, (y + rectHeight) - 1, x + rectWidth, y));
		g2.drawString("Line2D", x, stringY);
		x += gridWidth;

		// draw Rectangle2D.Double
		g2.setStroke(stroke);
		g2.draw(new Rectangle2D.Double(x, y, rectWidth, rectHeight));
		g2.drawString("Rectangle2D", x, stringY);
		x += gridWidth;

		// draw RoundRectangle2D.Double
		g2.setStroke(dashed);
		g2.draw(new RoundRectangle2D.Double(x, y, rectWidth, rectHeight, 10, 10));
		g2.drawString("RoundRectangle2D", x, stringY);
		x += gridWidth;

		// draw Arc2D.Double
		g2.setStroke(wideStroke);
		g2.draw(new Arc2D.Double(x, y, rectWidth, rectHeight, 90, 135,
				Arc2D.OPEN));
		g2.drawString("Arc2D", x, stringY);
		x += gridWidth;

		// draw Ellipse2D.Double
		g2.setStroke(stroke);
		g2.draw(new Ellipse2D.Double(x, y, rectWidth, rectHeight));
		g2.drawString("Ellipse2D", x, stringY);
		x += gridWidth;

		// draw GeneralPath (polygon)
		int x1Points[] = { x, x + rectWidth, x, x + rectWidth };
		int y1Points[] = { y, y + rectHeight, y + rectHeight, y };
		GeneralPath polygon = new GeneralPath(Path2D.WIND_EVEN_ODD,
				x1Points.length);
		polygon.moveTo(x1Points[0], y1Points[0]);
		for (int index = 1; index < x1Points.length; index++)
		{
			polygon.lineTo(x1Points[index], y1Points[index]);
		}

		polygon.closePath();

		g2.draw(polygon);
		g2.drawString("GeneralPath", x, stringY);

		// NEW ROW
		x = 5;
		y += gridHeight;
		stringY += gridHeight;

		// draw GeneralPath (polyline)

		int x2Points[] = { x, x + rectWidth, x, x + rectWidth };
		int y2Points[] = { y, y + rectHeight, y + rectHeight, y };
		GeneralPath polyline = new GeneralPath(Path2D.WIND_EVEN_ODD,
				x2Points.length);
		polyline.moveTo(x2Points[0], y2Points[0]);
		for (int index = 1; index < x2Points.length; index++)
		{
			polyline.lineTo(x2Points[index], y2Points[index]);
		}

		g2.draw(polyline);
		g2.drawString("GeneralPath (open)", x, stringY);
		x += gridWidth;

		// fill Rectangle2D.Double (red)
		g2.setPaint(red);
		g2.fill(new Rectangle2D.Double(x, y, rectWidth, rectHeight));
		g2.setPaint(fg);
		g2.drawString("Filled Rectangle2D", x, stringY);
		x += gridWidth;

		// fill RoundRectangle2D.Double
		GradientPaint redtowhite = new GradientPaint(x, y, red, x + rectWidth,
				y, white);
		g2.setPaint(redtowhite);
		g2.fill(new RoundRectangle2D.Double(x, y, rectWidth, rectHeight, 10, 10));
		g2.setPaint(fg);
		g2.drawString("Filled RoundRectangle2D", x, stringY);
		x += gridWidth;

		// fill Arc2D
		g2.setPaint(red);
		g2.fill(new Arc2D.Double(x, y, rectWidth, rectHeight, 90, 135,
				Arc2D.OPEN));
		g2.setPaint(fg);
		g2.drawString("Filled Arc2D", x, stringY);
		x += gridWidth;

		// fill Ellipse2D.Double
		redtowhite = new GradientPaint(x, y, red, x + rectWidth, y, white);
		g2.setPaint(redtowhite);
		g2.fill(new Ellipse2D.Double(x, y, rectWidth, rectHeight));
		g2.setPaint(fg);
		g2.drawString("Filled Ellipse2D", x, stringY);
		x += gridWidth;

		// fill and stroke GeneralPath
		int x3Points[] = { x, x + rectWidth, x, x + rectWidth };
		int y3Points[] = { y, y + rectHeight, y + rectHeight, y };
		GeneralPath filledPolygon = new GeneralPath(Path2D.WIND_EVEN_ODD,
				x3Points.length);
		filledPolygon.moveTo(x3Points[0], y3Points[0]);
		for (int index = 1; index < x3Points.length; index++)
		{
			filledPolygon.lineTo(x3Points[index], y3Points[index]);
		}
		filledPolygon.closePath();

		g2.setPaint(red);
		g2.fill(filledPolygon);

		g2.setStroke(fatDashed);
		g2.setPaint(fg);
		g2.draw(filledPolygon);

		g2.drawString("Filled and Stroked GeneralPath", x, stringY);
	}

	public static void main(String s[])
	{
		JFrame f = new JFrame("ShapesDemo2D");
		f.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		JApplet applet = new ShapesDemo2D();
		f.getContentPane().add("Center", applet);
		applet.init();
		f.pack();
		f.setSize(new Dimension(550, 100));
		f.setVisible(true);
	}

}
