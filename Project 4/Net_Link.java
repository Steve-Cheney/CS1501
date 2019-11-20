/*
   Stephen Cheney || 4275535

   Network_Link class is the object of each individual edge of the graph
 */
import java.util.Comparator;

class Net_Link implements Comparable<Net_Link>{

  private int vertex_0 = -1;
  private int vertex_1 = -2;
  private String cable_type = "";
  private int bandwidth = -3;
  private int cable_length = -4;
  private int[] coordinates = new int[2];

  Net_Link(){ // default constructor

  }

  Net_Link(int v0, int v1, String ctype, int band, int length){ // constructor for a connection
    this.vertex_0 = v0;
    this.vertex_1 = v1;
    this.cable_type = ctype;
    this.bandwidth = band;
    this.cable_length = length;
    updateCoords(this.vertex_0, this.vertex_1);

  }

  /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param  vertex one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     *         endpoints of this edge
     */
    public int other(int vertex) {
        if      (vertex == vertex_0) return vertex_1;
        else    return vertex_0;
    }

    public int getLatency(){
      if(cable_type.equals("copper")){
        return (int)Math.floor(((double)(cable_length))/230000000*Math.pow(10,9));
      }
      else {
        return (int)Math.floor(((double)(cable_length))/200000000*Math.pow(10,9));
      }
    }

  public void setV0(int v0){
    this.vertex_0 = v0;
    updateCoords(this.vertex_0, this.vertex_1);
  }
  public int getV0(){
    return vertex_0;
  }

  public void setV1(int v1){
    this.vertex_1 = v1;
    updateCoords(this.vertex_0, this.vertex_1);
  }
  public int getV1(){
    return vertex_1;
  }

  public void setCableType(String ctype){
    this.cable_type = ctype;
  }
  public String getCableType(){
    return cable_type;
  }

  public void setBandwidth(int band){
    this.bandwidth = band;
  }
  public int getBandwidth(){
    return bandwidth;
  }

  public void setCableLength(int length){
    this.cable_length = length;
  }
  public int getCableLength(){
    return cable_length;
  }

  public void updateCoords(int v0, int v1){
    coordinates[0] = v0;
    coordinates[1] = v1;
  }

  public int[] getCoords(){
    return coordinates;
  }

  @Override
  public int compareTo(Net_Link link) {
    //let's sort the employee based on an id in ascending order
    //returns a negative integer, zero, or a positive integer as this employee id
    //is less than, equal to, or greater than the specified object.
    return (this.getLatency() - link.getLatency());
  }

  @Override
  public String toString(){
    return "V: (" + getV0() + "," + getV1() + "), Type: " + getCableType() + ", Bandwidth: "
            + getBandwidth() + "gbs, Length: " + getCableLength() + "m  Latency -> " + getLatency() + "ns";
  }
}
