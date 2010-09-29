import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Copied from Java2D demo in JDK in order to add some animation effect to the demo.
 */
public class Intro extends Panel {

    static Color black = new Color(20, 20, 20);
    static Color white = new Color(240, 240, 255);
    static Color red = new Color(149, 43, 42);
    static Color blue = new Color(0, 102, 153);
    static Color yellow = new Color(255, 255, 140);

    static Surface surface;

    public Intro() {
        setLayout(new BorderLayout());
        setBackground(Color.gray);
        add(surface = new Surface());
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (surface.isRunning()) {
                    surface.stop();
                }
                else {
                    surface.start();
                }
                repaint();
            }
        });
    }

    public void start() {
        surface.start();
    }

    public void stop() {
        surface.stop();
    }

    /**
     * Surface is the stage where the Director plays its scenes.
     */
    static class Surface extends JPanel implements Runnable {

        static Surface surf;
        static BufferedImage bimg;
        public Director director;
        public int index;
        public long sleepAmt = 30;
        private Thread thread;


        public Surface() {
            surf = this;
            setBackground(black);
            setLayout(new BorderLayout());
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (thread == null) start();
                    else
                        stop();
                }
            });
            director = new Director();
        }


        static FontMetrics getMetrics(Font font) {
            return surf.getFontMetrics(font);
        }


        public void paint(Graphics g) {
            Dimension d = getSize();
            if (d.width <= 0 || d.height <= 0) {
                return;
            }
            if (bimg == null || bimg.getWidth() != d.width || bimg.getHeight() != d.height) {
                bimg = getGraphicsConfiguration().createCompatibleImage(d.width, d.height);
                // reset future scenes
                for (int i = index + 1; i < director.size(); i++) {
                    ((Scene) director.get(i)).reset(d.width, d.height);
                }
            }

            Scene scene = (Scene) director.get(index);
            if (scene.index <= scene.length) {
                if (thread != null) {
                    scene.step(d.width, d.height);
                }

                Graphics2D g2 = bimg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setBackground(getBackground());
                g2.clearRect(0, 0, d.width, d.height);

                scene.render(d.width, d.height, g2);

                if (thread != null) {
                    // increment scene.index after scene.render
                    scene.index++;
                }
                g2.dispose();
            }
            g.drawImage(bimg, 0, 0, this);
        }


        public void start() {
            if (thread == null) {
                thread = new Thread(this);
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setName("Intro");
                thread.start();
            }
        }

        public boolean isRunning() {
            return thread != null && thread.isAlive();
        }

        public synchronized void stop() {
            if (thread != null) {
                thread.interrupt();
            }
            thread = null;
            notifyAll();
        }


        public void reset() {
            index = 0;
            Dimension d = getSize();
            for (int i = 0; i < director.size(); i++) {
                ((Scene) director.get(i)).reset(d.width, d.height);
            }
        }


        public void run() {

            Thread me = Thread.currentThread();

            while (thread == me && !isShowing() || getSize().width <= 0) {
                try {
                    thread.sleep(500);
                }
                catch (InterruptedException e) {
                    return;
                }
            }

            if (index == 0) {
                reset();
            }

            while (thread == me) {
                Scene scene = (Scene) director.get(index);
                if (((Boolean) scene.participate).booleanValue()) {
                    repaint();
                    try {
                        thread.sleep(sleepAmt);
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                    if (scene.index > scene.length) {
                        scene.pause(thread);
                        if (++index >= director.size()) {
                            reset();
                        }
                    }
                }
                else {
                    if (++index >= director.size()) {
                        reset();
                    }
                }
            }
            thread = null;
        }


        /**
         * Part is a piece of the scene.  Classes must implement Part
         * inorder to participate in a scene.
         */
        interface Part {
            public void reset(int newwidth, int newheight);

            public void step(int w, int h);

            public void render(int w, int h, Graphics2D g2);

            public int getBegin();

            public int getEnd();
        }


        /**
         * Director is the holder of the scenes, their names & pause amounts
         * between scenes.
         */
        static class Director extends Vector {

            GradientPaint gp = new GradientPaint(0, 40, blue, 38, 2, black);
            Font f1 = new Font("serif", Font.PLAIN, 200);
            Font f2 = new Font("serif", Font.PLAIN, 75);
            Font f3 = new Font("serif", Font.PLAIN, 72);
            Font f4 = new Font("serif", Font.PLAIN, 30);
            Object parts[][][] = {
                    {{"J  -  scale text on gradient", "0"},
                            {new GpE(GpE.BURI, black, blue, 0, 20),
                                    new TxE("J", f1, TxE.SCI, yellow, 2, 20)}},
                    {{"I  -  scale & rotate text on gradient", "0"},
                            {new GpE(GpE.BURI, blue, black, 0, 22),
                                    new TxE("I", f1, TxE.RI | TxE.SCI, yellow, 2, 22)}},
                    {{"D  -  scale text on gradient", "0"},
                            {new GpE(GpE.BURI, black, blue, 0, 20),
                                    new TxE("D", f1, TxE.SCI, yellow, 2, 20)}},
                    {{"E  -  scale & rotate text on gradient", "0"},
                            {new GpE(GpE.BURI, blue, black, 0, 22),
                                    new TxE("E", f1, TxE.RI | TxE.SCI, yellow, 2, 22)}},
                    {{"JIDE Software -  scale & rotate text on gradient", "1000"},
                            {new GpE(GpE.SIH, blue, black, 0, 40),
                                    new TxE("JIDE Software", f2, TxE.RI | TxE.SCI, yellow, 0, 40)}},
                    {{"Previous scene dither dissolve out", "0"},
                            {new DdE(0, 20, 1)}},
                    {{"Heavyweight component", "1000"},
                            {new GpE(GpE.WI, blue, black, 0, 40),
                                    new GpE(GpE.WD, blue, black, 21, 80),
                                    new TpE(TpE.OI | TpE.NF, black, yellow, 4, 0, 20),
                                    new TpE(TpE.OD | TpE.NF, black, yellow, 4, 11, 40),
                                    new TpE(TpE.OI | TpE.NF | TpE.HAF, black, yellow, 5, 21, 80),
                                    new TxE("This is a heavyweight component", f4, 0, null, 0, 60)}},
            };


            public Director() {
                for (int i = 0; i < parts.length; i++) {
                    Vector v = new Vector();
                    for (int j = 0; j < parts[i][1].length; j++) {
                        v.addElement(parts[i][1][j]);
                    }
                    addElement(new Scene(v, parts[i][0][0], parts[i][0][1]));
                }
            }
        }


        /**
         * Scene is the manager of the parts.
         */
        static class Scene extends Object {
            public Object name;
            public Object participate = Boolean.TRUE;
            public Object pauseAmt;
            public Vector parts;
            public int index;
            public int length;

            public Scene(Vector parts, Object name, Object pauseAmt) {
                this.name = name;
                this.parts = parts;
                this.pauseAmt = pauseAmt;
                for (int i = 0; i < parts.size(); i++) {
                    if (((Part) parts.get(i)).getEnd() > length) {
                        length = ((Part) parts.get(i)).getEnd();
                    }
                }
            }

            public void reset(int w, int h) {
                index = 0;
                for (int i = 0; i < parts.size(); i++) {
                    ((Part) parts.get(i)).reset(w, h);
                }
            }

            public void step(int w, int h) {
                for (int i = 0; i < parts.size(); i++) {
                    Part part = (Part) parts.get(i);
                    if (index >= part.getBegin() && index <= part.getEnd()) {
                        part.step(w, h);
                    }
                }
            }

            public void render(int w, int h, Graphics2D g2) {
                for (int i = 0; i < parts.size(); i++) {
                    Part part = (Part) parts.get(i);
                    if (index >= part.getBegin() && index <= part.getEnd()) {
                        part.render(w, h, g2);
                    }
                }
            }

            public void pause(Thread thread) {
                try {
                    thread.sleep(Long.parseLong((String) pauseAmt));
                }
                catch (Exception e) {
                }
                System.gc();
            }
        } // End Scene class


        /**
         * Text Effect.  Transformation of characters.  Clip or fill.
         */
        static class TxE implements Part {

            static final int INC = 1;
            static final int DEC = 2;
            static final int R = 4;            // rotate
            static final int RI = R | INC;
            static final int RD = R | DEC;
            static final int SC = 8;            // scale
            static final int SCI = SC | INC;
            static final int SCD = SC | DEC;
            static final int SCX = 16;           // scale invert x
            static final int SCXI = SCX | SC | INC;
            static final int SCXD = SCX | SC | DEC;
            static final int SCY = 32;           // scale invert y
            static final int SCYI = SCY | SC | INC;
            static final int SCYD = SCY | SC | DEC;
            static final int AC = 64;           // AlphaComposite
            static final int CLIP = 128;          // Clipping
            static final int NOP = 512;          // No Paint
            private int beginning, ending;
            private int type;
            private double rIncr, sIncr;
            private double sx, sy, rotate;
            private Shape shapes[], txShapes[];
            private int sw;
            private int numRev;
            private Paint paint;


            public TxE(String text,
                       Font font,
                       int type,
                       Paint paint,
                       int beg,
                       int end) {
                this.type = type;
                this.paint = paint;
                this.beginning = beg;
                this.ending = end;

                setIncrements(2);

                char[] chars = text.toCharArray();
                shapes = new Shape[chars.length];
                txShapes = new Shape[chars.length];
                FontRenderContext frc = new FontRenderContext(null, true, true);
                TextLayout tl = new TextLayout(text, font, frc);
                sw = (int) tl.getOutline(null).getBounds().getWidth();
                for (int j = 0; j < chars.length; j++) {
                    String s = String.valueOf(chars[j]);
                    shapes[j] = new TextLayout(s, font, frc).getOutline(null);
                }
            }


            public void setIncrements(double numRevolutions) {
                this.numRev = (int) numRevolutions;
                rIncr = 360.0 / ((ending - beginning) / numRevolutions);
                sIncr = 1.0 / (ending - beginning);
                if ((type & SCX) != 0 || (type & SCY) != 0) {
                    sIncr *= 2;
                }
                if ((type & DEC) != 0) {
                    rIncr = -rIncr;
                    sIncr = -sIncr;
                }
            }


            public void reset(int w, int h) {
                if (type == SCXI) {
                    sx = -1.0;
                    sy = 1.0;
                }
                else if (type == SCYI) {
                    sx = 1.0;
                    sy = -1.0;
                }
                else {
                    sx = sy = (type & DEC) != 0 ? 1.0 : 0.0;
                }
                rotate = 0;
            }


            public void step(int w, int h) {

                float charWidth = w / 2 - sw / 2;

                for (int i = 0; i < shapes.length; i++) {
                    AffineTransform at = new AffineTransform();
                    Rectangle2D maxBounds = shapes[i].getBounds();
                    at.translate(charWidth, h / 2 + maxBounds.getHeight() / 2);
                    charWidth += (float) maxBounds.getWidth() + 1;
                    Shape shape = at.createTransformedShape(shapes[i]);
                    Rectangle2D b1 = shape.getBounds2D();

                    if ((type & R) != 0) {
                        at.rotate(Math.toRadians(rotate));
                    }
                    if ((type & SC) != 0) {
                        at.scale(sx, sy);
                    }
                    shape = at.createTransformedShape(shapes[i]);
                    Rectangle2D b2 = shape.getBounds2D();

                    double xx = (b1.getX() + b1.getWidth() / 2)
                            - (b2.getX() + b2.getWidth() / 2);
                    double yy = (b1.getY() + b1.getHeight() / 2)
                            - (b2.getY() + b2.getHeight() / 2);
                    AffineTransform toCenterAT = new AffineTransform();
                    toCenterAT.translate(xx, yy);
                    toCenterAT.concatenate(at);
                    txShapes[i] = toCenterAT.createTransformedShape(shapes[i]);
                }
                // avoid over rotation
                if (Math.abs(rotate) <= numRev * 360) {
                    rotate += rIncr;
                    if ((type & SCX) != 0) {
                        sx += sIncr;
                    }
                    else if ((type & SCY) != 0) {
                        sy += sIncr;
                    }
                    else {
                        sx += sIncr;
                        sy += sIncr;
                    }
                }
            }


            public void render(int w, int h, Graphics2D g2) {
                Composite saveAC = null;
                if ((type & AC) != 0 && sx > 0 && sx < 1) {
                    saveAC = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) sx));
                }
                GeneralPath path = null;
                if ((type & CLIP) != 0) {
                    path = new GeneralPath();
                }
                if (paint != null) {
                    g2.setPaint(paint);
                }
                for (int i = 0; i < txShapes.length; i++) {
                    if ((type & CLIP) != 0) {
                        path.append(txShapes[i], false);
                    }
                    else {
                        g2.fill(txShapes[i]);
                    }
                }
                if ((type & CLIP) != 0) {
                    g2.clip(path);
                }
                if (saveAC != null) {
                    g2.setComposite(saveAC);
                }
            }


            public int getBegin() {
                return beginning;
            }

            public int getEnd() {
                return ending;
            }
        } // End TxE class


        /**
         * GradientPaint Effect.  Burst, split, horizontal and
         * vertical gradient fill effects.
         */
        static class GpE implements Part {

            static final int INC = 1;             // increasing
            static final int DEC = 2;             // decreasing
            static final int CNT = 4;             // center
            static final int WID = 8;             // width
            static final int WI = WID | INC;
            static final int WD = WID | DEC;
            static final int HEI = 16;            // height
            static final int HI = HEI | INC;
            static final int HD = HEI | DEC;
            static final int SPL = 32 | CNT;      // split
            static final int SIW = SPL | INC | WID;
            static final int SDW = SPL | DEC | WID;
            static final int SIH = SPL | INC | HEI;
            static final int SDH = SPL | DEC | HEI;
            static final int BUR = 64 | CNT;     // burst
            static final int BURI = BUR | INC;
            static final int BURD = BUR | DEC;
            static final int NF = 128;           // no fill
            private Color c1, c2;
            private int beginning, ending;
            private float incr, index;
            private Vector rect = new Vector();
            private Vector grad = new Vector();
            private int type;


            public GpE(int type, Color c1, Color c2, int beg, int end) {
                this.type = type;
                this.c1 = c1;
                this.c2 = c2;
                this.beginning = beg;
                this.ending = end;
            }


            public void reset(int w, int h) {
                incr = 1.0f / (ending - beginning);
                if ((type & CNT) != 0) {
                    incr /= 2.3f;
                }
                if ((type & CNT) != 0 && (type & INC) != 0) {
                    index = 0.5f;
                }
                else if ((type & DEC) != 0) {
                    index = 1.0f;
                    incr = -incr;
                }
                else {
                    index = 0.0f;
                }
                index += incr;
            }


            public void step(int w, int h) {
                rect.clear();
                grad.clear();

                if ((type & WID) != 0) {
                    float w2 = 0, x1 = 0, x2 = 0;
                    if ((type & SPL) != 0) {
                        w2 = w * 0.5f;
                        x1 = w * (1.0f - index);
                        x2 = w * index;
                    }
                    else {
                        w2 = w * index;
                        x1 = x2 = w2;
                    }
                    rect.addElement(new Rectangle2D.Float(0, 0, w2, h));
                    rect.addElement(new Rectangle2D.Float(w2, 0, w - w2, h));
                    grad.addElement(new GradientPaint(0, 0, c1, x1, 0, c2));
                    grad.addElement(new GradientPaint(x2, 0, c2, w, 0, c1));
                }
                else if ((type & HEI) != 0) {
                    float h2 = 0, y1 = 0, y2 = 0;
                    if ((type & SPL) != 0) {
                        h2 = h * 0.5f;
                        y1 = h * (1.0f - index);
                        y2 = h * index;
                    }
                    else {
                        h2 = h * index;
                        y1 = y2 = h2;
                    }
                    rect.addElement(new Rectangle2D.Float(0, 0, w, h2));
                    rect.addElement(new Rectangle2D.Float(0, h2, w, h - h2));
                    grad.addElement(new GradientPaint(0, 0, c1, 0, y1, c2));
                    grad.addElement(new GradientPaint(0, y2, c2, 0, h, c1));
                }
                else if ((type & BUR) != 0) {

                    float w2 = w / 2;
                    float h2 = h / 2;

                    rect.addElement(new Rectangle2D.Float(0, 0, w2, h2));
                    rect.addElement(new Rectangle2D.Float(w2, 0, w2, h2));
                    rect.addElement(new Rectangle2D.Float(0, h2, w2, h2));
                    rect.addElement(new Rectangle2D.Float(w2, h2, w2, h2));

                    float x1 = w * (1.0f - index);
                    float x2 = w * index;
                    float y1 = h * (1.0f - index);
                    float y2 = h * index;

                    grad.addElement(new GradientPaint(0, 0, c1, x1, y1, c2));
                    grad.addElement(new GradientPaint(w, 0, c1, x2, y1, c2));
                    grad.addElement(new GradientPaint(0, h, c1, x1, y2, c2));
                    grad.addElement(new GradientPaint(w, h, c1, x2, y2, c2));
                }
                else if ((type & NF) != 0) {
                    float x = w * index;
                    float y = h * index;
                    grad.addElement(new GradientPaint(0, 0, c1, 0, y, c2));
                }

                if ((type & INC) != 0 || (type & DEC) != 0) {
                    index += incr;
                }
            }


            public void render(int w, int h, Graphics2D g2) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_OFF);
                for (int i = 0; i < grad.size(); i++) {
                    g2.setPaint((GradientPaint) grad.get(i));
                    if ((type & NF) == 0) {
                        g2.fill((Rectangle2D) rect.get(i));
                    }
                }
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
            }

            public int getBegin() {
                return beginning;
            }

            public int getEnd() {
                return ending;
            }
        } // End GpE class


        /**
         * TexturePaint Effect.  Expand and collapse a texture.
         */
        static class TpE implements Part {

            static final int INC = 1;             // increasing
            static final int DEC = 2;             // decreasing
            static final int OVAL = 4;            // oval
            static final int RECT = 8;            // rectangle
            static final int HAF = 16;            // half oval or rect size
            static final int OI = OVAL | INC;
            static final int OD = OVAL | DEC;
            static final int RI = RECT | INC;
            static final int RD = RECT | DEC;
            static final int NF = 32;             // no fill
            private Paint p1, p2;
            private int beginning, ending;
            private float incr, index;
            private TexturePaint texture;
            private int type;
            private int size;
            private BufferedImage bimg;
            private Rectangle rect;


            public TpE(int type, Paint p1, Paint p2, int size,
                       int beg, int end) {
                this.type = type;
                this.p1 = p1;
                this.p2 = p2;
                this.beginning = beg;
                this.ending = end;
                setTextureSize(size);
            }


            public void setTextureSize(int size) {
                this.size = size;
                bimg = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
                rect = new Rectangle(0, 0, size, size);
            }


            public void reset(int w, int h) {
                incr = (float) (size) / (float) (ending - beginning);
                if ((type & HAF) != 0) {
                    incr /= 2;
                }
                if ((type & DEC) != 0) {
                    index = size;
                    if ((type & HAF) != 0) {
                        index /= 2;
                    }
                    incr = -incr;
                }
                else {
                    index = 0.0f;
                }
                index += incr;
            }


            public void step(int w, int h) {
                Graphics2D g2 = bimg.createGraphics();
                g2.setPaint(p1);
                g2.fillRect(0, 0, size, size);
                g2.setPaint(p2);
                if ((type & OVAL) != 0) {
                    g2.fill(new Ellipse2D.Float(0, 0, index, index));
                }
                else if ((type & RECT) != 0) {
                    g2.fill(new Rectangle2D.Float(0, 0, index, index));
                }
                texture = new TexturePaint(bimg, rect);
                g2.dispose();
                index += incr;
            }


            public void render(int w, int h, Graphics2D g2) {
                g2.setPaint(texture);
                if ((type & NF) == 0) {
                    g2.fillRect(0, 0, w, h);
                }
            }

            public int getBegin() {
                return beginning;
            }

            public int getEnd() {
                return ending;
            }
        } // End TpE class

        /**
         * Dither Dissolve Effect. For each successive step in the animation,
         * a pseudo-random starting horizontal position is chosen using list,
         * and then the corresponding points created from xlist and ylist are
         * blacked out for the current "chunk".  The x and y chunk starting
         * positions are each incremented by the associated chunk size, and
         * this process is repeated for the number of "steps" in the
         * animation, causing an equal number of pseudo-randomly picked
         * "blocks" to be blacked out during each step of the animation.
         */
        static class DdE implements Part {

            private int beginning, ending;
            private BufferedImage bimg;
            private Graphics2D big;
            private List list, xlist, ylist;
            private int xeNum, yeNum;    // element number
            private int xcSize, ycSize;  // chunk size
            private int inc;
            private int blocksize;


            public DdE(int beg, int end, int blocksize) {
                this.beginning = beg;
                this.ending = end;
                this.blocksize = blocksize;
            }

            private void createShuffledLists() {
                int width = bimg.getWidth();
                int height = bimg.getHeight();
                Integer xarray[] = new Integer[width];
                Integer yarray[] = new Integer[height];
                Integer array[] = new Integer[ending - beginning + 1];
                for (int i = 0; i < xarray.length; i++) {
                    xarray[i] = new Integer(i);
                }
                for (int j = 0; j < yarray.length; j++) {
                    yarray[j] = new Integer(j);
                }
                for (int k = 0; k < array.length; k++) {
                    array[k] = new Integer(k);
                }
                java.util.Collections.shuffle(xlist = Arrays.asList(xarray));
                java.util.Collections.shuffle(ylist = Arrays.asList(yarray));
                java.util.Collections.shuffle(list = Arrays.asList(array));
            }

            public void reset(int w, int h) {
                bimg = null;
            }

            public void step(int w, int h) {
                if (bimg == null) {
                    int biw = Surface.bimg.getWidth();
                    int bih = Surface.bimg.getHeight();
                    bimg = new BufferedImage(biw, bih, BufferedImage.TYPE_INT_RGB);
                    createShuffledLists();
                    big = bimg.createGraphics();
                    big.drawImage(Surface.bimg, 0, 0, null);
                    xcSize = (xlist.size() / (ending - beginning)) + 1;
                    ycSize = (ylist.size() / (ending - beginning)) + 1;
                    xeNum = 0;
                    inc = 0;
                }
                xeNum = xcSize * ((Integer) list.get(inc)).intValue();
                yeNum = -ycSize;
                inc++;
            }


            public void render(int w, int h, Graphics2D g2) {
                big.setColor(black);

                for (int k = 0; k <= (ending - beginning); k++) {
                    if ((xeNum + xcSize) > xlist.size()) {
                        xeNum = 0;
                    }
                    else {
                        xeNum += xcSize;
                    }
                    yeNum += ycSize;

                    for (int i = xeNum; i < xeNum + xcSize && i < xlist.size(); i++) {
                        for (int j = yeNum; j < yeNum + ycSize && j < ylist.size(); j++) {
                            int xval = ((Integer) xlist.get(i)).intValue();
                            int yval = ((Integer) ylist.get(j)).intValue();
                            if (((xval % blocksize) == 0) &&
                                    ((yval % blocksize) == 0)) {
                                big.fillRect(xval, yval, blocksize, blocksize);
                            }
                        }
                    }
                }

                g2.drawImage(bimg, 0, 0, null);
            }

            public int getBegin() {
                return beginning;
            }

            public int getEnd() {
                return ending;
            }
        } // End DdE class
    } // End Surface class
} // End Intro class
