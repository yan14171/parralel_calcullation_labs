using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ParallelBubbleSortExperiment
{
    public interface IBubble<T> where T : IComparable<T>
    {
        Task<BasicSortResult<T>> Sort(IList<T> values);
        
    }
}
