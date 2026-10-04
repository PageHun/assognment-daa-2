public class Metrics {
    private long steps;
    private long moves;
    private long comparisons;

    public void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
    }

    public void addStep() {
        steps++;
    }

    public void addSteps(long n) {
        steps += n;
    }

    public void addMove() {
        moves++;
    }

    public void addMoves(long n) {
        moves += n;
    }

    public void addComparison() {
        comparisons++;
    }

    public void addComparisons(long n) {
        comparisons += n;
    }

    public long getSteps() {
        return steps;
    }

    public long getMoves() {
        return moves;
    }

    public long getComparisons() {
        return comparisons;
    }

    public Metrics copy() {
        Metrics m = new Metrics();
        m.steps = this.steps;
        m.moves = this.moves;
        m.comparisons = this.comparisons;
        return m;
    }

    @Override
    public String toString() {
        return "steps=" + steps + ", moves=" + moves + ", comparisons=" + comparisons;
    }
}
