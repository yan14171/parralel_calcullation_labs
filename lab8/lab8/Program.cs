using Microsoft.AspNetCore.Builder;
using Newtonsoft.Json;
using System.Linq;
using System.Text;

const int ARRAY_SIZE = 1000;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers().AddNewtonsoftJson();
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(builder =>
    {
        builder.AllowAnyOrigin()
               .AllowAnyHeader()
               .AllowAnyMethod();
    });
});

var app = builder.Build();

app.UseCors();

app.MapGet("/", async (context) =>
{
    int[,] matrix1 = MatrixMultiplication.GenerateRandomMatrix(ARRAY_SIZE, ARRAY_SIZE);
    int[,] matrix2 = MatrixMultiplication.GenerateRandomMatrix(ARRAY_SIZE, ARRAY_SIZE);
    int[,] result = MatrixMultiplication.Multiply(matrix1, matrix2);

    var responseJson = JsonConvert.SerializeObject(result);
    context.Response.ContentType = "application/json";
    await context.Response.WriteAsync(responseJson);
});
app.MapPost("/", async context =>
{
    var requestBodyJson = await new StreamReader(context.Request.Body).ReadToEndAsync();
    var requestBody = JsonConvert.DeserializeObject<MatrixRequestBody>(requestBodyJson);

    int[,] matrix1 = requestBody.Matrix1;
    int[,] matrix2 = requestBody.Matrix2;

    int[,] result = MatrixMultiplication.Multiply(matrix1, matrix2);

    var responseJson = JsonConvert.SerializeObject(result);
    context.Response.ContentType = "application/json";
    await context.Response.WriteAsync(responseJson);
});

app.Run();

public class MatrixMultiplication
{
    public static int[,] Multiply(int[,] matrix1, int[,] matrix2)
    {
        int rows1 = matrix1.GetLength(0);
        int cols1 = matrix1.GetLength(1);
        int rows2 = matrix2.GetLength(0);
        int cols2 = matrix2.GetLength(1);

        if (cols1 != rows2)
        {
            throw new ArgumentException("Matrices cannot be multiplied: number of columns in first matrix must be equal to the number of rows in second matrix");
        }

        int[,] result = new int[rows1, cols2];

        Parallel.For(0, rows1, i =>
        {
            for (int j = 0; j < cols2; j++)
            {
                int sum = 0;
                for (int k = 0; k < cols1; k++)
                {
                    sum += matrix1[i, k] * matrix2[k, j];
                }
                result[i, j] = sum;
            }
        });

        return result;
    }

    public static int[,] GenerateRandomMatrix(int rows, int cols)
    {
        int[,] matrix = new int[rows, cols];
        Random random = new Random();

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrix[i, j] = random.Next(10);
            }
        }

        return matrix;
    }
}


internal record MatrixRequestBody(int[,] Matrix1, int[,] Matrix2);