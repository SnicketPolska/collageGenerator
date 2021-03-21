package collage.services;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileAgent extends DataAgent{

    @Override
    public BufferedImage[] getData(int size) {

        String path = "./src/main/resources/";
        BufferedImage[] images = new BufferedImage[size*size];
        File[] files = new File(path).listFiles();

        //Read files to image array
        if(files != null) {
            int length = Math.min(images.length, files.length); //Avoid NullPointerException
            for (int i = 0; i < length; i++) {
                try {
                    images[i] = ImageIO.read(files[i]);
                } catch (IOException e) {
                    System.out.println("File cannot be read");
                }
            }
        }else return null;

        //Scale images if possible
        for (BufferedImage i : images) {
            if (i != null) {
                Draw.scaleImages(images);
                return images;
            }
        }
        return null;
    }






}
