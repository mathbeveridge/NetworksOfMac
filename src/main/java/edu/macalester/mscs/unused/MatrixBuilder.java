package edu.macalester.mscs.unused;

import edu.macalester.mscs.network.MatrixAndNames;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.List;

public class MatrixBuilder extends JApplet {
	
	//~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = 3256444702936019250L;

    //~ Instance fields --------------------------------------------------------

    //


	VisualizationViewer<Number,Number> vv;
	HashMap<Number, Double> weights = new HashMap<>();
	HashMap<Number, String> cnames = new HashMap<>();
	int[][] matrixAll;
	
//	Factory<Graph<Number,Number>> graphFactory;
	
	Map<Number,Paint> vertexPaints =
		LazyMap.decorate(new HashMap<Number, Paint>(),
				new ConstantTransformer(Color.white));
	Map<Number,Paint> edgePaints =
	LazyMap.decorate(new HashMap<Number, Paint>(),
			new ConstantTransformer(Color.blue));

	public final Color[] similarColors = {
		new Color(216, 134, 134),
		new Color(135, 137, 211),
		new Color(134, 206, 189),
		new Color(206, 176, 134),
		new Color(194, 204, 134),
		new Color(145, 214, 134),
		new Color(133, 178, 209),
		new Color(103, 148, 255),
		new Color(60, 220, 220),
		new Color(30, 250, 100),
		new Color(100, 68, 0),
		new Color(80, 0, 45),
		new Color(220,20,60),
		new Color(75,0,130),
		new Color(0,205,102),
		new Color(255,255,0),
		new Color(0,255,255),
		new Color(255,128,0),
		new Color(255,105,180),
		new Color(0,0,255),
		new Color(124,252,0),
		new Color(255,99,71),
		new Color(112,128,144),
		new Color(255,232,173),
		new Color(61, 93, 0)
	};
    
    /**
     * {@inheritDoc}
     */
    public void init() {

    	//InputStream is = this.getClass().getClassLoader().getResourceAsStream("./input.txt");
		//BufferedReader br = new BufferedReader( new InputStreamReader( is ));
        
		//MatrixAndNames man = getData();
    	
		
        try
        {
        	String line = "";
        	BufferedReader br = new BufferedReader(new FileReader("./three.csv"));
        	line = br.readLine();
        	String[] names = line.split(",");
        	int[][] csvMatrix = new int[names.length][names.length];
        	int row = 0;
    		while ((line = br.readLine()) != null) {
    			String[] temp = line.split(","); 
    			for(int j=0; j<names.length; j++){
    				csvMatrix[row][j] = (int) Double.parseDouble(temp[j]);
    			}
    			row++;
    		}
    		
    		MatrixAndNames man = new MatrixAndNames(csvMatrix,names);
            setUpView(man);
        }
        catch (IOException e)
        {
            System.out.println("Error in loading graph");
            e.printStackTrace();
        }
    }

private void setUpView(final MatrixAndNames man) throws IOException {

    	Factory<Number> vertexFactory = new Factory<Number>() {
            int n = 0;
            public Number create() { return n++; }
        };
        Factory<Number> edgeFactory = new Factory<Number>()  {
            int n = 0;
//            int m = 1;
//            int i = 0;
            public Number create() {
//            	if(m == man.getMatrix().length){
//            		n++;
//            		m=n+1;
//            	}
//            	while(man.getMatrix()[n][m] == 0){
//                	m++;
//                	if(m == man.getMatrix().length){
//                    	n++;
//                    	m=n+1;
//                    }
//            	}
//            	String w = Integer.toString(man.getMatrix()[n][m++]);
//            	int len = w.length();
//            	for(int j=0; j<5-len; j++){
//            		w="0"+w;
//            	}
//            	String id = "0." + w;
//            	//System.out.println(w);
//            	return (i++)+Double.parseDouble(id);
            	return n++;
            }
        };

        final Graph<Number,Number> graph = new SparseMultigraph<>();

        
        //pnr.load(br, graph);
        String[] names = man.getNames();
        String nameList = "nameList = [";
        for(int i=0; i<names.length; i++){
        	nameList+="\""+names[i]+"\",";
        }
        nameList=nameList.substring(0,nameList.length()-1)+"]";
        //System.out.println(nameList);
        int[][] matrix = man.getMatrix();
        matrixAll = new int[matrix.length][matrix.length];
        for(int i=0; i<matrix.length; i++){
        	for(int j=0; j<matrix.length; j++){
        		matrixAll[i][j] = matrix[i][j];
        	}
        }
        for(int i=0; i<names.length; i++){
        	cnames.put(i, names[i]);
            graph.addVertex(i);
        }
        int e = 0;
        for(int i=0; i<matrix.length; i++){
        	for(int j=i+1; j<matrix.length; j++){
        		if(matrix[i][j]>0){
        			graph.addEdge(e, i, j);
                    weights.put(e, matrix[i][j]*1.0);
        			e++;
        		}
	        }
        }
        
		//Create a simple layout frame
        //specify the Fruchterman-Rheingold layout algorithm
        final AggregateLayout<Number,Number> layout =
        	new AggregateLayout<>(new FRLayout<>(graph));

		vv = new VisualizationViewer<>(layout);
		vv.setBackground( Color.white );
		//Tell the renderer to use our own customized color rendering
		vv.getRenderContext().setVertexFillPaintTransformer(MapTransformer.getInstance(vertexPaints));
		vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<Number,Paint>() {
			public Paint transform(Number v) {
				if(vv.getPickedVertexState().isPicked(v)) {
					return Color.cyan;
				} else {
					return Color.BLACK;
				}
			}
		});

		vv.getRenderContext().setEdgeDrawPaintTransformer(MapTransformer.getInstance(edgePaints));

		vv.getRenderContext().setEdgeStrokeTransformer(new Transformer<Number,Stroke>() {
                protected final Stroke THIN = new BasicStroke(1);
                protected final Stroke THICK= new BasicStroke(2);
                public Stroke transform(Number e)
                {
                    Paint c = edgePaints.get(e);
                    if (c == Color.LIGHT_GRAY)
                        return THIN;
                    else 
                        return THICK;
                }
            });
		
		vv.getRenderContext().setVertexLabelTransformer(new Transformer<Number, String>() {
            public String transform(Number v) {
                return (v.toString()+" "+man.getNames()[(Integer) v]);
            }
        });
//		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		
		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<Number, String>() {
            public String transform(Number e) {
            	int x=-1;
            	int[][] purgedMatrix = man.getMatrix();
            	for(int i=0; i<purgedMatrix.length; i++){
		        	for(int j=i+1; j<purgedMatrix.length; j++){
		        		if(purgedMatrix[i][j]>0){
		        			x++;
		        			if(x == (Integer) e){
			        			return Integer.toString(purgedMatrix[i][j]);
			        		}
		        		}
			        }
		        }
                return "0";
            }
        });

		//add restart button
		JButton scramble = new JButton("Restart");
		scramble.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Layout layout = vv.getGraphLayout();
				layout.initialize();
				Relaxer relaxer = vv.getModel().getRelaxer();
				if(relaxer != null) {
					relaxer.stop();
					relaxer.prerelax();
					relaxer.relax();
				}
			}

		});
		
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		vv.setGraphMouse(gm);
		
		final JToggleButton groupVertices = new JToggleButton("Group Clusters");

		//Create slider to adjust the number of edges to remove when clustering
		final JSlider edgeBetweennessSlider = new JSlider(JSlider.HORIZONTAL);
        edgeBetweennessSlider.setBackground(Color.WHITE);
		edgeBetweennessSlider.setPreferredSize(new Dimension(400, 50));
		edgeBetweennessSlider.setPaintTicks(true);
		edgeBetweennessSlider.setMaximum(graph.getEdgeCount());
		edgeBetweennessSlider.setMinimum(0);
		edgeBetweennessSlider.setValue(0);
		edgeBetweennessSlider.setMajorTickSpacing(10);
		edgeBetweennessSlider.setPaintLabels(true);
		edgeBetweennessSlider.setPaintTicks(true);

//		edgeBetweennessSlider.setBorder(BorderFactory.createLineBorder(Color.black));
		//TO DO: edgeBetweennessSlider.add(new JLabel("Node Size (PageRank With Priors):"));
		//I also want the slider value to appear
		final JPanel eastControls = new JPanel();
		eastControls.setOpaque(true);
		eastControls.setLayout(new BoxLayout(eastControls, BoxLayout.Y_AXIS));
		eastControls.add(Box.createVerticalGlue());
		eastControls.add(edgeBetweennessSlider);

		final String COMMANDSTRING = "Edges removed for clusters: ";
		final String eastSize = COMMANDSTRING + edgeBetweennessSlider.getValue();
		
		final TitledBorder sliderBorder = BorderFactory.createTitledBorder(eastSize);
		eastControls.setBorder(sliderBorder);
		//eastControls.add(eastSize);
		eastControls.add(Box.createVerticalGlue());
		
		groupVertices.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
					clusterAndRecolor(layout, edgeBetweennessSlider.getValue(), 
							similarColors, e.getStateChange() == ItemEvent.SELECTED, weights, cnames);
					vv.repaint();
			}});


		clusterAndRecolor(layout, 0, similarColors, groupVertices.isSelected(), weights, cnames);

		edgeBetweennessSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int numEdgesToRemove = source.getValue();
					clusterAndRecolor(layout, numEdgesToRemove, similarColors,
							groupVertices.isSelected(), weights, cnames);
					sliderBorder.setTitle(
						COMMANDSTRING + edgeBetweennessSlider.getValue());
					eastControls.repaint();
					vv.validate();
					vv.repaint();
				}
			}
		});

		Container content = getContentPane();
		content.add(new GraphZoomScrollPane(vv));
		JPanel south = new JPanel();
		JPanel grid = new JPanel(new GridLayout(2,1));
		grid.add(scramble);
		grid.add(groupVertices);
		south.add(grid);
		south.add(eastControls);
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
		p.add(gm.getModeComboBox());
		south.add(p);
		content.add(south, BorderLayout.SOUTH);
		
	}
    
    public void clusterAndRecolor(AggregateLayout<Number,Number> layout,
    		int numEdgesToRemove, Color[] colors, boolean groupClusters, 
    		HashMap<Number, Double> weights, HashMap<Number, String> cnames) {
    		//Now cluster the vertices by removing the top 50 edges with highest betweenness
    		//		if (numEdgesToRemove == 0) {
    		//			colorCluster( g.getVertices(), colors[0] );
    		//		} else {
    		
    		Graph<Number,Number> g = layout.getGraph();
            layout.removeAll();

    		EdgeBetweennessClusterer<Number,Number> clusterer =
    			new EdgeBetweennessClusterer<>(numEdgesToRemove);
    		Set<Set<Number>> clusterSet = clusterer.transform(g, weights);
    		List<Number> edges = clusterer.getEdgesRemoved();

    		int i = 0;
    		//Set the colors of each node so that each cluster's vertices have the same color
			for (Set<Number> vertices : clusterSet) {

				Color c = colors[i % colors.length];

				colorCluster(vertices, c);
				if (groupClusters) {
					groupCluster(layout, vertices);
				}
				i++;
			}
    		for (Number e : g.getEdges()) {
    			if (edges.contains(e)) {
    				edgePaints.put(e, Color.lightGray);
    			} else {
    				edgePaints.put(e, Color.black);
    			}
    		}
    		
    		Double[][] communityMatrix = new Double[cnames.size()][cnames.size()];
    		for(int m=0;m<cnames.size();m++){
    			for(int n=0;n<cnames.size();n++){
    				communityMatrix[m][n] = 0.0;
    			}
    		}
    		for (Number e : g.getEdges()) {
    			if (!edges.contains(e)) {
    				int v1 = g.getEndpoints(e).getFirst().intValue();
					int v2 = g.getEndpoints(e).getSecond().intValue();
    				communityMatrix[v1][v2] = weights.get(e);
    				communityMatrix[v2][v1] = weights.get(e);
    			}
    		}    		
    		
    		/*String[] namesA = new String[cnames.size()];
    		int z=0;
    		for(String s: cnames.values()){
    			namesA[z] = s;
    			z++;
    		}
    		printMatrixDouble(communityMatrix, namesA);*/
    		
    	}
    
    	private void colorCluster(Set<Number> vertices, Color c) {
    		for (Number v : vertices) {
    			vertexPaints.put(v, c);
    		}
    	}
    	
    	private void groupCluster(AggregateLayout<Number,Number> layout, Set<Number> vertices) {
    		if(vertices.size() < layout.getGraph().getVertexCount()) {
    			Point2D center = layout.transform(vertices.iterator().next());
    			Graph<Number,Number> subGraph = SparseMultigraph.<Number,Number>getFactory().create();
    			for(Number v : vertices) {
    				subGraph.addVertex(v);
    			}
    			Layout<Number,Number> subLayout = 
    				new CircleLayout<>(subGraph);
    			subLayout.setInitializer(vv.getGraphLayout());
    			subLayout.setSize(new Dimension(40,40));

    			layout.put(subLayout,center);
    			vv.repaint();
    		}
    	}

	
    public static MatrixAndNames getData(){
    	MatrixAndNames data = null;
    	try {
			//File file = new File("./b4dae.txt");
			File file = new File("./got3.txt");
			String text = "";
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				text += scanner.next()+" ";
			}
			scanner.close();
			//String nameInput = "Bran Jon Ben Theon Eddard=Ned Cersei Robb";
			/*String nameInput = "Bran Jon Theon Robb Joffrey Tyrion=Imp Arya Sansa Cersei Jaime Eddard=Ned Gregor Catelyn Robert Rickon Benjen=Ben "
					+"Tywin Samwell=Sam Gendry Shae Varys Bronn Jeor Daenerys=Khaleesi=Dany "
					+"Jorah Petyr Sandor Viserys Drogo Lysa Selyse Renly Roose Walder Lothar "
					+"Balon Kevan Lancel Podrick=Pod Amory Osha Hodor Rickard Luwin Rodrik Mordane Nan Barristan Rakharo Doreah Irri Edmure Brynden "
					+"Mace Loras Aemon Grenn Rast Pypar Yoren Alliser Bowen Illyrio Mirri Qotho Beric Myr Anguy Marillion Syrio Myrcella Tommen "
					+"Pycelle Meryn Janos Dontos Ilyn Mance Davos Stannis Margaery Melisandre Ygritte Talisa Robin Shireen Salladhor Matthos "
					+"Cressen Ramsay Walder Yara Dagmer Alton Oberyn Brienne Jojen Meera Daario Missandei Kovarro Roslin Olenna Eddison "
					+"Karl Qhorin Kraznys Xaro Pyat Quaithe Qyburn Jaqen Lommy Hallyne Orell Craster Gilly Styr";*/
			
			// use this one
			String nameInput = "Bran Jon Theon Robb Joffrey Tyrion=Imp Arya Sansa Cersei Jaime Eddard=Ned Gregor Catelyn Robert Rickon Benjen=Ben "
					+"Tywin Samwell=Sam Gendry Shae Varys Bronn Daenerys=Khaleesi=Dany "
					+"Jorah Petyr Sandor Viserys Drogo Lysa Renly Walder Lothar "
					+"Kevan Podrick=Pod Hodor Rickard Nan Rakharo Irri Edmure "
					+"Mace Loras Aemon Grenn Alliser Beric Myr Anguy Marillion Myrcella Tommen "
					+"Pycelle Meryn Janos Dontos Ilyn Mance Davos Stannis Margaery Melisandre Ygritte Salladhor "
					+"Oberyn Brienne Jojen Meera Daario Missandei Roslin Olenna "
					+"Qhorin Kraznys Qyburn Craster Gilly Styr";
			HashMap<String,ArrayList<String>> nickname = new HashMap<>();
			String[] nicknameArr = nameInput.split(" ");
			for(int i=0; i<nicknameArr.length; i++){
				if(nicknameArr[i].contains("=")){
					String[] temp = nicknameArr[i].split("=");
					//System.out.println(temp.length);
					for(int j=1; j<temp.length; j++){
						if(!nickname.containsKey(temp[0])){
							ArrayList<String> others = new ArrayList<>();
							others.add(temp[j]);
							nickname.put(temp[0], others);
						}else{
							ArrayList<String> others = nickname.get(temp[0]);
							others.add(temp[j]);
							nickname.put(temp[0], others);
							//System.out.println(temp[j]);
						}
					}
				}
			}
			String[] nameInputArr = nameInput.split("[ =]");
			int[][] matrix = new int[nameInputArr.length][nameInputArr.length];
			
			String[] input = text.split(" ");
			// threshold: 3=13
			int reach = 13;
			
			for(int i=0; i<input.length; i++){
				for(int k=0; k<nameInputArr.length; k++){
					if(input[i].contains(nameInputArr[k])){
						for(int j=Math.max(0, i-reach); j<=Math.min(input.length-1, i+reach); j++){
							for(int m=0; m<nameInputArr.length; m++){
								if(m!=k && input[j].contains(nameInputArr[m]) && m>k){
									matrix[k][m]++;
									matrix[m][k]++;
								}
							}
						}
					}
				}
			}
			data = new MatrixAndNames(matrix, nameInputArr);
			data = removeNickname(data, nickname);
			//printMatrix(data.getMatrix(), data.getNames());
			matrix = data.getMatrix();
			nameInputArr = data.getNames();
			//printMatrix(matrix, nameInputArr);
			//System.out.println(text);	
			
			data = purgeData(data);
			printMatrix(data.getMatrix(), data.getNames());
			String[] purgedNames = data.getNames();
			int[][] purgedMatrix = data.getMatrix();
			
			try {
				File inputFile = new File("./input.txt"); 
				inputFile.delete(); 
		    } catch (Exception e) {
		        	System.out.println("No previously generated input file.");
		    }
			
			try {
				File newInput = new File("./input.txt");
				if (!newInput.exists()) {
					newInput.createNewFile();
				}
				FileWriter fw = new FileWriter(newInput.getAbsoluteFile());
				BufferedWriter out = new BufferedWriter(fw);
		        out.write("*vertices "+purgedNames.length);
		        out.newLine();
		        for (int i = 1; i <= purgedNames.length; i++) {
		            out.write(i + " \"" + purgedNames[i-1] + "\"" + "\n");
		        }
		        out.write("*edges");
		        out.newLine();
		        for(int i=0; i<purgedMatrix.length; i++){
		        	for(int j=i+1; j<purgedMatrix.length; j++){
		        		if(purgedMatrix[i][j]>0){
		        			out.write((i+1) + " " + (j+1) + " " + purgedMatrix[i][j] + "\n");
		        		}
			        }
		        }
		        out.close();
		        
		        File checkInput = new File("./input.txt");
		        System.out.println("\n\n");
		        System.out.println("This is the generated input for our graph processing:");
		        Scanner s = new Scanner(checkInput);
				while (s.hasNext()) {
					System.out.println(s.nextLine()+" ");
				}
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return data;
    }
    
	private static MatrixAndNames purgeData(MatrixAndNames data) {
		String[] names = data.getNames();
		int[][] matrix = data.getMatrix();
		int nicknameCount = 0;
		for(int i=0; i< names.length; i++){
			if(names[i].contains("void")) nicknameCount++;
		}
		String[] purgedNames = new String[names.length - nicknameCount];
		int[][] purgedMatrix = new int[purgedNames.length][purgedNames.length];
		int temp = 0;
		for(int j=0; j<names.length; j++){
			if(!names[j].contains("void")){
				purgedNames[temp] = names[j];
				int col = 0;
				for(int k=0; k<matrix.length; k++){
					if(matrix[j][k]>=0){
						purgedMatrix[temp][col] = Math.max(matrix[j][k]-5, matrix[j][k]/10);
						col++;
					}
				}
				temp++;
			}
		}
		HashMap<Integer, Integer> singleton = new HashMap<>();
		for(int i=0; i<purgedMatrix.length; i++){
			Boolean single = true;
			for(int j=0; j<purgedMatrix[i].length; j++){
				if(purgedMatrix[i][j] > 0){
					single = false;
					break;
				}
			}
			if(single)singleton.put(i, i);
		}
		String[] purgedNames2 = new String[purgedNames.length-singleton.size()];
		int[][] purgedMatrix2 = new int[purgedNames2.length][purgedNames2.length];
		int namesPos=0;
		System.out.print("Omitted singleton(s): ");
		for(int i=0; i<purgedNames.length; i++){
			if(!singleton.containsKey(i)){
				purgedNames2[namesPos] = purgedNames[i];
				namesPos++;
			}else{
				System.out.print(purgedNames[i]+" ");
			}
		}
		System.out.print("\n");
		int m=0;
		int n=0;
		for(int i=0; i<purgedNames.length; i++){
			n=0;
			if(!singleton.containsKey(i)){
				for(int j=0; j<purgedNames.length; j++){
					if(!singleton.containsKey(j)){
						purgedMatrix2[m][n] = purgedMatrix[i][j];
						n++;
					}
				}
				m++;
			}
		}
		// TODO
//		data.setMatrix(purgedMatrix);
//		data.setNames(purgedNames);
		return data;
	}

	public static void main(String[] args){
		MatrixBuilder applet = new MatrixBuilder();
        applet.init();
        
        JFrame jf = new JFrame();
		jf.getContentPane().add(applet);
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);

//        JFrame frame = new JFrame();
//        frame.getContentPane().add(applet);
//        frame.setTitle("Network Analysis");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);	
	}
	
	public static MatrixAndNames removeNickname(MatrixAndNames man, HashMap<String,ArrayList<String>> nickname){
		//System.out.println(nickname.containsKey("Eddard"));
		for(int i=0; i<man.getNames().length; i++){
			if(nickname.containsKey(man.getNames()[i])){
				int j = i+1;
				//System.out.println(man.getNames().length+" man.getNames().length");
				//System.out.println(man.getNames()[j]+" man.getNames()[j]");
				//System.out.println(nickname.containsValue(man.getNames()[j])+" nickname.containsValue(man.getNames()[j])");
				while(j<man.getNames().length && nickname.get(man.getNames()[i]).contains(man.getNames()[j])){
					for(int m=0; m<man.getMatrix().length; m++){
						if(m != j){
							man.getMatrix()[m][i] = man.getMatrix()[m][i] + man.getMatrix()[m][j];
							man.getMatrix()[m][j] = -1;
						}else{
							for(int n=0; n<man.getMatrix().length; n++){
								man.getMatrix()[i][n] = man.getMatrix()[i][n] + man.getMatrix()[j][n];
								man.getMatrix()[j][n] = -1;
							}
						}
					}
					man.getNames()[j] = "void:"+man.getNames()[j];
					man.getMatrix()[i][i] = 0;
					j++;
				}
			}
		}
		return man;
	}
	
	public static void printMatrix(int[][] matrix, String[] nameInputArr){
		String[] nameNum = new String[nameInputArr.length];
		String title = "";
		String firstline = "     #";
		for(int m=0; m<nameInputArr.length; m++){
			String namecard = "@"+Integer.toString(m);
			title+=namecard+"="+nameInputArr[m]+"  ";
			if(!nameInputArr[m].contains("void")){
				for(int n=namecard.length(); n<=4; n++){
					if(n%2 == 1){
						namecard += " ";
					}else{
						namecard = " "+namecard;
					}
				}
				nameNum[m] = namecard;
				firstline += namecard+"#";
			}
		}
		System.out.println(title);
		System.out.println();
		System.out.println(firstline);
		for(int i=0; i<matrix.length; i++){
			if(nameNum[i]!=null){
				String line = nameNum[i]+"|";
				for(int j=0; j<matrix[i].length; j++){
					if(matrix[i][j] >= 0){
						String num = Integer.toString(matrix[i][j]);
						for(int k=Integer.toString(matrix[i][j]).length(); k<=4; k++ ){
							if(k%2 == 1){
								num += " ";
							}else{
								num = " "+num;
							}
						}
						line+=num+"|";
					}
				}
				System.out.println(line);
			}
		}
	}
	
	public static void printMatrixDouble(Double[][] matrix, String[] nameInputArr){
		String[] nameNum = new String[nameInputArr.length];
		String title = "";
		String firstline = "     #";
		for(int m=0; m<nameInputArr.length; m++){
			String namecard = "@"+Integer.toString(m);
			title+=namecard+"="+nameInputArr[m]+"  ";
			if(!nameInputArr[m].contains("void")){
				for(int n=namecard.length(); n<=4; n++){
					if(n%2 == 1){
						namecard += " ";
					}else{
						namecard = " "+namecard;
					}
				}
				nameNum[m] = namecard;
				firstline += namecard+"#";
			}
		}
		System.out.println(title);
		System.out.println();
		System.out.println(firstline);
		for(int i=0; i<matrix.length; i++){
			if(nameNum[i]!=null){
				String line = nameNum[i]+"|";
				for(int j=0; j<matrix[i].length; j++){
					if(matrix[i][j] >= 0){
						String num = Integer.toString(matrix[i][j].intValue());
						for(int k=Integer.toString(matrix[i][j].intValue()).length(); k<=4; k++ ){
							if(k%2 == 1){
								num += " ";
							}else{
								num = " "+num;
							}
						}
						line+=num+"|";
					}
				}
				System.out.println(line);
			}
		}
	}

}
