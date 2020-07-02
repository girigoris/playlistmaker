package org.girigoris.music.media.player.playlist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PlayListMaker extends JPanel implements ActionListener, PropertyChangeListener {

    private JPanel rootPane = new JPanel(new BorderLayout());
    private JFrame frame = new JFrame("File cleaner");
    private JProgressBar jProgressBar;

    private File directory = null;
    private String baseDirName = null;
    private int percent;
    private Set<String> musicFiles;
    private Object[][] musicFileData;


    private void createAndShowGUI(){
        Dimension dimension = new Dimension(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenu menu = new JMenu("Options");
        menu.setMnemonic('O');

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Select a folder");
        menuItem.setMnemonic('s');
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic('x');
        menuItem.addActionListener(this);
        menu.add(menuItem);
        frame.setJMenuBar(menuBar);

        frame.add(rootPane);

        //Display the window.
        frame.pack();
        frame.setBounds(1000, 500, 700, 300);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getActionCommand() == "Exit"){
            System.exit(0);
        }

        if(actionEvent.getActionCommand() == "Select a folder"){

            JFileChooser fileChooser = new JFileChooser("Select a folder");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnValue = fileChooser.showOpenDialog(PlayListMaker.this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                directory = fileChooser.getSelectedFile();
                baseDirName = directory.getAbsolutePath();
                log("Base direcroty :"+baseDirName);


                percent = 0;
                //DisplayProgress displayProgress = new DisplayProgress();
                //displayProgress.start();
                CopyFile copyFile = new CopyFile();
                copyFile.start();
                log("Opening folder : " + directory.getName() + ".");
            } else {
                log("Open command cancelled by user.");
            }
            //log(fileChooser.getSelectedFile());
        }

    }

    class CopyFile extends Thread implements Runnable{
        public void run(){
            File[] files = directory.listFiles();
            musicFileData = new Object[files.length][files.length];
            musicFiles = new HashSet<String>();

            if ( files.length > 0){
                log("Number of files "+files.length);

                for (int x = 0; x < files.length; x++){
                    musicFileData[x][0] = files[x].getName();
                    musicFileData[x][1] = new Boolean(true);
                    musicFiles.add(files[x].getAbsolutePath());
                }

                String[] columnNames = {"File name", "Boolean"};
                //DefaultTableModel model = new DefaultTableModel(musicFileData, columnNames);

                JTable table = new JTable(musicFileData, columnNames);

                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);

                JPanel panel = new JPanel();
                panel.add(scrollPane);
                add(panel, BorderLayout.PAGE_START);
                setBorder(BorderFactory.createEmptyBorder(200, 200, 200, 200));
                rootPane.removeAll();
                rootPane.add(panel);
                frame.add(rootPane);
                frame.pack();
                frame.setBounds(1000, 500, 700, 300);
                frame.setVisible(true);


//                try {
//                    cleanStaleFiles(rawFiles,jpgFiles);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }


    private void populateJTable(Object[][] musicFilesData){


    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }


    private static void log(Object o){
        System.out.println(o);
    }



    public PlayListMaker(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void main(String[] args){ PlayListMaker playListMaker = new PlayListMaker(); }


}
