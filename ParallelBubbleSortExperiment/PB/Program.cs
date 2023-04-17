using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

class Program
{
    static async Task Main()
    {
        var startTime = DateTime.Now;

        var lst = new List<int>();
        var rnd = new Random();
        for (int i = 0; i < 50000; i++)
        {
            lst.Add(rnd.Next(0, 50000));
        }

        await ParallelBubbleSort(lst);

        var endTime = DateTime.Now - startTime;
        Assert.That(lst, Is.Ordered);
        Console.WriteLine("--- Sorted in {0} seconds ---", endTime.TotalSeconds);

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

    static async Task ParallelBubbleSort(List<int> lst)
    {
        int noThreads = Environment.ProcessorCount * 2;
        int biggestItem = lst.Max();
        int splitFactor = biggestItem / noThreads;
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

        Assert.That(lst, Is.Ordered);
    }

    static void NaiveBubbleSort(List<int> lst)
    {
        bool isOrdered;
        do
        {
            isOrdered = true;
            for (int j = 0; j < lst.Count/2 - 1; j++)
            {
                if (lst[2 * j + 1] > lst[2 * j + 2])
                {
                    isOrdered = false;
                    swap(lst, 2 * j + 1, 2 * j + 2);
                }
            }

            for (int k = 0; k < lst.Count/2 - 1; k++)
            {
                if (lst[2 * k] > lst[2 * k + 1])
                {
                    isOrdered = false;
                    swap(lst, 2 * k, 2 * k + 1);
                }
            }
        } while(!isOrdered);
        void swap (IList<int> list, int i, int j)
        {
            int temp = list[i];
            list[i] = list[j];
            list[j] = temp;
        }
    }
}
