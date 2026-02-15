package searchclient;

import java.util.Comparator;

public abstract class Heuristic
        implements Comparator<State>
{
    protected int goalRow;
    protected int goalCol;

    public Heuristic(State initialState)
    {
             // Find goal position of agent 0
        for (int r = 0; r < State.goals.length; r++) {
            for (int c = 0; c < State.goals[r].length; c++) {
                if (State.goals[r][c] == '0') {
                    goalRow = r;
                    goalCol = c;
                }
            }
        }
}


public int h(State s)
{
    int unsatisfiedGoals = 0;

    for (int row = 1; row < State.goals.length - 1; row++)
    {
        for (int col = 1; col < State.goals[row].length - 1; col++)
        {
            char goal = State.goals[row][col];

            // Agent goal
            if ('0' <= goal && goal <= '9')
            {
                int agent = goal - '0';
                if (!(s.agentRows[agent] == row && s.agentCols[agent] == col))
                {
                    unsatisfiedGoals++;
                }
            }

            // Box goal (future-proofing)
            else if ('A' <= goal && goal <= 'Z')
            {
                if (s.boxes[row][col] != goal)
                {
                    unsatisfiedGoals++;
                }
            }
        }
    }

    // Debug printing (for testing small levels only!)
    // System.err.println("State:\n" + s);
    // System.err.println("h(s) = " + unsatisfiedGoals);
    // System.err.println("--------------------");

    return unsatisfiedGoals;
}

    public abstract int f(State s);

    @Override
    public int compare(State s1, State s2)
    {
        return this.f(s1) - this.f(s2);
    }
}

class HeuristicAStar
        extends Heuristic
{
    public HeuristicAStar(State initialState)
    {
        super(initialState);
    }

    @Override
    public int f(State s)
    {
        return s.g() + this.h(s);
    }

    @Override
    public String toString()
    {
        return "A* evaluation";
    }
}

class HeuristicWeightedAStar
        extends Heuristic
{
    private int w;

    public HeuristicWeightedAStar(State initialState, int w)
    {
        super(initialState);
        this.w = w;
    }

    @Override
    public int f(State s)
    {
        return s.g() + this.w * this.h(s);
    }

    @Override
    public String toString()
    {
        return String.format("WA*(%d) evaluation", this.w);
    }
}

class HeuristicGreedy
        extends Heuristic
{
    public HeuristicGreedy(State initialState)
    {
        super(initialState);
    }

    @Override
    public int f(State s)
    {
        return this.h(s);
    }

    @Override
    public String toString()
    {
        return "greedy evaluation";
    }
}
