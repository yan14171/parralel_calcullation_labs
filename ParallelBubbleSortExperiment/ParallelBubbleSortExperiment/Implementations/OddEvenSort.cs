using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ParallelBubbleSortExperiment.Implementations;

public class OddEvenSort<T> : IBubble<T> where T : IComparable<T>
{
    public async Task<BasicSortResult<T>> Sort(IList<T> lst)
    {
        Stopwatch sw = Stopwatch.StartNew();
        bool isOrdered;
        do
        {
            isOrdered = true;

            List<Task> tasks = new List<Task>();

            for (int j = 0; j < lst.Count / 2 - 1; j++)
            {
                int index = j;
                Task task = Task.Run(() =>
                {
                    if (lst[2 * index + 1].CompareTo(lst[2 * index + 2]) > 0)
                    {
                        isOrdered = false;
                        swap(lst, 2 * index + 1, 2 * index + 2);
                    }
                });
                tasks.Add(task);
            }

            await Task.WhenAll(tasks);

            tasks.Clear();

            for (int k = 0; k < lst.Count / 2 - 1; k++)
            {
                int index = k;
                Task task = Task.Run(() =>
                {
                    if (lst[2 * index].CompareTo(lst[2 * index + 1]) > 0)
                    {
                        isOrdered = false;
                        swap(lst, 2 * index, 2 * index + 1);
                    }
                });
                tasks.Add(task);
            }

            await Task.WhenAll(tasks);

        } while (!isOrdered);


        sw.Stop();
        return new BasicSortResult<T>(null, lst, sw.Elapsed);

        void swap(IList<T> list, int i, int j)
        {
            T temp = list[i];
            list[i] = list[j];
            list[j] = temp;
        }
    }
}
