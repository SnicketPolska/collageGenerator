package collage;

import collage.services.DataAgent;
import collage.services.Draw;
import collage.services.FileAgent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DataHandler implements ActionListener {

    //JComponents allow access to current data in action handler
    private final JSpinner sizeField;
    private final JTextField fileNameField;
    private final DataAgent dataAgent; // Strategy

    public DataHandler(JTextField fileNameField, JSpinner sizeField, DataAgent dataAgent){
        this.fileNameField = fileNameField;
        this.sizeField = sizeField;
        this.dataAgent = dataAgent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //retrieve current JComponent values
        String fileName = fileNameField.getText();
        int size = Integer.parseInt(sizeField.getValue().toString());

        BufferedImage[] images = dataAgent.getData(size); //retrieve data depending on strategy
        if(images == null) {
            String error = dataAgent instanceof FileAgent ? //handle error depending on strategy
                            "An error occured while retrieving data, check if there are any images in the proper directory":
                            "An error occured while retrieving data, check your internet connection";
            JOptionPane.showMessageDialog(null, error);
        }

        else{
            BufferedImage combined = Draw.combine(images,size); //combine retrieved images array into one

            // Save as new image
            File file = new File("./src/main/results/" + fileName + ".png");
            try {
                ImageIO.write(combined, "png", file);
                JOptionPane.showMessageDialog(null, "Image saved in " + file.getAbsolutePath());
                //Open image in preferred viewer
                Desktop dt = Desktop.getDesktop();
                dt.open(file);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while saving the image, check if the filename contains forbidden characters");
            }
        }
    }
}
