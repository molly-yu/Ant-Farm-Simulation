import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.lang.Object;
import javax.swing.event.*;  // Needed for ActionListener
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.sound.sampled.AudioInputStream; //imports for music (javax)
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
class AntFarm extends JFrame implements ActionListener, ChangeListener, MouseListener
{
    static Colony colony = new Colony();
    static JSlider speedSldr = new JSlider ();
    static Timer t;
    private static JComboBox actionBox, creatureBox; //static combo box declaration (their values needs to be accessed later)
    private static JTextField fileName = new JTextField(); //static text field declaration, input needs to be accessed later
    int y, y2, x, x2;
    public static Clip clip, clip2; //music clip variables
    static boolean click = true, clicked2 = false; //needs to be used inmultiple methods
    private static JButton muteBtn = new JButton ("Mute"); //needs to be used in multiple methods
    
    //======================================================== constructor
    public AntFarm ()
    {
        // 1... Create/initialize components
        fileName.setPreferredSize(new Dimension(200,24)); //expands size of text field
        JButton simulateBtn = new JButton ("Simulate");
        simulateBtn.addActionListener (this);
        JButton saveBtn = new JButton ("Save");
        saveBtn.addActionListener(this);
        JButton loadBtn = new JButton ("Load");
        loadBtn.addActionListener(this);
        muteBtn.addActionListener(this);
        
        speedSldr.addChangeListener (this);
        
        String[] actions = new String[6]; //array of strings
        actions[0] = ("Populate"); //values for each index
        actions[1] = ("Eradicate");
        actions[2] = ("Spawn");
        actions[3] = ("Dig");
        actions[4] = ("Fill");
        actions[5] = ("Virus");
        actionBox = new JComboBox(actions); //adds array of strings to combo box
        
        String[] creatures = new String[3]; //array of strings
        creatures[0] = ("Anteater"); //values for each index
        creatures[1] = ("Antlion");
        creatures[2] = ("Mole");
        creatureBox = new JComboBox(creatures); //adds array of strings to combo box
        
        // 2... Create content pane, set layout
        JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
        JPanel north = new JPanel ();
        north.setLayout (new FlowLayout ()); // Use FlowLayout for input area
        
        DrawArea board = new DrawArea (600, 900);
        board.addMouseListener(this);
        
        // 3... Add the components to the input area.
        
        north.add (simulateBtn); //adds buttons to GUI
        north.add (speedSldr);
        north.add (actionBox);
        north.add (creatureBox);
        north.add (loadBtn);
        north.add(saveBtn);
        north.add (fileName);
        north.add(muteBtn);
        
        content.add (north, "North"); // Input area
        content.add (board,BorderLayout.CENTER ); // Output area
        
        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setTitle ("Life Simulation Demo");
        setSize (1000, 800);
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.
    }
    
    public void stateChanged (ChangeEvent e)
    {
        if (t != null)
            t.setDelay (400 - 4 * speedSldr.getValue ()); // 0 to 400 ms
    }
    
    public void music() //music method
    {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./Harmony.wav").getAbsoluteFile()); //music file locations will need to be altered as necessary
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(new File("./Adorno VI.wav").getAbsoluteFile());
            clip2 = AudioSystem.getClip();
            clip2.open(audioInputStream2);
        } catch(Exception ex) {}
    } //gets music clips (must be WAV file) and puts them in respective variables
    
    public static void moleLeave()
    {
        if (colony.isThereMole() == false) //if the isThereMole method returns false
        {
            clip2.stop(); //stops boss theme
            if (click) //if music is unmuted
                clip.start(); //resume relax soundtrack
        }
    }
    
    public void actionPerformed (ActionEvent e)
    {
        if (e.getActionCommand ().equals ("Simulate"))
        {
            Movement moveColony = new Movement (colony); // ActionListener
            t = new Timer (200, moveColony); // set up timer
            t.start (); // start simulation
            if (clicked2 == false)
            {
                music();
                clip.start();
                clicked2 = true;
            }
        }
        if (e.getActionCommand ().equals  ("Save"))
        {
            String name = fileName.getText(); //gets what user inputted into text field
            fileName.setText(""); //clears text field
            try {
                colony.save(name); //calls save method and inputs user inputted name
            }catch (IOException e1){} //error checking
        }
        if (e.getActionCommand ().equals  ("Load"))
        {
            JFileChooser fc = new JFileChooser(); //new file chooser
            fc.showOpenDialog(AntFarm.this); //opens file choosing dialogue box
            File file = fc.getSelectedFile(); //creates file object for the selected file
            try {
                colony.load(file); //calls load method and inputs user selected file
            }catch (IOException e1){} //error checking
        }
        
        if (e.getActionCommand ().equals  ("Mute") || e.getActionCommand ().equals  ("Unmute"))
        {
        	try {
            if (click) //click starts off true
            {
                
                muteBtn.setText("Unmute"); //button will now read "Unmute"
                clip.stop(); //stops music
                clip2.stop();
                click = false; //changed to false
            }
            else //if click is false meaning search was already clicked
            {
                click = true; //click is now true
                muteBtn.setText ("Mute"); //reverts button text to read Mute
                if (colony.isThereMole()) //calls isThereMole method to determine which track to play
                    clip2.start(); //if it returns true that means there is a mole still on the farm and the boss theme will play
                else
                    clip.start(); //otherwise (meaning there is no mole) the original soundtrack will play
            }
        	}catch(Exception f) {}
        }
        repaint ();
    }
    public void mousePressed(MouseEvent e)
    {
        x = e.getX()/6; //divides clicked coordinate by 6 to account for resizing
        y = e.getY()/6;
        JComboBox cb = actionBox, cb1 = creatureBox;
        int action = cb.getSelectedIndex(); //gets selected values from combo box
        int creature;
        if (action == 0){}
        //colony.populate();
        else if (action == 1){}
        //colony.eradicate();
        else if (action == 2) //runs if action chosen is "Spawn"
        {
            creature = cb1.getSelectedIndex(); //gets selected value from second combo box
            if(creature == 0){} //determines which creature will be spawned
            //colony.antEater();
            else if (creature == 1)
            {
                try{
                    colony.AntLion(y, x);
                }catch(Exception p){}
            }
            else if (creature == 2)
            {
                if (30 < y && y < 95) //will only run if click wasn't top third~
                {
                    colony.mole(y, x);
                    clip.stop(); //stops relaxed soundtracked
                    if (click) //if music is unmuted
                        clip2.start(); //begins boss theme
                }
            }
        }
        else if (action == 3){}
        //colony.dig;
        else if (action == 4){}
        //colony.fill
    }
    public void mouseReleased(MouseEvent e)
    {
        y2 = e.getY()/6;
        x2 = e.getX()/6;
        
        JComboBox cb = actionBox;
        int action = cb.getSelectedIndex(); //gets selected values from combo boxe
        
        if (action == 0) {
            try{
                colony.populate (y, y2, x, x2);
            }catch (Exception f) {}
        }
        else if (action == 1) {
            try{
                colony.eradicate (y, y2, x, x2);
            }catch (Exception f) {}
        }
        else if(action == 3) {
            try{
                colony.dig(y, y2,x,x2);
            }catch(Exception f) {}
        }
        else if(action == 4) {
            try{
                colony.fill(y, y2,x,x2);
            }catch(Exception f) {}
        }
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e)
    {
        int yy = (e.getY())/6;
        int xx = (e.getX())/6;
        JComboBox cb = actionBox, cb1 = creatureBox;
        int action = cb.getSelectedIndex(); //gets selected values from combo boxes
        int creature = cb1.getSelectedIndex();
        if (action == 0) {
            try{
                colony.populate (yy, yy, xx, xx);
            }catch (Exception f) {}
        }
        else if (action == 1) {
            try{
                colony.eradicate (yy, yy, xx, xx);
            }catch (Exception f) {}
        }
        else if(action == 2) {
            if(creature == 0){
                try{
                    colony.anteater(xx,yy);
                }catch(Exception f) {}
            }
        }
        else if (action == 3){
            try {
                colony.dig(yy,yy,xx,xx);
            }catch(Exception f) {}
        }
        else if (action == 4){
            try {
                colony.fill(yy,yy,xx,xx);
            }catch(Exception f) {}
        }
        else if (action == 5){
            try {
                colony.virus(yy, xx);
            }catch(Exception f) {}
        }
        repaint();
    }
    
    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }
        
        public void paintComponent (Graphics g)
        {
            colony.show (g);
        }
    }
    
    class Movement implements ActionListener
    {
        private Colony colony;
        public Movement (Colony col)
        {
            colony = col;
        }
        
        public void actionPerformed (ActionEvent event)
        {
            colony.advance ();
            repaint ();
        }
    }
}

//==================================================================Ant Class======================================
class Ants //contains characteristics of each element of the colony grid
{
    private int row, col;
    private char box;
    static char [][] array = fileRead();
    private int viruslife;
    
    public Ants (int x, int y)
    {
        row = x;
        col = y;
        box = array[x][y];
        viruslife = 0;
    }
    
    public static char[][] fileRead ()
    {
        char[][] textRead = new char [100][150]; //read char for each grid from a file to create tunnels
        File text = new File ("./template150x100.txt");
        Scanner scnr = new Scanner (System.in);
        try
        {
            scnr = new Scanner (text);
        }catch (FileNotFoundException ex){}
        
        for (int row = 0; row < textRead.length; row++)
        {
            String line = scnr.nextLine();
            for (int col = 0; col < textRead[row].length; col++)
                textRead[row][col] = line.charAt (col);
        }
        return textRead;
    }
    public int AntRow () //return row
    {
        return row;
    }
    
    public int AntCol () //return col
    {
        return col;
    }
    
    public int VirusLife () //for virus method; return virusLife
    {
        return viruslife;
    }
    
    public void VirusAge () //for virus method; return virusAge
    {
        viruslife++;
    }
    
    public char CharAt () //return box (char of the grid)
    {
        return box;
    }
    
    public void setChar (char set) //recieve a char, set box to that char
    {
        box = set;
    }
    public boolean sameAnt (int x, int y) //if row and col entered is same as row and col, return true
    {
        if (row == x && col == y)
            return true;
        else return false;
    }

}
// ========================================================================Colony==================================
class Colony
{
    private Ants grid[][];
    private char grid2[][];
    private FileReader reader;
    private boolean load;
    public Colony () //create grid of Ants
    {
        grid = new Ants[100][150];
        grid2 = new char[100][150];
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
                grid[row][col] = new Ants(row, col);
        
        char[][] textRead = new char [100][150]; //gets text file
        File text = new File ("./template150x100.txt");
        Scanner scnr = new Scanner (System.in);
        try
        {
            scnr = new Scanner (text);
        }catch (FileNotFoundException ex){}
        
        for (int row = 0; row < grid2.length; row++)
        {
            String line = scnr.nextLine();
            for (int col = 0; col < grid2[row].length; col++)
                grid2[row][col] = line.charAt (col); //creates duplicate grid of characters with no ants (updated separate to main grid)
        }
        
        for (int row = 0; row < grid.length; row++) //if tunnel and not dirt, make either black or red ant once every 10 boxes
        {
            for (int col = 0; col < grid[row].length; col++)
            {
                if (grid[row][col].CharAt() == '1')
                {
                    if (Math.random() < 0.1)
                    {
                        if (Math.random() >= 0.5)
                            grid[row][col].setChar ('2');
                        else
                            grid[row][col].setChar ('3');
                    }
                }
            }
        }
    }
    
    public Ants moveNext (int row, int col, char checkFor) //returns a grid around row and col entered that has the char checkFor
    {
        ArrayList<Ants> moveTo = new ArrayList<Ants> (); //add all Ants around with checkFor char to an ArrayList
        if (row > 0)
        {
            if (grid[row-1][col].CharAt() == checkFor)
                moveTo.add (grid[row-1][col]); //if not the top row, check square directly above it
            if (col > 0)
                if (grid[row-1][col-1].CharAt() == checkFor)
                    moveTo.add (grid[row-1][col-1]); //if not left upper corner, check square directly above and to the left
            if (col < grid[row].length-1)
                if (grid[row-1][col+1].CharAt() == checkFor)
                    moveTo.add (grid[row-1][col+1]); //if not right upper corner, check square directly above and to the right
        }
        if (row < grid.length-1)
        {
            if (grid[row+1][col].CharAt() == checkFor)
                moveTo.add (grid[row+1][col]);
            if (col > 0)
                if (grid[row+1][col-1].CharAt() == checkFor)
                    moveTo.add (grid[row+1][col-1]); //if not left lower corner, check square directly below and to the left
            if (col < grid[row].length-1)
                if (grid[row+1][col+1].CharAt() == checkFor)
                    moveTo.add (grid[row+1][col+1]); //if not right lower corner, check square directly below and to the right
        }
        if (col > 0)
            if (grid[row][col-1].CharAt() == checkFor)
                moveTo.add (grid[row][col-1]);
        if (col < grid[row].length-1)
            if (grid[row][col+1].CharAt() == checkFor)
                moveTo.add (grid[row][col+1]);
        if (moveTo.size() != 0)
        {
            int move = (int)(Math.random()*moveTo.size()); //randomly generate an Ant from the moveTo array
            while (move >= moveTo.size())
                move = (int)(Math.random()*moveTo.size());
            Ants emptySquare = moveTo.get(move);
            return emptySquare;
        }
        else return grid[row][col]; //if no square around it found with checkFor char, return original row and col
    }
    
    public void live (int row, int col) //find a tunnel around square using moveNext and move there
    {
        Ants move = moveNext (row, col, '1');
        move.setChar (grid[row][col].CharAt()); //set the tunnel square found to the ant
        grid[row][col].setChar ('1'); //set previous square to tunnel
    }
    
    public void autoeradicate (int row, int col, char ant) //if red ant meets black ant, one of them dies
    {
        Ants evil = moveNext (row, col, ant); //search for box with the opposite colored ant
        if (!evil.sameAnt (row, col)) //if one found
        {
            grid[row][col].setChar ('1'); //set that box to tunnel
            evil.setChar ('1');
        }
    }
    
    public void autopopulate (int row, int col, char ant) //if same ants meet, new ant is produced
    {
        Ants mate = moveNext (row, col, ant); //search for box with same ant
        if (!mate.sameAnt (row, col)) // if one found
            if (Math.random() > 0.99) //every 100th time this occurs
            {
                live (row, col);
                live (mate.AntRow(), mate.AntCol());
                grid[row][col].setChar(ant); //move that ant and leave new ant in its place
            }
    }
    
    public void populate (int row1, int row2, int col1, int col2) //recieves mousePressed and mouseReleased coordinates
    {
        int temp;
        if (row2 < row1) //if clicked right to left
        {
            temp = row1;
            row1 = row2;
            row2 = temp;
        }
        if (col2 < col1) //ic clicked down to up
        {
            temp = col1;
            col1 = col2;
            col2 = temp;
        }
        for (int x = row1; x < row2; x++) {
            for (int y = col1; y < col2; y++) { //within area selected by mouse
                if (grid[x][y].CharAt() == '1') { //if tunnel
                    if (Math.random() > 0.85) { //15% of the time
                        if (Math.random() > 0.5) //set either red or black ant
                            grid[x][y].setChar('2');
                        else
                            grid[x][y].setChar('3');
                    }
                }
            }
        }
    }
    
    public void eradicate (int row1, int row2, int col1, int col2) //same as populate, but sets all ants in selected area to tunnel
    {
        int temp;
        if (row2 < row1)
        {
            temp = row1;
            row1 = row2;
            row2 = temp;
        }
        if (col2 < col1)
        {
            temp = col1;
            col1 = col2;
            col2 = temp;
        }
        for (int x = row1; x < row2; x++)
            for (int y = col1; y < col2; y++)
                if (grid[x][y].CharAt() == '2' || grid[x][y].CharAt() == '3'||grid[x][y].CharAt() == '9')
                    grid[x][y].setChar('1');
    }
    
    //********************************************************************Advance
    public void advance ()
    {
        for (int row = 0 ; row < grid.length ; row++)
        {
            for (int col = 0 ; col < grid [row].length ; col++)
            {
                if (grid[row][col].CharAt() == '2') //if black ant
                {
                    live (row, col);
                    autoeradicate (row, col, '3');
                    autopopulate (row, col, '2');
                }
                else if (grid[row][col].CharAt() == '3') // if red ant
                {
                    live (row, col);
                    autoeradicate (row, col, '2');
                    autopopulate (row, col, '3');
                }
                else if(grid[row][col].CharAt()=='4') //if anteater tongue
                {
                    if(grid[row+1][col].CharAt()<='4')
                        grid[row][col].setChar('1');
                }
                else if (grid[row][col].CharAt() == '5') //if the index has character 5 (left antlion)
                {
                    int temp = col; //temporary variable declaration
                    if (col < 148 && grid[row][col+1].CharAt() != '5'&&grid[row][col+1].CharAt() != '8') //if the next index is not antlion or mole
                        col++; //increments col because since it checks the array from left to right, the antlion will move instantly because it will have moved into next index being checked
                    critterLive(row, temp); //calls critterLive with initial index
                }
                else if (grid[row][col].CharAt() == '7') //if the index has character 7 (left mole)
                {
                    int temp = col; //same logic as left antlion
                    if (col < 148 && grid[row][col+1].CharAt() != '7') //but only cares if next index is mole
                        col++;
                    critterLive(row, temp);
                }
                else if (grid[row][col].CharAt() == '6') //if the index has character 6 (right antlion)
                    critterLive(row, col);
                else if (grid[row][col].CharAt() == '8') //if the index has character 8 (mole)
                    critterLive(row, col);
                else if (grid[row][col].CharAt() == '9') // if virus
                {
                    if (grid[row][col].VirusLife() > 3) //if age of virus more than 3, die
                        grid[row][col].setChar('1');
                    else
                    {
                        live (row, col);
                        grid[row][col].VirusAge();
                        viruslive (row, col); //live, possibly spread virus, and age
                    }
                }
            }
        }
    }
    
    //********************************************************************Show
    public void show (Graphics g)
    {
        for (int row = 0 ; row < grid.length; row++)
        {
            for (int col = 0 ; col < grid [row].length ; col++)
            {
                // set different colours for different values in grid array
                if (grid[row][col].CharAt() == '0') //dirt
                { 
                    if((row+col)%2==0)
                        g.setColor (new Color (95, 65, 50));
                    else
                        g.setColor (new Color (90, 60, 45));
                }
                else if (grid[row][col].CharAt() == '1') //tunnel
                    g.setColor (new Color (127, 102, 76));
                else if (grid[row][col].CharAt() == '2') //black ant
                    g.setColor (new Color (127, 102, 76));
                else if (grid[row][col].CharAt() == '3') //red ant
                    g.setColor (new Color (127, 102, 76)); //anteater
                else if (grid[row][col].CharAt() == '4')
                    g.setColor (new Color (235, 150, 150));
                else if (grid[row][col].CharAt() == '5') //left antlion
                    g.setColor (Color.orange);
                else if (grid[row][col].CharAt() == '6') //right antlion
                    g.setColor (Color.yellow);
                else if (grid[row][col].CharAt() == '7') //left mole
                    g.setColor (new Color (50, 35, 0));
                else if (grid[row][col].CharAt() == '8') //right mole
                    g.setColor (new Color (50, 35, 0));
                else if(grid[row][col].CharAt()=='9') //virus
                    g.setColor(new Color(210,50,210));
                g.fillRect (col * 6 + 3, row * 6 + 3, 6, 6); // draw life form
                //ants are drawn as ovals
                if (grid[row][col].CharAt() == '3') 
                {
                    g.setColor (new Color (255, 0, 0));
                    g.fillOval (col * 6 + 3, row * 6+ 3, 4, 4);
                }
                if (grid[row][col].CharAt() == '2') 
                {
                    g.setColor (new Color (0, 0, 0));
                    g.fillRoundRect (col * 6 + 3, row * 6 + 3, 5, 5,3,3);
                }
            }
        }
    }
    
    //**********************************************************Get Out
    public void getOut() 
    {
        // ants have a 8% chance of leaving through the top
        for(int i = 0; i < grid[0].length; i++) {
            if((grid[0][i].CharAt()=='2'||grid[0][i].CharAt()=='3')&&Math.random()<0.08)
                grid[0][i].setChar('1');
        }
    }
    
    //***************************************************************Anteater
    public void anteater(int x, int y) 
    {
        if(y<26&&grid[y][x].CharAt()=='1') // check if coordinates in top third of simulation and if grid is tunnel or ant
        {    // initialize variables to be changed later on
            int xval = 0;
            int xleft = 0;
            int xright = 0;
            int x2right = 0;
            int x2left = 0;
            int xbound = 0;
            int xrbound = 0;
            for(int i = 0; i < grid[0].length; i++)      //find entrance to tunnel closest to clicking point
            {
                if(grid[0][i].CharAt()>='1'&&grid[0][i].CharAt()<='3')
                {
                    if(Math.abs(i-x)<Math.abs(xval-x))
                        xval = i;
                }
            }
            // find end points of tunnel
            xleft = leftendpt(0,xval);
            xright = rightendpt(0,xval);
            if(xleft ==xval)
                xleft = leftendpt(0,xright);
            if(xright ==xval)
                xright = rightendpt(0,xleft);
            x2left = leftendpt(y,x);
            x2right = rightendpt(y,x);
            if(x2left ==x)
                x2left = leftendpt(y,x2right);
            if(x2right ==x)
                x2right = rightendpt(y,x2left);
            
            // find largest interval to bound
            if(xleft<x2left)
                xbound = xleft;
            else
                xbound = x2left;
            if(xright>x2right)
                xrbound = xright;
            else
                xrbound = x2right;
            // call the anteater tongue
            tongue(xbound, xrbound, y);
        }
    }
    
    public int rightendpt(int b,int a)
    {// checks for right end point of tunnel
        int xright = a;
        while(grid[b][a].CharAt()>='1'&&grid[b][a].CharAt()<='3'&&a<grid[0].length-1)
        {
            if(grid[b][a+1].CharAt()=='0')
                xright = a;
            a++;
        }
        return xright;
    }
    
    public int leftendpt(int b,int a)
    {// checks for left end point of tunnel
        int xleft =a;
        while(grid[b][a].CharAt()>='1'&&grid[b][a].CharAt()<='3'&&a>1)
        {
            if(grid[b][a-1].CharAt()=='0')
                xleft = a;
            a--;
        }
        return xleft;
    }
    
    public void tongue(int x1, int x2, int y)
    {
        //spawn anteater tongue if area is tunnel or ant
        for(int i = 0; i < y; i++) 
        {
            for(int j = x1-6; j < x2+6; j++) 
            {
                if(grid[i][j].CharAt()>='1'&&grid[i][j].CharAt()<='3')
                    grid[i][j].setChar('4');
            }
        }
    }
//*******************************************************************Dig
    public void dig(int x1, int x2, int y1, int y2)
    {
        // reverse x and y values in case user clicks in different locations
        if(x1>x2)
        {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if(y1>y2)
        {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        if(x1>40&&x2>40&&x1<99&&x2<99){  // set area dug to tunnel if under top half of simulation, not bottom row
            for(int i = x1; i <= x2; i++)
            {
                for(int j =y1; j <= y2;j++)
                {
                    if(grid[i][j].CharAt()=='0')
                    {
                        grid[i][j].setChar('1');
                        grid2[i][j]='1';
                    }
                }
            }
        }
    }
    
    //*******************************************************************Fill
    public void fill(int x1, int x2, int y1, int y2)
    {// reverse x and y values in case user clicks in different locations
        if(x1>x2)
        {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if(y1>y2)
        {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        if(x1>40&&x2>40&&x1<99&&x2<99)// set area filled to soil if under top half of simulation, not bottom row
        {
            for(int i = x1; i <= x2; i++)
            {
                for(int j =y1; j <= y2;j++)
                {
                    if(grid[i][j].CharAt()>='1')
                    {
                        grid[i][j].setChar('0');
                        grid2[i][j]='0';
                    }
                }
            }
        }
    }
    
    //********************************************************************AntLion
    public void AntLion(int r, int c)
    {
        if (c < 75) //if input is on left of the farm (left antlion)
        {
            grid[r][0].setChar ('5'); //draw left antlion
            grid[r + 1][1].setChar ('5');
            grid[r - 1][1].setChar ('5');
            grid[r + 2][2].setChar ('5');
            grid[r - 2][2].setChar ('5');
        }
        else if (75 <= c) //otherwise if input is on right side (right antlion)
        {
            grid[r][148].setChar ('6'); //draw right antlion
            grid[r - 1][147].setChar ('6');
            grid[r + 1][147].setChar ('6');
            grid[r - 2][146].setChar ('6');
            grid[r + 2][146].setChar ('6');
        }
    }
    
    public void mole(int r, int c)
    {
        boolean occupied = false; //initial boolean declaration, set to false
        if (c < 75) //if the input is on the left half of the farm (left side mole)
        {
            for (int row = r - 4; row < r + 6; row++) //loops through rows around inputted location
            {
                for (int col = 0; col < grid[0].length; col++) //loops through every column
                {
                    if (grid[row][col].CharAt() == '7' || grid[row][col].CharAt() == '8') //if any of those indexes contain character 7 or 8 (meaning there is a mole in that row already)
                        occupied = true; //then set occupied to true
                }
            }
            if (occupied == false) //runs if occupied is false (no mole in the way)
            {
                grid[r][0].setChar ('7'); //draws mole
                grid[r + 1][0].setChar ('7');
                grid[r - 1][0].setChar ('7');
                grid[r + 2][0].setChar ('7');
                grid[r - 2][0].setChar ('7');
                grid[r + 3][0].setChar ('7');
                grid[r - 3][0].setChar ('7');
                grid[r][1].setChar ('7');
                grid[r - 1][1].setChar ('7');
                grid[r + 1][1].setChar ('7');
                grid[r - 2][1].setChar ('7');
                grid[r + 2][1].setChar ('7');
                grid[r - 3][1].setChar ('7');
                grid[r + 3][1].setChar ('7');
                grid[r][2].setChar ('7');
                grid[r - 1][2].setChar ('7');
                grid[r + 1][2].setChar ('7');
                grid[r - 2][2].setChar ('7');
                grid[r + 2][2].setChar ('7');
                grid[r - 3][2].setChar ('7');
                grid[r + 3][2].setChar ('7');
                grid[r][3].setChar ('7');
                grid[r - 1][3].setChar ('7');
                grid[r + 1][3].setChar ('7');
                grid[r - 3][3].setChar ('7');
                grid[r + 3][3].setChar ('7');
                grid[r - 4][3].setChar ('7');
                grid[r + 4][3].setChar ('7');
                grid[r][4].setChar ('7');
                grid[r - 3][4].setChar ('7');
                grid[r + 3][4].setChar ('7');
                grid[r - 4][4].setChar ('7');
                grid[r + 4][4].setChar ('7');
                grid[r - 4][5].setChar ('7');
                grid[r + 4][5].setChar ('7');
            }
        }
        else if (75 <= c) //if input is on the right half of the farm
        {
            for (int row = r - 4; row < r + 6; row++) //follows except same logic as previous but reversed (right side)
            {
                for (int col = 0; col < grid[0].length; col++)
                {
                    if (grid[row][col].CharAt() == '8' || grid[row][col].CharAt() == '7')
                        occupied = true;
                }
            }
            if (occupied == false)
            {
                grid[r][149].setChar ('8');
                grid[r + 1][149].setChar ('8');
                grid[r - 1][149].setChar ('8');
                grid[r + 2][149].setChar ('8');
                grid[r - 2][149].setChar ('8');
                grid[r + 3][149].setChar ('8');
                grid[r - 3][149].setChar ('8');
                grid[r][148].setChar ('8');
                grid[r - 1][148].setChar ('8');
                grid[r + 1][148].setChar ('8');
                grid[r - 2][148].setChar ('8');
                grid[r + 2][148].setChar ('8');
                grid[r - 3][148].setChar ('8');
                grid[r + 3][148].setChar ('8');
                grid[r][147].setChar ('8');
                grid[r - 1][147].setChar ('8');
                grid[r + 1][147].setChar ('8');
                grid[r - 2][147].setChar ('8');
                grid[r + 2][147].setChar ('8');
                grid[r - 3][147].setChar ('8');
                grid[r + 3][147].setChar ('8');
                grid[r][146].setChar ('8');
                grid[r - 1][146].setChar ('8');
                grid[r + 1][146].setChar ('8');
                grid[r - 3][146].setChar ('8');
                grid[r + 3][146].setChar ('8');
                grid[r - 4][146].setChar ('8');
                grid[r + 4][146].setChar ('8');
                grid[r][145].setChar ('8');
                grid[r - 3][145].setChar ('8');
                grid[r + 3][145].setChar ('8');
                grid[r - 4][145].setChar ('8');
                grid[r + 4][145].setChar ('8');
                grid[r - 4][144].setChar ('8');
                grid[r + 4][144].setChar ('8');
            }
        }
    }
    
    public void critterLive(int r, int c)
    {
        if (grid[r][c].CharAt() == '5') //if the inputted index of the array is character 5 (left antlion)
        {
            if (c < 148) //as long as the antlion is left of the second last right boundary of farm
            {
                if (grid[r][c+1].CharAt() != '5' && grid[r][c+1].CharAt() != '8') //if the character infront of it is neither similar antlion or mole
                    grid[r][c+1].setChar ('5'); //then set that index to be 5 as well (move to the right)
                if (0 <= c - 2) //if the current index is at least 2 away from left boundary of farm
                    grid[r][c-2].setChar(grid2[r][c-2]); //set that index to be either tunnel or dirt depending on original array
            }
            if ((c == 146 || c == 147 || c == 148) && (grid[r][c-1].CharAt() != '5')) //if the antlion is about to exit (boundary of farm)
                grid[r][c].setChar(grid2[r][c]); //set it to either tunnel or dirt depending on original array
        }
        else if (grid[r][c].CharAt() == '6') //if the inputted index of the array is character 6 (right antlion)
        {
            if (0 < c) //follows exact same logic as left antlion but reversed (moves to the left)
            {
                if (grid[r][c-1].CharAt() != '6' && grid[r][c-1].CharAt() != '5' && grid[r][c-1].CharAt() != '7') //gets eaten by left antlion
                    grid[r][c-1].setChar ('6');
                if (c + 2 < 150)
                    grid[r][c+2].setChar(grid2[r][c+2]);
            }
            if ((c == 0 || c == 1 || c == 2) && (grid[r][c+1].CharAt() != '6'))
                grid[r][c].setChar(grid2[r][c]);
        }
        else if (grid[r][c].CharAt() == '7') //if the character is 7 (left mole)
        {
            if (c < 148) //follows same logic as antlion
            {
                if (grid[r][c+1].CharAt() != '7') //but moves through everything (moves to the right)
                    grid[r][c+1].setChar ('7');
                if (0 <= c - 5)
                {
                    grid[r][c-5].setChar('1'); //leaves tunnel behind rather than dirt
                    grid2[r][c-5] = '1'; //updates duplicate grid to now be tunnel in the position it dug
                }
            }
            if ((c == 143 || c == 144 || c == 145 ||c == 146 || c == 147 || c == 148) && (grid[r][c-1].CharAt() != '7'))
            {
                grid[r][c].setChar('1');
                grid2[r][c] = '1';
                AntFarm.moleLeave(); //calls moleLeave method from AntFarm to determine what music is played
            }
        }
        else if (grid[r][c].CharAt() == '8') //if character is 8 (right mole)
        {
            if (0 < c) //follows exact same logic as left mole except reversed
            {
                if (grid[r][c-1].CharAt() != '8') //moves to the left
                    grid[r][c-1].setChar ('8');
                if (c + 5 <= 149)
                {
                    grid[r][c+5].setChar('1');
                    grid2[r][c+5] = '1';
                }
            }
            if ((c == 0 || c == 1 || c == 2 ||c == 3 || c == 4 || c == 5) && (grid[r][c+1].CharAt() != '8'))
            {
                grid[r][c].setChar('1');
                grid2[r][c] = '1';
                AntFarm.moleLeave();
            }
        }
    }
    
    public boolean isThereMole()
    {
        boolean mole = false; //boolean variable declaration, initially set to false
        for (int r = 0; r < grid.length; r++) //loop that goes through every row of array
        {
            for (int c = 0; c < grid[r].length; c++) //loop goes through every column of array
            {
                if (grid[r][c].CharAt() == '7' || grid[r][c].CharAt() == '8') //if any part of the array holds the character 7 or 8 (mole)
                    mole = true; //then the boolean is set to true
            }
        }
        return mole; //returns boolean
    }
    
    public void virus (int row, int col)
    {
        if (grid[row][col].CharAt() == '1')
            grid[row][col].setChar('9');
    }
    public void viruslive (int row, int col)
    {
        Ants spread = grid[row][col];
        if (Math.random() > 0.5)
        {
            spread = moveNext (row, col, '2');
            if (spread.sameAnt (row, col))
                spread = moveNext (row, col, '3');
        }
        else
        {
            spread = moveNext (row, col, '3');
            if (spread.sameAnt (row, col))
                spread = moveNext (row, col, '2');
        }
        
        if (!spread.sameAnt (row, col))
            spread.setChar('9');
    } 
    
    //********************************************************************Save
    public void save(String file) throws IOException
    {
    	String data=""; // initialize a string to add board information
        for (int row=0;row<grid.length;row++) {
            for (int col=0;col<grid[row].length;col++){  // go through board array and save values to string data
                data+=grid[row][col].CharAt();
        }
            data+='\n';
        }
        String fileName = file+".txt" ;

        FileWriter writer = new FileWriter( fileName ); // create new filewriter
        writer.write(data);// write new txt file with information from board
        writer.close(); // close filewriter
    }
    //****************************************************************Load
    public void load(File file)  throws IOException
    {
    	 try{
    	        FileReader reader = new FileReader(file);
    	        char str[] = new char [15000]; // create array of chars
    	        reader.read(str); // read in chars from file
    	        Ants data[][] = new Ants[100][150]; // create 2d array of Ants for the board
    	        for (int row = 0; row < grid.length; row++) {
    	            for (int col = 0; col < grid[row].length; col++) {   // go through 2d array and enter in char values
    	                    grid2[row][col] = str[row*101+col];
    	            }
    	        }
    	        grid = data;    // override previous board with new board
    	    }
    	    catch(Exception e){};
    	    
    }
}