import java.util.*;

public class StarSolver {
    private boardNode goal;

    //boardNode that stores the number of moves, the board, and the previous board
    private class boardNode {
        private int movenumber;
        private RubikTwo board;
        private boardNode prev;

        public boardNode(RubikTwo start) {
            movenumber = 0;
            prev = null;
            board = start;
        }
    }

    //StarSolver which takes in start board and heuristic
    public StarSolver(RubikTwo start, String heuristic) {

        //number of nodes visited
        int visitednodes = 0;

        //maxNodes
        long maxnodes = start.getMaxNodes();

        Set<RubikTwo> seen = new HashSet<RubikTwo>();

        //order which ranks the boardNodes by heuristic
        PriorityOrder order = new PriorityOrder(heuristic);

        //two priority queues, one for the right path and one for the wrong paths
        PriorityQueue<boardNode> queue = new PriorityQueue<boardNode>(order);
        boardNode Node = new boardNode(start);
        queue.add(Node);

        //loop while goal is not reached and visited nodes is less than maxnodes
        boolean found = false;
        while(!found && visitednodes < maxnodes && !queue.isEmpty()) {
            boardNode min = queue.remove();

            for (RubikTwo b : min.board.neighbors()) {

                //checks if state has been seen before
                if (seen.contains(b)) {
                    continue;
                }
                seen.add(b);

                //compares the boards to its neighbors and adds the best one
                if (min.prev == null || !b.equals(min.prev.board)) {
                    boardNode n = new boardNode(b);
                    n.movenumber = min.movenumber + 1;
                    n.prev = min;
                    queue.add(n);
                }
            }
            //increment visited nodes
            visitednodes++;

            //check for goal state
            if (min.board.isGoal()){
                found = true;
                goal = min;
            }

        }
        System.out.println("Visited Nodes: " + visitednodes);
    }


    //ordering the respective boardNodes by the respective heuristic
    private class PriorityOrder implements Comparator<boardNode> {
        private String heuristic;
        private double nheuristic;
        private double mheuristic;

        private PriorityOrder(String s){
            this.heuristic = s;
        }
        public int compare(boardNode m, boardNode n) {
            if (heuristic.equals("h1")){
                mheuristic = m.board.hamming() + m.movenumber;
                nheuristic = n.board.hamming() + n.movenumber;
            }
            if (heuristic.equals("h2")){
                mheuristic = m.board.manhattan() + m.movenumber;
                nheuristic = n.board.manhattan() + n.movenumber;
            }
            if (mheuristic > nheuristic) {
                return 1;
            }
            if (nheuristic > mheuristic){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    //checks whether the rubik's cube is solvable
    public boolean isSolvable() {
        return goal != null;
    }

    //returns the number of moves needed otherwise -1
    public int movenumber() {
        if (!isSolvable())
            return -1;
        else
            return goal.movenumber;
    }

    //gives list of moves by going through queue
    public String solution() {
        if (!isSolvable())
            return null;
        StringBuilder s = new StringBuilder();
        for (boardNode n = goal; n.prev != null; n = n.prev){
            RubikTwo prevBoardU = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardU.move("U");

            RubikTwo prevBoardUPrime = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardUPrime.move("U'");

            RubikTwo prevBoardF = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardF.move("F");

            RubikTwo prevBoardFPrime = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardFPrime.move("F'");

            RubikTwo prevBoardR = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardR.move("R");

            RubikTwo prevBoardRPrime = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardRPrime.move("R'");

            if (n.board.toString().equals(prevBoardF.toString()))
                s.append("F");
            if (n.board.toString().equals(prevBoardFPrime.toString()))
                s.append("F'");
            if (n.board.toString().equals(prevBoardU.toString()))
                s.append("U");
            if (n.board.toString().equals(prevBoardUPrime.toString()))
                s.append("U'");
            if (n.board.toString().equals(prevBoardR.toString()))
                s.append("R");
            if (n.board.toString().equals(prevBoardRPrime.toString()))
                s.append("R'");
        }
        return s.toString();
    }
}