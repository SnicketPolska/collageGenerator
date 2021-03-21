package collage.services;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Draw {

    private static final Comparator<BufferedImage> compareHeight = Comparator.comparing(BufferedImage::getHeight,Comparator.nullsFirst(Comparator.naturalOrder()));
    private static final Comparator<BufferedImage> compareWidth = Comparator.comparing(BufferedImage::getWidth,Comparator.nullsFirst(Comparator.naturalOrder()));

    public static int getMinHeight(BufferedImage[] images){
        int height = 0;
        BufferedImage[] newArray = Arrays.stream(images).filter(Objects::nonNull).toArray(BufferedImage[]::new);
        if(Arrays.stream(newArray).max(compareHeight).isPresent()) {
            height = Arrays.stream(newArray).min(compareHeight).get().getHeight();
        }
        return height;
    }

    public static int getMaxHeight(BufferedImage[] images){
        int height = 0;
        BufferedImage[] newArray = Arrays.stream(images).filter(Objects::nonNull).toArray(BufferedImage[]::new);
        if(Arrays.stream(newArray).max(compareHeight).isPresent()) {
            height = Arrays.stream(newArray).max(compareHeight).get().getHeight();
        }
        return height;
    }

    public static int getMaxWidth(BufferedImage[] images){
        int width = 0;
        BufferedImage[] newArray = Arrays.stream(images).filter(Objects::nonNull).toArray(BufferedImage[]::new);
        if(Arrays.stream(newArray).max(compareWidth).isPresent()) {
            width = Arrays.stream(newArray).max(compareWidth).get().getWidth();
        }
        return width;
    }

    public static void scaleImages(BufferedImage[] images){
        int h = getMinHeight(images);
        for(int i = 0; i < images.length; i++){
            if(images[i] != null) {
                Image resultingImage = images[i].getScaledInstance(images[i].getWidth() * h / images[i].getHeight(), h, Image.SCALE_DEFAULT);
                BufferedImage outputImage = new BufferedImage(images[i].getWidth() * h / images[i].getHeight(), h, BufferedImage.TYPE_INT_ARGB);
                outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
                images[i] = outputImage;
            }
        }
    }

    public static BufferedImage getBlackRect(BufferedImage[] images){
        int h = getMaxHeight(images);
        int w = getMaxWidth(images);
        BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,w,h);
        g.dispose();

        return image;
    }

    //Adds album and artist info to image
    public static BufferedImage drawInfo(BufferedImage image, String artist, String album){
        Graphics g = image.getGraphics();
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        if(artist != null)
            g.drawString(artist, 20, 20);
        if(album != null)
            g.drawString(album, 20, 40);
        g.dispose();
        return image;
    }
    
    public static BufferedImage combine(BufferedImage[] images, int size){
        int w = Draw.getMaxWidth(images);
        int h = Draw.getMaxHeight(images);

        BufferedImage combined = new BufferedImage(w * size, h * size, BufferedImage.TYPE_INT_ARGB);

        w = 0;
        Graphics g = combined.getGraphics();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (images[i * size + j] != null) {
                    g.drawImage(images[i * size + j], w, i * h, null);
                    w += images[i * size + j].getWidth();
                }
            }
            w = 0;
        }
        g.dispose();

        return combined;
    }

}
