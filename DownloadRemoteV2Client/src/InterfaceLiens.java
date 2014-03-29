import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultEditorKit;


@SuppressWarnings("serial")
public class InterfaceLiens extends JFrame implements ActionListener
{

	//JTextField champsLiens = new JTextField();
	JTextArea champsLiens = new JTextArea();
	JButton boutonEnvoyer = new JButton("Envoyer");
	JButton boutonAnnuler = new JButton("Annuler");
	JLabel label = new JLabel("Copiez vos liens ici :");
	JLabel liStatus = new JLabel("Déconnecté");
	JPanel panel = new JPanel();
	JFrame interfaceLiens = this;
	
	public InterfaceLiens()
	{
		super("Envoyer des liens");
		setContentPane(panel);
		panel.setLayout(new BorderLayout());
		panel.add("North",label);
		//champsLiens.setPreferredSize(new Dimension(500, 300));
		JScrollPane scrollpane = new JScrollPane(champsLiens);
		panel.add("Center",scrollpane);
		JPanel panelBoutons = new JPanel();
		panelBoutons.add(boutonAnnuler);
		panelBoutons.add(boutonEnvoyer);
		panel.add("South",panelBoutons);
		boutonEnvoyer.addActionListener(this);
		boutonAnnuler.addActionListener(this);
		JPopupMenu menu = new JPopupMenu();
		ActionMap actionMap = champsLiens.getActionMap();
		actionMap.get(DefaultEditorKit.copyAction);
		JMenuItem itemCopier = new JMenuItem("Copier");
		menu.add(itemCopier);
		itemCopier.addActionListener(new ActionListener() 
		{public void actionPerformed(ActionEvent e) 
			{champsLiens.copy();}});
		JMenuItem itemCouper = new JMenuItem("Couper");
		menu.add(itemCouper);
		itemCouper.addActionListener(new ActionListener() 
		{public void actionPerformed(ActionEvent e) 
			{champsLiens.cut();}});
		JMenuItem itemColler = new JMenuItem("Coller");
		menu.add(itemColler);
		itemColler.addActionListener(new ActionListener() 
		{public void actionPerformed(ActionEvent e) 
			{champsLiens.paste();}});
		champsLiens.setComponentPopupMenu(menu);
				
		setVisible(true);
		pack();
		setSize(getSize().width + 20,getSize().height + 20);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource() == boutonAnnuler)
			System.exit(0);
		if(arg0.getSource() == boutonEnvoyer)
		{
			String liens = new String(" ");
			String texte = champsLiens.getText();
			if (!texte.equals(""))
			{
				int test = 0;
				for (int i = 0; i < texte.length(); ++i ) 
				{
				       char c = texte.charAt(i);
				       int j = (int) c;
				       if(test == 4)
				       {
				    	   if(j == 10)
				    	   {
				    		   c = (char) 32;
				    		   test = 0;
				    	   }
		    			   liens += c;
				       }
					    	   
				       if(c == 'h' && test == 0)
				    	   test ++;
				       else
				    	   if(c == 't' && (test == 1 || test == 2))
				    		   test ++;
				    	   else
				    		   if(c == 'p' && test == 3)
				    		   {
				    			   test ++;
				    			   liens += "http";
				    		   }
				       
				       if(c == 'w' && (test == 0 || test == 1 || test == 2))
				    	   test ++;
				       else
				    	   if(c == '.' && test == 3)
				    	   {
				    		   test ++;
				    		   liens += "www.";
			    		   }
		       }
			
				if (!liens.equals(" "))
				{
					try {
						new remoteClient(liens);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				champsLiens.setText("");
			}
		}
		
	}




	
}
