using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ParallelBubbleSortExperiment
{
    public record BasicSortResult<T>(IEnumerable<T> StartingValues, IEnumerable<T> SortedValues, TimeSpan Elapsed);
}
