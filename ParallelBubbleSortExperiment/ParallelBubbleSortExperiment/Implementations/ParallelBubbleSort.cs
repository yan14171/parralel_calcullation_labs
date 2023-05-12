using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ParallelBubbleSortExperiment.Implementations;

public class ParallelBubbleSort : IBubble<int>
{
    public async Task<BasicSortResult<int>> Sort(IList<int> value)
    {
        List<int> lst = value.ToList();
        int noThreads = 12;
        int biggestItem = lst.Max();
        int splitFactor = biggestItem/ noThreads;
        var sw = Stopwatch.StartNew();
        
        var lists = new List<List<int>>();
        for (int i = 0; i < noThreads; i++)
        {
            lists.Add(new List<int>());
        }
        for (int j = 1; j < lists.Count; j++)
        {
            foreach (int i in lst.ToArray())
            {
                if (i <= (splitFactor * j))
                {
                    lists[j - 1].Add(i);
                    lst.Remove(i);
                }
            }
        }
        lists[lists.Count - 1].AddRange(lst);

        var tasks = new List<Task>();
        foreach (var list in lists)
        {
            var task = Task.Run(() => BubbleSort(list));
            tasks.Add(task);
        }

        await Task.WhenAll(tasks.ToArray());

        lst.Clear();
        foreach (var list in lists)
        {
            lst.AddRange(list);
        }
        sw.Stop();

        return new BasicSortResult<int>(null, lst, sw.Elapsed);
    }

    static void BubbleSort(List<int> lst)
    {
        int n = lst.Count;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n - 1; j++)
            {
                if (lst[j] > lst[j + 1])
                {
                    int temp = lst[j];
                    lst[j] = lst[j + 1];
                    lst[j + 1] = temp;
                }
            }
        }
    }
}
