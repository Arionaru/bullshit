
import java.awt.*;
import java.util.Random;

public class MeteorFlow {
    private final Meteor[] flow;
    private final int size;
    private static int count;
    private static Random rnd = new Random();


    public MeteorFlow(int size, Point point) {   // meteors around point
        this.flow = new Meteor[size];
        this.size = size;

        for (int i = 0; i < size; i++) {
            flow[i] = new Meteor(point);
        }
        count = 0; // timer
    }

    public MeteorFlow(int size, Point point, Point speed) {   // meteors around point
        this.flow = new Meteor[size];
        this.size = size;

        for (int i = 0; i < size; i++) {
            flow[i] = new Meteor(point, speed);
        }
        count = 0; // timer
    }

    public void update() {
        for (int i = 0; i < size; i++) {  // смена позиции каждого метеорита
            flow[i].update();
        }

        for (int i = 0; i < size; i++) {        // проверка коллизий
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    continue;
                }
                flow[i].collision(flow[j]);
            }
        }
        count++;
    }

    public Meteor get(int index) {
        if (flow == null || index < 0 || index >= flow.length) {
            throw new IllegalArgumentException();
        }
        return flow[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Meteors flow state: " + count + "\n");
        for (int i = 0; i < size; i++) {
            sb.append(flow[i].toString() + "\n");
        }

        return sb.toString();
    }


    private static MeteorFlow genFlow(Point center, Point mouse, double size, int n) {

        double distance = Math.sqrt(Math.pow(center.getX() - mouse.getX(), 2) +
                Math.pow(center.getY() - mouse.getY(), 2));
        distance = distance == 0 ? 10 : distance;
        double coeff = size / (distance * 10);  // coeff
        Point point = new Point(center.getX() + coeff * (mouse.getX() - center.getX()),
                center.getY() + coeff * (mouse.getY() - center.getY()));

        MeteorFlow flow = new MeteorFlow(n,
                new Point(point.getX(), point.getY()),
                new Point(0, 0));               // поток на 10 метеоритов вокруг 0.5,0.5
        return flow;
    }

    private static void setFlow(MeteorFlow flow, Point mouse) {
        for (int i = 0; i < flow.flow.length; i++) {
            Meteor meteor = flow.flow[i];
            Point point = meteor.getPoint();
            double dx = mouse.getX() - point.getX();
            double dy = mouse.getY() - point.getY();
            double speedX = rnd.nextDouble() * dx / 20 + dx / 100;  // speed = 0.01..0.05 od distance
            double speedY = rnd.nextDouble() * dy / 20 + dy / 100;  // speed = 0.01..0.05 od distance
            meteor.setSpeed(new Point(speedX, speedY));
        }
    }


    public static void main(String[] args) {

        int n = 10;
        double size = 1000; // size of canvas  1000 point
        Meteor circle = new Meteor(new Point(size * 0.5, size * 0.5), new Point(0,0));

        MeteorFlow flow = genFlow(circle.getPoint(), circle.getPoint(), size, n);

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(-0.05 * size, 1.05 * size);
        StdDraw.setYscale(-0.05 * size, 1.05 * size);   // leave a border to write te
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.001);


        while (true) {
            if (StdDraw.mousePressed()) {
                Point mouse = new Point(StdDraw.mouseX(), StdDraw.mouseY());
                flow = genFlow(circle.getPoint(), mouse, size, n);
                setFlow(flow, mouse);

                double dx = mouse.getX() - circle.getPoint().getX();
                double dy = mouse.getY() - circle.getPoint().getY();
                circle.setSpeed( new Point(-dx/100,-dy/100));
            }


            StdDraw.setPenColor(Color.BLACK);
            StdDraw.circle(circle.getPoint().getX(),circle.getPoint().getY(),size/10);

            for (int j = 0; j < flow.size; j++) {
                Meteor meteor = flow.flow[j];
                Point point = meteor.getPoint();
                StdDraw.setPenColor(meteor.getColor());
                StdDraw.filledCircle(point.getX(), point.getY(), meteor.getRadius());
            }


            StdDraw.show();
            StdDraw.pause(100);
            StdDraw.clear();
            flow.update();    // update all meteors
            circle.update();  // update central circle
        }

    }
}
