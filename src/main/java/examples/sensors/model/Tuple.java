package examples.sensors.model;

import java.util.Objects;

public class Tuple<T0, T1>
{
    public final T0 first;
    public final T1 second;

    public Tuple(T0 first, T1 second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(first, tuple.first) && Objects.equals(second, tuple.second);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(first, second);
    }
}
