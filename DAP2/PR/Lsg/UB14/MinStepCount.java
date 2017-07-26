public class MinStepCount{
    private final boolean[] parcour;
    private final int       steps;
    private int[][]         matrix;
    
    public MinStepCount(boolean[] parcour, int steps){
        this,parcour = parcour;
        this.steps   = steps;
        this.matrix  = functionC();
    }
    private static int[][] functionC(){
        int[][] result = new int[this.steps+this,parcour.length][steps+1];
        for(int i=this.steps+1; i<result.length; ++i){
            result[i][0] = Integer.MAX_VALUE;
        }
        result[this.steps][0] = 0;
        int tmp = Integer.MAX_VALUE;
        for(int i=this.steps; i<result.length; ++i){
            for(int j=0; j<result[i].length; ++j){
                if(parcour[i]){
                    for(int w=max(0,j-1); w<=min(this.steps, j+1); ++w){
                        if(tmp>result[i-j][w]){
                            tmp = result[i-j][w];
                        }
                    }
                    if(tmp != Integer.MAX_VALUE){
                        result[i][j] = tmp+1;
                        tmp = Integer.MAX_VALUE;
                    }else{
                        result[i][j] = Integer.MAX_VALUE;
                    }
                }else{
                    result[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        return result;
    }
    public get(int i, int v){
        return this.matrix[i][v];
    }
    public sequenceOfSteps(int x){
        int index = 0;
        int tmp = this,matrix[x][0]
        for(int i=1; i<this.matrix[x].length; ++i){
            if(tmp > matrix[x][i]){
                tmp = matrix[x][i];
                index = i;
            }
        }
        LinkedList<Integer> route = backtracking(x, index);
        displayRoute(route);
    }
    private LinkedList<Integer> backtracking(int x, int index){
        LinkedList<Integer> result = new LinkedList<Integer>();
        // TODO: find backtracking algorithm
        return result;
    }
    private void displayRoute(LinkedList<Integer> route){
        String[] representer = new String[this.parcour.length];
        for(int i=0; i<this.parcour.length; ++i){
            if(this.parcour[i]){
                representer[i] = "  ";
            }else{
                representer[i] = "XX";
            }
        }
        for(Integer pos : route){
            representer[pos.intValue()] = "<>";
        }
        System.out.print("|");
        for(String s : representer){
            System.out.print(s);
        }
        System.out.println("|");
    }
}
