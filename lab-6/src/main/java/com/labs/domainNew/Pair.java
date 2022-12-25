package com.labs.domainNew;

public class Pair implements Comparable<Pair>{
    private int utility ;
    private Edge edge;

    public Pair(Edge edge, int utility) {
        this.edge = edge ;
        this.utility = utility ;
    }

    public int getUtility() {
        return utility ;
    }

    public Edge getEdge() {
        return this.edge ;
    }

    public void setEdge(Edge edge) {
        this.edge = edge ;
    }
    public void setUtility(int utility) {
        this.utility = utility ;
    }

    @Override
    public int compareTo(Pair pair) {
        return this.utility - pair.utility;
    }
}
