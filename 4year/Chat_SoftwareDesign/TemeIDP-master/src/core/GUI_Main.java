package core;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import buttons.PauseButton;
import buttons.ResumeButton;
import buttons.AddButton;
import buttons.StartButton;
import buttons.StopButton;
import radiobuttons.ReceiveRadioButton;
import radiobuttons.SendRadioButton;
import tables.P2PJTable;
import test.Test;
/**
 * Class for building the gui
 */
public class GUI_Main extends JFrame {
	
	private static final long serialVersionUID = -8118352433755008641L;
	private final int HEIGHT = 900;
	private final int WIDTH = 700;
	public GUI_Main(String currentUser) {
		super("P2P File Transfer");
		this.setSize(HEIGHT, WIDTH);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		/**
		 * add JLists/JTable for available users,files and transfers
		 */
		final JList userJList, fileJList;
		final P2PJTable transferJTable;
		int defaultJFrameHeight = WIDTH;
		int defaultJFrameWidth = HEIGHT;
		DefaultTableModel transfer_model;
		DefaultListModel user_model;
		final DefaultListModel files_model;
		final Mediator med;
		String col[] = { "Source", "Destination", "File Name", "Progress",
				"Status" };
		/**
		 * initialize mediator
		 */
		med = new Mediator();
		user_model = new DefaultListModel<>();
		files_model = new DefaultListModel<>();
		transfer_model = new DefaultTableModel(col, 0);
		userJList = new JList(user_model);
		fileJList = new JList(files_model);
		transferJTable = new P2PJTable(transfer_model);
		med.setFilesModel(files_model);
		med.setUserModel(user_model);
		med.setCurrentUser("user1");
		med.setTransferModel(transfer_model);
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

		JLabel statusJLabel = new JLabel("Work in progress...");
		getContentPane().add(statusJLabel, BorderLayout.SOUTH);
		/**
		 * add listeneres for userJList
		 */
		MouseListener userMouseListener = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				med.updateTransferSelectedUser(userJList.getSelectedValue()
						.toString());
			}
		};

		userJList.addMouseListener(userMouseListener);
		/**
		 * add listeners for fileJList
		 */
		MouseListener fileMouseListener = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				med.setFileValue(fileJList.getSelectedValue().toString());
			}
		};

		fileJList.addMouseListener(fileMouseListener);
		/**
		 * add listeners for send/receive RadioButtons
		 */
		ActionListener radioButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileJList.clearSelection();
				userJList.clearSelection();
				files_model.clear();
				((Command) e.getSource()).execute();

			}
		};

		SendRadioButton sendJRadioButton = new SendRadioButton("Send file",
				radioButtonListener, med);
		ReceiveRadioButton receiveJRadioButton = new ReceiveRadioButton(
				"Receive file", radioButtonListener, med);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(sendJRadioButton);
		buttonGroup.add(receiveJRadioButton);

		ActionListener buttonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileJList.clearSelection();
				userJList.clearSelection();
				((Command) e.getSource()).execute();
			}
		};
		/**
		 * transfer operations Buttons
		 */
		AddButton addButton = new AddButton("Add", buttonListener, med,
				new ImageIcon(GUI_Main.class.getResource("/res/AddIcon.png")));
		StartButton startButton = new StartButton("Start", buttonListener, med,
				new ImageIcon(
						GUI_Main.class.getResource("/res/Play1Normal.png")));
		StopButton stopButton = new StopButton("Stop", buttonListener, med,
				new ImageIcon(
						GUI_Main.class.getResource("/res/Stop1NormalBlue.png")));
		PauseButton pauseButton = new PauseButton("Pause", buttonListener, med,
				new ImageIcon(GUI_Main.class.getResource("/res/Pause.png")));
		ResumeButton resumeButton = new ResumeButton("Resume", buttonListener,
				med, new ImageIcon(
						GUI_Main.class
								.getResource("/res/StepForwardNormalBlue.png")));
		MouseListener tableMouseListener = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				med.setSelectedTransfer(transferJTable.getSelectedRow());
			}
		};
		transferJTable.addMouseListener(tableMouseListener);
		transferJTable.setRowSelectionAllowed(true);
		transferJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumn col_aux = transferJTable.getColumnModel().getColumn(3);
		col_aux.setCellRenderer(new ProgressCellRenderer());

		JPanel fileJPanel = new JPanel();
		JPanel transferJPanel = new JPanel();
		JPanel userJPanel = new JPanel();
		JPanel operationJPanel = new JPanel(new GridLayout(0, 2));
		JPanel buttonsJPanel = new JPanel();
		JPanel fileJPanelLabels = new JPanel(new GridLayout(2, 0));
		JPanel transferJPanelLabels = new JPanel(new GridLayout(1, 1));
		JPanel fileJPanelLabels2 = new JPanel(new GridLayout(2, 0));

		fileJPanel.setLayout(new BoxLayout(fileJPanel, BoxLayout.PAGE_AXIS)); // 1
																				// row,
																				// any
																				// number
																				// of
																				// columns
		fileJPanel.setMinimumSize(new Dimension(defaultJFrameWidth * 3 / 4,
				defaultJFrameHeight / 2 - 30));
		transferJPanel.setLayout(new BoxLayout(transferJPanel,
				BoxLayout.PAGE_AXIS));
		transferJPanel.setMinimumSize(new Dimension(defaultJFrameWidth * 3 / 4,
				defaultJFrameHeight / 2 - 30));
		userJPanel.setLayout(new BoxLayout(userJPanel, BoxLayout.PAGE_AXIS));
		userJPanel.add(new JLabel("Users"));
		userJPanel.add(new JScrollPane(userJList, v, h));
		userJPanel.setMinimumSize(new Dimension(defaultJFrameWidth / 4 - 100,
				defaultJFrameHeight));
		operationJPanel.add(sendJRadioButton);
		operationJPanel.add(receiveJRadioButton);
		buttonsJPanel.add(addButton);
		buttonsJPanel.add(startButton);
		buttonsJPanel.add(stopButton);
		buttonsJPanel.add(resumeButton);
		buttonsJPanel.add(pauseButton);
		transferJPanel.add(buttonsJPanel);
		fileJPanelLabels.add(new JLabel("Hello " + currentUser));
		fileJPanelLabels.add(new JLabel("Select current operation"));
		fileJPanelLabels2.add(new JLabel("Available files"));
		fileJPanel.add(fileJPanelLabels);
		fileJPanel.add(operationJPanel);
		fileJPanel.add(fileJPanelLabels2);
		fileJPanel.add(new JScrollPane(fileJList, v, h));
		transferJPanelLabels.add(new JLabel("Ongoing transfers"));
		transferJPanel.add(transferJPanelLabels);
		transferJPanel.add(new JScrollPane(transferJTable, v, h));

		JSplitPane vertJSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				fileJPanel, transferJPanel);
		JSplitPane horizJSplitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, vertJSplitPane, userJPanel);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(horizJSplitPane, BorderLayout.CENTER);

		this.setVisible(true);

		Test tester = new Test(med);
		tester.execute();
	}

	public static void main(String[] args) {
		new GUI_Main("user1");
	}

}
