import java.util.*;
import java.io.*;
import java.lang.Math;
public class WordSearch{
  public static void main(String[] args) {
    WordSearch puzzle = new WordSearch(15, 15, "words.txt");
    System.out.println(puzzle);
  }
    private char[][]data;

//the random seed used to produce this WordSearch
    private int seed;

//a random Object to unify your random calls
    private Random randgen;

//all words from a text file get added to wordsToAdd, indicating that they have not yet been added
    private ArrayList<String> wordsToAdd = new ArrayList<String>();

//all words that were successfully added get moved into wordsAdded.
    private ArrayList<String> wordsAdded = new ArrayList<String>();

    /**Initialize the grid to the size specified
     *and fill all of the positions with '_'
     *@param row is the starting height of the WordSearch
     *@param col is the starting width of the WordSearch
     */
    public WordSearch(int rows,int cols, String fileName){
      if(rows<0||cols<0){
        throw new IllegalArgumentException("rows or cols cannot be negative");
      }
      try{
        File f = new File(fileName);
        Scanner in = new Scanner(f);
        while(in.hasNextLine()){
          wordsToAdd.add(in.nextLine());
        }
      }catch(FileNotFoundException e){
         System.out.println("File not found: " + fileName);
      }
      data = new char[rows][cols];
      for(int x = 0; x < data.length; x++){
        for(int y = 0; y < data[x].length; y++){
          data[x][y] = '_';
        }
      }
      randgen = new Random();
      addAllWords();
    }
    public WordSearch(int rows, int cols, String fileName, int randSeed){
      if(rows<0||cols<0){
        throw new IllegalArgumentException("rows or cols cannot be negative");
      }
      try{
        File f = new File(fileName);
        Scanner in = new Scanner(f);
        while(in.hasNextLine()){
          wordsToAdd.add(in.nextLine());
        }
      }catch(FileNotFoundException e){
         System.out.println("File not found: " + fileName);
      }
      seed = randSeed;
      data = new char[rows][cols];
      for(int x = 0; x < data.length; x++){
        for(int y = 0; y < data[x].length; y++){
          data[x][y] = '_';
        }
      }
    }

    /**Set all values in the WordSearch to underscores'_'*/
    private void clear(){
      for(int x = 0; x < data.length; x++){
        for(int y = 0; y < data[x].length; y++){
          data[x][y] = '_';
        }
      }
    }

    /**Each row is a new line, there is a space between each letter
     *@return a String with each character separated by spaces, and rows
     *separated by newlines.
     */
    public String toString(){
      String grid="";
      for(int x = 0; x < data.length; x++){
        grid+="| ";
        for(int y = 0; y < data[x].length; y++){
          if(y < data[x].length){
            grid += data[x][y] + " ";
          }
        }
        grid += "|\n";
      }
      grid+="Words: ";
      for(int i = 0; i < wordsAdded.size(); i++){
        grid+=wordsAdded.get(i)+" ";
      }
      grid+="(seed: " + seed + ")";
      return grid;
    }
    /**Attempts to add a given word to the specified position of the WordGrid.
     *The word is added in the direction rowIncrement,colIncrement
     *Words must have a corresponding letter to match any letters that it overlaps.
     *
     *@param word is any text to be added to the word grid.
     *@param row is the vertical locaiton of where you want the word to start.
     *@param col is the horizontal location of where you want the word to start.
     *@param rowIncrement is -1,0, or 1 and represents the displacement of each letter in the row direction
     *@param colIncrement is -1,0, or 1 and represents the displacement of each letter in the col direction
     *@return true when: the word is added successfully.
     *        false when: the word doesn't fit, OR  rowchange and colchange are both 0,
     *        OR there are overlapping letters that do not match
     */
    private boolean addWord(String word,int row, int col, int rowIncrement, int colIncrement){
      if(rowIncrement==0 && colIncrement==0){
        return false;
      }
      if(col<0||row<0||col>=data[row].length||row>=data.length){
        return false;
      }
      int c = 0;
      if(rowIncrement!=0){
        for(int x = row; x < data.length && x>0; x += rowIncrement){//problem encountered
          c++;
        }
        if(word.length()>c){
          return false;
        }else{
          c = 0;
        }
      }
      if(colIncrement!=0){
        for(int y = col; y < data[row].length && y>0; y += colIncrement){//problem encountered
          c++;
        }
        if(word.length()>c){
          return false;
        }
      }
      for(int i = 0; i < word.length(); i++){
        if(data[row+i*rowIncrement][col+i*colIncrement] != word.charAt(i) && data[row+i*rowIncrement][col+i*colIncrement] != '_'){
          return false;
        }
      }
      for(int i = 0; i < word.length(); i++){
        data[row+i*rowIncrement][col+i*colIncrement] = word.charAt(i);
      }
      return true;
    }

    /*[rowIncrement,colIncrement] examples:
     *[-1,1] would add up and the right because (row -1 each time, col + 1 each time)
     *[ 1,0] would add downwards because (row+1), with no col change
     *[ 0,-1] would add towards the left because (col - 1), with no row change
     */
    private void addAllWords(){
      int rows = data.length;
      int cols = data[0].length;
      for(int j = 0; j < wordsToAdd.size(); j++){
        if(wordsToAdd.get(j).length() > rows && wordsToAdd.get(j).length() > cols){
          wordsAdded.add(wordsToAdd.remove(j));
        }
      }
      for(int i = 0; i < 1000 && wordsToAdd.size() != 0; i++){
        int x = Math.abs(randgen.nextInt()%wordsToAdd.size());
        if(addWord(wordsToAdd.get(x), Math.abs(randgen.nextInt()%rows), Math.abs(randgen.nextInt()%cols), randgen.nextInt()%2, randgen.nextInt()%2)){
          wordsAdded.add(wordsToAdd.remove(x));
        }
      }
    }



    /**Attempts to add a given word to the specified position of the WordGrid.
     *The word is added from left to right, must fit on the WordGrid, and must
     *have a corresponding letter to match any letters that it overlaps.
     *
     *@param word is any text to be added to the word grid.
     *@param row is the vertical locaiton of where you want the word to start.
     *@param col is the horizontal location of where you want the word to start.
     *@return true when the word is added successfully. When the word doesn't fit,
     * or there are overlapping letters that do not match, then false is returned
     * and the board is NOT modified.
     */
    public boolean addWordHorizontal(String word,int row, int col){
      if(row<0||col<0||row>=data.length||word.length()>data[row].length-col){
        return false;
      }
      for(int i = 0; i < word.length(); i++){
        if(data[row][col+i] != word.charAt(i) && data[row][col+i] != '_'){
          return false;
        }
      }
      for(int i = 0; i < word.length(); i++){
        data[row][col+i] = word.charAt(i);
      }
      return true;
    }

   /**Attempts to add a given word to the specified position of the WordGrid.
     *The word is added from top to bottom, must fit on the WordGrid, and must
     *have a corresponding letter to match any letters that it overlaps.
     *
     *@param word is any text to be added to the word grid.
     *@param row is the vertical locaiton of where you want the word to start.
     *@param col is the horizontal location of where you want the word to start.
     *@return true when the word is added successfully. When the word doesn't fit,
     *or there are overlapping letters that do not match, then false is returned.
     *and the board is NOT modified.
     */
    public boolean addWordVertical(String word,int row, int col){
      if(col<0||row<0||col>=data[row].length||word.length()>data.length-row){
        return false;
      }
      for(int i = 0; i < word.length(); i++){
        if(data[row+i][col] != word.charAt(i) && data[row+i][col] != '_'){
          return false;
        }
      }
      for(int i = 0; i < word.length(); i++){
        data[row+i][col] = word.charAt(i);
      }
      return true;
    }
    /**Attempts to add a given word to the specified position of the WordGrid.
     *The word is added from top left to bottom right, must fit on the WordGrid,
     *and must have a corresponding letter to match any letters that it overlaps.
     *
     *@param word is any text to be added to the word grid.
     *@param row is the vertical locaiton of where you want the word to start.
     *@param col is the horizontal location of where you want the word to start.
     *@return true when the word is added successfully. When the word doesn't fit,
     *or there are overlapping letters that do not match, then false is returned.
     */
    public boolean addWordDiagonal(String word, int row, int col){
      if(col<0||row<0||col>=data[row].length||word.length()>data.length-row
      ||row>=data.length||word.length()>data[row].length-col){
        return false;
      }
      for(int i = 0; i < word.length(); i++){
        if(data[row+i][col+i] != word.charAt(i) && data[row+i][col+i] != '_'){
          return false;
        }
      }
      for(int i = 0; i < word.length(); i++){
        data[row+i][col+i] = word.charAt(i);
      }
      return true;
    }
}
