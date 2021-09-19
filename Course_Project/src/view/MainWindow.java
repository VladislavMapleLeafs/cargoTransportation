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
	// Створення URL для зображень та створення самих зображень
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
	// Список вантажівок
	List<Truck> trucks = Collections.synchronizedList(new ArrayList<Truck>());
	// Об'єкт класу митниця
	Customs customs = new Customs();
	// Об'єкт класу денний цикл
	Day day = new Day();
	// Два об'єкти класу бригада
	Brigade brigade1 = new Brigade();
	Brigade brigade2 = new Brigade();
	// Об'єкт класу Player (для програвання музики)
	protected Player player;
	// Булева змінна, що вказує що жодна вантажівка не їде від початкової точки до
	// митниці
	boolean noRides = true;
	// Булева змінна, що вказує що жодна вантажівка не їде від митниці до бригади
	boolean noRides2 = true;
	// Булева змінна, що вказує чи зайнята в даний час митниця
	boolean customIsBusy = false;
	// Змінна, що вказує на кількість всіх доставлених ящиків
	int allBoxCount = 0;
	// Об'єкти для синхронізації
	Object monitor = new Object();
	Object monitor2 = new Object();
	Object monitor3 = new Object();
	Object monitor4 = new Object();
	Object monitor5 = new Object();
	// Дві змінні, що відповідають за час розвантаження однієї вантажівки кожною з
	// бригад
	long brigadeTime1;
	long brigadeTime2;
	// Дві булеві змінні, що вказують яка з бригад використовується в даному потоці
	boolean brigadeUse1;
	boolean brigadeUse2;
	// Булева змінна, що вказує на те, що активована кнопка Stop
	boolean stop = false;
	// Булева змінна, що дозволяє правильно реалізувати афінне перетворення
	boolean firstSun = true;
	// Змінна для афінного перетворення
	AffineTransform transform = new AffineTransform();
	// Змінна, яка встановлює значення максимально можливої кількості ящиків, які перевозитиме вантажівка
	int maxBoxCount;
	// Дві змінні які встановлюють значення місткості черг митниці та вантажівок відповідно
	int customsQueueCount;
	int trucksQueueCount;

	{
		try {
			// Завантажуємо наші зображення
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
	// Панель для зображення митниці і руху вантажівки
	private JPanel firstTruckPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// Масштабуємо два зображення, перше з яких відповідає за звичайну вантажівку, а
			// друге за перевернуту
			// (друге зображення використовується при розвантаженні вантажівки та коли вона
			// повертається назад)
			scaledImgTruck = imgTruck.getScaledInstance(getWidth() / 6, getHeight() / 10 * 3, Image.SCALE_SMOOTH);
			scaledImgInvertTruck = imgInvertedTruck.getScaledInstance(getWidth() / 6, getHeight() / 10 * 3,
					Image.SCALE_SMOOTH);
			// Масштабуємо два зображення митниць, перше з яких позначає затримку
			// вантажівки, друге - пропуск вантажівки
			if (customs.getRed()) {
				scaledImgCustoms = imgCusRed.getScaledInstance(getWidth() / 5, getHeight() / 2, Image.SCALE_SMOOTH);
			} else {
				scaledImgCustoms = imgCusGreen.getScaledInstance(getWidth() / 5, getHeight() / 2, Image.SCALE_SMOOTH);
			}
			// Малюємо митницю
			g2d.drawImage(scaledImgCustoms, (getWidth() - scaledImgCustoms.getWidth(firstTruckPanel)) / 2,
					getHeight() / 20, this);
			for (int i = 0; i < trucks.size(); i++) {
				// Якщо вантажівка ще не почала розвантажуватися, малюємо звичайну вантажівку
				if (!(trucks.get(i).getArrived())) {
					g2d.drawImage(scaledImgTruck, trucks.get(i).getX(), trucks.get(i).getY(), this);
				} else {
					// Інакше малюємо перевернуту вантажівку
					g2d.drawImage(scaledImgInvertTruck, trucks.get(i).getX(), trucks.get(i).getY(), this);
				}
			}
		}
	};
	// Панель для зображення руху сонця чи місяця
	private JPanel dayPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// Масштабуємо два зображення, перше відповідає за сонце, друге за місяць
			if (day.getSun()) {
				// Якщо зараз день то малюємо сонце
				scaledImgDay = imgSun.getScaledInstance(getWidth() / 15, getHeight(), Image.SCALE_SMOOTH);
			} else {
				// Інакше малюємо місяць
				scaledImgDay = imgMoon.getScaledInstance(getWidth() / 14, getHeight(), Image.SCALE_SMOOTH);
			}
			if (firstSun) {
				// Перше зміщення малюнка на початку програми за допомоги афінного перетворення
				transform.translate(-scaledImgDay.getWidth(dayPanel), 0);
				day.setX(-scaledImgDay.getWidth(dayPanel));
			}
			firstSun = false;
			// Малюємо сонце чи місяць
			g2d.drawImage(scaledImgDay, transform, this);
		}
	};
	// Панель для зображення роботи першої бригади
	private JPanel firstForemanPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// Масштабуємо чотири зображення
			if (brigade1.getCalm()) {
				// Перше відповідає за бригадира в стані спокою
				scaledImgBrigade = imgCalmGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade1.getState() == 1) {
				// Наступні відповідають за зображення послідовних рухів бригадира при
				// перемещенні ящика
				scaledImgBrigade = imgLeftGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade1.getState() == 2) {
				scaledImgBrigade = imgMiddleGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade1.getState() == 3) {
				scaledImgBrigade = imgRightGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			}
			// Малюємо бригадира
			g2d.drawImage(scaledImgBrigade, 0, 0, this);
		}
	};
	// Панель для зображення роботи другої бригади
	private JPanel secondForemanPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// Масштабуємо чотири зображення
			if (brigade2.getCalm()) {
				// Перше відповідає за бригадира в стані спокою
				scaledImgBrigade = imgCalmGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade2.getState() == 1) {
				// Наступні відповідають за зображення послідовних рухів бригадира при
				// перемещенні ящика
				scaledImgBrigade = imgLeftGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade2.getState() == 2) {
				scaledImgBrigade = imgMiddleGuy.getScaledInstance(getWidth() / 3 * 2, getHeight(), Image.SCALE_SMOOTH);
			} else if (brigade2.getState() == 3) {
				scaledImgBrigade = imgRightGuy.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			}
			// Малюємо бригадира
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
				// Програвання музики
				playMusic();
				// Кнопка старт не може бути активована
				startButton.setEnabled(false);
				// Кнопка стоп не активована
				stop = false;
				
				// Перевірка правильного введення змінної, яка встановлює значення
				// максимально можливої кількості ящиків, які перевозитиме вантажівка
				try {
					maxBoxCount = Integer.parseInt(textFieldBox.getText());
				} catch (Exception e) {
					// Якщо змінна введена неправильно, то привласнюємо їй значення 0 
					maxBoxCount = 0;
				}
				// Перевірка правильного введення двох змінних для встановлення
				// значень місткості черг митниці та вантажівок відповідно
				try {
					customsQueueCount = Integer.parseInt(textFieldCustomsQueue.getText());
				} catch (Exception e) {
					// Якщо змінна введена неправильно, то привласнюємо їй значення 0  
					customsQueueCount = 0;
				}
				try {
					trucksQueueCount = Integer.parseInt(textFieldTruckQueue.getText());
				} catch (Exception e) {
					// Якщо змінна введена неправильно, то привласнюємо їй значення 0 
					trucksQueueCount = 0;
				}
				// Якщо якась змінна не підпадає під потрібний діапазон значень, то встановлюємо значення змінної 5 за замовчуванням
				// В нашому випадку максимальна кількість ящиків і черга митниці будуть мати діапазон значень від 1 до 20
				// а черга вантажівок буде мати діапазон значень від 2 до 20 (щоб другий бригадир не простоював даремно)
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
				// Тепер значення змінних, які відповідають за максимальну кількість ящиків 
				// та місткіть черги неможливо змінювати
				textFieldBox.setEnabled(false);
				textFieldCustomsQueue.setEnabled(false);
				textFieldTruckQueue.setEnabled(false);
				
				//Змінюємо максимальне значення відповідних слайдерів та перемальовуюмо їхні мітки 
				customsQueue.setLabelTable(null);
				customsQueue.setMajorTickSpacing(((customsQueueCount - 1) / 10) + 1);
				customsQueue.setMaximum(customsQueueCount);
							
				trucksQueue.setLabelTable(null);
				trucksQueue.setMajorTickSpacing(((trucksQueueCount - 1) / 10) + 1);
				trucksQueue.setMaximum(trucksQueueCount);		
				
				new Thread(() -> {

					while (true && !stop) {
						// Якщо сонце чи місяць зайшли за горизонт
						if (day.getX() > dayPanel.getWidth()) {
							// То змістити об'єкт за ліву частину фрейму
							transform.translate(-dayPanel.getWidth() - scaledImgDay.getWidth(dayPanel), 0);
							// Оновлення координати об'єкту
							day.setX(-scaledImgDay.getWidth(dayPanel));
							dayPanel.repaint();
							// Поміняти сонце на місяць чи навпаки (змінити частину дня)
							day.setSun(!day.getSun());
						}
						// Затримка
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// Послідовне переміщення об'єкта по панелі за допомоги афінних перетворень
						transform.translate(daySpeedSlider.getValue(), 0);
						// Оновлення координати об'єкту
						day.setX(day.getX() + daySpeedSlider.getValue());
						dayPanel.repaint();
					}
				}).start();

				new Thread(() -> {
					while (true) {
						try {
							// Затримка в залежності від того, день зараз чи ніч, затримка змінюється на 20%
							// від значення відповідного слайдера
							if (day.getSun()) {
								Thread.sleep((long) (0.4 * Math.random() * createTruckSlider.getValue()
										+ createTruckSlider.getValue() * 0.8));
							} else {
								// Якщо зараз ніч, то створення вантажівки в середньому буде займати удвічі
								// більше часу
								Thread.sleep((long) (2 * (0.4 * Math.random() * createTruckSlider.getValue()
										+ createTruckSlider.getValue() * 0.8)));
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// Перевірка чи рухається зараз якась вантажівка від початкової точки до митниці
						noRides = true;
						for (int i = 0; i < trucks.size(); i++) {
							if (trucks.get(i).getRides()) {
								// Якщо хоч одна вантажівка рухається, змінній noRides привласнимо значення
								// false
								noRides = false;
							}
						}
						// Якщо в черзі митниці є місце і жодна вантажівка не рухається від початкової
						// точки до митниці і не активована кнопка стоп
						if (customsQueue.getValue() < customsQueue.getMaximum() && noRides && !stop) {
							new Thread(() -> {
								// Створюємо нову вантажівку
								Truck truck = new Truck();
								// Генеруємо кількість ящиків, які перевозитиме вантажівка (від 1 до 5)
								truck.setBoxCount((int) (Math.random() * maxBoxCount + 1));
								// Підбираємо потрібну висоту для зображення вантажівки
								truck.setY(
										firstTruckPanel.getHeight() / 20 + scaledImgCustoms.getHeight(firstTruckPanel)
												- scaledImgTruck.getHeight(firstTruckPanel));
								firstTruckPanel.repaint();
								// Додаємо вантажівку в список вантажівок
								trucks.add(truck);
								// Та привласнимо значення булевої змінної rides true (тобто вантажівка починає
								// рухається від початкової точки до митниці)
								truck.setRides(true);
								// Поки вантажівка не доїхала до бригад
								while (truck.getX()
										+ scaledImgTruck.getWidth(firstTruckPanel) < firstForemanPanel.getX() - 5) {
									// Якщо вантажівка доїхала до митниці, але митниця її ще не пропустила
									if (truck.getX()
											+ scaledImgTruck.getWidth(firstTruckPanel) >= (firstTruckPanel.getWidth()
													- scaledImgCustoms.getWidth(firstTruckPanel)) / 2 - 5
											&& truck.getX() + scaledImgTruck
													.getWidth(firstTruckPanel) < (firstTruckPanel.getWidth()
															+ scaledImgCustoms.getWidth(firstTruckPanel)) / 2) {
										try {
											// То у випадку якщо вантажівка тільки приїхала до митниці
											if (truck.getRides()) {
												// Збільшуємо значення черги митниці на 1
												customsQueue.setValue(customsQueue.getValue() + 1);
												// змінній rides привласнимо значення false (тобто вантажівка припиняє
												// рух від початкової точки до митниці)
												truck.setRides(false);
											}
											synchronized (monitor) {
												// Перевірка чи рухається зараз якась вантажівка від митниці до бригад
												noRides2 = true;
												for (int i = 0; i < trucks.size(); i++) {
													if (trucks.get(i).getRides2()) {
														// Якщо хоч одна вантажівка рухається, змінній noRides2
														// привласнимо значення false
														noRides2 = false;
													}
												}
												// Якщо митниця не зайнята і є вільне місце в черзі вантажівок і жодна
												// вантажівка не рухається від митниці до бригад
												if (!customIsBusy && trucksQueue.getValue() < trucksQueue.getMaximum()
														&& noRides2) {
													// Змінній inCustom привласнимо значення true (вантажівка почала
													// перевірку в митниці)
													truck.setInCustom(true);
													// Митниця зайнята
													customIsBusy = true;
													// Затримка на перевірку в митниці, затримка змінюється на 20% від
													// значення відповідного слайдера
													Thread.sleep(
															(long) (0.4 * Math.random() * customsPassSlider.getValue()
																	+ customsPassSlider.getValue() * 0.8 - 800));
													// Митниця подає зелений сигнал, тобто зараз вантажівку буде
													// пропущено
													customs.setRed(false);
													firstTruckPanel.repaint();
													Thread.sleep(800);
													// Затримка для зображення зеленого сигналу
													customs.setRed(true);
													// Митниця подає червоний сигнал
													firstTruckPanel.repaint();
													// Вантажівка розташовується по правий бік від митниці
													truck.setX((firstTruckPanel.getWidth()
															+ scaledImgCustoms.getWidth(firstTruckPanel)) / 2);
													// Черга митниці зменшується на 1
													customsQueue.setValue(customsQueue.getValue() - 1);
													firstTruckPanel.repaint();
													// Митниця вільна
													customIsBusy = false;
												} else {
													// Інакше чекати звільнення митниці
													continue;
												}
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
									try {
										if (truck.getInCustom()) {
											// Якщо вантажівка вже пройшла перевірку в митниці, то позначити що вона їде
											// від митниці до бригад,
											// привласнивши булевій змінній rides2 значення true
											truck.setRides2(true);
										}
										// Затримка
										Thread.sleep(30);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									// Зміщення вантажівки, розмір якого залежить від значення відповідного слайдера
									truck.setX(truck.getX() + truckSpeedSlider.getValue());
									firstTruckPanel.repaint();

								}
								// Черга вантажівок збільшується на 1
								trucksQueue.setValue(trucksQueue.getValue() + 1);
								// Позначити що вантажівка вже не їде від митниці до бригад,
								// привласнивши булевій змінній rides2 значення false
								truck.setRides2(false);
								brigadeUse1 = false;
								brigadeUse2 = false;
								while (true) {
									synchronized (monitor2) {
										// Якщо хоч один з бригадирів не працює
										if (brigade1.getCalm() || brigade2.getCalm()) {
											// Повернути вантажівку
											truck.setArrived(true);
											firstTruckPanel.repaint();
											// Якщо перша бригада не працює
											if (brigade1.getCalm()) {
												// То визначити значення затримки на переміщення одного ящика для першої
												// бригади
												brigadeTime1 = (long) (brigadeTimeSlider.getValue() * 0.8
														+ Math.random() * brigadeTimeSlider.getValue() * 0.4);
												// Передати бригаді значення кількості ящиків, які потрібно розвантажити
												brigade1.setBoxCount(truck.getBoxCount());
												// Показати, що в даному потоці використовується перша бригада
												brigadeUse1 = true;
												// Перша бригада починає працювати
												brigade1.setCalm(false);
												// Перервати цикл
												break;
											} else if (brigade2.getCalm()) {
												// То визначити значення затримки на переміщення одного ящика для другої
												// бригади
												brigadeTime2 = (long) (brigadeTimeSlider.getValue() * 0.8
														+ Math.random() * brigadeTimeSlider.getValue() * 0.4);
												// Передати бригаді значення кількості ящиків, які потрібно розвантажити
												brigade2.setBoxCount(truck.getBoxCount());
												// Показати, що в даному потоці використовується друга бригада
												brigadeUse2 = true;
												// Друга бригада починає працювати
												brigade2.setCalm(false);
												// Перервати цикл
												break;
											}
										}
									}
								}
								// Визначити затримку для вантажівки в залежності від того, яка з бригад
								// займається розвантаженням
								try {
									if (brigadeUse1) {
										Thread.sleep(brigadeTime1 * truck.getBoxCount() + 10);
									} else if (brigadeUse2) {
										Thread.sleep(brigadeTime2 * truck.getBoxCount() + 10);
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// Черга вантажівок зменшується на 1
								trucksQueue.setValue(trucksQueue.getValue() - 1);
								// Зміщення вантажівки вниз для початку руху назад після успішного розвантаження
								truck.setY(
										firstTruckPanel.getHeight() - scaledImgInvertTruck.getHeight(firstTruckPanel));
								// Поки вантажівка не покинула межі фрейму
								while (truck.getX() + scaledImgInvertTruck.getWidth(firstTruckPanel) >= 0) {
									try {
										// Затримка
										Thread.sleep(30);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									// Зміщення вантажівки
									truck.setX(truck.getX() - truckSpeedSlider.getValue());
									firstTruckPanel.repaint();
								}
								// Видалення вантажівки зі списку вантажівок
								trucks.remove(truck);
							}).start();
						}
					}
				}).start();

				new Thread(() -> {
					while (true) {
						try {
							// Невелика затримка для правильної роботи потоку
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// Якщо бригада не в стані спокою (має працювати)
						synchronized (monitor4) {

							if (!brigade1.getCalm()) {
								try {
									// Поки не розвантажені всі ящики
									while (brigade1.getBoxCount() > 0) {
										firstTruckLabel
												.setText("К-сть ящиків у вантажівці(1):" + brigade1.getBoxCount());
										// Послідовна зміна станів бригадира
										brigade1.setState(1);
										firstForemanPanel.repaint();
										Thread.sleep(brigadeTime1 / 3);
										brigade1.setState(2);
										firstForemanPanel.repaint();
										Thread.sleep(brigadeTime1 / 3);
										brigade1.setState(3);
										firstForemanPanel.repaint();
										Thread.sleep(brigadeTime1 / 3);
										// Ящик розвантажено, кількість ящиків які треба розвантажити зменшується на 1
										brigade1.setBoxCount(brigade1.getBoxCount() - 1);
										// Кількість всього доставлених ящиків зростає на 1
										allBoxCount++;
										boxCountLabel.setText("Всього ящиків доставлено:" + allBoxCount);
									}
									firstTruckLabel.setText("К-сть ящиків у вантажівці(1):" + brigade1.getBoxCount());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// Бригада повертається в стан спокою
								brigade1.setCalm(true);
								firstForemanPanel.repaint();
							}
						}
					}
				}).start();

				new Thread(() -> {
					while (true) {
						try {
							// Невелика затримка для правильної роботи потоку
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// Якщо бригада не в стані спокою (має працювати)
						synchronized (monitor5) {
							if (!brigade2.getCalm()) {
								try {
									while (brigade2.getBoxCount() > 0) {
										secondTruckLabel
												.setText("К-сть ящиків у вантажівці(2):" + brigade2.getBoxCount());
										// Послідовна зміна станів бригадира
										brigade2.setState(1);
										secondForemanPanel.repaint();
										Thread.sleep(brigadeTime2 / 3);
										brigade2.setState(2);
										secondForemanPanel.repaint();
										Thread.sleep(brigadeTime2 / 3);
										brigade2.setState(3);
										secondForemanPanel.repaint();
										Thread.sleep(brigadeTime2 / 3);
										// Ящик розвантажено, кількість ящиків які треба розвантажити зменшується на 1
										brigade2.setBoxCount(brigade2.getBoxCount() - 1);
										// Кількість всього доставлених ящиків зростає на 1
										allBoxCount++;
										boxCountLabel.setText("Всього ящиків доставлено:" + allBoxCount);
									}
									secondTruckLabel.setText("К-сть ящиків у вантажівці(2):" + brigade2.getBoxCount());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// Бригада повертається в стан спокою
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
				// Активована кнопка стоп
				stop = true;
				// Зупинка програвання музики
				doStopPlay();
				// Чекаємо завершення потоку в методі onEndOfPlay
				Thread t1 = onEndOfPlay();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							t1.join();
							// Кнопка старт та текстові поля активуються
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
				// Виведення фрейму з інформацією про розробника
				onDeveloperClick();
			}
		});
		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Task");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Виведення фрейму з інформацією про завдання
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
					// Програвання музики
					player.play();
					// Після закінчення програвання активування кнопки стоп
					stop = true;
					// Чекаємо завершення потоку в методі onEndOfPlay
					Thread t2 = onEndOfPlay();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								t2.join();
								// Кнопка старт та текстові поля активуються
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
			// Чекаємо поки список вантажівок стане пустим (на фреймі не буде жодної
			// вантажівки)
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

	// Функція яка виводить фрейм з інформацією про розробника
	protected void onDeveloperClick() {
		infoFrame.setLocation(
				frmTransavtoVasylenkoVladyslav.getLocation().x
						+ (frmTransavtoVasylenkoVladyslav.getWidth() - infoFrame.getWidth()) / 2,
				infoFrame.getLocation().y);
		infoFrame.setVisible(true);
	}

	// Функція яка виводить фрейм з інформацією про завдання
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
