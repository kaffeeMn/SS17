import java.util.LinkedList;

public class MinStepCount{
    private final boolean[] parcour;
    private final int       steps;
    private int[][]         matrix;
    
    public MinStepCount(boolean[] parcour, int steps){
        this.parcour = parcour;
        this.steps   = steps;
        this.matrix  = functionC();
    }
    private int min(int... vals){
        if(vals.length>0){
            int min = vals[0];
            for(int i=1; i<vals.length; ++i){
                if(min > vals[i]){
                    min = vals[i];
                }
            }
            return min;
        }else{
            throw new IllegalArgumentException("Empty array submitted");
        }
    }
    private int max(int... vals){
        if(vals.length>0){
            int max = vals[0];
            for(int i=1; i<vals.length; ++i){
                if(max < vals[i]){
                    max = vals[i];
                }
            }
            return max;
        }else{
            throw new IllegalArgumentException("Empty array submitted");
        }
    }
    private int[][] functionC(){
        int[][] result = new int[this.steps+this.parcour.length][steps+1];
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
    public int get(int i, int v){
        return this.matrix[i][v];
    }
    public void sequenceOfSteps(int x){
        LinkedList<Integer> route = backtracking(x);
        displayRoute(route);
    }
    private LinkedList<Integer> backtracking(int x){
        LinkedList<Integer> result = new LinkedList<Integer>();
        int val = Integer.MAX_VALUE;
        boolean add = false;
        while(x >= 0){
            for(int i=0; i<=this.steps; ++i){
                if(matrix[x][i] < val){
                    val = matrix[x][i];
                    add = true;
                }
            }
            if(add){
                result.add(0, x);
                add = false;
            }
            --x;
        }
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
