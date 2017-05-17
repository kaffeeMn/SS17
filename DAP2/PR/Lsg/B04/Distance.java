public interface Distance<T>{
    // euklid distance as default since the distance method is not neccessary generic
    public double distance(<T> p1, <T> p2);
}
