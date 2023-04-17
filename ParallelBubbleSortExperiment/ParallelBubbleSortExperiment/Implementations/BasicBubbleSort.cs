using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ParallelBubbleSortExperiment.Implementations
{
    public class BasicBubbleSort<T> : IBubble<T> where T : IComparable<T>
    {
        public Task<BasicSortResult<T>> Sort(IList<T> values)
        {
            int n = values.Count;

            Stopwatch sw = Stopwatch.StartNew();

            for (int i = 0; i < n - 1; i++)
            {
                for (int j = 0; j < n - i - 1; j++)
                {
                    if (values[j].CompareTo(values[j + 1]) > 0)
                    {
                        T temp = values[j];
                        values[j] = values[j + 1];
                        values[j + 1] = temp;
                    }
                }
            }

            sw.Stop(); // Stop stopwatch
            Console.WriteLine($"Bubble sort elapsed time: {sw.ElapsedMilliseconds} ms");

            return Task.FromResult(new BasicSortResult<T>(null, values, sw.Elapsed));
        }
    }
}
