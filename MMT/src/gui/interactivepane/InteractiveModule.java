package gui.interactivepane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListMap;

import defaults.MidiIOThrough;
import dev.MidiIOCommunicator;
import dev.hostevents.HostEvent;
import dev.hostevents.NewInputEvent;
import dev.hostevents.NewOutputEvent;
import dev.pluginevents.NewInputRequestEvent;
import dev.pluginevents.NewOutputRequestEvent;
import dev.pluginevents.PluginError;
import dev.pluginevents.PluginEvent;
import dev.pluginevents.PluginLoadingError;
import dev.pluginevents.PluginMidiProcessingError;
import dev.pluginevents.PluginSavingError;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import pluginhost.PluginHost;
import pluginhost.PluginStateChangedListener;
import stdlib.grouping.Groupable;
import stdlib.grouping.Grouping;
import controller.interactivepane.FullViewClosingListener;
import controller.interactivepane.InteractiveController;
import model.graph.Module;

public class InteractiveModule extends InteractiveComponent implements
		CablePointHost, PluginStateChangedListener, Groupable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6925557408863491387L;
	private Module module;
	private transient JFrame fullView;
	private boolean fullViewShowing;
	private JComponent contentPane;
	private InteractiveModuleHeader displayHeader;
	protected boolean inputPopoutActive, outputPopoutActive,
			inputPopoutPermanent, outputPopoutPermanent, closed;
	private Popout inputPopout, outputPopout;
	private CablePointSimple inputPopupConnector, outputPopupConnector;
	private InteractiveCable inputPopoutCable, outputPopoutCable;
	private ConcurrentSkipListMap<CablePointSimple, MidiIOThrough> inputMap;
	private ConcurrentSkipListMap<CablePointSimple, MidiIOThrough> outputMap;
	private transient TimerTask t;
	private transient DefaultMutableTreeNode tree;

	public InteractiveModule(Vector origin, Module module,
			InteractiveController controller) {
		super(controller, origin);
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		super.setLayout(new GridBagLayout());

		this.module = module;
		this.module.origin = origin;
		this.initView();
		inputPopupConnector = new CablePointSimple(CablePointType.INPUT);
		outputPopupConnector = new CablePointSimple(CablePointType.OUTPUT);
		this.inputPopout = new Popout(this.controller, this, this
				.getOriginLocation().addVector(new Vector(-60, 0)),
				CablePointType.INPUT);
		this.outputPopout = new Popout(this.controller, this, this
				.getOriginLocation().addVector(
						new Vector(this.getOriginDimension().width + 10, 0)),
				CablePointType.OUTPUT);
		this.inputMap = new ConcurrentSkipListMap<CablePointSimple, MidiIOThrough>();
		this.outputMap = new ConcurrentSkipListMap<CablePointSimple, MidiIOThrough>();
		inputPopoutCable = new InteractiveCable(inputPopout.connector,
				inputPopupConnector, 2, Color.BLUE, controller);
		outputPopoutCable = new InteractiveCable(outputPopout.connector,
				outputPopupConnector, 2, Color.MAGENTA, controller);

		this.fullViewShowing = false;
		this.module.setPluginStateChangedListener(this);

		this.tree = new DefaultMutableTreeNode(this);
		if (this.module.getPlugin() instanceof Grouping) {
			for (DefaultMutableTreeNode n : ((Grouping) this.module.getPlugin())
					.treeView())
				tree.add(n);
		}
		this.updateIO();
		this.updateView();

	}

	private void initView() {
		if (module.getPlugin() != null) {
			this.contentPane = module.getPlugin().getMinimizedView();

			this.setOriginDimension(new Dimension(200, 100));
			this.displayHeader = new InteractiveModuleHeader(this);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 0;
			displayHeader.setMinimumSize(new Dimension(0, 0));
			displayHeader.setPreferredSize(new Dimension(0, 0));
			displayHeader.setBackground(Color.CYAN);
			this.add(displayHeader, c);
			if (contentPane != null) {
				c.gridy = 1;
				c.weighty = 2;
				contentPane.setMinimumSize(new Dimension(0, 0));
				contentPane.setPreferredSize(new Dimension(0, 0));
				this.add(contentPane, c);
			}
			this.displayHeader.setText(this.getName());
		}
	}

	public MidiIOThrough getMidiIO(CablePoint tmp) {
		if (tmp.getType() == CablePointType.INPUT) {
			return this.inputMap.get(tmp);
		} else {
			return this.outputMap.get(tmp);
		}
	}

	public void updateIO() {
		updateInputs();
		updateOutputs();
	}

	public void resetIO() {
		this.inputMap.clear();
		this.outputMap.clear();
		this.inputPopout = new Popout(this.controller, this, this
				.getOriginLocation().addVector(new Vector(-60, 0)),
				CablePointType.INPUT);
		this.outputPopout = new Popout(this.controller, this, this
				.getOriginLocation().addVector(
						new Vector(this.getOriginDimension().width + 10, 0)),
				CablePointType.OUTPUT);
		updateIO();
	}

	public void setController(InteractiveController c) {
		boolean updatePopouts = this.inputPopout != null
				&& this.outputPopout != null && this.inputPopoutCable != null
				&& this.outputPopoutCable != null;
		if (updatePopouts) {
			this.inputPopout(false, this.inputPopoutPermanent);
			this.outputPopout(false, this.outputPopoutPermanent);
			super.setController(c);
			this.inputPopout.setController(c);
			this.outputPopout.setController(c);
			this.inputPopoutCable.setController(c);
			this.outputPopoutCable.setController(c);

		} else {
			super.setController(c);
		}

		if (updatePopouts) {
			this.inputPopout(this.inputPopoutPermanent,
					this.inputPopoutPermanent);
			this.outputPopout(this.outputPopoutPermanent,
					this.outputPopoutPermanent);
		}

	}

	private void updateInputs() {
		LinkedList<MidiIOCommunicator> inputs = module.getInputs();
		boolean newPoint = false;
		for (MidiIOCommunicator m : inputs) {
			if (!inputMap.containsValue(m)) {
				CablePointSimple cps = new CablePointSimple(
						CablePointType.INPUT);
				cps.setHost(this);
				cps.setIndex(inputs.indexOf(m));
				inputMap.put(cps, (MidiIOThrough) m);
				newPoint = true;
			}
		}
		if (newPoint) {
			this.inputPopout.updateIO();
		}
	}

	private void updateOutputs() {
		LinkedList<MidiIOCommunicator> outputs = module.getOutputs();
		boolean newPoint = false;
		for (MidiIOCommunicator m : outputs) {
			if (!outputMap.containsValue(m)) {
				CablePointSimple cps = new CablePointSimple(
						CablePointType.OUTPUT);
				cps.setHost(this);
				cps.setIndex(outputs.indexOf(m));
				outputMap.put(cps, (MidiIOThrough) m);
				newPoint = true;
			}
		}
		if (newPoint) {
			this.outputPopout.updateIO();
		}
	}

	public Module getModule() {
		return this.module;
	}

	@Override
	public void setHovered(boolean set) {
		super.setHovered(set);

		if (set) {
			this.setBorder(BorderFactory.createLineBorder(Color.black));

			if (t != null)
				t.cancel();
			Timer t = new Timer("ModuleTimer");
			this.t = new TimerTask() {
				@Override
				public void run() {
					if (!controller.getDragged()) {
						if (!inputPopoutActive && !closed) {
							inputPopout(true, inputPopoutPermanent);
						}
						if (!outputPopoutActive && !closed) {
							outputPopout(true, outputPopoutPermanent);
						}
					}
				}
			};
			t.schedule(this.t, 1000);

		} else {
			if (t != null)
				t.cancel();
			Timer t = new Timer("ModuleTimer");
			this.t = new TimerTask() {
				@Override
				public void run() {
					while (outputPopout.navigating()
							|| inputPopout.navigating()
							|| controller.getCableAddProcess()) {
						close();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					close();
				}

				private void close() {
					if (inputPopoutActive
							&& !closed
							&& !inputPopout.navigating()
							&& !inputPopout.isAncestorOf((Component) controller
									.getCableAddProcessSource())) {
						inputPopout(inputPopoutPermanent, inputPopoutPermanent);
					}
					if (outputPopoutActive
							&& !closed
							&& !outputPopout.navigating()
							&& !outputPopout
									.isAncestorOf((Component) controller
											.getCableAddProcessSource())) {
						outputPopout(outputPopoutPermanent,
								outputPopoutPermanent);

					}
				}
			};
			t.schedule(this.t, 1000);
			if (this.isSelected()) {
				this.setBorder(BorderFactory.createLineBorder(Color.red));
			} else {
				this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		}
	}

	@Override
	public void setSelected(boolean set) {
		super.setSelected(set);
		if (set)
			this.setBorder(BorderFactory.createLineBorder(Color.red));
		else
			this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}

	public boolean hasInputPopout() {
		return this.inputPopoutActive;
	}

	public void inputPopout(boolean set, boolean permanent) {
		if (!this.inputMap.isEmpty() || this.inputPopoutActive) {
			this.inputPopoutActive = set;
			this.inputPopoutPermanent = permanent;
			if (set) {
				if (!controller.getPane().isAncestorOf(this.inputPopout)) {
					controller.getPane().add(this.inputPopout);
					controller.getPane().addStatic(this.inputPopoutCable);
				}
			} else {
				if (controller.getPane().isAncestorOf(this.inputPopout)) {
					controller.getPane().removeStatic(this.inputPopoutCable);
					controller.getPane().remove(this.inputPopout);
				}

			}
		}
	}

	public boolean hasOutputPopout() {
		return this.outputPopoutActive;
	}

	public void outputPopout(boolean set, boolean permanent) {
		if (!this.outputMap.isEmpty() || this.outputPopoutActive) {
			this.outputPopoutActive = set;
			this.outputPopoutPermanent = permanent;
			if (set) {
				if (!controller.getPane().isAncestorOf(this.outputPopout)) {
					controller.getPane().add(this.outputPopout);
					controller.getPane().addStatic(this.outputPopoutCable);
				}
			} else {
				if (controller.getPane().isAncestorOf(this.outputPopout)) {
					controller.getPane().removeStatic(this.outputPopoutCable);
					controller.getPane().remove(this.outputPopout);
				}
			}
		}

	}

	public void openFullView() {
		if (this.module.getPlugin() instanceof Grouping) {
			this.controller.getProject().registerTab(
					((Grouping) this.module.getPlugin()).getController());

		} else {
			if (this.fullView == null) {
				JFrame frame = new JFrame();
				frame.setTitle(this.getName());
				JComponent view = this.module.getPlugin().getFullView();
				if (view != null) {
					frame.add(this.module.getPlugin().getFullView());
					frame.pack();
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					frame.addWindowListener(new FullViewClosingListener(this));
					this.fullView = frame;
				}

			}
			if (this.fullView != null) {
				this.fullView.setVisible(true);
				this.fullViewShowing = true;
			}
		}
	}

	public void closeFullView() {
		if (this.module.getPlugin() instanceof Grouping) {
			this.controller.getProject().unregisterTab(this);

		} else {
			if (this.fullView != null) {
				this.fullView.setVisible(false);
			}
			this.fullViewShowing = false;
		}
	}

	public String getName() {
		return this.module.getName();
	}

	public void setName(String name) {
		this.module.setName(name);
		this.displayHeader.setText(name);
		if (this.fullView != null) {
			this.fullView.setTitle(this.getName());
		}

		this.tree.setUserObject(this);
	}

	@Override
	public void updateView() {
		super.updateView();
		this.updateCablePoints();
	}

	@Override
	public void translateOriginLocation(Vector translation) {
		super.translateOriginLocation(translation);
		this.module.origin = super.getOriginLocation();
		if (!this.inputPopout.isSelected())
			this.inputPopout.translateOriginLocation(translation);
		if (!this.outputPopout.isSelected())
			this.outputPopout.translateOriginLocation(translation);
	}

	private void updateCablePoints() {
		if (this.isShowing()) {

			inputPopupConnector.setXOnScreen((int) this.getLocationOnScreen()
					.getX());
			inputPopupConnector.setYOnScreen((int) this.getLocationOnScreen()
					.getY() + this.getHeight() / 2);
			outputPopupConnector.setXOnScreen((int) (this.getLocationOnScreen()
					.getX() + this.getWidth()));
			outputPopupConnector.setYOnScreen((int) this.getLocationOnScreen()
					.getY() + this.getHeight() / 2);
			int i = 1;
			if (!this.inputPopoutActive) {
				Set<CablePointSimple> inputSet = this.inputMap.keySet();
				for (CablePointSimple cps : inputSet) {
					cps.setXOnScreen(this.getLocationOnScreen().x + 5);
					cps.setYOnScreen(this.getLocationOnScreen().y + i++
							* this.getHeight() / (1 + inputSet.size()));
				}
			}
			i = 1;
			if (!this.outputPopoutActive) {
				Set<CablePointSimple> outputSet = this.outputMap.keySet();
				for (CablePointSimple cps : outputSet) {
					cps.setXOnScreen(this.getLocationOnScreen().x
							+ this.getWidth() - 5);
					cps.setYOnScreen(this.getLocationOnScreen().y + i++
							* this.getHeight() / (1 + outputSet.size()));
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		this.updateCablePoints();
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints() {
		LinkedList<CablePointSimple> tmp = new LinkedList<CablePointSimple>();
		tmp.addAll(this.inputMap.keySet());
		tmp.addAll(this.outputMap.keySet());
		return tmp;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type) {
		LinkedList<CablePointSimple> tmp = new LinkedList<CablePointSimple>();
		if (type == CablePointType.INPUT) {
			tmp.addAll(this.inputMap.keySet());
		} else {
			tmp.addAll(this.outputMap.keySet());
		}
		return tmp;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type,
			int... indices) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CablePoint getCablePoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CablePoint getCablePoint(Point sourceInComponent) {

		CablePointType type = (sourceInComponent.x < this.getWidth() / 2) ? CablePointType.INPUT
				: CablePointType.OUTPUT;

		return this.getCablePoint(type);
	}

	@Override
	public CablePoint getCablePoint(CablePointType type, int index) {
		LinkedList<CablePointSimple> tmp = new LinkedList<CablePointSimple>();
		if (type == CablePointType.INPUT) {
			tmp.addAll(this.inputMap.keySet());
		} else {
			tmp.addAll(this.outputMap.keySet());
		}
		if (index >= tmp.size()) {
			return null;
		}
		return tmp.get(index);
	}

	@Override
	public CablePoint getCablePoint(CablePointType type) {
		Set<CablePointSimple> cpsSet;
		if (type == CablePointType.INPUT) {
			cpsSet = this.inputMap.keySet();
		} else {
			cpsSet = this.outputMap.keySet();
		}

		for (CablePointSimple cps : cpsSet) {
			if (!cps.isConnected()) {
				return cps;
			}
		}
		// no free slots aviable
		CablePointSimple cps = null;
		if (type == CablePointType.INPUT) {
			if (this.getModule().getPlugin().getMaxInputs() == -1
					|| cpsSet.size() < this.getModule().getPlugin()
							.getMaxInputs()) {

				MidiIOThrough tmp = this.getModule().newInput();
				for (Map.Entry<CablePointSimple, MidiIOThrough> entry : this.inputMap
						.entrySet()) {
					if (entry.getValue() == tmp) {
						cps = entry.getKey();
						cps.setHost(this);
						cps.setIndex(this.module.getInputs().indexOf(tmp));
						this.inputMap.put(cps, tmp);
						this.inputPopout.updateIO();
						break;
					}
				}
				this.updateCablePoints();
			}
		} else {
			if (this.getModule().getPlugin().getMaxOutputs() == -1
					|| cpsSet.size() < this.getModule().getPlugin()
							.getMaxOutputs()) {

				MidiIOThrough tmp = this.getModule().newOutput();
				for (Map.Entry<CablePointSimple, MidiIOThrough> entry : this.outputMap
						.entrySet()) {
					if (tmp == entry.getValue()) {
						cps = entry.getKey();
						cps.setHost(this);
						cps.setIndex(this.module.getOutputs().indexOf(tmp));
						this.outputMap.put(cps, tmp);
						this.outputPopout.updateIO();
						this.updateCablePoints();
						break;
					}
				}

			}
		}

		// max i/o rate achived => null

		return cps;
	}

	@Override
	public CablePoint getFreeCablePoint(CablePointType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean forceExistence(CablePoint... forceThis) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(CablePoint point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean close() {
		this.inputPopout(false, inputPopoutPermanent);
		this.outputPopout(false, outputPopoutPermanent);
		//TODO: Locality versus MVC
		this.getModule().close();
		this.closed = true;
		boolean tmp = this.fullViewShowing;
		this.closeFullView();
		this.fullViewShowing = tmp;
		return true;
	}

	@Override
	public boolean reopen() {
		this.inputPopout(inputPopoutPermanent, inputPopoutPermanent);
		this.outputPopout(outputPopoutPermanent, outputPopoutPermanent);
		//TODO: not MVC here, but more lokal behaviour.
		this.getModule().reOpen();
		this.closed = false;
		if (this.fullViewShowing) {
			this.openFullView();
		}
		//TODO: when implementing Error-Management, allow false openings
		return true;

	}

	private class Popout extends InteractiveComponent implements CablePoint {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2829655320069136959L;
		public CablePointSimple connector;
		private CablePointType type;

		public Popout(InteractiveController parent,
				InteractiveModule moduleDisplay, Vector origin,
				CablePointType type) {
			super(parent, origin);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.type = type;
			this.connector = new CablePointSimple(type);
		}

		public void updateIO() {
			this.removeAll();
			Set<CablePointSimple> cpsSet;
			if (type == CablePointType.INPUT) {
				cpsSet = inputMap.keySet();
			} else {
				cpsSet = outputMap.keySet();
			}
			CablePointPanel p;
			for (CablePointSimple cps : cpsSet) {
				p = new CablePointPanel(cps);
				if (cps.getIndex() % 2 == 0) {
					p.setBackground(Color.LIGHT_GRAY);
				} else {
					p.setBackground(Color.DARK_GRAY);
				}
				this.add(p);
			}
			this.setOriginDimension(new Dimension(51, cpsSet.size() * 51));
			this.updateView();
			this.revalidate();
		}

		@Override
		public boolean close() {
			if (this.type == CablePointType.INPUT) {
				inputPopout(false, inputPopoutPermanent);
			} else if (this.type == CablePointType.OUTPUT) {
				outputPopout(false, outputPopoutPermanent);
			}
			return false;
		}

		@Override
		public void updateView() {
			super.updateView();
			this.updateCablePoints();

		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			this.updateCablePoints();
		}

		private void updateCablePoints() {
			if (this.isShowing()) {
				connector.setYOnScreen(this.getLocationOnScreen().y
						+ this.getHeight() / 2);
				if (connector.getType() == CablePointType.OUTPUT) {
					connector.setXOnScreen(this.getLocationOnScreen().x);
				} else if (connector.getType() == CablePointType.INPUT) {
					connector.setXOnScreen(this.getLocationOnScreen().x
							+ this.getWidth());
				}
				for (Component p : this.getComponents()) {
					((CablePointPanel) p).updatePoint();
				}
			}
		}

		@Override
		public boolean reopen() {
			if (this.type == CablePointType.INPUT) {
				inputPopout(inputPopoutPermanent, inputPopoutPermanent);
			} else if (this.type == CablePointType.OUTPUT) {
				outputPopout(outputPopoutPermanent, outputPopoutPermanent);
			}
			return false;
		}

		@Override
		public int getXOnScreen() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getYOnScreen() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public InteractiveCable getCable() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCable(InteractiveCable cable) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isConnected() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CablePointType getType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setHost(CablePointHost host) {
			// TODO Auto-generated method stub

		}

		@Override
		public CablePointHost getHost() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void disconnect() {
			// TODO Auto-generated method stub

		}

		@Override
		public void setIndex(int i) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setHovered(boolean set) {
			super.setHovered(set);
			if (set) {
				this.setBorder(new LineBorder(Color.BLACK));
			} else {
				this.setBorder(new EmptyBorder(1, 1, 1, 1));

			}
		}

		@Override
		public int compareTo(CablePoint o) {
			return this.getIndex() - o.getIndex();
		}

		@Override
		public boolean getTmpDisconnect() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void tmpDisconnect(boolean set) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void changedState(CablePoint point) {
		controller.connectionModification(point,this.inputMap, this.outputMap);

	}

	@Override
	public void listen(HostEvent e) {
		if (e instanceof NewInputEvent) {
			if (!this.inputMap.containsValue(((NewInputEvent) e).getNewInput()))
				this.updateInputs();
		} else if (e instanceof NewOutputEvent) {
			if (!this.outputMap.containsValue(((NewOutputEvent) e)
					.getNewOutput()))
				this.updateOutputs();
		}
	}

	@Override
	public void listen(PluginEvent e) {
		if (e instanceof NewInputRequestEvent
				|| e instanceof NewOutputRequestEvent) {
			// normal event
			this.updateIO();
		} else {
			// error
			if (e.getClass() == PluginLoadingError.class) {

			} else if (e.getClass() == PluginSavingError.class) {

			} else if (e.getClass() == PluginMidiProcessingError.class) {

			} else if (e.getClass() == PluginError.class) {

			}
			// this.controller.addPluginError(this, e)
			System.out.println("Error: "
					+ (((PluginHost)((PluginError) e).getSourceHost()).getName()));
			((PluginError) e).getException().printStackTrace();

		}
	}

	public InteractiveModule clone() {
		return cloneTo(this.getOriginLocation(), this.getController());

	}

	public InteractiveModule cloneTo(Vector offset,
			InteractiveController controller) {
		Module m = this.module.clone();
		InteractiveModule tmp = new InteractiveModule(this.getOriginLocation()
				.addVector(offset), m, controller);
		return tmp;

	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		this.removeAll();
		oos.defaultWriteObject();
		this.initView();
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		this.module.setPluginStateChangedListener(this);
		this.removeAll();
		this.initView();
	}

	public DefaultMutableTreeNode treeView() {
		if (this.module.getPlugin() instanceof Grouping) {
			this.tree.removeAllChildren();
			for (DefaultMutableTreeNode n : ((Grouping) this.module.getPlugin())
					.treeView())
				tree.add(n);
		}
		return this.tree;
	}

	public TreePath treePath() {
		return new TreePath(this.tree.getPath());
	}

	public String toString() {
		return this.module.getName();
	}
}
