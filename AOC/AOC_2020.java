import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/** Solutions for Advent of Code 2020. */
public class AOC_2020
{

   /** Constructor. */
   public AOC_2020()
   {
   
      day10_2();
   
   }
   
   /** Day 10 - part 2 */
   private void day10_2()
   {
   
      final List<Long> lines = 
         Stream
            .concat
            (
               this.fetchLines("day10.txt"),
               Stream.of("0")  
            )
            .mapToLong(Long::parseLong)
            .sorted()
            .boxed()
            .toList()
            ;
      
      final Map<Integer, Long> pathsPerIndex = new HashMap<>();
      
      for (int i = lines.size() - 1; i >= 0; i--)
      {
      
         long count = 0;
      
         for (int j = i + 1; j < lines.size() && j <= i + 3; j++)
         {
         
            final long start = lines.get(i);
            final long next  = lines.get(j);
         
            if ((next - start) <= 3 && pathsPerIndex.containsKey(j))
            {
               
               count += pathsPerIndex.get(j);
               
            }
         
         }
         
         pathsPerIndex.put(i, (count == 0 ? 1 : count));
      
      }
      
      System.out.println(pathsPerIndex);
      
   }
   
   /** Day 10 - part 1 */
   private void day10_1()
   {
   
      List<Long> lines = 
         //LongStream.of(28, 33, 18, 42, 31, 14, 46, 20, 48, 47, 24, 23, 49, 45, 19, 38, 39, 11, 1, 32, 25, 35, 8, 17, 7, 9, 4, 2, 34, 10, 3)
         this.
            fetchLines("day10.txt")
               .mapToLong(Long::parseLong)
               .sorted()
               .boxed()
               .toList()
               ;
   
      Map<Integer, List<Long>> histogram = 
         Map.ofEntries(
               Map.entry(1, new ArrayList<>()),
               Map.entry(2, new ArrayList<>()),
               Map.entry(3, new ArrayList<>())
            )
            ;
            
      long previous = 0;
   
      for (long each : lines)
      {
      
         final int diff = (int)(each - previous);
      
         if (diff >= 1 && diff <= 3)
         {
         
            histogram.get(diff).add(each);
         
         }
         
         else
         {
         
            throw new IllegalStateException(each + " is an unacceptable value because it is out of range from " + previous);
         
         }
         
         previous = each;
      
      }
      
      /** Perform the final hop - a 3 point jolt bounce when connecting to the laptop. */
      List<Long> list1 = histogram.get(1);
      List<Long> list3 = histogram.get(3);
      
      long list1Max = Collections.max(list1);
      long list3Max = Collections.max(list3);
      
      long max = Math.max(list1Max, list3Max);
      
      histogram.get(3).add(max + 3);
      
      /** Print results. */
      System.out.println(histogram);
      
      System.out.println("1 - " + list1.size());
      System.out.println("3 - " + list3.size());
   
      long answer = list1.size() * list3.size();
      
      System.out.println(answer);
   
   }
   
   /**
    * 
    * Fetches lines from the listed fileName, which must include extension at the end.
    * 
    * @param   fileName    The name of the file that we are fetching the lines from.
    * @return              List of Strings, one String for each line. Line breaks not included.
    * @throw               If file cannot be found, or for other IO errors.
    * 
    */
   private Stream<String> fetchLines(String fileName)
   {
   
      try
      {
      
         return
            Files.lines(Path.of("..", "input_files", fileName))
               ;
            
      }
            
      catch (IOException ioe)
      {
            
         throw new IllegalArgumentException("Could not find the file titled " + fileName);
            
      }
   
   }

   /**
    * 
    * MAIN.
    * 
    * @param args ignore
    * 
    */
   public static void main(String[] args)
   {
   
      new AOC_2020();
   
   }

}