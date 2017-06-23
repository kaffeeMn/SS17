class Method implements Callable<int[]>{
    private final String name;

    public Method(String name){
        this.name = name;
    }
    public String name(){
        return this.name;
    }
    @override
    public <int[]> call(){
        return null;
    }
}
