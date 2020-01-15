import java.awt.*;                  //copy/paste the code into a personal IDE in order to run
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.*;


class TitleScreen extends JFrame implements ActionListener {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    BufferedImage img = null;
    Splash titleSplash;
    String text;        //possible text to be displayed- if no text is to be drawn, text will be an empty string
    
    JButton start = new JButton("Start");
    JButton tutorial = new JButton("How To Play");
    JButton credits = new JButton("Credits");
    
    Box layout = new Box(BoxLayout.Y_AXIS);
    Box buttons = new Box(BoxLayout.X_AXIS);

    public TitleScreen(String title)  {
        super(title);
        setLayout(new FlowLayout());
        setResizable(false);
        
        text = "";              //sets paintComponent to draw an Image
        titleSplash = new Splash(500, 400);         //Size of the title Splash- also the size of the image
        
        try {
            img = ImageIO.read(new File("titlesplash.png"));       //read the image
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        start.addActionListener(this);          //add ActionListeners for all the buttons
        tutorial.addActionListener(this);
        credits.addActionListener(this);
        
        start.setActionCommand("start");        //define ActionCommands for all the buttons
        tutorial.setActionCommand("tutorial");
        credits.setActionCommand("credits");
        
        buttons.add(start);             //add the buttons to the JPanel
        buttons.add(Box.createRigidArea(new Dimension(10, 1)));     //nice spacing between the buttons
        buttons.add(tutorial);
        buttons.add(Box.createRigidArea(new Dimension(10, 1)));
        buttons.add(credits);
        
        layout.add(titleSplash);        //add the title splash and buttons to layout
        layout.add(Box.createRigidArea(new Dimension(1, 10)));
        layout.add(buttons);
        add(layout);            //add the layout to the JFrame
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           //set the JFrame to close when the X is pressed
    }
  
    public void actionPerformed(ActionEvent evt){
        switch(evt.getActionCommand()) {
            case "start":
                AntFarm window = new AntFarm ();
                window.setVisible (true);
                break;
            case "tutorial":    
                tutorial();
                break;
            case "credits":
                credits();
                break;
        }
    }
    
    

    public void tutorial() {
        Frame frame2 = new Frame("How To Play");
        frame2.setBounds((int)(0.5 * screenSize.getWidth())/2, (int) (0.5 * screenSize.getHeight())/2, 900, 500);
        Splash tutorialSplash = new Splash(900, 500);         
        frame2.add(tutorialSplash);
        JLabel label = new JLabel("<html>Welcome to Super Ant Farmulation!<br>To begin, press Start. Click Simulate to start simulation."
        		+ "<br>Choose to import a file using the first dropdown menu <br>on the left ( - to randomly generate). "
        		+ "Press Create to load the file. <br>Choose your actions from the second dropdown menu on the left. <br>"
        		+ "From there, you can choose what the spawn using the last "
        		+ "dropdown menu. <br>Keep in mind that anteater tongues only spawn in a tunnel close to the surface, <br>and moles only when deep enough.<br>Have fun!<html>", JLabel.CENTER);
        label.setFont(new Font("Comic Sans MS", Font.ITALIC, 22));
        frame2.setBackground(Color.pink);
        label.setForeground(new Color(200,60,20));
        tutorialSplash.add(label, BorderLayout.CENTER);
        text = " ";                //text to be displayed

       frame2.repaint();
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);           //set THIS JFrame to close when the X is pressed- not tne entire program
        frame2.pack();
        frame2.setVisible(true);
    }
    
    public void credits() {
        Frame frame3 = new Frame("Credits");
        frame3.setBounds((int)(0.5 * screenSize.getWidth())/2 , (int) (0.5 * screenSize.getHeight())/2, 600, 480);
        Splash creditsSplash = new Splash(650, 400);        
        frame3.add(creditsSplash);
        JLabel label2 = new JLabel("<html>This was created by YuMaQiu for their <br>ICS3U1 summative project.<br>"
        		+ "Special thanks to Mr. Jay for his guidance<br>throughout the course. =)<html>", JLabel.CENTER);
        label2.setFont(new Font("Comic Sans MS", Font.ITALIC, 22));
        frame3.setBackground(new Color(50,100,70));
        label2.setForeground(new Color(70,200,90));
        creditsSplash.add(label2, BorderLayout.CENTER);
        text = " ";                //text to be displayed
        frame3.repaint();
        frame3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);           //set THIS JFrame to close when the X is pressed- not tne entire program
        frame3.pack();
        frame3.setVisible(true);
    }
    
    class Splash extends JPanel {
        public Splash (int width, int height) {
            this.setPreferredSize (new Dimension (width, height));
        }
        
        public void paintComponent(Graphics g) {
            if(text.equals("")) {     
                g.drawImage(img, 1, 1, this);
            }
            else {          
                g.setColor(Color.black);
                Font font = Font.getFont("Arial");
                g.setFont(font);
                g.drawString(text, 200, 200);       //draws whatever is in text
            }
        }
    }
}

class Frame extends JFrame {
    public Frame (String title) {
        super(title);
        setResizable(false);
    }
}
