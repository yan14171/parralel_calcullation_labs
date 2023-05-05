using ParallelBubbleSortExperiment;
using ParallelBubbleSortExperiment.Implementations;
using System.Diagnostics;

[TestFixture]
public class BubbleSortTests
{
    private IBubble<int> _bubble;
    [SetUp]
    public void Setup()
    {
        _bubble = new ParallelBubbleSort();
    }

    [Theory]
    [TestCase(10, TestName = "Sort_ArrayOf1000Elements_Correctness")]
    [TestCase(1000, TestName = "Sort_ArrayOf1000Elements_Correctness")]
    [TestCase(10000, TestName = "Sort_ArrayOf10000Elements_Correctness")]
    [TestCase(50000, TestName = "Sort_ArrayOf50000Elements_Correctness")]
    public async Task Sort_IntArray_Correctness(int arraySize)
    { 
        var random = new Random();
        var input = Enumerable.Range(1, arraySize).Select(i => random.Next()).ToArray();

        var result = await _bubble.Sort(input);
       
        Assert.That(result.SortedValues, Is.Ordered);
    }

    [Theory]
    [TestCase(10_000,10)]
    [TestCase(20_000,10)]
    [TestCase(50_000,10)]
    [TestCase(100_000, 10)]
    public async Task Sort_IntArray_Experiment(int arraySize, int repeatTimes)
    {
        var random = new Random();
        int[] input;
        TimeSpan averageDuration = TimeSpan.Zero;


        for (int i = 0; i < repeatTimes; i++)
        {
            input = Enumerable.Range(1, arraySize).Select(i => random.Next()).ToArray();
            var result = await _bubble.Sort(input);
            averageDuration += result.Elapsed;
        }

        averageDuration /= repeatTimes;
        Console.WriteLine($"{repeatTimes},{arraySize},{averageDuration}");
    }
}
