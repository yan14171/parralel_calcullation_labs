using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ParallelBubbleSortExperiment.Implementations;

public class OddEvenSort<T> : IBubble<T> where T : IComparable<T>
{
    public async Task<BasicSortResult<T>> Sort(IList<T> list)
    {
        Stopwatch sw = Stopwatch.StartNew();
        int n = list.Count;
        bool sorted = false;
        while (!sorted)
        {
            sorted = true;
            // Odd phase
            Parallel.For(1, n - 1, i =>
            {
                if (i % 2 == 1 && list[i].CompareTo(list[i + 1]) > 0)
                {
                    swap(list, i, i + 1);
                    sorted = false;
                }
            });
            // Even phase
            Parallel.For(0, n - 1, i =>
            {
                if (i % 2 == 0 && list[i].CompareTo(list[i + 1]) > 0)
                {
                    swap(list, i, i + 1);
                    sorted = false;
                }
            });
        }

        sw.Stop();
        return new BasicSortResult<T>(null, list, sw.Elapsed);

        void swap<T>(IList<T> list, int i, int j)
        {
            T temp = list[i];
            list[i] = list[j];
            list[j] = temp;
        }
    }
}
