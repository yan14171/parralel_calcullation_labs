const matrixSize = 1000;
const minCellValue = 0;
const maxCellValue = 1000;
const matrix1 = Array.from({ length: matrixSize }, () =>
  Array.from({ length: matrixSize }, () =>
    Math.floor(Math.random() * (maxCellValue - minCellValue + 1)) + minCellValue
  )
);
const matrix2 = Array.from({ length: matrixSize }, () =>
  Array.from({ length: matrixSize }, () =>
    Math.floor(Math.random() * (maxCellValue - minCellValue + 1)) + minCellValue
  )
);
const requestBody = {
  matrix1: matrix1,
  matrix2: matrix2
};



startTime = performance.now();

fetch('http://localhost:5223/', {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json'
  },
})
  .then(response => response.json())
  .then(async result => {
    const endTime = performance.now();
    const timeTaken = endTime - startTime;
    //console.log('Result:', result);
    console.log('Time taken for the parameterless:', timeTaken, 'ms');
    await testMatrixMultiplication();
  })


function testBody() {
  startTime = Date.now();
  fetch('http://localhost:5223/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestBody)
  })
    .then(response => response.json())
    .then(result => {
      const endTime = Date.now();
      const timeTaken = endTime - startTime;
      console.log('Result:', result);
      console.log('Time taken:', timeTaken, 'ms');
    })
    .catch(error => console.error(error));
}

async function testMatrixMultiplication() {
  const matrixSizes = [1600];
  const numTests = 5;
  const minCellValue = 0;
  const maxCellValue = 5000;

  for (const size of matrixSizes) {
    // Generate two random matrices
    const matrix1 = Array.from({ length: size }, () =>
      Array.from({ length: size }, () =>
        Math.floor(Math.random() * (maxCellValue - minCellValue + 1)) + minCellValue
      )
    );
    const matrix2 = Array.from({ length: size }, () =>
      Array.from({ length: size }, () =>
        Math.floor(Math.random() * (maxCellValue - minCellValue + 1)) + minCellValue
      )
    );

    // Send the matrices to the API for multiplication
    const requestBody = {
      matrix1: matrix1,
      matrix2: matrix2
    };

    let totalTime = 0;
    for (let i = 0; i < numTests; i++) {

      let bodyJson = JSON.stringify(requestBody);
      const startTime = Date.now();

      const response = await fetch('http://localhost:5223/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: bodyJson
      });

      const endTime = Date.now();
      totalTime += endTime - startTime;

      console.log(response.json());
      console.log(`[${size}x${size}] Test ${i + 1}: ${endTime - startTime}`);
    }

    const averageTime = totalTime / numTests;
    console.log(`[${size}x${size}] Average time:`, averageTime, 'ms');
  }
}















function bubbleSort(arr): 
  n = length of arr
  for i = 0 to n - 1: 
    for j = 0 to n - i - 2: 
      if arr[j] > arr[j + 1]: 
        swap arr[j] with arr[j + 1]
    return arr 

    function oddEvenSort(arr):
      isSorted = false 
      n = length of arr 
      while isSorted == false 
        isSorted = true 
        for i = 1 to n-1 step 2 
          if arr[i] > arr[i+1] 
            swap arr[i] with arr[i+1] 
            isSorted = false 
        for i = 2 to n-1 step 2 
          if arr[i] > arr[i+1] 
            swap arr[i] with arr[i+1] 
            isSorted = false 
      end while 
      return arr


      function parallelBubbleSort(arr): 
      n = length of arr
      sorted = false 
      while sorted == false: 
        sorted = true 
        parallel for i = 1 to n-1 step 2: 
          if arr[i] > arr[i+1]: 
            swap arr[i] with arr[i+1] 
            sorted = false 
        parallel for i = 2 to n-1 step 2: 
          if arr[i] > arr[i+1]: 
            swap arr[i] with arr[i+1]
            sorted = false 
      end while
      return arr 
      
