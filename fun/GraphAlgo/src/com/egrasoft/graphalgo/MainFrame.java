package com.egrasoft.graphalgo;

import com.egrasoft.graphalgo.components.GraphComponentFactorySelector;
import com.egrasoft.graphalgo.components.GraphComponentUI;
import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.components.edges.EdgeFactorySelector;
import com.egrasoft.graphalgo.components.edges.EdgeUI;
import com.egrasoft.graphalgo.components.nodes.Node;
import com.egrasoft.graphalgo.components.nodes.NodeFactorySelector;
import com.egrasoft.graphalgo.components.nodes.NodeUI;
import com.egrasoft.graphalgo.listeners.ClickAdapter;
import com.egrasoft.graphalgo.listeners.DragAdapter;
import com.egrasoft.graphalgo.listeners.TypeAdapter;
import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.tools.SafeCollectionWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.egrasoft.graphalgo.controls.ControlAction.*;
import static com.egrasoft.graphalgo.controls.ControlActionHotkey.Constants.*;

/**
 * Frame class for GraphAlgo project.
 */
public class MainFrame extends JFrame{


    /*==========================Fields==========================*/


    /**
     * String bundle object.
     */
    private static ResourceBundle strings = ResourceBundle.getBundle("StringResources");

    /**
     * Panel object handling a GUI graph representation.
     */
    private GraphPanel gpanel = new GraphPanel();

    /**
     * Object handling a physical graph representation.
     */
    private Graph graph = new Graph();

    /**
     * Toolbox object.
     */
    private GraphToolbox toolbox = new GraphToolbox();

    /**
     * Frame size dimension.
     */
    private Dimension frameSize = new Dimension(800, 600);

    /**
     * Height of the bottom control panel.
     */
    private int bottomPanelHeight = 100;

    /**
     * Color of the bottom control panel.
     */
    private Color bottomPanelColor = new Color(221, 221, 221);


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     */
    private MainFrame(){
        super(strings.getString("heading"));
        initFrameAndComponents();
        setVisible(true);
    }


    /*======================Private Methods======================*/


    /**
     * Method setting the frame and its components.
     */
    private void initFrameAndComponents(){
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenRes.width-frameSize.width)/2, (screenRes.height-frameSize.height)/2, frameSize.width, frameSize.height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu algoMenu = new JMenu(strings.getString("menuAlgorithms"));
        menuBar.add(algoMenu);
        JMenu editMenu = new JMenu(strings.getString("menuEdit"));
        menuBar.add(editMenu);
        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.setBackground(bottomPanelColor);
        bottomPanel.setPreferredSize(new Dimension(0, bottomPanelHeight));

        mainPanel.add(gpanel, BorderLayout.CENTER);
        mainPanel.add(toolbox, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }


    /*======================Public Methods======================*/


    /**
     * Project's main method.
     * @param args command line args
     */
    public static void main(String[] args){ SwingUtilities.invokeLater(MainFrame::new); }

    public static ResourceBundle getResourceBundle(){ return strings; }


    /*======================Nested Classes======================*/


    /**
     * Panel class providing methods for graph constructing and
     * event handling.
     */
    private class GraphPanel extends JPanel{


        /*==========================Fields==========================*/


        /**
         * Graph component GUI representation being selected at the moment
         * (or null if not).
         */
        private GraphComponentUI selected;

        /**
         * Half a size of the square, which is used to determine
         * if mouse event caught any graph component.
         */
        private int searchingSize = 4;

        /**
         * A color of this panel in the edit mode.
         */
        private Color fieldEditColor = new Color(245, 255, 245);

        /**
         * Stroke of the line connecting two nodes.
         */
        private Stroke connectStroke = new BasicStroke(1);

        /**
         * Color of the line connecting two nodes.
         */
        private Color connectColor = new Color(0,0,0);

        /**
         * Object, used to handle key events. Overrides {@link KeyAdapter#keyPressed(KeyEvent)} method.
         */
        private TypeAdapter keyAdapter = new TypeAdapter() {
            @Override
            protected int getSubmask(){
                int sub = (selected != null)? SELECTION_SET : SELECTION_UNSET;
                return KEY_EVENT | sub;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int submask = getSubmask();

                if (DELETE_COMPONENT.check(submask, e))
                    deleteSelectedComponent();
            }
        };

        /**
         * Object, used to handle mouse dragging events. Overrides {@link DragAdapter#mouseDragged(MouseEvent)},
         * {@link DragAdapter#mousePressed(MouseEvent)} and {@link DragAdapter#mouseReleased(MouseEvent)} methods.
         */
        private DragAdapter dragAdapter = new DragAdapter() {
            @Override
            protected int getSubmask(GraphComponentUI clicked){
                int sub = 0;
                if (clicked == null)
                    sub |= CLICKED_ON_NOTHING;
                else if (clicked instanceof NodeUI)
                    sub |= CLICKED_ON_NODE;
                else
                    sub |= CLICKED_ON_EDGE;
                sub |= (selected != null)? SELECTION_SET : SELECTION_UNSET;
                return MOUSE_DRAG_EVENT | sub;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                switch(actionPerforming){
                    case CONNECTING:
                        endDrag.setLocation(e.getX(), e.getY());
                        break;
                    case MOVING:
                        NodeUI node = (NodeUI) dragged;
                        node.moveTo(e.getX(), e.getY());
                        break;
                    case CURVING:
                        EdgeUI edge = (EdgeUI) dragged;
                        edge.setCurving(e.getX(), e.getY());
                        break;
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Point coords = new Point(e.getX(), e.getY());
                dragged = getElementByCoords(coords);
                if (dragged==null)
                    return;
                startDrag = coords;

                int submask = getSubmask(dragged);

                if (CONNECT_NODES.check(submask, e) && dragged instanceof NodeUI) {
                    endDrag = (Point) startDrag.clone();
                    actionPerforming = DragAction.CONNECTING;
                } else if (MOVE_NODE.check(submask, e) && dragged instanceof NodeUI)
                    actionPerforming = DragAction.MOVING;
                else if (CURVE_EDGE.check(submask, e) && dragged instanceof EdgeUI)
                    actionPerforming = DragAction.CURVING;
                else
                    startDrag = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch(actionPerforming){
                    case CONNECTING:
                        GraphComponentUI from = dragged,
                                to = getElementByCoords(endDrag);
                        if (to == null)
                            to = makeNode(endDrag);
                        makeEdge((NodeUI)from, (NodeUI)to);
                        break;
                }

                repaint();
                actionPerforming = DragAction.NONE;
            }
        };

        /**
         * Object, used to handle mouse clicking event. Overrides {@link MouseAdapter#mouseClicked(MouseEvent)}
         * method.
         */
        private ClickAdapter mouseAdapter = new ClickAdapter() {
            @Override
            protected int getSubmask(GraphComponentUI clicked){
                int sub = 0;
                if (clicked == null)
                    sub |= CLICKED_ON_NOTHING;
                else if (clicked instanceof NodeUI)
                    sub |= CLICKED_ON_NODE;
                else
                    sub |= CLICKED_ON_EDGE;
                sub |= (selected != null)? SELECTION_SET : SELECTION_UNSET;
                return MOUSE_CLICK_EVENT | sub;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Point coords = new Point(e.getX(), e.getY());
                GraphComponentUI element = getElementByCoords(coords);

                int submask = getSubmask(element);

                if (MAKE_NODE.check(submask, e))
                    makeNode(coords);
                else if (REMOVE_EDGE_CURVING.check(submask, e)){
                    EdgeUI elem = (EdgeUI) element;
                    elem.removeCurving();
                } else if (SELECT_COMPONENT.check(submask, e))
                    setSelection(element);
            }
        };


        /*=======================Constructors=======================*/


        /**
         * Class constructor.
         */
        GraphPanel(){
            addMouseListener(mouseAdapter);
            addMouseListener(dragAdapter);
            addMouseMotionListener(dragAdapter);
            addKeyListener(keyAdapter);
            setBackground(fieldEditColor);
        }


        /*======================Private Methods======================*/


        /**
         * Used to check if mouse event on particular point had caught any
         * graph component. Returns {@link GraphComponentUI} object if it's so.
         * Uses square of a 2*searchingSize size to detect an intersection with
         * any item.
         * @param coords mouse event location
         * @return component targeted with mouse event (or null)
         */
        private GraphComponentUI getElementByCoords(Point coords){
            Rectangle rect = new Rectangle(coords.x-searchingSize, coords.y-searchingSize, 2*searchingSize, 2*searchingSize);

            SafeCollectionWrapper<Node> nodes = graph.getNodes();
            for (Node n : nodes){
                NodeUI ui = n.getUI();
                if (ui.intersects(rect))
                    return ui;
            }

            SafeCollectionWrapper<Edge> edges = graph.getEdges();
            for (Edge e : edges){
                EdgeUI ui = e.getUI();
                if (ui.intersects(rect))
                    return ui;
            }
            return null;
        }

        /**
         * Used to select {@link GraphComponentUI} object.
         * @param element item to be selected
         */
        private void setSelection(GraphComponentUI element){
            selected = element;
            repaint();
        }

        /**
         * Must be used to create a {@link Node} object. Packs node's location
         * into {@link InfoPack} object and invokes {@link Graph#addNode(InfoPack)}
         * method. Returns node's GUI representation.
         * @param coords new node's location
         * @return GUI representation of currently created node
         */
        private NodeUI makeNode(Point coords){
            InfoPack data = graph.getNodeInfo();
            data.put("nodeCoords", coords);
            Node n = graph.addNode(data);
            repaint();
            return n.getUI();
        }

        /**
         * Must be used to connect 2 node objects and make an {@link Edge} object.
         * Packs start and end {@link Node} objects into {@link InfoPack} object
         * and invokes {@link Graph#addEdge(InfoPack)} method. Returns new edge's
         * GUI representation.
         * @param from GUI representation of node, which should be the start node
         * @param to GUI representation of node, which should be the end node
         * @return GUI representation of newly created edge
         */
        private EdgeUI makeEdge(NodeUI from, NodeUI to){
            InfoPack data = graph.getEdgeInfo();
            data.put("fromNode", from.getEntity());
            data.put("toNode", to.getEntity());
            Edge e = graph.addEdge(data);
            repaint();
            return e.getUI();
        }

        /**
         * Must be used to delete selected graph component and its GUI representation.
         */
        private void deleteSelectedComponent(){
            graph.removeGraphComponent(selected.getEntity());
            selected = null;
            repaint();
        }


        /*=====================Inherited Methods=====================*/


        /**
         * Method inherited from {@link JComponent} and invoked every time frame
         * repaints. Every node and edge from nodes and edges collections respectively
         * is drawn here. Method also displays connection line between nodes currently
         * being connected and graph component selection. Invokes {@link NodeUI#draw(Graphics2D)},
         * {@link EdgeUI#draw(Graphics2D)}, {@link NodeUI#select(Graphics2D)} and
         * {@link EdgeUI#select(Graphics2D)} methods.
         * @param gr graphics object
         */
        @Override
        public void paintComponent(Graphics gr){
            super.paintComponent(gr);
            Graphics2D g = (Graphics2D) gr;

            SafeCollectionWrapper<Node> nodes = graph.getNodes();
            SafeCollectionWrapper<Edge> edges = graph.getEdges();

            //Edges
            for (Edge e : edges)
                e.getUI().draw(g);
            //SelectedEdge
            if (selected!=null && selected instanceof EdgeUI)
                selected.select(g);

            //Connecting
            g.setStroke(connectStroke);
            g.setColor(connectColor);
            if (dragAdapter.actionPerforming == DragAdapter.DragAction.CONNECTING)
                g.drawLine(dragAdapter.startDrag.x, dragAdapter.startDrag.y, dragAdapter.endDrag.x, dragAdapter.endDrag.y);

            //Nodes
            for (Node n : nodes)
                n.getUI().draw(g);
            //SelectedNode
            if (selected!=null && selected instanceof NodeUI)
                selected.select(g);

            setFocusable(true);
            requestFocusInWindow();
        }


    }

    private class GraphToolbox extends JPanel{


        /*==========================Fields==========================*/


        /**
         * Size of the toolbox button.
         */
        private Dimension buttonSize = new Dimension(25, 25);

        /**
         * Size of the toolbox button icon.
         */
        private Dimension buttonIconSize = new Dimension(18, 18);

        /**
         * Toolbox width.
         */
        private int toolboxPanelWidth = 70;

        /**
         * Color of the bottom control panel.
         */
        private Color toolboxPanelColor = new Color(225, 225, 250);

        /**
         * List of node selector buttons.
         */
        private ButtonGroup nodes = new ButtonGroup();

        /**
         * List of edge selector buttons.
         */
        private ButtonGroup edges = new ButtonGroup();

        /**
         * Toolbox button margin.
         */
        private int toolboxButtonMargin = 5;


        /*=======================Constructors=======================*/


        GraphToolbox(){
            setPreferredSize(new Dimension(toolboxPanelWidth, 0));
            setBackground(toolboxPanelColor);
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setAlignmentY(TOP_ALIGNMENT);

            JPanel nodesPanel = new JPanel();
            nodesPanel.setLayout(new BoxLayout(nodesPanel, BoxLayout.PAGE_AXIS));
            nodesPanel.setBackground(toolboxPanelColor);
            ButtonGroup nodes = new ButtonGroup();
            Consumer<GraphComponentFactorySelector> nodeFunc = graph::setNodeFactory;
            fillButtonColumn(nodesPanel, nodes, nodeFunc, NodeFactorySelector.values());

            JPanel edgesPanel = new JPanel();
            edgesPanel.setLayout(new BoxLayout(edgesPanel, BoxLayout.PAGE_AXIS));
            edgesPanel.setBackground(toolboxPanelColor);
            ButtonGroup edges = new ButtonGroup();
            Consumer<GraphComponentFactorySelector> edgeFunc = graph::setEdgeFactory;
            fillButtonColumn(edgesPanel, edges, edgeFunc, EdgeFactorySelector.values());

            add(Box.createHorizontalGlue());
            add(nodesPanel);
            add(Box.createHorizontalGlue());
            add(edgesPanel);
            add(Box.createHorizontalGlue());
        }


        /*======================Private Methods======================*/


        private void fillButtonColumn(JPanel panel, ButtonGroup group, Consumer<GraphComponentFactorySelector> func, GraphComponentFactorySelector[] selectors){
            panel.add(Box.createRigidArea(new Dimension(0, toolboxButtonMargin)));
            boolean first = true;
            for (GraphComponentFactorySelector fs : selectors){
                JToggleButton butt = new JToggleButton();
                butt.setIcon(new ImageIcon(fs.getImg().getScaledInstance(buttonIconSize.width, buttonIconSize.height, Image.SCALE_SMOOTH)));
                butt.setPreferredSize(buttonSize);
                butt.setFocusPainted(false);
                butt.setToolTipText(fs.getHint() + strings.getString("hintToDescription"));
                butt.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e))
                            func.accept(fs);
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            butt.setToolTipText(fs.getDescription());
                            MouseEvent phantom = new MouseEvent(butt, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0,
                                    e.getX(), e.getY(), 0, false);
                            ToolTipManager.sharedInstance().mouseMoved(phantom);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        butt.setToolTipText(fs.getHint() + strings.getString("hintToDescription"));
                    }
                });
                group.add(butt);
                panel.add(butt);
                panel.add(Box.createRigidArea(new Dimension(0, toolboxButtonMargin)));
                if (first) {
                    butt.setSelected(true);
                    func.accept(fs);
                    first = false;
                }
            }
            panel.add(Box.createVerticalGlue());
        }


    }


}