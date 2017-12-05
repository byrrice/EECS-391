import java.util.*;

public class RubikTwo {

    //representation of the rubik's cube
    public char[] cube = new char[24];

    //the goalcube
    public char[] goalcube = "yyyyggggoooobbbbrrrrwwww".toCharArray();

    //maxnodes
    public long maxNodes = 10000;

    //initialize move count
    static int movecount = 0;

    //print out movelist
    static String movelist = "";

    //manhattan distance table
    int[][] table = new int[][]{
            {0, 1, 2, 1, 2, 3, 2, 1},
            {1, 0, 1, 2, 3, 2, 1, 2},
            {2, 1, 0, 1, 2, 1, 2, 3},
            {1, 2, 1, 0, 1, 2, 3, 2},
            {2, 3, 2, 1, 0, 1, 2, 1},
            {3, 2, 1, 2, 1, 0, 1, 2},
            {2, 1, 2, 3, 2, 1, 0, 1},
            {1, 2, 1, 0, 1, 2, 3, 2}};

    //default constructor makes solved rubik's cube
    public RubikTwo() {
        this("yyyyggggoooobbbbrrrrwwww");
    }

    //constructor for rubik's cube that takes in String
    public RubikTwo(String colors) {
        for (int i = 0; i < colors.length(); i++) {
            cube[i] = colors.charAt(i);
        }
    }

    //computes hashcode of the character array
    @Override
    public int hashCode() {
        return this.cube.hashCode();
    }

    //Method determining whether two Rubik's cubes are equal
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RubikTwo) && Arrays.equals(this.cube, ((RubikTwo) obj).cube);
    }

    public void setState(String s) {
        for (int i = 0; i < s.length(); i++) {
            cube[i] = s.charAt(i);
        }
    }

    //randomizes the Rubik's cube with n moves
    public void randomizeState(int n) {
        String[] moves = new String[6];
        moves[0] = "F";
        moves[1] = "F'";
        moves[2] = "U";
        moves[3] = "U'";
        moves[4] = "R";
        moves[5] = "R'";
        for (int i = 0; i < n; i++) {
            Random numgenerator = new Random();
            int choice = numgenerator.nextInt(6);
            this.move(moves[choice]);
        }
    }

    //goal state of rubik's cube in 8 by 3 representation
    public char[][] goalState() {
        char[][] cubearray = new char[8][3];
        cubearray[0] = new char[]{goalcube[0], goalcube[16], goalcube[4]};
        cubearray[1] = new char[]{goalcube[1], goalcube[13], goalcube[17]};
        cubearray[2] = new char[]{goalcube[2], goalcube[9], goalcube[12]};
        cubearray[3] = new char[]{goalcube[3], goalcube[5], goalcube[8]};
        cubearray[4] = new char[]{goalcube[20], goalcube[6], goalcube[11]};
        cubearray[5] = new char[]{goalcube[21], goalcube[10], goalcube[15]};
        cubearray[6] = new char[]{goalcube[22], goalcube[14], goalcube[19]};
        cubearray[7] = new char[]{goalcube[23], goalcube[7], goalcube[18]};
        return cubearray;
    }

    //set max nodes
    public void setMaxNodes(long n) {
        this.maxNodes = n;
    }

    //get max nodes
    public long getMaxNodes() {
        return maxNodes;
    }

    //returns the 24 character array representation of rubik's cube
    public char[] getCube() {
        return this.cube;
    }

    //calculates the manhattan heuristic
    public double manhattan() {
        char[][] ga = this.goalState();
        char[][] ta = this.toCubies();
        for (int i = 0; i < 8; i++) {
            Arrays.sort(ta[i]);
            Arrays.sort(ga[i]);
        }
        int[] cubearray = new int[8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Arrays.equals(ga[j], ta[i]))
                    cubearray[i] = j;
            }
        }
        double manhattanheuristic = this.calculateManhattan(cubearray);
        return manhattanheuristic;
    }

    //helper method for calculating manhattan
    public double calculateManhattan(int[] array) {
        int manhattan = 0;
        for (int i = 0; i < 8; i++) {
            manhattan += this.table[i][array[i]];
        }
        return manhattan / 4.0;
    }

    //calculates the hamming heuristic for the rubik's cube
    public double hamming() {
        double hammingheuristic = 0;
        char[][] cubies = this.toCubies();
        for (int i = 0; i < 8; i++) {
            if (!Arrays.equals(this.goalState()[i], cubies[i]))
                hammingheuristic++;
        }
        return hammingheuristic / 4.0;
    }

    //Converts to 8 by 3 array representation of rubik's cube
    public char[][] toCubies() {
        char[][] cubearray = new char[8][3];
        cubearray[0] = new char[]{cube[0], cube[16], cube[4]};
        cubearray[1] = new char[]{cube[1], cube[13], cube[17]};
        cubearray[2] = new char[]{cube[2], cube[9], cube[12]};
        cubearray[3] = new char[]{cube[3], cube[5], cube[8]};
        cubearray[4] = new char[]{cube[20], cube[6], cube[11]};
        cubearray[5] = new char[]{cube[21], cube[10], cube[15]};
        cubearray[6] = new char[]{cube[22], cube[14], cube[19]};
        cubearray[7] = new char[]{cube[23], cube[7], cube[18]};
        return cubearray;
    }

    //returns whether we have reached the goal or not
    public boolean isGoal() {
        if ("yyyyggggoooobbbbrrrrwwww".equals(new String(this.cube)))
            return true;
        else
            return false;
    }

    //prints out string representation of Rubik's cube
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("  ");
        s.append(Character.toString(cube[0]));
        s.append(Character.toString(cube[1]));
        s.append("    ");
        s.append("\n");
        s.append("  ");
        s.append(Character.toString(cube[3]));
        s.append(Character.toString(cube[2]));
        s.append("    ");
        s.append("\n");
        s.append(Character.toString(cube[4]));
        s.append(Character.toString(cube[5]));
        s.append(Character.toString(cube[8]));
        s.append(Character.toString(cube[9]));
        s.append(Character.toString(cube[12]));
        s.append(Character.toString(cube[13]));
        s.append(Character.toString(cube[16]));
        s.append(Character.toString(cube[17]));
        s.append("\n");
        s.append(Character.toString(cube[7]));
        s.append(Character.toString(cube[6]));
        s.append(Character.toString(cube[11]));
        s.append(Character.toString(cube[10]));
        s.append(Character.toString(cube[15]));
        s.append(Character.toString(cube[14]));
        s.append(Character.toString(cube[19]));
        s.append(Character.toString(cube[18]));
        s.append("\n");
        s.append("  ");
        s.append(Character.toString(cube[20]));
        s.append(Character.toString(cube[21]));
        s.append("    ");
        s.append("\n");
        s.append("  ");
        s.append(Character.toString(cube[23]));
        s.append(Character.toString(cube[22]));
        s.append("    ");
        return s.toString();
    }

    //uses the up/front/right to make the moves
    public void move(String s) {
        if (s.equals("U"))
            this.up();
        else if (s.equals("U'")) {
            this.up();
            this.up();
            this.up();
        } else if (s.equals("F"))
            this.front();
        else if (s.equals("F'")) {
            this.front();
            this.front();
            this.front();
        } else if (s.equals("R"))
            this.right();
        else if (s.equals("R'")) {
            this.right();
            this.right();
            this.right();
        }
    }

    //swaps the 4 corners when rotating
    public void cyclefour(int c1, int c2, int c3, int c4) {
        this.swap(c1, c2);
        this.swap(c1, c3);
        this.swap(c1, c4);
    }

    //swaps two cube locations
    public void swap(int i, int j) {
        char temp = cube[i];
        cube[i] = cube[j];
        cube[j] = temp;
    }

    //all relevant moves (up front and right) for our rubik's cube (Speffz's notation)
    public void up() {
        this.cyclefour(0, 1, 2, 3);
        this.cyclefour(4, 16, 12, 8);
        this.cyclefour(5, 17, 13, 9);
    }

    public void front() {
        this.cyclefour(3, 12, 21, 6);
        this.cyclefour(8, 9, 10, 11);
        this.cyclefour(2, 15, 20, 5);
    }

    public void right() {
        this.cyclefour(12, 13, 14, 15);
        this.cyclefour(2, 16, 22, 10);
        this.cyclefour(1, 19, 21, 9);
    }

    //gets all neighbors of the current board
    public Stack<RubikTwo> neighbors() {
        Stack<RubikTwo> boards = new Stack<RubikTwo>();

        RubikTwo currentBoardU = new RubikTwo(new String(cube));
        currentBoardU.move("U");
        boards.push(currentBoardU);

        RubikTwo currentBoardUPrime = new RubikTwo(new String(cube));
        currentBoardUPrime.move("U'");
        boards.push(currentBoardUPrime);

        RubikTwo currentBoardF = new RubikTwo(new String(cube));
        currentBoardF.move("F");
        boards.push(currentBoardF);

        RubikTwo currentBoardFPrime = new RubikTwo(new String(cube));
        currentBoardFPrime.move("F'");
        boards.push(currentBoardFPrime);

        RubikTwo currentBoardR = new RubikTwo(new String(cube));
        currentBoardR.move("R");
        boards.push(currentBoardR);

        RubikTwo currentBoardRPrime = new RubikTwo(new String(cube));
        currentBoardRPrime.move("R'");
        boards.push(currentBoardRPrime);

        return boards;

    }
}