import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Diese Klasse beinhaltet einerseits die Main-Methode andererseits ist sie ein
 * JPanel der in der Mainmethode in ein Fenster gepackt wird und den Synthesizer
 * samt Controls anzeigt
 * 
 * @author Hendrik Langebrake
 *
 */

@SuppressWarnings("serial")
public class SynthPanel extends JPanel implements KeyListener, ActionListener,
		ChangeListener {
	protected Synth synth;

	public static void main(String[] args) {
		// Wenn das Programm mit java SynthWindow ausgefuehrt wird soll:

		// ein Hauptfenster wird erzeugt
		JFrame mainFrame = new JFrame();
		// unser Synthesizer-Visualisierer wird als Panel erzeugt
		JPanel synthPanel = new SynthPanel();

		// ein Panel wird erzeugt, auf den nachher sauber der Synthesizerview
		// eingefuegt wird
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		// Synthesizer-Waveform-Darstellung wird auf Panel gefuegt
		contentPane.add(synthPanel, BorderLayout.CENTER);
		// Synthesizer-Control-Panel wird auf Panel gefuegt
		contentPane.add(((SynthPanel) synthPanel).createControlGui(),
				BorderLayout.SOUTH);
		mainFrame.setContentPane(contentPane);

		// Synthesizer wird aktualisiert, sodass korrekte Parameter eingestellt
		// sind
		((SynthPanel) synthPanel).updateSynthSettings();

		// Wenn das Hauptfenster schliesst soll das gesamte Programm enden
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Groesse des Fensters festlegen
		mainFrame.pack();

		// da SynthWindow das Interface KeyListener implementiert, koennen wir
		// das Objekt verwenden um auf Tastatureingaben zu reagieren
		mainFrame.addKeyListener((KeyListener) synthPanel);

		// Eigenschaften des Hauptfensters aendern
		mainFrame.setResizable(false);
		mainFrame
				.setTitle("UCABS (Unglaublich Cooler Additiver 8-Bit Synthesizer)");

		// mache Fenster sichtbar
		mainFrame.setVisible(true);

	}

	public SynthPanel() {
		// Wenn wir ein SynthWindow erzeugen wollen brauchen wir
		// einen Synthesizer
		synth = new Synth();
		// der soll gestartet werden
		synth.start();

		// einen Timer, der alle 100 Millisekunden die Methode actionPerformed()
		// an diesem Objekt (dem SynthWindow selber) aufruft, die dann das
		// Fenster neu zeichnet
		Timer T = new Timer(100, (ActionListener) this);
		T.start();

		// eine Vorgabe, wie gross das Visualisierungspanel sein soll
		this.setPreferredSize(new Dimension(1000, 2 * 127));
	}

	// Funktionen als KeyListener, damit Tastendruck wahrgenommen wird
	@Override
	public void keyPressed(KeyEvent arg0) {
		// Wenn eine Taste gedrueckt wurde....
		double frequency = 0;
		switch (arg0.getKeyCode()) {
		// ...ueberpruefe welche es war, ordne Frequenz zu
		case KeyEvent.VK_Y:
			frequency = 261.626 / 2;
			break; // C
		case KeyEvent.VK_S:
			frequency = 277.183 / 2;
			break; // Cis
		case KeyEvent.VK_X:
			frequency = 293.665 / 2;
			break; // D
		case KeyEvent.VK_D:
			frequency = 311.127 / 2;
			break; // Dis
		case KeyEvent.VK_C:
			frequency = 329.628 / 2;
			break; // E
		case KeyEvent.VK_V:
			frequency = 349.228 / 2;
			break; // F
		case KeyEvent.VK_G:
			frequency = 369.994 / 2;
			break; // Fis
		case KeyEvent.VK_B:
			frequency = 391.995 / 2;
			break; // G
		case KeyEvent.VK_H:
			frequency = 415.305 / 2;
			break; // Gis
		case KeyEvent.VK_N:
			frequency = 440.000 / 2;
			break; // A
		case KeyEvent.VK_J:
			frequency = 466.164 / 2;
			break; // Ais
		case KeyEvent.VK_M:
			frequency = 493.883 / 2;
			break; // H
		case KeyEvent.VK_Q:
			frequency = 261.626;
			break; // Klavier mittleres C
		case KeyEvent.VK_2:
			frequency = 277.183;
			break; // Cis
		case KeyEvent.VK_W:
			frequency = 293.665;
			break; // D
		case KeyEvent.VK_3:
			frequency = 311.127;
			break; // Dis
		case KeyEvent.VK_E:
			frequency = 329.628;
			break; // E
		case KeyEvent.VK_R:
			frequency = 349.228;
			break; // F
		case KeyEvent.VK_5:
			frequency = 369.994;
			break; // Fis
		case KeyEvent.VK_T:
			frequency = 391.995;
			break; // G
		case KeyEvent.VK_6:
			frequency = 415.305;
			break; // Gis
		case KeyEvent.VK_Z:
			frequency = 440.000;
			break;// A
		case KeyEvent.VK_7:
			frequency = 466.164;
			break; // Ais
		case KeyEvent.VK_U:
			frequency = 493.883;
			break; // H
		case KeyEvent.VK_I:
			frequency = 523.251;
			break; // C
		}
		;
		// starte diese Grundtonfrequenz
		synth.startFrequency(frequency);

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Wenn eine Taste losgelassen wurde....
		double frequency = 0;
		switch (arg0.getKeyCode()) {
		// ueberpruefe welche es war und ordne entsprechende Frequenz zu
		case KeyEvent.VK_Y:
			frequency = 261.626 / 2;
			break; // C
		case KeyEvent.VK_S:
			frequency = 277.183 / 2;
			break; // Cis
		case KeyEvent.VK_X:
			frequency = 293.665 / 2;
			break; // D
		case KeyEvent.VK_D:
			frequency = 311.127 / 2;
			break; // Dis
		case KeyEvent.VK_C:
			frequency = 329.628 / 2;
			break; // E
		case KeyEvent.VK_V:
			frequency = 349.228 / 2;
			break; // F
		case KeyEvent.VK_G:
			frequency = 369.994 / 2;
			break; // Fis
		case KeyEvent.VK_B:
			frequency = 391.995 / 2;
			break; // G
		case KeyEvent.VK_H:
			frequency = 415.305 / 2;
			break; // Gis
		case KeyEvent.VK_N:
			frequency = 440.000 / 2;
			break; // A
		case KeyEvent.VK_J:
			frequency = 466.164 / 2;
			break; // Ais
		case KeyEvent.VK_M:
			frequency = 493.883 / 2;
			break; // H
		case KeyEvent.VK_Q:
			frequency = 261.626;
			break; // Klavier mittleres C
		case KeyEvent.VK_2:
			frequency = 277.183;
			break; // Cis
		case KeyEvent.VK_W:
			frequency = 293.665;
			break; // D
		case KeyEvent.VK_3:
			frequency = 311.127;
			break; // Dis
		case KeyEvent.VK_E:
			frequency = 329.628;
			break; // E
		case KeyEvent.VK_R:
			frequency = 349.228;
			break; // F
		case KeyEvent.VK_5:
			frequency = 369.994;
			break; // Fis
		case KeyEvent.VK_T:
			frequency = 391.995;
			break; // G
		case KeyEvent.VK_6:
			frequency = 415.305;
			break; // Gis
		case KeyEvent.VK_Z:
			frequency = 440.000;
			break;// A
		case KeyEvent.VK_7:
			frequency = 466.164;
			break; // Ais
		case KeyEvent.VK_U:
			frequency = 493.883;
			break; // H
		case KeyEvent.VK_I:
			frequency = 523.251;
			break; // C
		}
		;
		// sende Stoppsignal fuer diese Grundtonfrequenz
		synth.stopFrequency(frequency);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Methode muss vorkommen da das Interface Keylistener implementiert
		// wird. Brauchen wir fuer
		// unser Programm aber nicht

	}

	// diese Methode wird aufgerufen, wenn der SynthWindow Panel zum ersten Mal
	// gezeichnet wird
	// oder wenn repaint() aufgerufen wird
	protected void paintComponent(Graphics g) {
		// muss immer aufgerufen werden, damit die vererbende Klasse ihr Fenster
		// zeichnen kann
		super.paintComponent(g);

		// in dieser Applikation weiss ich, dass die Grafikengine 2D ist
		Graphics2D g2d = (Graphics2D) g;

		// wir wollen die Koordinatenlinien gestrichelt haben
		float[] dash = { 10.0f };
		g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

		// Koordinatenlinie bei der die Lautsprecherauslenkung 0 ist wird
		// gezeichnet
		g2d.drawLine(0, 127, 1000, 127);
		// maximale Lautsprecherauslenkung wird eingezeichnet
		g2d.drawLine(0, 0, 1000, 0);
		// minimale wird eingezeichnet
		g2d.drawLine(0, 2 * 127, 1000, 2 * 127);

		// unser Auslenkungsdiagram wird als Polygon konzipiert
		Polygon p = new Polygon();
		// -> alle Punkte des Buffers werden dem Polygon hinzugefuegt
		for (int x = 0; x < synth.getDisplayBuffer().length; x++) {
			p.addPoint(x, 127 - synth.getDisplayBuffer()[x]);

		}

		// das Polygon wird fett und rot gezeichnet
		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawPolyline(p.xpoints, p.ypoints, p.npoints);

	}

	// Jedesmal, wenn der Timer seine Periode von 100 erreicht wird
	// actionPerformed
	// aufgerufen (da das SythWindow als ActionListener im Timer registriert
	// ist);
	// so kann alle paar (festgelegt im Timer) Millisekunden das Fenster mit
	// repaint()
	// neu aufgebaut werden
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();

	}

	// Hier werden fuer das Control-Panel alle wichtigen Schaltelemente
	// deklariert
	private JSlider loudnessSlider;

	private JLabel overtoneInfo;
	private JSlider overtoneCount;
	private JSlider overtoneStart;
	private JSlider overtoneIncrease;

	private JLabel vibratoInfo;
	private JSlider vibratoLength;
	private JSlider vibratoDepth;

	private JLabel deinFilterInfo;
	private JSlider deinFilterParam1;
	private JSlider deinFilterParam2;

	private JLabel arInfo;
	private JSlider arAttack;
	private JSlider arRelease;

	/**
	 * Diese Funktion baut das Control-Panel des Synthesizers auf Das
	 * Control-Panel wird seperat von der Anzeige generiert um spaeter eventuell
	 * flexibel die Controls an andere Stelle schieben zu koennen
	 * 
	 * @return
	 */
	JPanel createControlGui() {

		// Fenster werden geschachtelt aufgebaut:
		// man benoetigt ein Master-Panel auf dem alle sub-Panel eingefuegt
		// werden
		// Dies geht beliebig geschachtelt.
		// Wichtig ist, dass jedes Panel ein Layout bekommt, damit
		// bei Einfuegen von Sub-Elementen eine Regel besteht, wo dieses Element
		// eingefuegt werden soll (links, rechts, ...)

		// Controls - Master-Panel:
		JPanel controls = new JPanel();
		controls.setSize(1000, 200);
		controls.setBackground(Color.RED);
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));

		// GeneralControls - erster Sub-Panel:
		JPanel generalControls = new JPanel();
		generalControls.setLayout(new BorderLayout());
		generalControls.setBackground(Color.RED);
		// Slider, der auf Sub-Panel gefuegt werden soll
		loudnessSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		// Keylistener notwendig, da in Java der Keyfokus stets wechselt
		// zwischen Elementen
		// Kann schoener implementiert werden
		loudnessSlider.addKeyListener((KeyListener) this);
		loudnessSlider.addChangeListener(this);
		loudnessSlider.setBackground(Color.RED);
		// fuege Slider und Beschriftung auf Sub-Panel GeneralControls
		generalControls.add(new JLabel("Lautstaerke"), BorderLayout.NORTH);
		generalControls.add(loudnessSlider, BorderLayout.CENTER);
		// fuege GeneralControls Sub-Panel auf Master-Panel Controls
		controls.add(generalControls, FlowLayout.LEFT);

		// Oszillator-Controls - zweiter Sub-Panel
		JPanel oscControls = new JPanel();
		oscControls.setLayout(new BoxLayout(oscControls, BoxLayout.Y_AXIS));
		oscControls.setBackground(Color.RED);
		overtoneCount = new JSlider(JSlider.HORIZONTAL, 0, 40, 0);
		overtoneCount.addKeyListener(this);
		overtoneCount.addChangeListener(this);
		overtoneCount.setBackground(Color.RED);

		overtoneStart = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		overtoneStart.addKeyListener(this);
		overtoneStart.addChangeListener(this);
		overtoneStart.setBackground(Color.RED);

		overtoneIncrease = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
		overtoneIncrease.addKeyListener(this);
		overtoneIncrease.addChangeListener(this);
		overtoneIncrease.setBackground(Color.RED);

		overtoneInfo = new JLabel("");
		// Slider zu Sub-Panel hinzufuegen
		oscControls.add(overtoneInfo);
		oscControls.add(overtoneCount);
		oscControls.add(overtoneStart);
		oscControls.add(overtoneIncrease);
		// sub-Panel auf Master-Panel fuegen
		controls.add(oscControls);

		// Vibrato Controls
		JPanel vibControls = new JPanel();
		vibControls.setLayout(new BoxLayout(vibControls, BoxLayout.Y_AXIS));
		vibControls.setBackground(Color.RED);
		vibratoLength = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		vibratoLength.addKeyListener(this);
		vibratoLength.addChangeListener(this);
		vibratoLength.setBackground(Color.RED);

		vibratoDepth = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
		vibratoDepth.addKeyListener(this);
		vibratoDepth.addChangeListener(this);
		vibratoDepth.setBackground(Color.RED);

		vibratoInfo = new JLabel("");
		vibControls.add(vibratoInfo);
		vibControls.add(vibratoLength);
		vibControls.add(vibratoDepth);
		controls.add(vibControls);

		// AR controls
		JPanel arControls = new JPanel();
		arControls.setLayout(new BoxLayout(arControls, BoxLayout.Y_AXIS));
		arControls.setBackground(Color.RED);
		arAttack = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		arAttack.addKeyListener(this);
		arAttack.addChangeListener(this);
		arAttack.setBackground(Color.RED);

		arRelease = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		arRelease.addKeyListener(this);
		arRelease.addChangeListener(this);
		arRelease.setBackground(Color.RED);

		arInfo = new JLabel("");
		arControls.add(arInfo);
		arControls.add(arAttack);
		arControls.add(arRelease);
		controls.add(arControls);

		// DeinFilter controls
		JPanel deinFilterControls = new JPanel();
		deinFilterControls.setLayout(new BoxLayout(deinFilterControls, BoxLayout.Y_AXIS));
		deinFilterControls.setBackground(Color.RED);
		deinFilterParam1 = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		deinFilterParam1.addKeyListener(this);
		deinFilterParam1.addChangeListener(this);
		deinFilterParam1.setBackground(Color.RED);

		deinFilterParam2 = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		deinFilterParam2.addKeyListener(this);
		deinFilterParam2.addChangeListener(this);
		deinFilterParam2.setBackground(Color.RED);

		deinFilterInfo = new JLabel("");
		deinFilterControls.add(deinFilterInfo);
		deinFilterControls.add(deinFilterParam1);
		deinFilterControls.add(deinFilterParam2);
		controls.add(deinFilterControls);

		// an dieser Stelle ist der Master-Panel komplett aufgebaut.
		// Hier wird noch die gewuenschte Anzeigegroesse gesetzt
		controls.setPreferredSize(new Dimension(1000, 100));

		return controls;
	}

	/**
	 * wird aufgerufen, wenn sich ein Slider veraendert hat, aktualisiert dann
	 * die Einstellungen im Synthesizer und die Beschriftungen in dem Fenster
	 */
	void updateSynthSettings() {
		synth.setLoudness((byte) ((float) loudnessSlider.getValue() / 100 * 60));
		synth.setOscillator(overtoneStart.getValue(), overtoneCount.getValue(),
				overtoneIncrease.getValue());
		overtoneInfo.setText("Obertoene: Anzahl (" + overtoneCount.getValue()
				+ ")," + "Start(" + overtoneStart.getValue() + "),"
				+ "Inkrement(" + overtoneIncrease.getValue() + ")");
		vibratoInfo.setText("Vibrato: Tiefe (" + vibratoDepth.getValue() + "),"
				+ "Laenge: (" + vibratoLength.getValue() * 10 + "ms),");
		synth.setVibrato(vibratoLength.getValue() * 10,
				12 - vibratoDepth.getValue());
		deinFilterInfo.setText("Dein Filter: Param1 ("
				+ deinFilterParam1.getValue() + ") Param2 ("
				+ deinFilterParam2.getValue() + ")");
		synth.setDeinFilter((byte) deinFilterParam1.getValue(),
				(byte) deinFilterParam2.getValue());
		arInfo.setText("AR: Attack (" + arAttack.getValue() * 10
				+ "ms) Release (" + arRelease.getValue() * 10 + "ms)");
		synth.setAR(arAttack.getValue() * 10, arRelease.getValue() * 10);
	}

	/*
	 * Wenn ein Slider bewegt wird, dann wird diese Methode aufgerufen
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		// dann soll natuerlich der Synthesizer aktualisiert werden
		updateSynthSettings();

	}

}
