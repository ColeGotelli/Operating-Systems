import java.util.Scanner;
import java.io.File;

class Sudoku 
{
	public static void main(String[] args) {
		try {
			Scanner file = new Scanner(new File(args[0]));
			file.useDelimiter(",");

			int[][] grid = new int[9][9];

			while(file.hasNextLine()) {
				for(int i = 0; i < 9; ++i) {
					for(int j = 0; j < 9; ++j) {
						String temp = file.next();
						grid[i][j] = Integer.parseInt(temp.replaceAll("[\\D]", ""));
						System.out.print(grid[i][j]);

					}
					System.out.println();
				}
			}		
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
}
