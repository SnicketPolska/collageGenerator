package collage;

import collage.services.APIAgent;
import collage.services.DataAgent;
import collage.services.FileAgent;

import javax.swing.*;


public class GUI {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Collage Generator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Generate panel for each retrieval method with descriptive caption
        JPanel panel1 = generatePanel(new APIAgent(),"Files read from: Last.fm API");
        JPanel panel2 = generatePanel(new FileAgent(),"Files read from: \r\n" + System.getProperty("user.dir") + "\\src\\main\\resources\\");

        JTabbedPane mainPanel = new JTabbedPane();
        mainPanel.add("Last.fm",panel1);
        mainPanel.add("File directory", panel2);

        frame.add(mainPanel);
        frame.setSize(400,200);
        frame.setVisible(true);
    }

    private static JPanel generatePanel(DataAgent dataAgent,String captionText){

        JLabel sizeLabel = new JLabel("Choose size:");
        JSpinner size = new JSpinner(new SpinnerNumberModel(5,1,10,1));

        JLabel fileNameLabel = new JLabel("Enter output file name:");
        JTextField fileName = new JTextField(16);

        JButton generate = new JButton("Generate");
        generate.addActionListener(new DataHandler(fileName,size,dataAgent));

        JTextArea caption = new JTextArea(captionText);
        caption.setEditable(false);

        JPanel panel = new JPanel();

        panel.add(sizeLabel);
        panel.add(size);
        panel.add(fileNameLabel);
        panel.add(fileName);
        panel.add(generate);
        panel.add(caption);

        return panel;
    }

}
