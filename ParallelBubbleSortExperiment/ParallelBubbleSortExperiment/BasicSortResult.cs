namespace ParallelBubbleSortExperiment
{
    public record BasicSortResult<T>(IEnumerable<T> StartingValues, IEnumerable<T> SortedValues, TimeSpan Elapsed);
}
