package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.URL;
// Фрейм для виведення інформації про розробника проєкту
public class InfoFrame extends JFrame {

	private JPanel contentPane;
	private URL url =
			InfoFrame.class.getResource("/resources/me.jpg");
	private BufferedImage img = null;

	{
		 try {
			img = ImageIO.read(url);
			} catch (IOException e1) {
			e1.printStackTrace();
			}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InfoFrame frame = new InfoFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InfoFrame() {
		setTitle("Developer information");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 367, 600);	
		setResizable(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{519, 29, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel() {
		  public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  Graphics2D g2d = (Graphics2D) g;
			  int originalHeight = img.getHeight();
			  int originalWidth = img.getWidth();	
			  int boundWidth = getBounds().width;
			  int boundHeight = getBounds().height;
			  int newWidth = originalWidth;
			  int newHeight = originalHeight;
			if(originalWidth != boundWidth) {
				newWidth = boundWidth;
				newHeight = (newWidth * originalHeight) / originalWidth;			
			}
			if(newHeight > boundHeight) {
				newHeight = boundHeight;
				newWidth = (newHeight * originalWidth) / originalHeight;
			}
			  Image scaledImg = img.getScaledInstance(newWidth,
					  newHeight, Image.SCALE_SMOOTH);
			  g2d.drawImage(scaledImg, (boundWidth - newWidth) / 2, (boundHeight - newHeight) / 2, this);
		  }
		};
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textArea.setLineWrap(true);
		textArea.setText("\u0420\u043E\u0437\u0440\u043E\u0431\u043D\u0438\u043A \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u0438:  \u0412\u043B\u0430\u0434\u0438\u0441\u043B\u0430\u0432 \r\n ");
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		contentPane.add(textArea, gbc_textArea);
		
	}

}
