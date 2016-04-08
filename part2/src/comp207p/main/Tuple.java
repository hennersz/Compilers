package comp207p.main;
import java.util.*;

public class Tuple<X,Y>
{
  private X first;
  private Y second;

  public Tuple(X first, Y second)
  {
    this.first = first;
    this.second = second;
  }

  public X getFirst(){return first;}
  public Y getSecond(){return second;}

  @Override public String toString()
  {
      StringBuilder result = new StringBuilder();

      result.append(first);

      return result.toString();
  }
}
