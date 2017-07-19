import java.io.IOException;
import java.util.ArrayList;

public class GraphTest {

	/**
	 * Loest das SSSP-Proplem mit Hilfe des Algortihmus von Bellman-Ford.
	 * @param g der Graph
	 * @param source id des Startknotes
	 * @return Array mit Weglaengen; Element i gibt die Laenge eines kuerzesten
	 * Weges von dem Knoten mit der id source zu dem Knoten mit id i an
	 */
	public static double[] sssp(Graph g, int source) {
		//throw new UnsupportedOperationException("Aufgabe 13.1 noch nicht bearbeitet!");		
        ArrayList<Node> nodes = g.getNodes();
        int length = nodes.size();
        int max = length + 1;
        double[][] matrix = new double[length][length];
        for(int i=0; i<matrix[0].length; ++i){
            matrix[0][i] = max;
        }
        matrix[0][source] = 0;
        double minVal = Double.MAX_VALUE;
        double tmp;
        Node u;
        for(int i=1; i<matrix.length; ++i){
            for(Node n : nodes){
                for(Edge e : n.getIncidenceList()){
                    u = e.getSource();
                    tmp = min(matrix[i-1][n.getID()], min(matrix[i-1][u.getID()], e.getCost()));
                    if(tmp < minVal){
                        minVal = tmp;
                    }
                }
                matrix[i][n.getID()] = minVal;
                minVal = Double.MAX_VALUE;
            }
        }
        return matrix[matrix.length-1];
	}
    private static double min(double... vals){
        if(vals.length > 0){
            double tmp = vals[0];
            for(int i=1; i<vals.length; ++i){
                if(vals[i] < tmp){
                    tmp = vals[i];
                }
            }
            return tmp;
        }
        throw new IllegalArgumentException("no parameters for min");
    }

	/**
	 * Loest das APSP-Problem mit Hilfe des Algorithmus von Floyd-Warshall 
	 * @param g der Graph
	 * @return Matrix mit Weglaengen; Element (i,j) gibt die Laenge eines 
	 * kuerzesten Weges von dem Knoten mit der id i zu dem Knoten mit id j an 
	 */
	public static double[][] apsp(Graph g) {
		//throw new UnsupportedOperationException("Aufgabe 13.2 noch nicht bearbeitet!");	
        ArrayList<Node> nodes = g.getNodes();
        int length = nodes.size();
        double[][][] matrix = new double[length+1][length][length];
        for(Node n : nodes){
            for(Edge e : n.getIncidenceList()){
                matrix[0][e.getSource().getID()][e.getDest().getID()] = e.getCost();
            }
        }
        for(int k=1; k<matrix.length; ++k){
            for(int i=0; i<matrix[k].length; ++i){
                for(int j=0; k<matrix[k][i].length; ++j){
                    matrix[k][i][j] = min(matrix[k-1][i][j], matrix[k-1][i][k], matrix[k-1][k][j]);
                }
            }
        }
        return matrix[matrix.length];
	}

	/**
 	 * Realisiert einen APSP-Algorithmus, indem fuer alle Knoten das 
 	 * SSSP-Problem mittels Bellman-Ford geloest wird.
 	 * @param g der Graph
	 * @return Matrix mit Weglaengen; Element (i,j) gibt die Laenge eines 
	 * kuerzesten Weges von dem Knoten mit der id i zu dem Knoten mit id j an 
	 */
	public static double[][] apspBellmanFord(Graph g) {
		// Knoten holen
		ArrayList<Node> nodes = g.getNodes();
		// Tabelle anlegen
		double[][] result = new double[nodes.get(nodes.size()-1).getID()+1][];
		for (Node n : nodes){
			// Bellman-Ford fuer jeden Knoten
			result[n.getID()] = sssp(g, n.getID());
		}
		return result;
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		if (args.length < 1) {
			System.err.println("Syntax: java GraphTest <filename> [<nodenumber>]");
			System.exit(-1);
		}
		
		Graph g = Graph.fromFile(args[0]);
		if (g==null) {
			System.err.println("Konnte Datei "+args[0]+ " nicht oeffnen oder enthaelt keinen Graphen");
			System.exit(1);
		}
		if (g.getNodes().isEmpty()) {
			System.err.println("Leerer Graph.");
			System.exit(2);
		}
		
		if (args.length == 2) {
			// Fuehre Bellman-Ford-Algorithmus aus
			int nodenumber=-1;
			try {
				nodenumber=Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
			}
			if (g.getNode(nodenumber) == null) {
				System.err.println("Ungueltiger Startknoten angegeben: "+args[1]);
				System.exit(1);
			}
			double[] minCost = sssp(g, nodenumber);
			ArrayList<Node> nodes = g.getNodes();
			Node s = g.getNode(nodenumber), e = s;
			double maxDist = 0d;
			for (Node n : nodes){
				if (nodes.size()<= 20){
					System.out.println("Abstand von Knoten " + n.getID() + " zu Knoten " + s.getID() + ": " + minCost[n.getID()]);
				}
				if (minCost[n.getID()] != Double.POSITIVE_INFINITY && minCost[n.getID()] > maxDist){
					maxDist = minCost[n.getID()];
					e = n;
				}
			}
			System.out.println("Der maximale Abstand ist " + maxDist + " fuer Knoten " + e.getID());
		} else {
			// Fuehre Floyd-Warshall-Algorithmus aus
			double[][] minCost=apsp(g);
			ArrayList<Node> nodes = g.getNodes();
			Node s = nodes.get(0), e = s;
			double maxDist = 0d;
			for (Node u : nodes){
				for (Node v : nodes){
					if (nodes.size()<= 10){
						System.out.print((minCost[u.getID()][v.getID()] == Double.POSITIVE_INFINITY? "\u221E": "" +minCost[u.getID()][v.getID()]) + "\t");
					}
					if (minCost[u.getID()][v.getID()] != Double.POSITIVE_INFINITY && minCost[u.getID()][v.getID()] > maxDist){
						maxDist = minCost[u.getID()][v.getID()];
						s = u;
						e = v;
					}
				}
				if (nodes.size()<= 10) System.out.println();
			}
			System.out.println("Der maximale Abstand ist " + maxDist + " fuer das Knotenpaar (" + s.getID() + ", " + e.getID() + ")");
		}
	}
}
