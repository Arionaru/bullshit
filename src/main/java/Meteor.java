import java.awt.*;
import java.util.Random;

public class Meteor {
    private static double MAX_RADIUS = 10;                 // 250 метров radius from center
    private static double BASE_SIZE = 250;                 // 250 метров radius from center
    private static double MIN_RADIUS = BASE_SIZE / 100;    // 0.005 radius from center
    private static double MAX_SIZE = BASE_SIZE / 25;      // used as mass
    private static double MIN_SIZE = BASE_SIZE / 100;     // used as mass
    private static double MAX_SPEED = BASE_SIZE / 100;
    private static double MIN_SPEED = BASE_SIZE / 1000;
    private static int MAX_INERTION = 25;  // скорость падает на 100 тиков
    private static Color[] colors = new Color[]{
            Color.BLACK, Color.GREEN, Color.GRAY, Color.BLUE, Color.CYAN, Color.MAGENTA,
            Color.ORANGE, Color.RED, Color.YELLOW, Color.PINK
    };

    private static Random rnd = new Random();

    private int inertion;  // speed inertion counter
    private Point speed;
    private Point delta;
    private double mass;    // radius used as mass
    private Point point;
    private double radius;  // radius of object
    private Color color;
    private boolean isCollision;


    public Meteor(Point point) {   // center point around
        isCollision = false;
        radius = getRandom(MIN_SIZE, MAX_SIZE);  // radius of object
        mass = radius;
        this.point = new Point(getRandom(MIN_RADIUS, MAX_RADIUS, rnd.nextBoolean()) + point.getX(),
                getRandom(MIN_RADIUS, MAX_RADIUS, rnd.nextBoolean()) + point.getY());
        speed = new Point(getRandom(MIN_SPEED, MAX_SPEED, rnd.nextBoolean()),
                getRandom(MIN_SPEED, MAX_SPEED, rnd.nextBoolean()));
        delta = new Point(speed.getX() / MAX_INERTION, speed.getY() / MAX_INERTION);
        color = colors[rnd.nextInt(colors.length)];
        inertion = MAX_INERTION;
    }

    public Color getColor() {
        return color;
    }

    public Meteor(Point point, Point speed) {   // center point around
        isCollision = false;
        radius = getRandom(MIN_SIZE, MAX_SIZE);  // radius of object
        mass = radius;
        this.point = new Point(getRandom(MIN_RADIUS, MAX_RADIUS, rnd.nextBoolean()) + point.getX(),
                getRandom(MIN_RADIUS, MAX_RADIUS, rnd.nextBoolean()) + point.getY());

        setSpeed(speed);
        color = colors[rnd.nextInt(colors.length)];

    }

    private double getRandom(double min, double max, boolean dir) {
        int sign = dir ? 1 : -1;
        return (sign) * (rnd.nextDouble() * (max - min) + min);  // range
    }

    private double getRandom(double min, double max) {
        return rnd.nextDouble() * (max - min) + min;  // range
    }


    /**
     * Проверяет есть ли наложение по трем измерениям
     * данного метеорита с other
     *
     * @param other другой метеорит
     * @return true если по трем измерениям есть наложение, значит есть столкновение
     */
    public boolean collisionCheck(Meteor other) {

        if (Math.abs(point.getX() - other.point.getX()) < radius + other.radius &&
                Math.abs(point.getY() - other.point.getY()) < radius + other.radius) {
            return true;            // collision detected по всем трем измерениям идет наложение
        }
        return false;
    }

    private int counter = 0;

    private void updateSpeed(Meteor other) {         // if collision detected
        // не реализовано
    }

    public void update() {
        point.setX(point.getX() + speed.getX());
        point.setY(point.getY() + speed.getY());

        if (inertion > 0) {
            inertion--;
            if (Math.abs(speed.getX()) > Math.abs(delta.getX())) {
                speed.setX(speed.getX() - delta.getX());
            } else {
                speed.setX(0);
            }
            if (Math.abs(speed.getY()) > Math.abs(delta.getY())) {
                speed.setY(speed.getY() - delta.getY());
            } else {
                speed.setY(0);
            }
        }
    }


    /**
     * Проверяет есть ли столкновение метеорита с other
     * если есть столкновение изменить скорости метеоритов
     * для изменения скоростей берется в расчет масса метеорита и скорость
     *
     * @param other другой метеорит
     */
    public boolean collision(Meteor other) {
        if (collisionCheck(other)) {  // столкновения есть
            updateSpeed(other);       // обновить скорости
            isCollision = true;
        }
        return isCollision;
    }

    public Point getSpeed() {
        return speed;
    }

    public Point getPoint() {
        return point;
    }

    public double getRadius() {
        return radius;
    }

    public void setSpeed(Point speed) {
        this.speed = speed;
        delta = new Point(speed.getX() / MAX_INERTION, speed.getY() / MAX_INERTION);
        inertion = MAX_INERTION;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[ pos: %7.3f,%7.3f ", point.getX(), point.getY()));
        sb.append(String.format("  v:%7.3f,%7.3f ", speed.getX(), speed.getY()));
        sb.append(String.format(" r:%7.4f m:%7.4f ]", radius, mass));
        if (isCollision) {
            sb.append("*");
            isCollision = false;
        }

        return sb.toString();
    }
}
