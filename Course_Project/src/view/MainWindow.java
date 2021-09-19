package view;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import javazoom.jlme.util.Player;
import model.Brigade;
import model.Customs;
import model.Day;
import model.Truck;
import view.MainWindow;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

public class MainWindow {

	private JFrame frmTransavtoVasylenkoVladyslav;
	private JButton startButton;
	private JButton stopButton;
	private JSlider daySpeedSlider;
	private JLabel boxCountLabel;
	private JLabel firstTruckLabel;
	private JLabel secondTruckLabel;
	private JTextField textFieldBox;
	private JTextField textFieldCustomsQueue;
	private JTextField textFieldTruckQueue;
	private InfoFrame infoFrame = new InfoFrame();
	private TaskFrame taskFrame = new TaskFrame();
	// ��������� URL ��� ��������� �� ��������� ����� ���������
	private URL urlTruck = MainWindow.class.getResource("/resources/truck.png");
	private URL urlInvertedTruck = MainWindow.class.getResource("/resources/invertedtruck.png");
	private URL urlSun = MainWindow.class.getResource("/resources/sun.png");
	private URL urlMoon = MainWindow.class.getResource("/resources/moon.png");
	private URL urlCusGrenn = MainWindow.class.getResource("/resources/customersgreen.png");
	private URL urlCusRed = MainWindow.class.getResource("/resources/customersred.png");
	private URL urlCalmGuy = MainWindow.class.getResource("/resources/calmguy.png");
	private URL urlLeftGuy = MainWindow.class.getResource("/resources/leftguy.png");
	private URL urlMiddleGuy = MainWindow.class.getResource("/resources/middleguy.png");
	private URL urlRightGuy = MainWindow.class.getResource("/resources/rightguy.png");
	private BufferedImage imgTruck = null;
	private BufferedImage imgInvertedTruck = null;
	private BufferedImage imgSun = null;
	private BufferedImage imgMoon = null;
	private BufferedImage imgCusGreen = null;
	private BufferedImage imgCusRed = null;
	private BufferedImage imgCalmGuy = null;
	private BufferedImage imgLeftGuy = null;
	private BufferedImage imgMiddleGuy = null;
	private BufferedImage imgRightGuy = null;
	Image scaledImgTruck;
	Image scaledImgInvertTruck;
	Image scaledImgCustoms;
	Image scaledImgDay;
	Image scaledImgBrigade;
	// ������ ���������
	List<Truck> trucks = Collections.synchronizedList(new ArrayList<Truck>());
	// ��'��� ����� �������
	Customs customs = new Customs();
	// ��'��� ����� ������ ����
	Day day = new Day();
	// ��� ��'���� ����� �������
	Brigade brigade1 = new Brigade();
	Brigade brigade2 = new Brigade();
	// ��'��� ����� Player (��� ����������� ������)
	protected Player player;
	// ������ �����, �� ����� �� ����� ��������� �� ��� �� ��������� ����� ��
	// �������
	boolean noRides = true;
	// ������ �����, �� ����� �� ����� ��������� �� ��� �� ������� �� �������
	boolean noRides2 = true;
	// ������ �����, �� ����� �� ������� � ����� ��� �������
	boolean customIsBusy = false;
	// �����, �� ����� �� ������� ��� ����������� �����
	int allBoxCount = 0;
	// ��'���� ��� ������������
	Object monitor = new Object();
	Object monitor2 = new Object();
	Object monitor3 = new Object();
	Object monitor4 = new Object();
	Object monitor5 = new Object();
	// �� ����, �� ���������� �� ��� ������������� ���� ��������� ������ �
	// ������
	long brigadeTime1;
	long brigadeTime2;
	// �� ����� ����, �� �������� ��� � ������ ��������������� � ������ ������
	boolean brigadeUse1;
	boolean brigadeUse2;
	// ������ �����, �� ����� �� ��, �� ���������� ������ Stop
	boolean stop = false;
	// ������ �����, �� �������� ��������� ���������� ������ ������������
	boolean firstSun = true;
	// ����� ��� �������� ������������
	AffineTransform transform = new AffineTransform();
	// �����, ��� ���������� �������� ����������� ������� ������� �����, �� ������������ ���������
	int maxBoxCount;
	// �� ���� �� ������������ �������� ������� ���� ������� �� ��������� ��������
	int customsQueueCount;
	int trucksQueueCount;

	{
		try {
			// ����������� ���� ����������
			imgTruck = ImageIO.read(urlTruck);
			imgInvertedTruck = ImageIO.read(urlInvertedTruck);
			imgSun = ImageIO.read(urlSun);
			imgMoon = ImageIO.read(urlMoon);
			imgCusGreen = ImageIO.read(urlCusGrenn);
			imgCusRed = ImageIO.read(urlCusRed);
			imgCalmGuy = ImageIO.read(urlCalmGuy);
			imgLeftGuy = ImageIO.read(urlLeftGuy);
			imgMiddleGuy = ImageIO.read(urlMiddleGuy);
			imgRightGuy = ImageIO.read(urlRightGuy);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	// ������ ��� ���������� ������� � ���� ���������
	private JPanel firstTruckPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// ���������� ��� ����������, ����� � ���� ������� �� �������� ���������, �
			// ����� �� �����������
			// (����� ���������� ��������������� ��� ������������ ��������� �� ���� ����
			// ����������� �����)
			scaledImgTruck = imgTruck.getScaledInstance(getWidth() / 6, getHeight() / 10 * 3, Image.SCALE_SMOOTH);
			scaledImgInvertTruck = imgInvertedTruck.getScaledInstance(getWidth() / 6, getHeight() / 10 * 3,
					Image.SCALE_SMOOTH);
			// ���������� ��� ���������� �������, ����� � ���� ������� ��������
			// ���������, ����� - ������� ���������
			if (customs.getRed()) {
				scaledImgCustoms = imgCusRed.getScaledInstance(getWidth() / 5, getHeight() / 2, Image.SCALE_SMOOTH);
			} else {
				scaledImgCustoms = imgCusGreen.getScaledInstance(getWidth() / 5, getHeight() / 2, Image.SCALE_SMOOTH);
			}
			// ������� �������
			g2d.drawImage(scaledImgCustoms, (getWidth() - scaledImgCustoms.getWidth(firstTruckPanel)) / 2,
					getHeight() / 20, this);
			for (int i = 0; i < trucks.size(); i++) {
				// ���� ��������� �� �� ������ ����������������, ������� �������� ���������
				if (!(trucks.get(i).getArrived())) {
					g2d.drawImage(scaledImgTruck, trucks.get(i).getX(), trucks.get(i).getY(), this);
				} else {
					// ������ ������� ����������� ���������
					g2d.drawImage(scaledImgInvertTruck, trucks.get(i).getX(), trucks.get(i).getY(), this);
				}
			}
		}
	};
	// ������ ��� ���������� ���� ����� �� �����
	private JPanel dayPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// ���������� ��� ����������, ����� ������� �� �����, ����� �� �����
			if (day.getSun()) {
				// ���� ����� ���� �� ������� �����
				scaledImgDay = imgSun.getScaledInstance(getWidth() / 15, getHeight(), Image.SCALE_SMOOTH);
			} else {
				// ������ ������� �����
				scaledImgDay = imgMoon.getScaledInstance(getWidth() / 14, getHeight(), Image.SCALE_SMOOTH);
			}
			if (firstSun) {
				// ����� ������� ������� �� ������� �������� �� �������� �������� ������������
				transform.translate(-scaledImgDay.getWidth(dayPanel), 0);
				day.setX(-scaledImgDay.getWidth(dayPanel));
			}
			firstSun = false;
			// ������� ����� �� �����
			g2d.drawImage(scaledImgDay, transform, this);
		}
	};
	// ������ ��� ���������� ������ ����� �������
	private JPanel firstForemanPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// ���������� ������ ����������
			if (brigade1.getCalm()) {
				// ����� ������� �� ��������� � ���� ������
				scaledImgBrigade = imgCalmGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade1.getState() == 1) {
				// ������� ���������� �� ���������� ���������� ����� ��������� ���
				// ���������� �����
				scaledImgBrigade = imgLeftGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade1.getState() == 2) {
				scaledImgBrigade = imgMiddleGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade1.getState() == 3) {
				scaledImgBrigade = imgRightGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			}
			// ������� ���������
			g2d.drawImage(scaledImgBrigade, 0, 0, this);
		}
	};
	// ������ ��� ���������� ������ ����� �������
	private JPanel secondForemanPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// ���������� ������ ����������
			if (brigade2.getCalm()) {
				// ����� ������� �� ��������� � ���� ������
				scaledImgBrigade = imgCalmGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade2.getState() == 1) {
				// ������� ���������� �� ���������� ���������� ����� ��������� ���
				// ���������� �����
				scaledImgBrigade = imgLeftGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade2.getState() == 2) {
				scaledImgBrigade = imgMiddleGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade2.getState() == 3) {
				scaledImgBrigade = imgRightGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			}
			// ������� ���������
			g2d.drawImage(scaledImgBrigade, 0, 0, this);
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmTransavtoVasylenkoVladyslav.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTransavtoVasylenkoVladyslav = new JFrame();
		frmTransavtoVasylenkoVladyslav.setTitle("TransAvto, Vasylenko Vladyslav, PI-181");
		frmTransavtoVasylenkoVladyslav.getContentPane().setBackground(SystemColor.control);
		frmTransavtoVasylenkoVladyslav.setBounds(100, 100, 1095, 721);
		frmTransavtoVasylenkoVladyslav.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTransavtoVasylenkoVladyslav.getContentPane().setLayout(null);
		firstTruckPanel.setBounds(0, 166, 828, 250);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(firstTruckPanel);

		dayPanel.setBounds(0, 0, 1081, 71);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(dayPanel);

		firstForemanPanel.setBounds(827, 166, 60, 78);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(firstForemanPanel);

		secondForemanPanel.setBounds(827, 271, 60, 78);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(secondForemanPanel);

		JSlider createTruckSlider = new JSlider();
		createTruckSlider.setPaintLabels(true);
		createTruckSlider.setPaintTicks(true);
		createTruckSlider.setValue(3000);
		createTruckSlider.setMajorTickSpacing(1000);
		createTruckSlider.setMaximum(5000);
		createTruckSlider.setMinimum(1000);
		createTruckSlider.setMinorTickSpacing(100);
		createTruckSlider.setBounds(10, 111, 200, 45);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(createTruckSlider);

		JSlider customsPassSlider = new JSlider();
		customsPassSlider.setValue(2000);
		customsPassSlider.setMajorTickSpacing(1000);
		customsPassSlider.setMinorTickSpacing(100);
		customsPassSlider.setMinimum(2000);
		customsPassSlider.setMaximum(5000);
		customsPassSlider.setPaintLabels(true);
		customsPassSlider.setPaintTicks(true);
		customsPassSlider.setBounds(220, 111, 200, 45);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(customsPassSlider);

		JSlider truckSpeedSlider = new JSlider();
		truckSpeedSlider.setValue(5);
		truckSpeedSlider.setPaintTicks(true);
		truckSpeedSlider.setPaintLabels(true);
		truckSpeedSlider.setMajorTickSpacing(1);
		truckSpeedSlider.setMinorTickSpacing(1);
		truckSpeedSlider.setMaximum(10);
		truckSpeedSlider.setMinimum(1);
		truckSpeedSlider.setBounds(642, 111, 200, 45);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(truckSpeedSlider);

		daySpeedSlider = new JSlider();
		daySpeedSlider.setValue(1);
		daySpeedSlider.setMajorTickSpacing(1);
		daySpeedSlider.setMinorTickSpacing(1);
		daySpeedSlider.setMaximum(10);
		daySpeedSlider.setMinimum(1);
		daySpeedSlider.setPaintTicks(true);
		daySpeedSlider.setPaintLabels(true);
		daySpeedSlider.setBounds(852, 111, 200, 45);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(daySpeedSlider);

		JSlider customsQueue = new JSlider();
		customsQueue.setMaximum(5);
		customsQueue.setToolTipText("");
		customsQueue.setPaintLabels(true);
		customsQueue.setPaintTicks(true);
		customsQueue.setValue(0);
		customsQueue.setMajorTickSpacing(1);
		customsQueue.setMinorTickSpacing(1);
		customsQueue.setOrientation(SwingConstants.VERTICAL);
		customsQueue.setBounds(348, 457, 82, 168);
		customsQueue.setEnabled(false);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(customsQueue);

		JSlider trucksQueue = new JSlider();
		trucksQueue.setPaintLabels(true);
		trucksQueue.setPaintTicks(true);
		trucksQueue.setValue(0);
		trucksQueue.setMinorTickSpacing(1);
		trucksQueue.setMajorTickSpacing(1);
		trucksQueue.setMaximum(5);
		trucksQueue.setOrientation(SwingConstants.VERTICAL);
		trucksQueue.setBounds(743, 457, 82, 168);
		trucksQueue.setEnabled(false);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(trucksQueue);

		JSlider brigadeTimeSlider = new JSlider();
		brigadeTimeSlider.setPaintTicks(true);
		brigadeTimeSlider.setPaintLabels(true);
		brigadeTimeSlider.setMajorTickSpacing(1000);
		brigadeTimeSlider.setMaximum(2500);
		brigadeTimeSlider.setMinorTickSpacing(100);
		brigadeTimeSlider.setMinimum(500);
		brigadeTimeSlider.setValue(1500);
		brigadeTimeSlider.setBounds(430, 111, 200, 45);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(brigadeTimeSlider);

		JLabel lblNewLabel_6 = new JLabel("BrigadeTime");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_6.setBounds(484, 86, 98, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_6);

		JLabel lblNewLabel = new JLabel("CreateTruckTime");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(55, 86, 129, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("CustomsPassTime");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(265, 86, 137, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("TruckSpeed");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(705, 86, 82, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("DaySpeed");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(922, 86, 71, 20);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("CustomsQueue");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(348, 426, 107, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("TrucksQueue");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_5.setBounds(743, 426, 99, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_5);

		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// ����������� ������
				playMusic();
				// ������ ����� �� ���� ���� ����������
				startButton.setEnabled(false);
				// ������ ���� �� ����������
				stop = false;
				
				// �������� ����������� �������� �����, ��� ���������� ��������
				// ����������� ������� ������� �����, �� ������������ ���������
				try {
					maxBoxCount = Integer.parseInt(textFieldBox.getText());
				} catch (Exception e) {
					// ���� ����� ������� �����������, �� ������������ �� �������� 0 
					maxBoxCount = 0;
				}
				// �������� ����������� �������� ���� ������ ��� ������������
				// ������� ������� ���� ������� �� ��������� ��������
				try {
					customsQueueCount = Integer.parseInt(textFieldCustomsQueue.getText());
				} catch (Exception e) {
					// ���� ����� ������� �����������, �� ������������ �� �������� 0  
					customsQueueCount = 0;
				}
				try {
					trucksQueueCount = Integer.parseInt(textFieldTruckQueue.getText());
				} catch (Exception e) {
					// ���� ����� ������� �����������, �� ������������ �� �������� 0 
					trucksQueueCount = 0;
				}
				// ���� ����� ����� �� ������ �� �������� ������� �������, �� ������������ �������� ����� 5 �� �������������
				// � ������ ������� ����������� ������� ����� � ����� ������� ������ ���� ������� ������� �� 1 �� 20
				// � ����� ��������� ���� ���� ������� ������� �� 2 �� 20 (��� ������ �������� �� ���������� �������)
				if (maxBoxCount < 1 || maxBoxCount > 20) {
					textFieldBox.setText("5");
					maxBoxCount = 5;
				}
				if (customsQueueCount < 1 || customsQueueCount > 20) {
					textFieldCustomsQueue.setText("5");
					customsQueueCount = 5;
				}
				if (trucksQueueCount < 2 || trucksQueueCount > 20) {
					textFieldTruckQueue.setText("5");
					trucksQueueCount = 5;
				}
				// ����� �������� ������, �� ���������� �� ����������� ������� ����� 
				// �� ������ ����� ��������� ��������
				textFieldBox.setEnabled(false);
				textFieldCustomsQueue.setEnabled(false);
				textFieldTruckQueue.setEnabled(false);
				
				//������� ����������� �������� ��������� �������� �� �������������� ��� ���� 
				customsQueue.setLabelTable(null);
				customsQueue.setMajorTickSpacing(((customsQueueCount - 1) / 10) + 1);
				customsQueue.setMaximum(customsQueueCount);
							
				trucksQueue.setLabelTable(null);
				trucksQueue.setMajorTickSpacing(((trucksQueueCount - 1) / 10) + 1);
				trucksQueue.setMaximum(trucksQueueCount);		
				
				new Thread(() -> {

					while (true && !stop) {
						// ���� ����� �� ����� ������ �� ��������
						if (day.getX() > dayPanel.getWidth()) {
							// �� ������� ��'��� �� ��� ������� ������
							transform.translate(-dayPanel.getWidth() - scaledImgDay.getWidth(dayPanel), 0);
							// ��������� ���������� ��'����
							day.setX(-scaledImgDay.getWidth(dayPanel));
							dayPanel.repaint();
							// ������� ����� �� ����� �� ������� (������ ������� ���)
							day.setSun(!day.getSun());
						}
						// ��������
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// ��������� ���������� ��'���� �� ����� �� �������� ������� �����������
						transform.translate(daySpeedSlider.getValue(), 0);
						// ��������� ���������� ��'����
						day.setX(day.getX() + daySpeedSlider.getValue());
						dayPanel.repaint();
					}
				}).start();

				new Thread(() -> {
					while (true) {
						try {
							// �������� � ��������� �� ����, ���� ����� �� ��, �������� ��������� �� 20%
							// �� �������� ���������� ��������
							if (day.getSun()) {
								Thread.sleep((long) (0.4 * Math.random() * createTruckSlider.getValue()
										+ createTruckSlider.getValue() * 0.8));
							} else {
								// ���� ����� ��, �� ��������� ��������� � ���������� ���� ������� �����
								// ����� ����
								Thread.sleep((long) (2 * (0.4 * Math.random() * createTruckSlider.getValue()
										+ createTruckSlider.getValue() * 0.8)));
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// �������� �� �������� ����� ����� ��������� �� ��������� ����� �� �������
						noRides = true;
						for (int i = 0; i < trucks.size(); i++) {
							if (trucks.get(i).getRides()) {
								// ���� ��� ���� ��������� ��������, ����� noRides ����������� ��������
								// false
								noRides = false;
							}
						}
						// ���� � ���� ������� � ���� � ����� ��������� �� �������� �� ���������
						// ����� �� ������� � �� ���������� ������ ����
						if (customsQueue.getValue() < customsQueue.getMaximum() && noRides && !stop) {
							new Thread(() -> {
								// ��������� ���� ���������
								Truck truck = new Truck();
								// �������� ������� �����, �� ������������ ��������� (�� 1 �� 5)
								truck.setBoxCount((int) (Math.random() * maxBoxCount + 1));
								// ϳ������� ������� ������ ��� ���������� ���������
								truck.setY(
										firstTruckPanel.getHeight() / 20 + scaledImgCustoms.getHeight(firstTruckPanel)
												- scaledImgTruck.getHeight(firstTruckPanel));
								firstTruckPanel.repaint();
								// ������ ��������� � ������ ���������
								trucks.add(truck);
								// �� ����������� �������� ������ ����� rides true (����� ��������� ������
								// �������� �� ��������� ����� �� �������)
								truck.setRides(true);
								// ���� ��������� �� ������ �� ������
								while (truck.getX()
										+ scaledImgTruck.getWidth(firstTruckPanel) < firstForemanPanel.getX() - 5) {
									// ���� ��������� ������ �� �������, ��� ������� �� �� �� ����������
									if (truck.getX()
											+ scaledImgTruck.getWidth(firstTruckPanel) >= (firstTruckPanel.getWidth()
													- scaledImgCustoms.getWidth(firstTruckPanel)) / 2 - 5
											&& truck.getX() + scaledImgTruck
													.getWidth(firstTruckPanel) < (firstTruckPanel.getWidth()
															+ scaledImgCustoms.getWidth(firstTruckPanel)) / 2) {
										try {
											// �� � ������� ���� ��������� ����� ������� �� �������
											if (truck.getRides()) {
												// �������� �������� ����� ������� �� 1
												customsQueue.setValue(customsQueue.getValue() + 1);
												// ����� rides ����������� �������� false (����� ��������� ��������
												// ��� �� ��������� ����� �� �������)
												truck.setRides(false);
											}
											synchronized (monitor) {
												// �������� �� �������� ����� ����� ��������� �� ������� �� ������
												noRides2 = true;
												for (int i = 0; i < trucks.size(); i++) {
													if (trucks.get(i).getRides2()) {
														// ���� ��� ���� ��������� ��������, ����� noRides2
														// ����������� �������� false
														noRides2 = false;
													}
												}
												// ���� ������� �� ������� � � ����� ���� � ���� ��������� � �����
												// ��������� �� �������� �� ������� �� ������
												if (!customIsBusy && trucksQueue.getValue() < trucksQueue.getMaximum()
														&& noRides2) {
													// ����� inCustom ����������� �������� true (��������� ������
													// �������� � �������)
													truck.setInCustom(true);
													// ������� �������
													customIsBusy = true;
													// �������� �� �������� � �������, �������� ��������� �� 20% ��
													// �������� ���������� ��������
													Thread.sleep(
															(long) (0.4 * Math.random() * customsPassSlider.getValue()
																	+ customsPassSlider.getValue() * 0.8 - 800));
													// ������� ���� ������� ������, ����� ����� ��������� ����
													// ���������
													customs.setRed(false);
													firstTruckPanel.repaint();
													Thread.sleep(800);
													// �������� ��� ���������� �������� �������
													customs.setRed(true);
													// ������� ���� �������� ������
													firstTruckPanel.repaint();
													// ��������� ������������� �� ������ �� �� �������
													truck.setX((firstTruckPanel.getWidth()
															+ scaledImgCustoms.getWidth(firstTruckPanel)) / 2);
													// ����� ������� ���������� �� 1
													customsQueue.setValue(customsQueue.getValue() - 1);
													firstTruckPanel.repaint();
													// ������� �����
													customIsBusy = false;
												} else {
													// ������ ������ ��������� �������
													continue;
												}
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
									try {
										if (truck.getInCustom()) {
											// ���� ��������� ��� ������� �������� � �������, �� ��������� �� ���� ���
											// �� ������� �� ������,
											// ������������ ������ ����� rides2 �������� true
											truck.setRides2(true);
										}
										// ��������
										Thread.sleep(30);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									// ������� ���������, ����� ����� �������� �� �������� ���������� ��������
									truck.setX(truck.getX() + truckSpeedSlider.getValue());
									firstTruckPanel.repaint();

								}
								// ����� ��������� ���������� �� 1
								trucksQueue.setValue(trucksQueue.getValue() + 1);
								// ��������� �� ��������� ��� �� ��� �� ������� �� ������,
								// ������������ ������ ����� rides2 �������� false
								truck.setRides2(false);
								brigadeUse1 = false;
								brigadeUse2 = false;
								while (true) {
									synchronized (monitor2) {
										// ���� ��� ���� � ��������� �� ������
										if (brigade1.getCalm() || brigade2.getCalm()) {
											// ��������� ���������
											truck.setArrived(true);
											firstTruckPanel.repaint();
											// ���� ����� ������� �� ������
											if (brigade1.getCalm()) {
												// �� ��������� �������� �������� �� ���������� ������ ����� ��� �����
												// �������
												brigadeTime1 = (long) (brigadeTimeSlider.getValue() * 0.8
														+ Math.random() * brigadeTimeSlider.getValue() * 0.4);
												// �������� ������ �������� ������� �����, �� ������� ������������
												brigade1.setBoxCount(truck.getBoxCount());
												// ��������, �� � ������ ������ ��������������� ����� �������
												brigadeUse1 = true;
												// ����� ������� ������ ���������
												brigade1.setCalm(false);
												// ��������� ����
												break;
											} else if (brigade2.getCalm()) {
												// �� ��������� �������� �������� �� ���������� ������ ����� ��� �����
												// �������
												brigadeTime2 = (long) (brigadeTimeSlider.getValue() * 0.8
														+ Math.random() * brigadeTimeSlider.getValue() * 0.4);
												// �������� ������ �������� ������� �����, �� ������� ������������
												brigade2.setBoxCount(truck.getBoxCount());
												// ��������, �� � ������ ������ ��������������� ����� �������
												brigadeUse2 = true;
												// ����� ������� ������ ���������
												brigade2.setCalm(false);
												// ��������� ����
												break;
											}
										}
									}
								}
								// ��������� �������� ��� ��������� � ��������� �� ����, ��� � ������
								// ��������� ��������������
								try {
									if (brigadeUse1) {
										Thread.sleep(brigadeTime1 * truck.getBoxCount() + 10);
									} else if (brigadeUse2) {
										Thread.sleep(brigadeTime2 * truck.getBoxCount() + 10);
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// ����� ��������� ���������� �� 1
								trucksQueue.setValue(trucksQueue.getValue() - 1);
								// ������� ��������� ���� ��� ������� ���� ����� ���� �������� �������������
								truck.setY(
										firstTruckPanel.getHeight() - scaledImgInvertTruck.getHeight(firstTruckPanel));
								// ���� ��������� �� �������� ��� ������
								while (truck.getX() + scaledImgInvertTruck.getWidth(firstTruckPanel) >= 0) {
									try {
										// ��������
										Thread.sleep(30);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									// ������� ���������
									truck.setX(truck.getX() - truckSpeedSlider.getValue());
									firstTruckPanel.repaint();
								}
								// ��������� ��������� � ������ ���������
								trucks.remove(truck);
							}).start();
						}
					}
				}).start();

				new Thread(() -> {
					while (true) {
						try {
							// �������� �������� ��� ��������� ������ ������
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// ���� ������� �� � ���� ������ (�� ���������)
						synchronized (monitor4) {

							if (!brigade1.getCalm()) {
								try {
									// ���� �� ����������� �� �����
									while (brigade1.getBoxCount() > 0) {
										firstTruckLabel
												.setText("�-��� ����� � ���������(1):" + brigade1.getBoxCount());
										// ��������� ���� ����� ���������
										brigade1.setState(1);
										firstForemanPanel.repaint();
										Thread.sleep(brigadeTime1 / 3);
										brigade1.setState(2);
										firstForemanPanel.repaint();
										Thread.sleep(brigadeTime1 / 3);
										brigade1.setState(3);
										firstForemanPanel.repaint();
										Thread.sleep(brigadeTime1 / 3);
										// ���� ������������, ������� ����� �� ����� ������������ ���������� �� 1
										brigade1.setBoxCount(brigade1.getBoxCount() - 1);
										// ʳ������ ������ ����������� ����� ������ �� 1
										allBoxCount++;
										boxCountLabel.setText("������ ����� ����������:" + allBoxCount);
									}
									firstTruckLabel.setText("�-��� ����� � ���������(1):" + brigade1.getBoxCount());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// ������� ����������� � ���� ������
								brigade1.setCalm(true);
								firstForemanPanel.repaint();
							}
						}
					}
				}).start();

				new Thread(() -> {
					while (true) {
						try {
							// �������� �������� ��� ��������� ������ ������
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// ���� ������� �� � ���� ������ (�� ���������)
						synchronized (monitor5) {
							if (!brigade2.getCalm()) {
								try {
									while (brigade2.getBoxCount() > 0) {
										secondTruckLabel
												.setText("�-��� ����� � ���������(2):" + brigade2.getBoxCount());
										// ��������� ���� ����� ���������
										brigade2.setState(1);
										secondForemanPanel.repaint();
										Thread.sleep(brigadeTime2 / 3);
										brigade2.setState(2);
										secondForemanPanel.repaint();
										Thread.sleep(brigadeTime2 / 3);
										brigade2.setState(3);
										secondForemanPanel.repaint();
										Thread.sleep(brigadeTime2 / 3);
										// ���� ������������, ������� ����� �� ����� ������������ ���������� �� 1
										brigade2.setBoxCount(brigade2.getBoxCount() - 1);
										// ʳ������ ������ ����������� ����� ������ �� 1
										allBoxCount++;
										boxCountLabel.setText("������ ����� ����������:" + allBoxCount);
									}
									secondTruckLabel.setText("�-��� ����� � ���������(2):" + brigade2.getBoxCount());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// ������� ����������� � ���� ������
								brigade2.setCalm(true);
								secondForemanPanel.repaint();
							}
						}
					}
				}).start();

			}
		});
		startButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		startButton.setBounds(943, 440, 85, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(startButton);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// ���������� ������ ����
				stop = true;
				// ������� ����������� ������
				doStopPlay();
				// ������ ���������� ������ � ����� onEndOfPlay
				Thread t1 = onEndOfPlay();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							t1.join();
							// ������ ����� �� ������� ���� �����������
							startButton.setEnabled(true);
							textFieldBox.setEnabled(true);
							textFieldCustomsQueue.setEnabled(true);
							textFieldTruckQueue.setEnabled(true);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		stopButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		stopButton.setBounds(943, 483, 85, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(stopButton);

		firstTruckLabel = new JLabel(
				"\u041A-\u0441\u0442\u044C \u044F\u0449\u0438\u043A\u0456\u0432 \u0443 \u0432\u0430\u043D\u0442\u0430\u0436\u0456\u0432\u0446\u0456(1):0");
		firstTruckLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		firstTruckLabel.setBounds(860, 360, 211, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(firstTruckLabel);

		secondTruckLabel = new JLabel(
				"\u041A-\u0441\u0442\u044C \u044F\u0449\u0438\u043A\u0456\u0432 \u0443 \u0432\u0430\u043D\u0442\u0430\u0436\u0456\u0432\u0446\u0456(2):0");
		secondTruckLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		secondTruckLabel.setBounds(860, 390, 211, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(secondTruckLabel);

		boxCountLabel = new JLabel(
				"\u0412\u0441\u044C\u043E\u0433\u043E \u044F\u0449\u0438\u043A\u0456\u0432 \u0434\u043E\u0441\u0442\u0430\u0432\u043B\u0435\u043D\u043E:0");
		boxCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		boxCountLabel.setBounds(890, 248, 191, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(boxCountLabel);

		JLabel lblNewLabel_7 = new JLabel(
				"\u041C\u0430\u043A-\u043D\u0430 \u043A-\u0441\u0442\u044C \u044F\u0449\u0438\u043A\u0456\u0432 (\u0432\u0456\u0434 1 \u0434\u043E 20)");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_7.setBounds(10, 453, 235, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel(
				"\u041C\u0456-\u0441\u0442\u044C \u0447\u0435\u0440\u0433\u0438 \u043C\u0438\u0442\u043D\u0438\u0446\u0456 (\u0432\u0456\u0434 1 \u0434\u043E 20)");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_8.setBounds(10, 511, 239, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_8);

		JLabel lblNewLabel_9 = new JLabel(
				"\u041C\u0456-\u0441\u0442\u044C \u0447\u0435\u0440\u0433\u0438 \u0431\u0440\u0438\u0433\u0430\u0434\u0438 (\u0432\u0456\u0434 2 \u0434\u043E 20)");
		lblNewLabel_9.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_9.setBounds(10, 567, 250, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(lblNewLabel_9);

		textFieldBox = new JTextField();
		textFieldBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldBox.setBounds(254, 456, 60, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(textFieldBox);
		textFieldBox.setColumns(10);

		textFieldCustomsQueue = new JTextField();
		textFieldCustomsQueue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldCustomsQueue.setColumns(10);
		textFieldCustomsQueue.setBounds(254, 511, 60, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(textFieldCustomsQueue);

		textFieldTruckQueue = new JTextField();
		textFieldTruckQueue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldTruckQueue.setColumns(10);
		textFieldTruckQueue.setBounds(254, 567, 60, 21);
		frmTransavtoVasylenkoVladyslav.getContentPane().add(textFieldTruckQueue);

		JMenuBar menuBar = new JMenuBar();
		frmTransavtoVasylenkoVladyslav.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Info");
		mnNewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Developer");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// ��������� ������ � ����������� ��� ����������
				onDeveloperClick();
			}
		});
		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Task");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��������� ������ � ����������� ��� ��������
				onTaskClick();
			}
		});
		mntmNewMenuItem_1.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		mnNewMenu.add(mntmNewMenuItem_1);

	}

	private Thread playMusic() {
		Thread t = new Thread() {
			public void run() {
				try {
					URL u = getClass().getResource("/resources/zaGorizont.mp3");
					player = new Player(new BufferedInputStream(u.openStream(), 2048));
					// ����������� ������
					player.play();
					// ϳ��� ��������� ����������� ����������� ������ ����
					stop = true;
					// ������ ���������� ������ � ����� onEndOfPlay
					Thread t2 = onEndOfPlay();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								t2.join();
								// ������ ����� �� ������� ���� �����������
								startButton.setEnabled(true);
								textFieldBox.setEnabled(true);
								textFieldCustomsQueue.setEnabled(true);
								textFieldTruckQueue.setEnabled(true);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		return t;
	}

	private Thread onEndOfPlay() {
		synchronized (monitor3) {
			// ������ ���� ������ ��������� ����� ������ (�� ����� �� ���� �����
			// ���������)
			Thread t = new Thread(() -> {
				while (true) {
					if (trucks.isEmpty())
						break;
				}
			});
			t.start();
			return t;
		}
	}

	// ������� ��� �������� ����� � ����������� ��� ����������
	protected void onDeveloperClick() {
		infoFrame.setLocation(
				frmTransavtoVasylenkoVladyslav.getLocation().x
						+ (frmTransavtoVasylenkoVladyslav.getWidth() - infoFrame.getWidth()) / 2,
				infoFrame.getLocation().y);
		infoFrame.setVisible(true);
	}

	// ������� ��� �������� ����� � ����������� ��� ��������
	protected void onTaskClick() {
		taskFrame.setLocation(
				frmTransavtoVasylenkoVladyslav.getLocation().x
						+ (frmTransavtoVasylenkoVladyslav.getWidth() - taskFrame.getWidth()) / 2,
				taskFrame.getLocation().y);
		taskFrame.setVisible(true);
	}

	private void doStopPlay() {
		player.stop();
	}
	
}
