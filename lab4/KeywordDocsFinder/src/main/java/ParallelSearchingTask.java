import java.util.*;
import java.util.concurrent.RecursiveTask;

public class ParallelSearchingTask extends RecursiveTask<Set<String>> {
  private final List<Document> documents;
  private List<String> itKeys = Arrays.asList("Artificial Intelligence", "Cybersecurity", "Machine Learning", "Data Science", "Cloud Computing",
          "Internet of Things", "Virtual Reality", "Big Data", "Blockchain", "Computer Networking",
          "Software Development", "User Experience Design", "Mobile Application Development",
          "Operating Systems", "Database Management", "Web Development", "Computer Graphics",
          "Information Systems", "Human-Computer Interaction", "Game Development", "Programming Languages",
          "Algorithms and Data Structures", "Computer Architecture", "Computer Organization",
          "Object-Oriented Programming", "Operating Systems Concepts", "Computer Networks and Communications",
          "Information Technology Management", "Web Design and Development Fundamentals", "Software Testing and Quality Assurance",
          "Data Modeling and Database Design", "Computer Graphics Principles and Practice", "Digital Signal Processing",
          "Multimedia Computing", "Artificial Neural Networks", "Computer Vision and Image Processing",
          "Internet Technologies and Applications", "Systems Analysis and Design", "Project Management for IT",
          "IT Governance and Risk Management", "Coding", "Cloud", "Cyber", "Data", "Security", "IoT", "VR", "AI",
          "ML", "Blockchain", "UX", "Web Dev", "Mobile", "Network", "OS", "Database", "Graphics", "IT Management", "Programming",
          "Software Testing", "IT Governance");

  public ParallelSearchingTask(List<Document> documents) {
    this.documents = documents;
  }

  @Override
  protected Set<String> compute(){
    List<RecursiveTask<Set<String>>> forks = new ArrayList<>();

    if(this.documents.stream().count() > 1)
    {
      for(Document doc : documents)
      {
        ArrayList<Document> singleDoc = new ArrayList<>();
        singleDoc.add(doc);
        ParallelSearchingTask task =
                new ParallelSearchingTask(singleDoc);
        forks.add(task);
        task.fork();
      }
    }
    else
    {
      List<String> documentLines = documents.get(0).getLines();
      Set<String> result = new HashSet<>();
      for(String documentLine : documentLines)
      {
       List<String> words = getWordsFromLine(documentLine);
       if(words.stream().anyMatch(n -> itKeys.contains(n)))
       {
         result.add(documents.get(0).getDocumentName());
         break;
       }
      }
      return result;
    }

      Set<String> result = forks.get(0).join();
      forks.remove(0);
      for (RecursiveTask<Set<String>> task : forks) {
        result.addAll(task.join());
      }
      return result;
  }

  private static List<String> getWordsFromLine(String line) {
    String[] words = line.trim().split("\\s+");
    return Arrays.asList(words);
  }
}
