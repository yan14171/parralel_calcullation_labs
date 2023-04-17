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
        _bubble = new BasicBubbleSort<int>();
    }

    [Theory]
    //[TestCase(new int[] { }, new int[] { }, TestName = "Sort_EmptyArray_ReturnsEmptyArray")]
    //[TestCase(new int[] { 1 }, new int[] { 1 }, TestName = "Sort_ArrayWithOneElement_ReturnsSameArray")]
    [TestCase(new int[] { 4, 2, 1, 3, 0, -14, 5120, 23, 23, 64, 23, 12 , 4, 52, 51, -345, 3, 6, 23, 65, 2234, 99, 23 }, TestName = "Sort_ArrayOfIntegers_CorrectlySorts")]
    public async Task Sort_IntArray_ReturnsSortedArray(int[] input)
    {
        // Arrange

        // Act
        var result = await _bubble.Sort(input);

        // Assert
        Assert.That(result.SortedValues, Is.Ordered);
    }

    [Theory]
    [TestCase(1000, TestName = "Sort_ArrayOf1000Elements_SortTime")]
    [TestCase(10000, TestName = "Sort_ArrayOf10000Elements_SortTime")]
    [TestCase(50000, TestName = "Sort_ArrayOf50000Elements_SortTime")]
    public async Task Sort_IntArray_Performance(int arraySize)
    { 
        var random = new Random();
        var input = Enumerable.Range(1, arraySize).Select(i => random.Next()).ToArray();

        var stopwatch = Stopwatch.StartNew();
        var result = await _bubble.Sort(input);
        stopwatch.Stop();

        Console.WriteLine("Finished in " + result.Elapsed);
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

        var stopwatch = new Stopwatch();

        for (int i = 0; i < repeatTimes; i++)
        {
            stopwatch.Restart();
            input = Enumerable.Range(1, arraySize).Select(i => random.Next()).ToArray();
            var result = await _bubble.Sort(input);
            stopwatch.Stop();
            averageDuration += stopwatch.Elapsed;
        }

        averageDuration /= repeatTimes;
        Console.WriteLine($"{repeatTimes},{arraySize},{averageDuration}");
    }

    [Test]
    public async Task WW()
    {
        for (int i = 0; i < 10; i++)
        {
            await Console.Out.WriteLineAsync("Sending a " + i + " to a task");

            var temp = i;

            Task.Run(() => { Task.Delay(10); Console.WriteLine("Getting a " + temp + "inside"); });
        }
    }
}
