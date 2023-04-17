using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ParallelBubbleSortExperiment.Implementations;

public class ParallelBubbleSort<T> : IBubble<T> where T : IComparable<T>
{
    public async Task<BasicSortResult<T>> Sort(IList<T> value)
    {
        var arr = value.ToArray();

        int numThreads = Math.Min(Environment.ProcessorCount, arr.Length);

        // Divide the input array into numThreads pieces
        int chunkSize = (int)Math.Ceiling(arr.Length / (double)numThreads);

        var sw = Stopwatch.StartNew();
        Task[] tasks = new Task[numThreads];
        for (int i = 0; i < numThreads; i++)
        {
            int startIndex = i * chunkSize;
            int endIndex = Math.Min(startIndex + chunkSize, arr.Length);
            tasks[i] = Task.Run(() => SortChunk(arr, startIndex, endIndex));
        }

        // Wait for all worker threads to finish
        await Task.WhenAll(tasks);

        // Merge the sorted pieces of the input array
        for (int i = 0; i < numThreads - 1; i++)
        {
            int leftStart = i * chunkSize;
            int leftEnd = Math.Min(leftStart + chunkSize, arr.Length);
            int rightStart = (i + 1) * chunkSize;
            int rightEnd = Math.Min(rightStart + chunkSize, arr.Length);

            T[] merged = new T[leftEnd - leftStart + rightEnd - rightStart];
            int leftIndex = leftStart;
            int rightIndex = rightStart;
            int mergeIndex = 0;

            while (leftIndex < leftEnd && rightIndex < rightEnd)
            {
                if (arr[leftIndex].CompareTo(arr[rightIndex]) <= 0)
                {
                    merged[mergeIndex++] = arr[leftIndex++];
                }
                else
                {
                    merged[mergeIndex++] = arr[rightIndex++];
                }
            }

            while (leftIndex < leftEnd)
            {
                merged[mergeIndex++] = arr[leftIndex++];
            }

            while (rightIndex < rightEnd)
            {
                merged[mergeIndex++] = arr[rightIndex++];
            }

            Array.Copy(merged, 0, arr, leftStart, merged.Length);
        }
        sw.Stop();

        return new BasicSortResult<T>(null, arr, sw.Elapsed);
    }

    private static void SortChunk<T>(T[] arr, int startIndex, int endIndex) where T : IComparable<T>
    {
        for (int i = startIndex; i < endIndex - 1; i++)
        {
            for (int j = startIndex; j < endIndex - i - 1 + startIndex; j++)
            {
                if (arr[j].CompareTo(arr[j + 1]) > 0)
                {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
