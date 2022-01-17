import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.security.auth.callback.TextInputCallback;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.TextAction;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;


public class XatGrafic implements ActionListener {
    private static JList<String> users;
    private static DefaultListModel<String> usersModel;
    private static JTextField text;
    private static JButton button;
    private static JTextArea messages;
    private static MySocket sc;

    public XatGrafic() {
        messages = new JTextArea(/* 20, 30 */);
        text = new JTextField(/* 25 */);
        button = new JButton("Enviar");
        button.addActionListener(this);
        usersModel = new DefaultListModel<>();
        users = new JList<String>(usersModel);
    }

    private static void createAndShowGUI() {
        // Set the look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
        }

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("Xat");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        Border blackLine = BorderFactory.createLineBorder(Color.black);
        TitledBorder usuarisTitle = BorderFactory.createTitledBorder(blackLine, "Usuaris");

        usuarisTitle.setTitleJustification(TitledBorder.CENTER);
        users.setBorder(usuarisTitle);
        
        TitledBorder missatgesTitle = BorderFactory.createTitledBorder(blackLine, "Missatges");
        missatgesTitle.setTitleJustification(TitledBorder.CENTER);
        messages.setBorder(missatgesTitle);
        messages.setEditable(false);
        
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        sp.setResizeWeight(0.3);
        sp.setEnabled(false);
        sp.setDividerSize(0);
        sp.add(new JScrollPane(users));
        sp.add(new JScrollPane(messages));


        JPanel input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.LINE_AXIS));
        input.add(text);
        input.add(button);
        input.setMaximumSize(new Dimension(input.getMaximumSize().width, input.getMaximumSize().height));

        // add panels to main frame
        frame.add(sp, BorderLayout.CENTER);
        frame.add(input, BorderLayout.PAGE_END);

        // Display the window centered.
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 500);
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(button);
    }

    public void actionPerformed(ActionEvent event) {
        String missatge = text.getText();
        if (!sc.isClosed() && !missatge.equals("")) {
            sc.println(missatge);
            messages.append(missatge+"\n");
            text.setText(null);
        }

    }

    public static void main(String[] args) throws UnknownHostException, IOException{
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        sc = new MySocket(args[0], Integer.parseInt(args[1]));
        sc.println(args[2]);
        
        new XatGrafic();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }});

        ArrayList<String> initialUsers = new ArrayList<String>(Arrays.asList(sc.readLine().split(",")));
        for (String user : initialUsers) {
            if(!user.equals("")){
                usersModel.addElement(user);
            }
        }

        new Thread() {
            public void run() {
                String line;
                while ((line = sc.readLine()) != null && !sc.isClosed()) {
                    String[] line_splitted = line.split(">");
                    if (line_splitted[0].equals("Server")) {
                        if (line_splitted[1].equals("Left")) {
                            for(int i = 0; i<usersModel.size(); i++){
                                if(usersModel.get(i).contains(line_splitted[2])){
                                    usersModel.remove(i);
                                    break;
                                }
                            }
                        }else{
                            usersModel.addElement(line_splitted[2]);
                        }

                        
                    } else {
                        messages.append(line + "\n");
                    }
                    
                }
            };
        }.start();
    }

    




}
