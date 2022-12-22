/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Algorithms.DFS;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Mazes.MyMaze;

/**
 *
 * @author Harshad Rathod
 */

public class View extends JFrame implements ActionListener, MouseListener {
    
    // 0 = not visited
    // 1 = blocked wall
    // 2 = visited
    // 9 = target
    
//    private int[][] maze = MyMaze.
//        {    
//        {1,1,1,1,1,1,1,1,1,1,1,1,1},
//        {1,0,1,0,1,0,1,0,0,0,0,0,1},
//        {1,0,1,0,0,0,1,0,1,1,1,0,1},
//        {1,0,0,0,1,1,1,0,0,0,0,0,1},
//        {1,0,1,0,0,0,0,0,1,1,1,0,1},
//        {1,0,1,0,1,1,1,0,1,0,0,0,1},
//        {1,0,1,0,1,0,0,0,1,1,1,0,1},
//        {1,0,1,0,1,1,1,0,1,0,1,0,1},
//        {1,0,0,0,0,0,0,0,0,0,1,9,1},
//        {1,1,1,1,1,1,1,1,1,1,1,1,1}
//    };

    private int[][] getMaze() {
        MyMaze maze = new MyMaze(7);
        maze.solve();
        maze.draw();
        System.out.println();
        
        int[][] grid = maze.getGrid();
        return grid;
        
    }
    
    private int[][] maze = getMaze();
    
    private int[] target = {maze.length-2, maze[0].length-3}; // our target 
    private List<Integer> path = new ArrayList<>();
    
    
    JButton submitButton;
    JButton cancelButton;
    JButton clearButton;
    JButton newButton;
    
    public View () {
        this.setTitle("Maze Solver"); // title of app
        this.setSize(1169, 700); // size of window
        this.setLayout(null); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // what should happen after closing the window
        this.setLocationRelativeTo(null); // to get the window at center of our screen
        
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this); // if the button is clicked 
        submitButton.setBounds(400, 600, 80, 30);
        
        newButton = new JButton("New Maze");
        newButton.addActionListener(this);
        newButton.setBounds(500, 600, 100, 30);
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        clearButton.setBounds(620, 600, 80, 30);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBounds(720, 600, 80, 30);
        
        this.addMouseListener(this);
        
        // button should appear on window
        this.add(submitButton);
        this.add(clearButton);
        this.add(cancelButton);
        this.add(newButton);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g); // to paint the maze on the window
        
        // iterate through the maze we created
        for (int row = 0; row<maze.length; row++) {
            for (int col = 0; col<maze[0].length; col++) {
                Color color; // object to color maze
                
                switch(maze[row][col]) {
                    
                    case 1:
                        color = Color.BLACK;
                        break;
                    
                    case 9:
                        color = Color.RED; // for target
                        break;
                        
                    default:
                        color = Color.WHITE; // for path
                        break;
                }
                
                g.setColor(color);
                g.fillRect(40*col, 40*row, 40, 40);
                g.setColor(Color.BLACK);
                g.drawRect(40*col, 40*row, 40, 40);
            }
        }
        
        
        // to paint our path with green color
        for (int p=0; p<path.size(); p+=2) {
            int pathX = path.get(p);
            int pathY = path.get(p+1);
            g.setColor(Color.GREEN);
            g.fillRect(40*pathY, 40*pathX, 40, 40);
        }
    }
    
    public void newGUI() {
        View gui = new View();
        gui.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try {
                DFS.searchPath(maze, 1, 2, path);
                this.repaint(); // paint the path green
                
            } catch (Exception excp) {
                JOptionPane.showMessageDialog(null, excp.toString());
            }
        }
        
        if (e.getSource() == cancelButton) {
             int flag = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Submit", JOptionPane.YES_NO_OPTION);
             if (flag == 0) System.exit(0);
        }
        
        if (e.getSource() == newButton) {
            int flag = JOptionPane.showConfirmDialog(null, "Are you sure you want a new Maze?", "Submit", JOptionPane.YES_NO_OPTION);
             if (flag == 0) {
                dispose();
                newGUI();
             }
        }
        
        if (e.getSource() == clearButton) {
            path.clear();
            
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    if (maze[row][col] == 2) {
                        maze[row][col] = 0;
                    }
                }
            }
            
            // paint the path which was green to white
            this.repaint();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getX() >= 0 && e.getX() <= maze[0].length*40 && e.getY() >= 0 && e.getY() <= maze.length*40) {
            int row = e.getY()/40;
            int col = e.getX()/40;
            
            if (maze[row][col] == 1) return;
            
            Graphics g = getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(40*target[1], 40*target[0], 40, 40);
            g.setColor(Color.red);
            g.fillRect(col*40, row*40, 40, 40);
            maze[target[0]][target[1]] = 0;
            maze[row][col] = 9;
            target[0] = row;
            target[1] = col;
        }
    }
    
    public void mousePressed(MouseEvent e){
        
    }
    
    public void mouseReleased(MouseEvent e){
        
    }
    
    public void mouseEntered(MouseEvent e){
        
    }
    
    public void mouseExited(MouseEvent e){
        
    }
    
    public void printMaze() {
        for (int i=0; i<maze.length; i++) {
            for (int j=0; j<maze[0].length; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        View gui = new View();
        gui.setVisible(true); // by default the JFrame is not visible
        System.out.println();
        gui.printMaze();
    }
}
