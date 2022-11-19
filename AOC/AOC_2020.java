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
   
      day11_1();
   
   }

   /** Day 11 - part 1 */
   private void day11_1()
   {
   
      final List<List<String>> grid =
         this.fetchFromFileAsStringGrid("day11.txt");
   
      final int rowLimit = grid.size();
   
      List<List<String>> oldGrid = new ArrayList<>();
      List<List<String>> newGrid = grid;
      
      int iteration = 1;
      
      record Grid(List<List<String>> temp)
      {
      
         public int numberOfOccupiedSeats()
         {
         
            int countOfSeats = 0;
            
            for (List<String> eachRow : temp)
            {
            
               for (String eachCell : eachRow)
               {
               
                  if (eachCell.equals("#"))
                  {
                  
                     countOfSeats++;
                     
                  }
               
               }
            
            }
            
            return countOfSeats;
         
         }
      
         public String toString()
         {
         
            String output = "";
         
            for (List<String> eachRow : temp)
            {
            
               for (String eachCell : eachRow)
               {
               
                  output+= eachCell;
               
               }
            
               output += "\n";
            
            }
            
            return output;
         
         }
      
      }
      
      while (!oldGrid.equals(newGrid))
      {
      
         System.out.println("Iteration " + iteration++);
      
         oldGrid = newGrid;
         newGrid = new ArrayList<>();
         
         for (int row = 0; row < rowLimit; row++)
         {
         
            newGrid.add(new ArrayList<>());
         
            final int columnLimit = oldGrid.get(row).size();
         
            for (int column = 0; column < columnLimit; column++)
            {
            
               final String newCharacter = day11_1_calculateNewValue(oldGrid, row, column, rowLimit, columnLimit);
            
               newGrid.get(row).add(column, newCharacter);
            
            }
         
         }
      
         var newViewer = new Grid(newGrid);
         
         System.out.print("");
      
      }
      
      System.out.println("done");
      System.out.println(new Grid(newGrid).numberOfOccupiedSeats());
     
   }

   private String day11_1_calculateNewValue(List<List<String>> grid, int row, int column, int rowLimit, int columnLimit)
   {
   
      final String currentCell = grid.get(row).get(column);
   
      if (currentCell.equals("."))
      {
      
         return currentCell;
      
      }
   
      int surroundingNumberOfPeople = 0;
   
      for (int nextRow = row - 1; nextRow <= row + 1; nextRow++)
      {
      
         for (int nextColumn = column - 1; nextColumn <= column + 1; nextColumn++)
         {
         
            if (this.day11_1_isAdjacent(nextRow, nextColumn, rowLimit, columnLimit, row, column))
            {
            
               final String adjacentCell = grid.get(nextRow).get(nextColumn);
            
               if (adjacentCell.equals("#"))
               {
               
                  surroundingNumberOfPeople++;
               
               }
            
            }
         
         }
      
      }
   
      if (currentCell.equals("L") && surroundingNumberOfPeople == 0)
      {
      
         return "#";
      
      }
      
      else if (currentCell.equals("#") && surroundingNumberOfPeople >= 4)
      {
      
         return "L";
      
      }
      
      else
      {
      
         return currentCell;
      
      }
   
   }

   private boolean day11_1_isAdjacent(int nextRow, int nextColumn, int rowLimit, int columnLimit, int row, int column)
   {
   
      return
            nextRow < rowLimit
         && nextRow >= 0
         && nextColumn < columnLimit
         && nextColumn >= 0
         &&
            (
                  nextRow != row
               || nextColumn != column
            )
         ;
   
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
    * Fetches lines from the listed fileName, which must include extension at the end.
    *
    * @param   fileName    The name of the file that we are fetching the lines from.
    * @return              List of Strings, one String for each line. Line breaks not included.
    * @throw               If file cannot be found, or for other IO errors.
    *
    */
   private List<List<String>> fetchFromFileAsStringGrid(String fileName)
   {
   
      try
      {
      
         final List<String> list =
            Files
               .lines(Path.of("..", "input_files", fileName))
               .toList()
               ;
      
         final List<List<String>> result = new ArrayList<>();
      
         for (String each : list)
         {
         
            result.add(List.of(each.split("")));
         
         }
      
         return List.copyOf(result);
      
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